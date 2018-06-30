package sample;

import filter_iirj.ButterworthFilter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.Sample;
import parameters.HeartRate;
import parameters.HeartRateVariability;
import qrs.*;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    final static String ECG_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\System for presentation and simple analysis of ECG signal\\patient-ecg-data";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        final NumberAxis xAxis = new NumberAxis(0, 10000, 50);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);

        XYChart.Series inputSignalSeries = new XYChart.Series();
        XYChart.Series filteredSignalSeries = new XYChart.Series();
        XYChart.Series peaksSeries = new XYChart.Series();
        XYChart.Series intervalsRRSeries = new XYChart.Series();

        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);

        float[] inputSignal = Arrays.copyOfRange(ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels).getAllData()[0], 102000, 112000); //length = 9653888
        inputSignal = Normalization.normalize(Normalization.cancelDC(inputSignal));


        float[] filteredSignal = ButterworthFilter.filter(inputSignal);

        QrsDetection qrsDetection = new QrsDetection(inputSignal);

        List<Sample> peaks = qrsDetection.detect(
                Integration.integration(
                        Squaring.squaring(
                                Derivative.derivative(
                                        filteredSignal))));

        float[] intervalsRR = HeartRateVariability.getIntervalsRR(peaks);


        for (int i = 0; i < inputSignal.length; i++) {
            inputSignalSeries.getData().add(new XYChart.Data<>(i, inputSignal[i]));
        }

        for (int i = 0; i < filteredSignal.length; i++) {
            filteredSignalSeries.getData().add(new XYChart.Data<>(i, filteredSignal[i]));
        }

        int peakId;
        for (int i = 0; i < peaks.size(); i++) {
            peakId = peaks.get(i).getId();
            peaksSeries.getData().add(new XYChart.Data<>(peakId, inputSignal[peakId]));
        }


        intervalsRRSeries.getData().add(new XYChart.Data<>(0, intervalsRR[0]));
        for (int i = 1; i < intervalsRR.length; i++) {
//          intervalsRRSeries.getData().add(new XYChart.Data<>(i, intervalsRR[i - 1]));
            intervalsRRSeries.getData().add(new XYChart.Data<>(i, intervalsRR[i]));
        }

        Scene scene = new Scene(lineChart, 800, 600);

        lineChart.getData().add(inputSignalSeries);
//        lineChart.getData().add(filteredSignalSeries);
        lineChart.getData().add(peaksSeries);
//        lineChart.getData().add(intervalsRRSeries);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
