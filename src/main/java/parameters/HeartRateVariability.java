package parameters;

import model.Sample;

import java.util.List;

public class HeartRateVariability {

    private static float SAMPLING_FREQUENCY = 128.0f;
    private static float SAMPLING_PERIOD = 1.0f / SAMPLING_FREQUENCY;

    public static float toMillisecond(float value) {
        return 1000.0f * value;
    }

    private static float intervalRR(Long firstPeak, Long secondPeak) {
        return toMillisecond((secondPeak - firstPeak) * SAMPLING_PERIOD);
    }

    public static float[] getIntervalsRR(List<Sample> peaks) {
        float[] intervalsRR = new float[peaks.size() - 1];
        Long firstPeak, secondPeak;
        for (int i = 1; i < peaks.size(); i++) {
            secondPeak = peaks.get(i - 1).getId();
            firstPeak = peaks.get(i).getId();
            intervalsRR[i - 1] = intervalRR(secondPeak, firstPeak);
        }
        return intervalsRR;
    }


}
