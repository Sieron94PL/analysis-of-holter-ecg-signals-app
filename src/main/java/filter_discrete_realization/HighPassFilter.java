package filter_discrete_realization;

public class HighPassFilter {

    /**
     * α := RC / (RC + dt)
     *
     * @param samplingFrequency
     * @param cuttoffFrequency
     * @return
     */
    private static float alpha(float samplingFrequency, float cuttoffFrequency) {
        float RC = (float) (1 / (2 * Math.PI * cuttoffFrequency));
        float dt = 1 / samplingFrequency;
        return RC / (RC + dt);
    }

    /**
     * y[i] := α * y[i-1] + α * (x[i] - x[i-1])
     *
     * @param input
     * @param samplingFrequency
     * @param cuttoffFrequency
     * @return
     */
    public static float[] filter(float[] input, float samplingFrequency, float cuttoffFrequency) {
        float[] output = new float[input.length - 1];
        float alpha = alpha(samplingFrequency, cuttoffFrequency);

        output[0] = input[0];
        for (int i = 1; i < input.length - 1; i++) {
            output[i] = alpha * (output[i - 1] + input[i] - input[i - 1]);
        }

        return output;
    }
}
