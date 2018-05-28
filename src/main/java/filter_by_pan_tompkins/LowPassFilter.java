package filter_by_pan_tompkins;

public class LowPassFilter {


    /**
     * Transfer function:
     * H(z) = (1 - z^-6)^2 / (1 - z^-1)^2
     * Difference equation:
     * y(n) = 2*y(n-1) - y(n - 2) + x(n) - 2*x(n-6) + x(n-12)
     *
     * @param input
     * @return
     */
    public static float[] filter(float[] input) {
        return null;
    }
}
