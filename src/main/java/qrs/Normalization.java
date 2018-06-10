package qrs;

public class Normalization {


    public static float[] normalize(float[] input) {
        float[] output = new float[input.length];
        float max = max(input);
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] / max;
        }
        return output;

    }

    public static float[] cancelDC(float[] input) {
        float[] output = new float[input.length];
        float mean = mean(input);

        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] - mean;
        }
        return output;
    }

    private static float mean(float[] input) {
        float sum = 0.0f;
        for (int i = 0; i < input.length; i++) {
            sum += input[i];
        }
        return sum / input.length;
    }

    private static float max(float[] input) {
        float max = input[0];
        for (int i = 1; i < input.length; i++) {
            if (max < Math.abs(input[i])) {
                max = Math.abs(input[i]);
            }
        }
        return max;
    }
}
