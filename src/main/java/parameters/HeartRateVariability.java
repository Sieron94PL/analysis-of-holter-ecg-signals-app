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
            float intervalRR = intervalRR(firstPeak, secondPeak, samplingFrequency);
            if (intervalRR < 2000.0f && intervalRR > 300.0f) {
                intervalsRR.add(new Sample(firstPeak, intervalRR(firstPeak, secondPeak, samplingFrequency)));
            }
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

}
