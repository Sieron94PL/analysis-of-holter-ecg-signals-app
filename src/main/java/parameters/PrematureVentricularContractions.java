package parameters;

import utils.Math;

public class PrematureVentricularContractions {

    public static float[] energy(float[] input) {
        int windowSize = 100 / (int) Math.toMillisecond(Math.SAMPLING_PERIOD);
        float[] output = new float[input.length - windowSize];
        for (int i = 0; i < input.length - windowSize; i++) {
            for (int j = i; j < i + windowSize; j++) {
                output[i] += input[j] * input[j];
            }
        }
        return output;
    }


}
