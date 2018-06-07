package filter_pan_tompkins;

public class HighPassFilter {

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

        for (int i = 32; i < input.length; i++) {
            if (i < 32) {
                output[i] = input[i];
            }
            output[i] = 32 * input[i - 16] - (output[i - 1] + input[i] - input[i - 32]);
        }

        return output;
    }
}
