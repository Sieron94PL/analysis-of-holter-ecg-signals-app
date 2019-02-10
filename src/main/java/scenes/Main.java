package scenes;

import filter_iirj.ButterworthFilter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Sample;
import parameters.*;
import qrs.*;
import utils.*;
import utils.Math;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    private float[] inputSignal;
    private float averageIntervalsRR;
    private float heartRate;
    private float SDNN;
    private float DC;
    private float AC;
    private float averageTO;
    private float averageTS;
    private float samplingFrequency = 128;

    private String fileDirectory;
    private String fileExtension;


    private List<Sample> peaks;
    private List<Sample> intervalsRR;
    private List<LineChart<Number, Number>> lineCharts;

    private int counter = 0;
    private int step = 0;
    private int start;
    private int stop;
    private int channels = 0;
    private int timeSelectFromTime;
    private int timeSelectToTime;

    private File file;

    private Label fileDirectoryLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MenuBar menuBar = Partials.menu();
        HBox timeRangeHBox = Partials.timeRangeHBox();
        HBox peaksRadioButtonHBox = Partials.peaksRadioButtonHBox();
        HBox samplingFrequencyHBox = Partials.samplingFrequencyHBox();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(menuBar);

        TextField samplingFrequencyTextField = (TextField) samplingFrequencyHBox.getChildren().get(1);

        /*Peaks radio button*/
        final ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton disablePeaksRadioButton = (RadioButton) peaksRadioButtonHBox.getChildren().get(0);
        disablePeaksRadioButton.setToggleGroup(toggleGroup);
        RadioButton enablePeaksRadioButton = (RadioButton) peaksRadioButtonHBox.getChildren().get(1);
        enablePeaksRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle();
                if (chk.getText().equals("Enable peaks")) {
                    lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, true, intervalsRR, lineCharts);
                    updateLineCharts(lineCharts, vbox);
                } else if (chk.getText().equals("Disable peaks")) {
                    lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, false, intervalsRR, lineCharts);
                    updateLineCharts(lineCharts, vbox);
                }
            }
        });

        final ToggleGroup toggleGroup1 = new ToggleGroup();

        /*Opening file*/
        Menu menuFile = menuBar.getMenus().get(0);
        MenuItem menuItemOpen = menuFile.getItems().get(0);
        menuItemOpen.setOnAction(event -> {

            fileDirectory = FileHelper.getPath();
            fileExtension = FileHelper.getFileExtension(fileDirectory);

            if (fileExtension.equals(".dat") || fileExtension.equals(".csv")) {

                if (fileExtension.equals(".dat")) {
                    file = new File(fileDirectory);
                    channels = ReadCardioPathNumberOfChannels.load(file.getParent());
                } else if (fileExtension.equals(".csv")) {
                    channels = ReadCardioPathSimple.getRecords(fileDirectory).get(0).length - 1;
                }

                HBox channelNumberRadioButtonHBox = Partials.channelNumberRadioButtonHBox(channels);

                for (int i = 1; i <= channels; i++) {
                    RadioButton rb = (RadioButton) channelNumberRadioButtonHBox.getChildren().get(i);
                    rb.setToggleGroup(toggleGroup1);
                    if (i == 1)
                        rb.setSelected(true);
                }

                CheckBox checkBox = (CheckBox) samplingFrequencyHBox.getChildren().get(2);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (!Validator.isNumber(samplingFrequencyTextField.getText())) {
                            if (samplingFrequencyHBox.getChildren().size() == 4) {
                                samplingFrequencyHBox.getChildren().remove(3);
                            }
                            samplingFrequencyHBox.getChildren().add(errorMessageLabel("Sampling frequency must be a number."));
                        } else {
                            checkBox.setDisable(true);
                            samplingFrequencyHBox.setDisable(true);
                            channelNumberRadioButtonHBox.setDisable(true);
                            fileDirectoryLabel.setDisable(true);
                            timeRangeHBox.setDisable(false);
                            samplingFrequency = Float.parseFloat(samplingFrequencyTextField.getText());
                            Partials.updateFileDirectoryLabel(fileDirectory, fileDirectoryLabel, samplingFrequency, channels);
                        }

                    }
                });

                timeRangeHBox.setDisable(true);
                fileDirectoryLabel = Partials.fileDirectoryLabel(fileDirectory);

                vbox.getChildren().addAll(
                        fileDirectoryLabel,
                        channelNumberRadioButtonHBox,
                        samplingFrequencyHBox,
                        timeRangeHBox);
            }


        });

        /**HRT, HRV analysis**/
        Menu analysisMenu = menuBar.getMenus().get(1);
        MenuItem parametersMenuItem = analysisMenu.getItems().get(0);
        parametersMenuItem.setOnAction(event -> {
            TextField textFieldFromTime = (TextField) timeRangeHBox.getChildren().get(1);
            ParametersScene.show(start, SDNN, heartRate, averageTO, averageTS, DC, AC, intervalsRR, samplingFrequency);
            HeartRateVariabilityScene.show(intervalsRR);
        });

        /**Loading signal**/
        Button btnLoadSignal = (Button) timeRangeHBox.getChildren().get(4);
        btnLoadSignal.setOnAction(event -> {

            if (timeRangeHBox.getChildren().size() == 6) {
                timeRangeHBox.getChildren().remove(5);
            }

            TextField textFieldFromTime = (TextField) timeRangeHBox.getChildren().get(1);
            TextField textFieldToTime = (TextField) timeRangeHBox.getChildren().get(3);

            int fromTime = Math.localTimeToSeconds(textFieldFromTime.getText());
            int toTime = Math.localTimeToSeconds(textFieldToTime.getText());
            start = Math.secondsToSample(fromTime, samplingFrequency);
            stop = Math.secondsToSample(toTime, samplingFrequency);

            RadioButton rb1 = (RadioButton) toggleGroup1.getSelectedToggle();

            if (fileExtension.equals(".dat")) {
                ECGSignal signal = ReadCardioPathSimple.load(file.getAbsolutePath(), channels, (int) samplingFrequency);
                inputSignal = Arrays.copyOfRange(signal.getChannel(Integer.valueOf(rb1.getText()) - 1), start, stop);
            } else if (fileExtension.equals(".csv")) {
                inputSignal = ReadCardioPathSimple.loadCSV(fileDirectory, Integer.valueOf(rb1.getText()), start, Math.secondsToSample(toTime, samplingFrequency));
            }

            /**Computing parameters**/
            QrsDetection qrsDetection = new QrsDetection(inputSignal);
            inputSignal = Normalization.cancelDC(inputSignal);
            peaks = qrsDetection.detect(Integration.integration(Squaring.squaring(Derivative.derivative(ButterworthFilter.filter(inputSignal, samplingFrequency), samplingFrequency)), samplingFrequency), samplingFrequency);
            intervalsRR = HeartRateVariability.getIntervalsRR(peaks, samplingFrequency);
            averageIntervalsRR = HeartRate.averageIntervalRR(intervalsRR);
            intervalsRR = PrematureVentricularContractions.detectPVCs(intervalsRR, averageIntervalsRR);
            intervalsRR = HeartRateTurbulence.calculateTS(HeartRateTurbulence.calculateTO(intervalsRR));
            heartRate = HeartRate.getHeartRate(averageIntervalsRR);
            SDNN = HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR);
            averageTO = HeartRateTurbulence.averageTO(intervalsRR);
            averageTS = HeartRateTurbulence.averageTS(intervalsRR);
            AC = Acceleration.acceleration(Acceleration.signalAveraging(intervalsRR));
            DC = Deceleration.deceleration(Deceleration.signalAveraging(intervalsRR));
            inputSignal = Normalization.normalize(inputSignal);

            HBox selectTimeHBox = Partials.selectTimeHBox(fromTime);

            int oneMinuteToSample = Math.secondsToSample(60, samplingFrequency);
            step = counter * oneMinuteToSample;

            if (oneMinuteToSample > inputSignal.length - step) {
                stop = start + inputSignal.length - step;
            } else {
                stop = start + oneMinuteToSample;
            }

            lineCharts = MainScene.showInputSignal(start, stop, samplingFrequency, inputSignal, step);

            Label labelSelectTime = (Label) selectTimeHBox.getChildren().get(1);

            Button btnPrevious = (Button) selectTimeHBox.getChildren().get(0);
            btnPrevious.setDisable(true);

            btnPrevious.setOnAction(event2 -> {
                counter--;
                updateBtnPrevious(btnPrevious, counter);
                step = counter * oneMinuteToSample;
                timeSelectFromTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(0, 8)) - 60;
                timeSelectToTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(11, 19)) - 60;
                labelSelectTime.setText(Math.secondsToLocalTime(timeSelectFromTime) + " - " + Math.secondsToLocalTime(timeSelectToTime));
                start = Math.secondsToSample(timeSelectFromTime, samplingFrequency);
                if (oneMinuteToSample > inputSignal.length - step) {
                    stop = start + inputSignal.length - step;
                } else {
                    stop = start + oneMinuteToSample;
                }
                lineCharts = MainScene.showInputSignal(start, stop, samplingFrequency, inputSignal, step);
                RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();
                if (rb != null) {
                    if (rb.getText().equals("Enable peaks")) {
                        lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, true, intervalsRR, lineCharts);
                    } else {
                        lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, false, intervalsRR, lineCharts);
                    }
                }

                updateLineCharts(lineCharts, vbox);

                lineCharts = MainScene.showInputSignal(start, stop, samplingFrequency, inputSignal, step);
            });

            Button btnNext = (Button) selectTimeHBox.getChildren().get(2);
            btnNext.setOnAction(event1 -> {
                counter++;
                updateBtnPrevious(btnPrevious, counter);
                step = counter * oneMinuteToSample;
                timeSelectFromTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(0, 8)) + 60;
                timeSelectToTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(11, 19)) + 60;
                labelSelectTime.setText(Math.secondsToLocalTime(timeSelectFromTime) + " - " + Math.secondsToLocalTime(timeSelectToTime));
                start = Math.secondsToSample(timeSelectFromTime, samplingFrequency);
                if (oneMinuteToSample > inputSignal.length - step) {
                    stop = start + inputSignal.length - step;
                } else {
                    stop = start + oneMinuteToSample;
                }
                lineCharts = MainScene.showInputSignal(start, stop, samplingFrequency, inputSignal, step);
                RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();

                if (rb != null) {
                    if (rb.getText().equals("Enable peaks")) {
                        lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, true, intervalsRR, lineCharts);
                    } else {
                        lineCharts = MainScene.showPeaks(start, step, samplingFrequency, inputSignal, false, intervalsRR, lineCharts);
                    }
                }

                updateLineCharts(lineCharts, vbox);
            });

            if (vbox.getChildren().size() == 5) {
                vbox.getChildren().addAll(
                        selectTimeHBox,
                        peaksRadioButtonHBox,
                        lineCharts.get(0),
                        lineCharts.get(1),
                        lineCharts.get(2),
                        lineCharts.get(3)
                );
            } else {
                vbox.getChildren().set(5, selectTimeHBox);
                vbox.getChildren().set(6, peaksRadioButtonHBox);
                vbox.getChildren().set(7, lineCharts.get(0));
                vbox.getChildren().set(8, lineCharts.get(1));
                vbox.getChildren().set(9, lineCharts.get(2));
                vbox.getChildren().set(10, lineCharts.get(3));
            }

        });

        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/main-scene.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();

    }

    public static void updateLineCharts(List<LineChart<Number, Number>> lineCharts, VBox vbox) {
        vbox.getChildren().set(7, lineCharts.get(0));
        vbox.getChildren().set(8, lineCharts.get(1));
        vbox.getChildren().set(9, lineCharts.get(2));
        vbox.getChildren().set(10, lineCharts.get(3));
    }

    public Label errorMessageLabel(String text) {
        Label label = new Label();
        label.setText("Invalid format data [HH:MM:SS].");
        label.setTextFill(Color.RED);
        return label;
    }

    public static void updateBtnPrevious(Button btnPrevious, int counter) {
        if (counter > 0) {
            btnPrevious.setDisable(false);
        } else {
            btnPrevious.setDisable(true);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
