package parameters;

import model.Sample;
import utils.Math;

import java.util.ArrayList;
import java.util.List;

public class HeartRateVariability {

    private static float intervalRR(int firstPeak, int secondPeak, float samplingFrequency) {
        return java.lang.Math.round(Math.toMillisecond((secondPeak - firstPeak) * Math.samplingPeriod(samplingFrequency)));
    }

    public static List<Sample> getIntervalsRR(List<Sample> peaks, float samplingFrequency) {
        List<Sample> intervalsRR = new ArrayList<>();
        int firstPeak, secondPeak;
        for (int i = 1; i < peaks.size(); i++) {
            firstPeak = peaks.get(i - 1).getId();
            secondPeak = peaks.get(i).getId();
            intervalsRR.add(new Sample(firstPeak, intervalRR(firstPeak, secondPeak, samplingFrequency)));
        }
        return intervalsRR;
    }

    public static float SDNN(List<Sample> intervalsRR, float averageIntervalsRR) {
        float sum = 0.0f;
        for (int i = 0; i < intervalsRR.size(); i++) {
            sum += (averageIntervalsRR - intervalsRR.get(i).getValue()) * (averageIntervalsRR - intervalsRR.get(i).getValue());
        }
        return (float) java.lang.Math.sqrt(1.0f / (intervalsRR.size() - 1.0f) * sum);
    }

    public static void displayIntervalsRR(List<Sample> intervalsRR) {
        System.out.println("Intervals RR");
        for (int i = 0; i < intervalsRR.size(); i++) {
            System.out.println("intervalRR " + intervalsRR.get(i).getId() + " = " + intervalsRR.get(i).getValue() + "ms "
                    + intervalsRR.get(i).isPVC());
        }
    }
}
