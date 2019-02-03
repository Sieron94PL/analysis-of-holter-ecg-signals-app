package qrs;

import utils.Math;

import java.util.Arrays;

public class Normalization {

    public static float[] normalize(float[] input, float samplingFrequency) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            int start = i;
            int stop = start + (int) (samplingFrequency * 4.0f);
            float max = Math.max(Arrays.copyOfRange(input, start, stop));
            float min = Math.min(Arrays.copyOfRange(input, start, stop));
            stop = (stop > input.length - 1) ? input.length - 1 : stop;
            while (i <= stop) {
                output[i] = (input[i] - min) / (max - min);
                i++;
            }
        }
        return output;
    }

    public static float[] normalize(float[] input) {
        float[] output = new float[input.length];
        float max = Math.max(input);
        float min = Math.min(input);
        for (int i = 0; i < input.length; i++) {
            output[i] = (input[i] - min) / (max - min);
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
