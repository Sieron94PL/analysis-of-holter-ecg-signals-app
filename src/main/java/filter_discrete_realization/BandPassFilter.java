package filter_discrete_realization;

public class BandPassFilter {

    public static float[] filter(float[] input) {
        return HighPassFilter.filter(LowPassFilter.filter(input, 128, 11),
                128, 5);
    }
}
