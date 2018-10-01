package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.IntervalRR;
import model.Sample;

import java.util.List;

public class HeartRateVariabilityChart {

    private static ObservableList<IntervalRR> tableViewData(List<Sample> intervalsRR) {
        ObservableList<IntervalRR> tableViewData = FXCollections.observableArrayList();
        for (int i = 0; i < intervalsRR.size(); i++) {
            String beatNo = String.valueOf(i + 1);
            String duration = String.valueOf(intervalsRR.get(i).getValue());
            tableViewData.add(new IntervalRR(beatNo, duration));
        }
        return tableViewData;
    }

    public static TableView tableViewIntervalsRR(List<Sample> intervalsRR) {
        TableView<IntervalRR> tableViewIntervalsRR = new TableView();
        tableViewIntervalsRR.setPrefWidth(150);

        TableColumn beatNumberColumn = new TableColumn("Beat no.");
        beatNumberColumn.setCellValueFactory(new PropertyValueFactory<IntervalRR, String>("beatNo"));

        TableColumn durationColumn = new TableColumn("Duration[ms]");
        durationColumn.setCellValueFactory(new PropertyValueFactory<IntervalRR, String>("duration"));

        tableViewIntervalsRR.setItems(tableViewData(intervalsRR));
        tableViewIntervalsRR.getColumns().addAll(beatNumberColumn, durationColumn);
        return tableViewIntervalsRR;
    }

    private static LineChart lineChartHeartRateVariability(XYChart.Series intervalsRRSeries) {
        final NumberAxis heartRateVariabilityAxisX = new NumberAxis(0, intervalsRRSeries.getData().size(), 10);
        final NumberAxis heartRateVariabilityAxisY = new NumberAxis();

        LineChart<Number, Number> lineChartHeartRateVariability = new LineChart<Number, Number>(heartRateVariabilityAxisX, heartRateVariabilityAxisY);
        lineChartHeartRateVariability.setCreateSymbols(false);
        lineChartHeartRateVariability.getData().add(intervalsRRSeries);
        return lineChartHeartRateVariability;
    }

    private static XYChart.Series intervalsRRSeries(List<Sample> intervalsRR) {
        XYChart.Series intervalsRRSeries = new XYChart.Series();
        for (int i = 0; i < intervalsRR.size(); i++) {
            intervalsRRSeries.getData().add(new XYChart.Data<>(i, intervalsRR.get(i).getValue()));
        }
        return intervalsRRSeries;
    }

    public static void show(List<Sample> intervalsRR) {
        XYChart.Series intervalsRRSeries = intervalsRRSeries(intervalsRR);
        LineChart lineChartHeartRateVariability = lineChartHeartRateVariability(intervalsRRSeries);
        lineChartHeartRateVariability.setLegendVisible(false);

        HBox hBox = new HBox();
        hBox.setHgrow(lineChartHeartRateVariability, Priority.ALWAYS);
        hBox.getChildren().addAll(tableViewIntervalsRR(intervalsRR), lineChartHeartRateVariability);

        Scene scene = new Scene(hBox, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Heart Rate Variability Analysis");
        stage.setScene(scene);
        stage.show();
    }

}
