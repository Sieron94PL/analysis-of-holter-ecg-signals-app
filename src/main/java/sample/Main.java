package sample;

import filter_discrete_realization.BandPassFilter;
import filter_discrete_realization.HighPassFilter;
import filter_discrete_realization.LowPassFilter;
import filter_iirj.ButterworthFilter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Sample;
import parameters.HeartRate;
import parameters.HeartRateVariability;
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

        final NumberAxis xAxis = new NumberAxis(0, 100, 1);
        xAxis.setLabel("Sample no");

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Analysis of Holter ECG signals");
        lineChart.setCreateSymbols(false);

        XYChart.Series seriesInputSignal = new XYChart.Series();
        XYChart.Series seriesOutputSignal = new XYChart.Series();
        XYChart.Series seriesOutputSignal1 = new XYChart.Series();
        int channels = ReadCardioPathNumberOfChannels.load(ECG_DATA_DIRECTORY);

        float[] input = Arrays.copyOfRange(ReadCardioPathSimple.load(ECG_DATA_DIRECTORY, channels).getAllData()[0], 102000, 130000); //length = 9653888
//        input = Normalization.normalize(Normalization.cancelDC(input));



        List<Sample> output = QrsDetection.detect(
                Integration.integration(
                        Squaring.squaring(
                                Derivative.derivative(
                                        ButterworthFilter.filter(
                                                Normalization.normalize(
                                                        Normalization.cancelDC(input)))))));


//        float[] output_ = Integration.integration(
//                Squaring.squaring(
//                        Derivative.derivative(
//                                ButterworthFilter.filter(
//                                        Normalization.normalize(
//                                                Normalization.cancelDC(input))))));

        for (int i = 0; i < input.length; i++) {
            seriesInputSignal.getData().add(new XYChart.Data<>(i, input[i]));
            //System.out.println(i + ": " + input[i]);
        }

        //System.out.println(output.size());

//        for (int i = 0; i < output.size(); i++) {
//            seriesOutputSignal.getData().add(new XYChart.Data<>(output.get(i).getId(), input[output.get(i).getId().intValue()]));
//        }
//
//        for(int i = 0; i < output_.length; i++){
//            seriesOutputSignal1.getData().add(new XYChart.Data<>(i, output_[i]));
//        }


        float[] output_ = HeartRateVariability.getIntervalsRR(output);

        System.out.println(HeartRate.getHeartRate(output_));

//        seriesOutputSignal1.getData().add(new XYChart.Data<>(0, output_[0]));
//
//        for(int i = 1; i < output_.length; i++){
//            seriesOutputSignal1.getData().add(new XYChart.Data<>(i, output_[i - 1]));
//            seriesOutputSignal1.getData().add(new XYChart.Data<>(i, output_[i]));
//
//        }

        //System.out.println(seriesOutputSignal.getData());

        Scene scene = new Scene(lineChart, 800, 600);

        lineChart.getData().addAll(seriesInputSignal, seriesOutputSignal, seriesOutputSignal1);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Analysis of Holter ECG signals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
