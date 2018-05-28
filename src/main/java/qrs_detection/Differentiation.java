package qrs_detection;

public class Differentiation {

    private static final int SAMPLING_FREQUENCE = 128;

    public static float[] differentiation(float[] input) {

        float[] output = new float[input.length];

        output[0] = input[0];

        for (int i = 1; i < input.length; i++) {
            output[i] = SAMPLING_FREQUENCE * (input[i] - input[i - 1]);
        }

        return output;

    }
}
