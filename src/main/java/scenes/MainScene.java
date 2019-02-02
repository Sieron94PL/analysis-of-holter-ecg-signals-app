package scenes;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Sample;
import utils.Math;

import java.util.ArrayList;
import java.util.List;

public class MainScene {

    public static List<LineChart<Number, Number>> showInputSignal(int start, int stop, float samplingFrequency, float[] inputSignal, int step) {

        List<LineChart<Number, Number>> inputSignalLineCharts = new ArrayList<>();

        double tickUnitAxisX = 1.0;

        double lowerBoundAxisY = -0.5;
        double upperBoundAxisY = 1.2;
        double tickUnitAxisY = 0.1;

        int seconds = (int) Math.sampleToSecond(start, samplingFrequency);

        final NumberAxis firstLineChartX = new NumberAxis(seconds, seconds + 15, tickUnitAxisX);
        final NumberAxis secondLineChartX = new NumberAxis(seconds + 15, seconds + 30, tickUnitAxisX);
        final NumberAxis thirdLineChartX = new NumberAxis(seconds + 30, seconds + 45, tickUnitAxisX);
        final NumberAxis fourthLineChartX = new NumberAxis(seconds + 45, seconds + 60, tickUnitAxisX);

        firstLineChartX.setTickLabelFormatter(new NumberAxis.DefaultFormatter(firstLineChartX) {
            @Override
            public String toString(Number object) {
                return Math.secondsToLocalTime(object.intValue());
            }
        });
        secondLineChartX.setTickLabelFormatter(new NumberAxis.DefaultFormatter(secondLineChartX) {
            @Override
            public String toString(Number object) {
                return Math.secondsToLocalTime(object.intValue());
            }
        });
        thirdLineChartX.setTickLabelFormatter(new NumberAxis.DefaultFormatter(thirdLineChartX) {
            @Override
            public String toString(Number object) {
                return Math.secondsToLocalTime(object.intValue());
            }
        });
        fourthLineChartX.setTickLabelFormatter(new NumberAxis.DefaultFormatter(fourthLineChartX) {
            @Override
            public String toString(Number object) {
                return Math.secondsToLocalTime(object.intValue());
            }
        });


        fourthLineChartX.setLabel("Time [seconds]");
        fourthLineChartX.setStyle("-fx-font-style: italic;");

        final NumberAxis firstLineChartY = new NumberAxis(lowerBoundAxisY, upperBoundAxisY, tickUnitAxisY);
        final NumberAxis secondLineChartY = new NumberAxis(lowerBoundAxisY, upperBoundAxisY, tickUnitAxisY);
        final NumberAxis thirdLineChartY = new NumberAxis(lowerBoundAxisY, upperBoundAxisY, tickUnitAxisY);
        final NumberAxis fourthLineChartY = new NumberAxis(lowerBoundAxisY, upperBoundAxisY, tickUnitAxisY);

        final LineChart<Number, Number> firstLineChartInputSignal = new LineChart<Number, Number>(firstLineChartX, firstLineChartY);
        final LineChart<Number, Number> secondLineChartInputSignal = new LineChart<Number, Number>(secondLineChartX, secondLineChartY);
        final LineChart<Number, Number> thirdLineChartInputSignal = new LineChart<Number, Number>(thirdLineChartX, thirdLineChartY);
        final LineChart<Number, Number> fourthLineChartInputSignal = new LineChart<Number, Number>(fourthLineChartX, fourthLineChartY);
        firstLineChartInputSignal.setAnimated(false);
        firstLineChartInputSignal.setCreateSymbols(false);
        secondLineChartInputSignal.setAnimated(false);
        secondLineChartInputSignal.setCreateSymbols(false);
        thirdLineChartInputSignal.setAnimated(false);
        thirdLineChartInputSignal.setCreateSymbols(false);
        fourthLineChartInputSignal.setAnimated(false);
        fourthLineChartInputSignal.setCreateSymbols(false);

        XYChart.Series firstLineChartInputSignalSeries = new XYChart.Series();
        XYChart.Series secondLineChartInputSignalSeries = new XYChart.Series();
        XYChart.Series thirdLineChartInputSignalSeries = new XYChart.Series();
        XYChart.Series fourthLineChartInputSignalSeries = new XYChart.Series();

        float samplingPeriod = Math.samplingPeriod(samplingFrequency);

        for (int i = start; i < stop; i++) {
            int localStep = (int) (samplingFrequency * 60) / 4;
            XYChart.Data<Number, Number> data = new XYChart.Data<>(i * samplingPeriod, inputSignal[i - start + step]);
            if (i <= start + localStep)
                firstLineChartInputSignalSeries.getData().add(data);
            else if (i > start + localStep && i <= start + (2 * localStep))
                secondLineChartInputSignalSeries.getData().add(data);
            else if (i > start + (2 * localStep) && i <= start + (3 * localStep))
                thirdLineChartInputSignalSeries.getData().add(data);
            else
                fourthLineChartInputSignalSeries.getData().add(data);
        }

        firstLineChartInputSignal.getData().add(firstLineChartInputSignalSeries);
        secondLineChartInputSignal.getData().add(secondLineChartInputSignalSeries);
        thirdLineChartInputSignal.getData().add(thirdLineChartInputSignalSeries);
        fourthLineChartInputSignal.getData().add(fourthLineChartInputSignalSeries);


        inputSignalLineCharts.add(firstLineChartInputSignal);
        inputSignalLineCharts.add(secondLineChartInputSignal);
        inputSignalLineCharts.add(thirdLineChartInputSignal);
        inputSignalLineCharts.add(fourthLineChartInputSignal);

        return inputSignalLineCharts;
    }


