package filter;

public class BandPassFilter {

    public static float[] filter(float[] input, float samplingFrequency, float leftCuttoffFrequency, float rightCuttoffFrequency) {
        float[] outputLP = LowPassFilter.filter(input, samplingFrequency, rightCuttoffFrequency);
        return HighPassFilter.filter(outputLP, samplingFrequency, leftCuttoffFrequency);
    }
}
