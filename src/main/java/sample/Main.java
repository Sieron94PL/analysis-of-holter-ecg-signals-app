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
import parameters.HeartRateTurbulence;
import parameters.HeartRateVariability;
import parameters.PrematureVentricularContractions;
import qrs.*;
import utils.*;

import java.util.List;

public class Main extends Application {

    final static String ECG_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\System for presentation and simple analysis of ECG signal\\patient-ecg-data";
    final static String CSV_DATA_DIRECTORY = "D:\\Politechnika Łódzka\\Informatyka - studia magisterskie\\3 semestr\\Praca magisterska\\MIT-BIH\\";

    @Override
    public void start(Stage primaryStage) throws Exception {


        Controller controller = new Controller();


//        int samplingFrequency = 360;
//        //String fileDAT = "crecg.dat";
//        String fileCSV = "105.csv";
//        /*ECG Signal from .csv file.*/
//        float[] inputSignal = ReadCardioPathSimple.loadCSV(CSV_DATA_DIRECTORY + fileCSV, 1, 0, 1000);
//        /*ECG Signal from .dat file.*/
////        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);
////        ECGSignal signal = ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels, samplingFrequency, fileDAT);
////        float[] inputSignal = Arrays.copyOfRange(signal.getChannel(0), 20000, 40000); //length = 9653888
//        /*Normalization*/
//        inputSignal = Normalization.normalize(Normalization.cancelDC(inputSignal));
//        /*Filtering*/
//        float[] filteredSignal = ButterworthFilter.filter(inputSignal, samplingFrequency);
//        /*QRS Detection*/
//        QrsDetection qrsDetection = new QrsDetection(inputSignal);
//        List<Sample> peaks = qrsDetection.detect(Integration.integration(
//                Squaring.squaring(
//                        Derivative.derivative(
//                                filteredSignal))));
//
//        XYChart.Series inputSignalSeries = new XYChart.Series();
//        XYChart.Series filteredSignalSeries = new XYChart.Series();
//        XYChart.Series peaksSeries = new XYChart.Series();
//        XYChart.Series intervalsRRSeries = new XYChart.Series();
//        XYChart.Series integrationSeries = new XYChart.Series();
//
////        for (int i = 0; i < integration.length; i++) {
////            integrationSeries.getData().add(new XYChart.Data<>(i, integration[i]));
////        }
////        for (int i = 0; i < inputSignal.length; i++) {
////            inputSignalSeries.getData().add(new XYChart.Data<>(i, inputSignal[i]));
////        }
////        for (int i = 0; i < filteredSignal.length; i++) {
////            filteredSignalSeries.getData().add(new XYChart.Data<>(i * Math.SAMPLING_PERIOD, filteredSignal[i]));
////        }
//
//        List<Sample> intervalsRR;
//        if (peaks.size() != 0) {
//            intervalsRR = HeartRateVariability.getIntervalsRR(peaks);
//            float averageIntervalRR = HeartRate.averageIntervalRR(intervalsRR);
//            int peakId;
//            for (int i = 0; i < peaks.size(); i++) {
//                peakId = peaks.get(i).getId();
//                peaksSeries.getData().add(new XYChart.Data<>(peakId, inputSignal[peakId]));
//            }
//            intervalsRRSeries.getData().add(new XYChart.Data<>(0, intervalsRR.get(0).getValue()));
//            for (int i = 1; i < intervalsRR.size(); i++) {
////                intervalsRRSeries.getData().add(new XYChart.Data<>(i, intervalsRR[i - 1]));
//                intervalsRRSeries.getData().add(new XYChart.Data<>(i, intervalsRR.get(i).getValue()));
//            }
//            intervalsRR = PrematureVentricularContractions.detectPVCs(intervalsRR, averageIntervalRR);
//            HeartRateTurbulence.calculate(intervalsRR);
////            PrematureVentricularContractions.displayPVCs(PVCs);
//            HeartRateVariability.displayIntervalsRR(intervalsRR);
////            System.out.println("Heart Rate: " + HeartRate.getHeartRate(intervalsRR) + " bpm");
//        }

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        int lowerBound = 0;
        int upperBound = 200;
        float tickUnit = 1;

        final NumberAxis xAxis = new NumberAxis(lowerBound, upperBound, tickUnit);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);


        Scene scene = new Scene(root, 800, 600);

//        lineChart.getData().add(inputSignalSeries);
//        lineChart.getData().add(filteredSignalSeries);
//        lineChart.getData().add(peaksSeries);
//        lineChart.getData().add(intervalsRRSeries);
//        lineChart.getData().add(integrationSeries);
//        lineChart.getData().add(averageIntervalsRR);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
