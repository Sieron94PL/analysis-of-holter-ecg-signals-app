package qrs;

public class Derivative {

    public static float[] derivative(float[] input, float samplingFrequency) {
        float[] output = new float[input.length];
        output[0] = input[0];
        for (int i = 1; i < input.length; i++) {
            output[i] = samplingFrequency * (input[i] - input[i - 1]);
        }
        return output;
    }

}
