package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Analysis of Holter ECG signals");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sample number");

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Analysis of Holter ECG signals");

        XYChart.Series series = new XYChart.Series();

        int channels = ReadCardioPathNumberOfChannels.load("C:\\Users\\Damian\\Desktop\\Patient_ECG_data");
        float[] data = ReadCardioPathSimple.load("C:\\Users\\Damian\\Desktop\\Patient_ECG_data", channels).getAllData()[0];

        for (int sampleNumber = 0; sampleNumber < 1024; sampleNumber++) {
            series.getData().add(new XYChart.Data(sampleNumber, data[sampleNumber]));
        }

        Scene scene = new Scene(lineChart, 800, 600);

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);

        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
//        int channels = ReadCardioPathNumberOfChannels.load("C:\\Users\\Damian\\Desktop\\Patient_ECG_data");
//        float [] data = ReadCardioPathSimple.load("C:\\Users\\Damian\\Desktop\\Patient_ECG_data", channels).getAllData()[0];
//        for(int i = 0; i < data.length; i++){
//            double time = i / 128.0;
//            System.out.println(time + " " + data[i]);
//        }
        launch(args);
    }
}
