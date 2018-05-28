package qrs_detection;

public class Integration {

    public static float[] integration(float[] input) {
        float[] output = new float[input.length - 24];

        for (int i = 24; i < input.length; i++) {
            for (int j = i - 24; j < i; j++) {
                output[i - 24] += input[j];
            }
            output[i - 24] = 0.04f * output[i - 24];
        }
        return output;
    }
}
