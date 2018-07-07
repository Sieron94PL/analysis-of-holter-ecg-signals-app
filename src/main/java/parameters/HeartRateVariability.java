package parameters;

import model.Sample;
import utils.Math;

import java.util.List;

public class HeartRateVariability {

    private static float intervalRR(int firstPeak, int secondPeak) {
        return Math.toMillisecond((secondPeak - firstPeak) * Math.SAMPLING_PERIOD);
    }

    public static float[] getIntervalsRR(List<Sample> peaks) {
        float[] intervalsRR = new float[peaks.size() - 1];
        int firstPeak, secondPeak;
        for (int i = 1; i < peaks.size(); i++) {
            secondPeak = peaks.get(i - 1).getId();
            firstPeak = peaks.get(i).getId();
            intervalsRR[i - 1] = intervalRR(secondPeak, firstPeak);
        }
        return intervalsRR;
    }

    public static void displayIntervalsRR(float[] intervalsRR) {
        for (int i = 0; i < intervalsRR.length; i++) {
            System.out.println("intervalRR " + (i + 1) + " = " + intervalsRR[i] + "ms");
        }
    }


}
