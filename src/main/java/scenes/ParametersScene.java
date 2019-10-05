package scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HeartRateTurbulenceData;
import model.HeartRateVariabilityData;
import model.Sample;
import parameters.HeartRate;
import parameters.PrematureVentricularContractions;
import utils.Math;

import java.util.List;


public class ParametersScene {

    private static HBox parametersHBox(float SDNN, float HR, float TO, float TS, float DC, float AC) {
        Label SDNNLabel = new Label("SDNN");
        Label HRLabel = new Label("HR");
        Label TOLabel = new Label("TO");
        Label TSLabel = new Label("TS");
        Label DCLabel = new Label("DC");
        Label ACLabel = new Label("AC");

        VBox parametersNamesVBox = new VBox();
        parametersNamesVBox.getChildren().addAll(SDNNLabel, HRLabel, TOLabel, TSLabel, DCLabel, ACLabel);
        parametersNamesVBox.setStyle("-fx-font-weight: bold;");

        Label SDNNValue = new Label(String.format("%.2f", SDNN));
        Label HRValue = new Label(String.valueOf(java.lang.Math.round(HR)) + " bpm");
        Label TOValue = new Label(String.format("%.2f", TO) + "%");
        Label TSValue = new Label(String.format("%.2f", TS) + " RR/ms");
        Label DCValue = new Label(String.format("%.2f", DC));
        Label ACValue = new Label(String.format("%.2f", AC));

        VBox parametersValuesVBox = new VBox();
        parametersValuesVBox.getChildren().addAll(SDNNValue, HRValue, TOValue, TSValue, DCValue, ACValue);

        HBox parametersHBox = new HBox();
        parametersHBox.setAlignment(Pos.TOP_CENTER);
        parametersHBox.setSpacing(20);
        parametersHBox.getChildren().addAll(parametersNamesVBox, parametersValuesVBox);
        parametersHBox.setStyle("-fx-font-size: 20px; -fx-font-style: italic;");
        return parametersHBox;
    }

    private static ObservableList<HeartRateTurbulenceData> heartRateTurbulenceData(int start, List<Sample> intervalsRR, float samplingFrequency) {
        List<Sample> PVCs = PrematureVentricularContractions.detectPVCs(intervalsRR, HeartRate.averageIntervalRR(intervalsRR));
        ObservableList<HeartRateTurbulenceData> heartRateTurbulenceData = FXCollections.observableArrayList();
        for (int i = 0; i < PVCs.size(); i++) {
            if (PVCs.get(i).isPVC()) {
                int peakNo = PVCs.get(i).getId() + start;
                String time = String.valueOf(Math.secondsToLocalTime((int) Math.sampleToSecond(peakNo, samplingFrequency)));
                float TO = PVCs.get(i).getTO();
                float TS = PVCs.get(i).getTS();
                heartRateTurbulenceData.add(new HeartRateTurbulenceData(peakNo, time, TO, TS));
            }
        }
        return heartRateTurbulenceData;
    }

    private static ObservableList<HeartRateVariabilityData> heartRateVariabilityData(int start, List<Sample> intervalsRR, float samplingFrequency) {
        ObservableList<HeartRateVariabilityData> heartRateVariabilityData = FXCollections.observableArrayList();
        List<Sample> PVCs = PrematureVentricularContractions.detectPVCs(intervalsRR, samplingFrequency);
        for (int i = 0; i < PVCs.size(); i++) {
            int sampleNo = PVCs.get(i).getId() + start;
            String time = String.valueOf(Math.secondsToLocalTime((int) Math.sampleToSecond(sampleNo + start, samplingFrequency)));
            float duration = PVCs.get(i).getValue();
            boolean isPVC = PVCs.get(i).isPVC();
            heartRateVariabilityData.add(new HeartRateVariabilityData(i + 1, sampleNo, time, duration, isPVC));
        }
        return heartRateVariabilityData;
    }


    private static TableView heartRateTurbulenceTableView(int start, List<Sample> intervalsRR, float samplingFrequency) {
        TableView<HeartRateTurbulenceData> heartRateTurbulenceTableView = new TableView();
        heartRateTurbulenceTableView.setPrefWidth(350);

        TableColumn peakNumberColumn = new TableColumn("Peak no.");
        peakNumberColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("peakNo"));

        TableColumn timeColumn = new TableColumn("Time[HH:mm:ss]");
        timeColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("time"));

        TableColumn TOColumn = new TableColumn("TO");
        TOColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("TO"));

        TableColumn TSColumn = new TableColumn("TS");
        TSColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("TS"));

        heartRateTurbulenceTableView.setItems(heartRateTurbulenceData(start, intervalsRR, samplingFrequency));
        heartRateTurbulenceTableView.getColumns().addAll(peakNumberColumn, timeColumn, TOColumn, TSColumn);
        return heartRateTurbulenceTableView;
    }


    private static TableView heartRateVariabilityTableView(int start, List<Sample> intervalsRR, float samplingFrequency) {
        TableView<HeartRateVariabilityData> heartRateVariabilityTableView = new TableView();
        heartRateVariabilityTableView.setPrefWidth(450);

        TableColumn beatNumberColumn = new TableColumn("Beat no.");
        beatNumberColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("beatNo"));

        TableColumn peakNumberColumn = new TableColumn("Peak no.");
        peakNumberColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("peakNo"));

        TableColumn timeColumn = new TableColumn("Time[HH:mm:ss]");
        timeColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("time"));

        TableColumn durationColumn = new TableColumn("Duration[ms]");
        durationColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("duration"));

        TableColumn isPVCColumn = new TableColumn("PVC[true/false]");
        isPVCColumn.setCellValueFactory(new PropertyValueFactory<HeartRateVariabilityData, String>("isPVC"));

        heartRateVariabilityTableView.setItems(heartRateVariabilityData(start, intervalsRR, samplingFrequency));
        heartRateVariabilityTableView.getColumns().addAll(beatNumberColumn, peakNumberColumn, timeColumn, durationColumn, isPVCColumn);
        return heartRateVariabilityTableView;
    }

    public static Scene show(int start, float SDNN, float HR, float TO, float TS, float DC, float AC, List<Sample> intervalsRR, float samplingFrequency) {

        HBox mainHBox = new HBox();
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        VBox rightVBox = new VBox();

        TableView heartRateVariabilityTableView = heartRateVariabilityTableView(start, intervalsRR, samplingFrequency);
        TableView heartRateTurbulenceTableView = heartRateTurbulenceTableView(start, intervalsRR, samplingFrequency);

        VBox.setVgrow(heartRateTurbulenceTableView, Priority.ALWAYS);
        HBox.setHgrow(leftHBox, Priority.ALWAYS);
        HBox.setHgrow(rightHBox, Priority.ALWAYS);

        rightVBox.getChildren().addAll(parametersHBox(SDNN, HR, TO, TS, DC, AC), heartRateTurbulenceTableView);
        rightHBox.getChildren().addAll(rightVBox);

        leftHBox.getChildren().addAll(heartRateVariabilityTableView);

        mainHBox.getChildren().addAll(leftHBox, rightHBox);

        Scene scene = new Scene(mainHBox, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Parameters");
        stage.setScene(scene);
        stage.show();

        return scene;
    }


}
