package filters.pantompkins;

public class BandPassFilter {

    public static float[] filter(float[] input) {
        return HighPassFilter.filter(LowPassFilter.filter(input));
    }

}
