package qrs_detection;

public class Power {

    public static float[] power(float[] input) {
        float[] output = new float[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] * input[i];
        }

        return output;
    }

}