    public static List<LineChart<Number, Number>> showPeaks(int start, int step, float samplingFrequency, float[] inputSignal, boolean enabled, List<Sample> peaks, List<LineChart<Number, Number>> inputSignalLineCharts) {

        XYChart.Series<Number, Number> firstLineChartPeakSeries = new XYChart.Series();
        XYChart.Series secondLineChartPeakSeries = new XYChart.Series();
        XYChart.Series thirdLineChartPeakSeries = new XYChart.Series();
        XYChart.Series fourthLineChartPeakSeries = new XYChart.Series();


        XYChart.Series<Number, Number> firstLineChartPVCsSeries = new XYChart.Series();
        XYChart.Series<Number, Number> secondLineChartPVCsSeries = new XYChart.Series();
        XYChart.Series<Number, Number> thirdLineChartPVCsSeries = new XYChart.Series();
        XYChart.Series<Number, Number> fourthLineChartPVCsSeries = new XYChart.Series();

        int peakId;
        for (int i = 0; i < peaks.size(); i++) {
            peakId = peaks.get(i).getId();
            int localStep = (int) (samplingFrequency * 60) / 4;
            float seconds = Math.sampleToSecond(peakId + start - step, samplingFrequency);
            XYChart.Data<Number, Number> data = new XYChart.Data<>(seconds, inputSignal[peakId]);

            if (peakId <= localStep + step) {
                if (peaks.get(i).isPVC()) {
                    firstLineChartPVCsSeries.getData().add(new XYChart.Data<>(seconds, inputSignal[peakId]));
                    firstLineChartPVCsSeries.getData().add(linePVC(data, seconds, Color.RED));
                } else
                    firstLineChartPeakSeries.getData().add(data);
            } else if (peakId > localStep + step && peakId <= (2 * localStep) + step) {
                if (peaks.get(i).isPVC()) {
                    secondLineChartPVCsSeries.getData().add(new XYChart.Data<>(seconds, inputSignal[peakId]));
                    secondLineChartPVCsSeries.getData().add(linePVC(data, seconds, Color.RED));
                } else
                    secondLineChartPeakSeries.getData().add(data);

            } else if (peakId > (2 * localStep) + step && peakId <= (3 * localStep) + step)
                if (peaks.get(i).isPVC()) {
                    thirdLineChartPVCsSeries.getData().add(new XYChart.Data<>(seconds, inputSignal[peakId]));
                    thirdLineChartPVCsSeries.getData().add(linePVC(data, seconds, Color.RED));
                } else
                    thirdLineChartPeakSeries.getData().add(data);

            else {
                if (peaks.get(i).isPVC()) {
                    fourthLineChartPVCsSeries.getData().add(new XYChart.Data<>(seconds, inputSignal[peakId]));
                    fourthLineChartPVCsSeries.getData().add(linePVC(data, seconds, Color.RED));
                } else
                    fourthLineChartPeakSeries.getData().add(data);
            }
        }

        if (enabled) {
            inputSignalLineCharts.get(0).setCreateSymbols(true);
            inputSignalLineCharts.get(1).setCreateSymbols(true);
            inputSignalLineCharts.get(2).setCreateSymbols(true);
            inputSignalLineCharts.get(3).setCreateSymbols(true);

            if (inputSignalLineCharts.get(0).getData().size() == 1) {
                inputSignalLineCharts.get(0).getData().add(firstLineChartPeakSeries);
                inputSignalLineCharts.get(0).getData().add(firstLineChartPVCsSeries);
                inputSignalLineCharts.get(1).getData().add(secondLineChartPeakSeries);
                inputSignalLineCharts.get(1).getData().add(secondLineChartPVCsSeries);
                inputSignalLineCharts.get(2).getData().add(thirdLineChartPeakSeries);
                inputSignalLineCharts.get(2).getData().add(thirdLineChartPVCsSeries);
                inputSignalLineCharts.get(3).getData().add(fourthLineChartPeakSeries);
                inputSignalLineCharts.get(3).getData().add(fourthLineChartPVCsSeries);

                addTooltipPVCs(firstLineChartPVCsSeries, samplingFrequency, peaks, start, step);
                addTooltipPVCs(secondLineChartPVCsSeries, samplingFrequency, peaks, start, step);
                addTooltipPVCs(thirdLineChartPVCsSeries, samplingFrequency, peaks, start, step);
                addTooltipPVCs(fourthLineChartPVCsSeries, samplingFrequency, peaks, start, step);

                addPeaksTooltips(firstLineChartPeakSeries, samplingFrequency, peaks, start, step);
                addPeaksTooltips(secondLineChartPeakSeries, samplingFrequency, peaks, start, step);
                addPeaksTooltips(thirdLineChartPeakSeries, samplingFrequency, peaks, start, step);
                addPeaksTooltips(fourthLineChartPeakSeries, samplingFrequency, peaks, start, step);
            }

        } else if (inputSignalLineCharts.get(0).getData().size() > 1) {
            inputSignalLineCharts.get(0).getData().remove(2);
            inputSignalLineCharts.get(1).getData().remove(2);
            inputSignalLineCharts.get(2).getData().remove(2);
            inputSignalLineCharts.get(3).getData().remove(2);
            inputSignalLineCharts.get(0).getData().remove(1);
            inputSignalLineCharts.get(1).getData().remove(1);
            inputSignalLineCharts.get(2).getData().remove(1);
            inputSignalLineCharts.get(3).getData().remove(1);
        }


        return inputSignalLineCharts;
    }

