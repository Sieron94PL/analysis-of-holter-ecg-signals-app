package filter_pan_tompkins;

public class HighPassFilter {

    /**
     * Transfer function:
     * H(z) = (-1 + 32z^-16 + z^-32) / (1 + z^-1)
     * Difference equation:
     * y(n) = 32*x(n-16) - [y[n-1] + x[n] - x[n-32]
     *
     * @param input
     * @return
     */
    public static float[] filter(float[] input) {
        float[] output = new float[input.length];

        for (int i = 0; i < input.length; i++) {
            if (i < 32)
                output[i] = input[i];
            else
                output[i] = 32 * input[i - 16] - (output[i - 1] + input[i] - input[i - 32]);
        }

        return output;
    }
}
