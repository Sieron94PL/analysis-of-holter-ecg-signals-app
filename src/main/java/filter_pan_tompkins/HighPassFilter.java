package filter_pan_tompkins;

public class HighPassFilter {

    /**
     * Transfer function:
     * H(z) = (-1 + 32z^-16 + z^-32) / (1 + z^-1)
     * Difference equation:
     * y(n) = 32 * x(n - 16) - [y(n - 1) + x(n) - x(n - 32)]
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


    /**
     * http://mule.cslab.ece.ntua.gr/docs/c8.pdf
     * y(n) = y(n - 1) - 1/32 * x(n) + x(n - 16) - x(n - 17) + 1/32 * x(n - 32)
     *
     * @param input
     * @return
     */
    public static float[] filterByC8(float[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            if (i < 32)
                output[i] = input[i];
            else
                output[i] = output[i - 1] - (1.0f / 32.0f) * input[i] + input[i - 16] - input[i - 17] + (1.0f / 32.0f) * input[i - 32];
        }
        return output;
    }


}
