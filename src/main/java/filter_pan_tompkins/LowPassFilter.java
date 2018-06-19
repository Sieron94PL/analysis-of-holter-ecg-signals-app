package filter_pan_tompkins;

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
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            if (i < 12)
                output[i] = input[i];
            else
                output[i] = 2 * output[i - 1] - output[i - 2] + input[i] - 2 * input[i - 6] + input[i - 12];
        }
        return output;
    }
}
