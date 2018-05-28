package sample;

import filter.BandPassFilter;
import filter.LowPassFilter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import qrs_detection.QRSDetection;
import uk.me.berndporr.iirj.Butterworth;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    final static int SAMPLING_FREQUENCY = 128;
    final static String ECG_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\System for presentation and simple analysis of ECG signal\\patient-ecg-data";
    List<Double> samples = new ArrayList<Double>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        //final NumberAxis xAxis = new NumberAxis(100000, 100128, 10);
        final NumberAxis xAxis = new NumberAxis(0, 1024, 10);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();

        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);
        float[] input = ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels).getAllData()[0];
        float[] _input = Arrays.copyOfRange(input, 100000, 101024);

        float[] output = QRSDetection.qrsDetection(_input);

        for (int i = 0; i < output.length; i++) {
            series.getData().add(new XYChart.Data<>(i, output[i]));
        }

        Scene scene = new Scene(lineChart, 800, 600);

        lineChart.getData().add(series);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
