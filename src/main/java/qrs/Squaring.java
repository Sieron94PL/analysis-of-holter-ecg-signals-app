package qrs;

public class Squaring {

    public static float[] squaring(float[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] * input[i];
        }
        return output;
    }

}
