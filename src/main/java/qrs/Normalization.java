package qrs;

import utils.Math;

public class Normalization {

    public static float[] normalize(float[] input) {
        float[] output = new float[input.length];
        float max = Math.max(input);
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] / max;
        }
        return output;
    }

    public static float[] cancelDC(float[] input) {
        float[] output = new float[input.length];
        float mean = Math.mean(input);
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] - mean;
        }
        return output;
    }

}