    private static void addTooltipPVCs(XYChart.Series<Number, Number> series, float samplingFrequency, List<Sample> peaks, int start, int step) {
        for (XYChart.Data<Number, Number> d : series.getData()) {
            Sample sample = Sample.findById(Math.secondsToSample(d.getXValue().floatValue(), samplingFrequency) - start + step, peaks);
            String info =
                    "Peak no: " + Math.secondsToSample(d.getXValue().floatValue(), samplingFrequency) + "\n" +
                            "Time: " + Math.secondsToLocalTime((int) Math.sampleToSecond(sample.getId() + start, samplingFrequency)) + "\n" +
                            "TO: " + sample.getTO() + "\n" +
                            "TS: " + sample.getTS() + "\n" +
                            "Duration: " + sample.getValue() + "ms";

            Tooltip.install(d.getNode(), new Tooltip(info));
            d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHoverPVCs"));
            d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHoverPVCs"));
        }
    }

    private static void addPeaksTooltips(XYChart.Series<Number, Number> series, float samplingFrequency, List<Sample> peaks, int start, int step) {
        for (XYChart.Data<Number, Number> d : series.getData()) {
            Sample sample = Sample.findById(Math.secondsToSample(d.getXValue().floatValue(), samplingFrequency) - start + step, peaks);
            String info =
                    "Peak no: " + sample.getId() + "\n" +
                            "Time: " + Math.secondsToLocalTime((int) Math.sampleToSecond(sample.getId() + start, samplingFrequency)) + "\n" +
                            "Duration: " + sample.getValue() + "ms" + "\n";

            Tooltip tooltip = new Tooltip(info);
            Tooltip.install(d.getNode(), tooltip);

            d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHoverPeaks"));
            d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHoverPeaks"));
        }
    }

    private static XYChart.Data<Number, Number> linePVC(XYChart.Data<Number, Number> data, float seconds, Color color) {
        Line line = new Line(seconds, -100, seconds, 100);
        line.setStroke(color);
        line.setStrokeWidth(1);
        data.setNode(line);
        return data;
    }
}
