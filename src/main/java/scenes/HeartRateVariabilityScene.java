package scenes;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.IntervalRRData;
import model.Sample;

import java.util.List;

public class HeartRateVariabilityScene {

    private static ObservableList<IntervalRRData> tableViewData(List<Sample> intervalsRR) {
        ObservableList<IntervalRRData> tableViewData = FXCollections.observableArrayList();
        for (int i = 0; i < intervalsRR.size(); i++) {
            int beatNo = i + 1;
            float duration = intervalsRR.get(i).getValue();
            tableViewData.add(new IntervalRRData(new SimpleIntegerProperty(beatNo), new SimpleFloatProperty(duration)));
        }
        return tableViewData;
    }

    public static TableView tableViewIntervalsRR(List<Sample> intervalsRR) {
        TableView<IntervalRRData> tableViewIntervalsRR = new TableView();
        tableViewIntervalsRR.setPrefWidth(150);

        TableColumn beatNumberColumn = new TableColumn("Beat no.");
        beatNumberColumn.setCellValueFactory(new PropertyValueFactory<IntervalRRData, String>("beatNo"));

        TableColumn durationColumn = new TableColumn("Duration[ms]");
        durationColumn.setCellValueFactory(new PropertyValueFactory<IntervalRRData, String>("duration"));

        tableViewIntervalsRR.setItems(tableViewData(intervalsRR));
        tableViewIntervalsRR.getColumns().addAll(beatNumberColumn, durationColumn);
        return tableViewIntervalsRR;
    }

    private static LineChart lineChartHeartRateVariability(XYChart.Series intervalsRRSeries) {
        final NumberAxis heartRateVariabilityAxisX = new NumberAxis(0, intervalsRRSeries.getData().size(), 1);
        heartRateVariabilityAxisX.setLabel("Beat no. [-]");
        final NumberAxis heartRateVariabilityAxisY = new NumberAxis();
        heartRateVariabilityAxisY.setLabel("Interval RR [ms]");

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

    private static void addTooltipIntervalsRR(XYChart.Series<Number, Number> intervalsRRSeries) {
        int i = 0;
        for (XYChart.Data<Number, Number> d : intervalsRRSeries.getData()) {
            i++;
            String info = "Beat no.: " + i;
            info += "\nDuration: " + d.getYValue() + "ms";
            Tooltip tooltip = new Tooltip(info);
            Tooltip.install(d.getNode(), tooltip);
            d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHoverBeat"));
            d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHoverBeat"));
        }
    }

    public static void show(List<Sample> intervalsRR) {
        XYChart.Series<Number, Number> intervalsRRSeries = intervalsRRSeries(intervalsRR);
        LineChart lineChartHeartRateVariability = lineChartHeartRateVariability(intervalsRRSeries);
        lineChartHeartRateVariability.setLegendVisible(false);
        lineChartHeartRateVariability.setCreateSymbols(true);
        addTooltipIntervalsRR(intervalsRRSeries);

        HBox hBox = new HBox();
        hBox.setHgrow(lineChartHeartRateVariability, Priority.ALWAYS);
        hBox.getChildren().addAll(tableViewIntervalsRR(intervalsRR), lineChartHeartRateVariability);

        Scene scene = new Scene(hBox, 800, 600);

        scene.getStylesheets().add("heart-rate-variability-scene.css");
        Stage stage = new Stage();
        stage.setTitle("Heart Rate Variability Analysis");
        stage.setScene(scene);
        stage.show();
    }

}
