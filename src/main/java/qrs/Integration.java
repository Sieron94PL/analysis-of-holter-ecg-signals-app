package qrs;

import utils.Math;

public class Integration {

    public static float[] integration(float[] input) {
        int windowSize = 150 / (int) Math.toMillisecond(Math.SAMPLING_PERIOD);
        float[] output = new float[input.length - windowSize];
        for (int i = windowSize; i < input.length; i++) {
            for (int j = i - windowSize; j < i; j++) {
                output[i - windowSize] += input[j];
            }
            output[i - windowSize] = (1.0f / windowSize + 1.0f) * output[i - windowSize];
        }
        return output;
    }
}
