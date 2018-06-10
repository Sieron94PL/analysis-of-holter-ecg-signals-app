package sample;

import filter_pan_tompkins.BandPassFilter;
import filter_pan_tompkins.HighPassFilter;
import filter_pan_tompkins.LowPassFilter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import qrs.*;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    final static String ECG_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\System for presentation and simple analysis of ECG signal\\patient-ecg-data";
    List<Double> samples = new ArrayList<Double>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        final NumberAxis xAxis = new NumberAxis(0, 1000, 50);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final ScatterChart<Number, Number> sc = new
                ScatterChart<Number, Number>(xAxis, yAxis);

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();

        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);
        float[] input = ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels).getAllData()[0];
        float[] _input = Arrays.copyOfRange(input, 24000, 25000);

        //float[] output_ = ButterworthFilter.filterCascade(_input);
//        float[] output_ = Detection.normalize(Integration.integration(Squaring.squaring(Derivative.derivative(HighPassFilter.filter(LowPassFilter.filter(_input))))));
//        List<Integer> output = Detection.detect(output_);

        float[] output = LowPassFilter.filter(_input);

//
//        float[] output = Detection.detect(Integration.integration(Squaring.squaring(Derivative.derivative(HighPassFilter.filter(LowPassFilter.filter(_input))))));

        for (int i = 0; i < output.length; i++) {
            series.getData().add(new XYChart.Data<>(i, output[i]));
        }


        for (int i = 0; i < _input.length; i++) {
            series1.getData().add(new XYChart.Data<>(i, _input[i]));
        }
//
//        for (int i = 0; i < _input.length; i++) {
//            if (output.contains(i)) {
//                series1.getData().add(new XYChart.Data<>(i, 255));
//            } else {
//                series1.getData().add(new XYChart.Data<>(i, 100));
//            }
//        }

        Scene scene = new Scene(lineChart, 800, 600);

        lineChart.getData().addAll(series, series1);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
