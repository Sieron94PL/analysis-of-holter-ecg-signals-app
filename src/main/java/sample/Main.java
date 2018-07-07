package sample;

import com.opencsv.CSVReader;
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
import parameters.HeartRateVariability;
import qrs.*;
import utils.*;
import utils.Math;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    final static String ECG_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\System for presentation and simple analysis of ECG signal\\patient-ecg-data";
    final static String CSV_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\MIT-BIH\\";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        int samplingFrequency = 360;
        String fileDAT = "aami3a.dat";
        String fileCSV = "100.csv";

        /*ECG Signal from .csv file.*/
        //float[] inputSignal = ReadCardioPathSimple.loadCSV(CSV_DATA_DIRECTORY + fileCSV, 2);

        /*ECG Signal from .dat file.*/
        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);
        ECGSignal signal = ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels, samplingFrequency, fileDAT);
        float[] inputSignal = Arrays.copyOfRange(signal.getChannel(0), 5000, 6000); //length = 9653888

        inputSignal = Normalization.normalize(Normalization.cancelDC(inputSignal));

        float[] filteredSignal = ButterworthFilter.filter(inputSignal, samplingFrequency);
        float[] intervalsRR;

        QrsDetection qrsDetection = new QrsDetection(inputSignal);

        List<Sample> peaks = qrsDetection.detect(
                Integration.integration(
                        Squaring.squaring(
                                Derivative.derivative(
                                        filteredSignal))));

        XYChart.Series inputSignalSeries = new XYChart.Series();
        XYChart.Series filteredSignalSeries = new XYChart.Series();
        XYChart.Series peaksSeries = new XYChart.Series();
        XYChart.Series intervalsRRSeries = new XYChart.Series();


        for (int i = 0; i < inputSignal.length; i++) {
            inputSignalSeries.getData().add(new XYChart.Data<>(i, inputSignal[i]));
        }

//        for (int i = 0; i < filteredSignal.length; i++) {
//            filteredSignalSeries.getData().add(new XYChart.Data<>(i, filteredSignal[i]));
//        }

        if (peaks.size() != 0) {

            intervalsRR = HeartRateVariability.getIntervalsRR(peaks);
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

            HeartRateVariability.displayIntervalsRR(intervalsRR);
        }

        int lowerBound = 0;
        int upperBound = inputSignal.length;
        float tickUnit = 1.0f;

        final NumberAxis xAxis = new NumberAxis(lowerBound, upperBound, tickUnit);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);

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
