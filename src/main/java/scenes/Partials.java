package scenes;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import utils.FileHelper;
import utils.Math;
import utils.ReadCardioPathSimple;

import java.time.LocalTime;

public class Partials {

    public static MenuBar menu() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        Menu menuAnalysis = new Menu("Analysis");

        MenuItem menuItemOpen = new MenuItem("Open");
        menuFile.getItems().add(menuItemOpen);

        MenuItem menuItemHeartRateVariability = new MenuItem("Heart Rate Variability");
        menuAnalysis.getItems().add(menuItemHeartRateVariability);

        menuBar.getMenus().addAll(menuFile, menuAnalysis);
        return menuBar;
    }

    public static HBox peaksRadioButtonHBox() {
        HBox hbox = new HBox();

        RadioButton radioButtonDisablePeaks = new RadioButton("Disable peaks");

        RadioButton radioButtonEnablePeaks = new RadioButton("Enable peaks");

        hbox.setStyle("-fx-padding: 5 0 0 0; -fx-font-size: 10px; -fx-font-style: italic;");
        hbox.setSpacing(35);
        hbox.getChildren().addAll(radioButtonDisablePeaks, radioButtonEnablePeaks);

        return hbox;
    }

    public static HBox timeRangeHBox() {
        HBox hbox = new HBox();

        Label labelFromTime = new Label("From");
        Label labelToTime = new Label("To");

        TextField textFieldFromTime = new TextField();
        textFieldFromTime.setPromptText("HH:MM:SS");
        textFieldFromTime.setPrefWidth(70);

        TextField textFieldToTime = new TextField();
        textFieldToTime.setPromptText("HH:MM:SS");
        textFieldToTime.setPrefWidth(70);

        Button btnLoadSignal = new Button("Load signal");
        btnLoadSignal.setPrefWidth(86);

        hbox.setStyle("-fx-padding: 5 0 5 0; -fx-font-size: 10px; -fx-font-style: italic;");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        hbox.getChildren().addAll(labelFromTime, textFieldFromTime, labelToTime, textFieldToTime, btnLoadSignal);

        return hbox;
    }

    public static HBox selectTimeHBox(int seconds) {

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);

        String from = Math.secondsToLocalTime(seconds);
        String to = Math.secondsToLocalTime(seconds + 60);

        Label labelTimeRange = new Label(from + " - " + to);
        labelTimeRange.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 0 10 0 10;");

        Button btnPrevious = new Button("Previous");
        btnPrevious.setPrefWidth(100);
        btnPrevious.setStyle(" -fx-font-size: 10px; -fx-font-style: italic;");

        Button btnNext = new Button("Next");
        btnNext.setStyle("-fx-font-size: 10px; -fx-font-style: italic;");
        btnNext.setPrefWidth(100);

        hbox.getChildren().addAll(btnPrevious, labelTimeRange, btnNext);
        return hbox;
    }

    public static Label fileDirectoryLabel(String path) {
        Label labelFileDirectory = new Label(path);
        labelFileDirectory.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-font-weight: bold;");
        return labelFileDirectory;
    }


    public static void updateFileDirectoryLabel(String path, Label fileDirectoryLabel, float samplingFrequency, int channels) {
        float seconds;
        if (FileHelper.getFileExtension(path).equals(".csv"))
            seconds = Math.sampleToSecond(ReadCardioPathSimple.getRecords(path).size(), samplingFrequency);
        else
            seconds = Math.sampleToSecond(ReadCardioPathSimple.load(path, channels, (int) samplingFrequency).getChannelLength(0), samplingFrequency);
        fileDirectoryLabel.setText(path + " (duration: " + LocalTime.MIN.plusSeconds((int) seconds).toString() + ")");
    }


    public static HBox channelNumberRadioButtonHBox(int channelNumber) {
        HBox hbox = new HBox();

        ToggleGroup toggleGroup = new ToggleGroup();

        Label channelNumberLabel = new Label("Channel number");
        channelNumberLabel.setStyle("-fx-font-weight: bold;");
        hbox.getChildren().add(channelNumberLabel);

        for (int i = 1; i <= channelNumber; i++) {
            RadioButton radioButton = new RadioButton(String.valueOf(i));
            radioButton.setToggleGroup(toggleGroup);
            hbox.getChildren().add(radioButton);
        }

        hbox.setStyle("-fx-padding: 5 0 0 0; -fx-font-size: 10px; -fx-font-style: italic;");
        hbox.setSpacing(15);
        return hbox;
    }

    public static HBox samplingFrequencyHBox() {
        Label samplingFrequencyLabel = new Label("Sampling frequency");
        samplingFrequencyLabel.setStyle("-fx-font-weight: bold;");

        TextField samplingFrequencyTextField = new TextField();

        HBox hbox = new HBox();
        hbox.setStyle("-fx-padding: 5 0 0 0; -fx-font-size: 10px; -fx-font-style: italic;");
        hbox.setSpacing(15);
        hbox.getChildren().addAll(samplingFrequencyLabel, samplingFrequencyTextField);
        return hbox;
    }

}
