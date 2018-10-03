package sample;

import filter_iirj.ButterworthFilter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
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

    float[] inputSignal;
    float averageIntervalsRR;
    float heartRate;
    float SDNN;
    float DC;
    float AC;
    float averageTO;
    float averageTS;
    float samplingFrequency = 128;

    String fileDirectory;
    String fileExtenstion;

    List<Sample> peaks;
    List<Sample> intervalsRR;
    List<LineChart<Number, Number>> lineCharts;

    int counter = 0;
    int step = 0;
    int start;
    int stop;
    int channels;

    @Override
    public void start(Stage primaryStage) throws Exception {

        MenuBar menuBar = Partials.menu();
        HBox timeRangeHBox = Partials.timeRange();
        HBox radioButtonPeaks = Partials.radioButtonsPeaks();
        HBox samplingFrequencyHBox = Partials.samplingFrequencyHBox();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(menuBar);

        TextField samplingFrequencyTextField = (TextField) samplingFrequencyHBox.getChildren().get(1);

        final ToggleGroup group = new ToggleGroup();

        RadioButton radioButtonDisablePeaks = (RadioButton) radioButtonPeaks.getChildren().get(0);
        radioButtonDisablePeaks.setToggleGroup(group);

        RadioButton radioButtonEnablePeaks = (RadioButton) radioButtonPeaks.getChildren().get(1);
        radioButtonEnablePeaks.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle();
                if (chk.getText().equals("Enable peaks")) {
                    lineCharts = MinuteInputSignal.showPeaks(start, step, samplingFrequency, inputSignal, true, intervalsRR, lineCharts);
                    updateLineCharts(lineCharts, vbox);
                } else if (chk.getText().equals("Disable peaks")) {
                    lineCharts = MinuteInputSignal.showPeaks(start, step, samplingFrequency, inputSignal, false, intervalsRR, lineCharts);
                    updateLineCharts(lineCharts, vbox);
                }
            }
        });

        Menu menuFile = menuBar.getMenus().get(0);
        MenuItem menuItemOpen = menuFile.getItems().get(0);
        menuItemOpen.setOnAction(event -> {
            fileDirectory = Partials.getPath();
            fileExtenstion = Math.getFileExtension(fileDirectory);
            vbox.getChildren().addAll(
                    Partials.fileDirectory(fileDirectory, samplingFrequency),
                    Partials.channelNumberRadioButton(2),
                    samplingFrequencyHBox,
                    timeRangeHBox);
        });

        Menu analysisMenu = menuBar.getMenus().get(1);
        MenuItem parametersMenuItem = analysisMenu.getItems().get(0);
        parametersMenuItem.setOnAction(event -> {
            ParametersScene.show(start, SDNN, heartRate, averageTO, averageTS, DC, AC, intervalsRR, samplingFrequency);
            HeartRateVariabilityChart.show(intervalsRR);
        });

        Button btnLoadSignal = (Button) timeRangeHBox.getChildren().get(4);
        btnLoadSignal.setOnAction(event -> {
            samplingFrequency = Float.parseFloat(samplingFrequencyTextField.getText());
            TextField textFieldFromTime = (TextField) timeRangeHBox.getChildren().get(1);
            TextField textFieldToTime = (TextField) timeRangeHBox.getChildren().get(3);

            int fromTime = Math.localTimeToSeconds(textFieldFromTime.getText());
            int toTime = Math.localTimeToSeconds(textFieldToTime.getText());
            start = Math.secondsToSample(fromTime, samplingFrequency);
            stop = Math.secondsToSample(toTime, samplingFrequency);


            if(fileExtenstion.equals(".dat")){
                File file = new File(fileDirectory);
                channels = ReadCardioPathNumberOfChannels.load(file.getParent());
                ECGSignal signal = ReadCardioPathSimple.load(file.getAbsolutePath(), channels, (int) samplingFrequency);
                inputSignal = Arrays.copyOfRange(signal.getChannel(0), start, stop);
            } else {
                inputSignal = ReadCardioPathSimple.loadCSV(fileDirectory, 1, start, Math.secondsToSample(toTime, samplingFrequency));
            }

            inputSignal = Normalization.normalize(Normalization.cancelDC(inputSignal));
            QrsDetection qrsDetection = new QrsDetection(inputSignal);
            peaks = qrsDetection.detect(Integration.integration(Squaring.squaring(Derivative.derivative(ButterworthFilter.filter(inputSignal, samplingFrequency), samplingFrequency)), samplingFrequency));
            inputSignal = Normalization.normalize(Normalization.cancelDC(inputSignal));
            intervalsRR = HeartRateVariability.getIntervalsRR(peaks, samplingFrequency);
            averageIntervalsRR = HeartRate.averageIntervalRR(intervalsRR);
            intervalsRR = PrematureVentricularContractions.detectPVCs(intervalsRR, averageIntervalsRR);
            intervalsRR = HeartRateTurbulence.calculateTS(HeartRateTurbulence.calculateTO(intervalsRR));
            heartRate = HeartRate.getHeartRate(averageIntervalsRR);
            SDNN = HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR);
            averageTO = HeartRateTurbulence.averageTO(intervalsRR);
            averageTS = HeartRateTurbulence.averageTS(intervalsRR);
            DC = Deceleration.deceleration(intervalsRR);
            AC = Deceleration.acceleration(intervalsRR);

            HBox selectTimeHBox = Partials.selectTime(fromTime);

            int oneMinuteToSample = Math.secondsToSample(60, samplingFrequency);
            step = counter * oneMinuteToSample;

            if (oneMinuteToSample > inputSignal.length - step) {
                stop = start + inputSignal.length - step;
            } else {
                stop = start + oneMinuteToSample;
            }

            lineCharts = MinuteInputSignal.showInputSignal(start, stop, samplingFrequency, inputSignal, step);

            Button btnNext = (Button) selectTimeHBox.getChildren().get(2);
            btnNext.setOnAction(event1 -> {
                Label labelSelectTime = (Label) selectTimeHBox.getChildren().get(1);
                int timeSelectFromTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(0, 8)) + 60;
                int timeSelectToTime = Math.localTimeToSeconds(labelSelectTime.getText().substring(11, 19)) + 60;
                labelSelectTime.setText(Math.secondsToLocalTime(timeSelectFromTime) + " - " + Math.secondsToLocalTime(timeSelectToTime));
                counter++;
                step = counter * oneMinuteToSample;
                start = Math.secondsToSample(timeSelectFromTime, samplingFrequency);
                if (oneMinuteToSample > inputSignal.length - step) {
                    stop = start + inputSignal.length - step;
                } else {
                    stop = start + oneMinuteToSample;
                }
                lineCharts = MinuteInputSignal.showInputSignal(start, stop, samplingFrequency, inputSignal, step);
                RadioButton rb = (RadioButton) group.getSelectedToggle();
                if (rb.getText().equals("Enable peaks")) {
                    lineCharts = MinuteInputSignal.showPeaks(start, step, samplingFrequency, inputSignal, true, intervalsRR, lineCharts);
                }
                updateLineCharts(lineCharts, vbox);
            });
            Line line = new Line(10 * (1.0f / samplingFrequency), -100, 10 * (1.0f / samplingFrequency), 100);
            vbox.getChildren().addAll(
                    selectTimeHBox,
                    radioButtonPeaks,
                    lineCharts.get(0),
                    lineCharts.get(1),
                    lineCharts.get(2),
                    lineCharts.get(3)
            );
        });

        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/chart.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void updateLineCharts(List<LineChart<Number, Number>> lineCharts, VBox vbox){
        vbox.getChildren().set(7, lineCharts.get(0));
        vbox.getChildren().set(8, lineCharts.get(1));
        vbox.getChildren().set(9, lineCharts.get(2));
        vbox.getChildren().set(10, lineCharts.get(3));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
