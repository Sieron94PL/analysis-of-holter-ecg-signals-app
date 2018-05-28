package filter;

public class LowPassFilter {

    /**
     * RC = 1 / 2PI * fc
     * α := dt / (RC + dt)
     *
     * @param samplingFrequency
     * @param cuttoffFrequency
     * @return
     */
    private static float alpha(float samplingFrequency, float cuttoffFrequency) {
        float RC = (float) (1 / (2 * Math.PI * cuttoffFrequency));
        float dt = 1 / samplingFrequency;
        return dt / (RC + dt);
    }

    /**
     * y[i] := y[i-1] + α * (x[i] - y[i-1])
     *
     * @param input
     * @param samplingFrequency
     * @param cuttoffFrequency
     * @return
     */
    public static float[] filter(float[] input, float samplingFrequency, float cuttoffFrequency) {
        float[] output = new float[input.length - 1];
        float alpha = alpha(samplingFrequency, cuttoffFrequency);

        output[0] = alpha * input[0];

        for (int i = 1; i < input.length - 1; i++) {
            output[i] = output[i - 1] + alpha * (input[i] - output[i - 1]);
        }
        return output;
    }

}
