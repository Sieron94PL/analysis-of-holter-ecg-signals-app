package filter_discrete_realization;

public class BandPassFilter {

    /**
     * @param input
     * @param samplingFrequency
     * @param leftCuttoffFrequency
     * @param rightCuttoffFrequency
     * @return
     */
    public static float[] filter(float[] input, float samplingFrequency, float leftCuttoffFrequency, float rightCuttoffFrequency) {
        float[] output = LowPassFilter.filter(input, samplingFrequency, rightCuttoffFrequency);
        return HighPassFilter.filter(output, samplingFrequency, leftCuttoffFrequency);
    }
}
