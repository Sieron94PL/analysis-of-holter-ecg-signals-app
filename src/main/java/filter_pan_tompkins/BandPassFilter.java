package filter_pan_tompkins;

public class BandPassFilter {

    public static float[] filter(float[] input) {
        return HighPassFilter.filter(LowPassFilter.filter(input));
    }

}
