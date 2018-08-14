package parameters;

import model.Sample;
import qrs.Normalization;
import utils.Math;

import java.util.ArrayList;
import java.util.List;

public class PrematureVentricularContractions {

    public static float[] energy(float[] input) {
        int windowSize = 100 / (int) Math.toMillisecond(Math.SAMPLING_PERIOD);
        input = Normalization.normalize(input);
        float[] output = new float[input.length - windowSize];
        for (int i = 0; i < input.length - windowSize; i++) {
            for (int j = i; j < i + windowSize; j++) {
                output[i] += input[j] * input[j];
            }
        }
        return output;
    }

    private static float prematurity(float intervalRR, float averageIntervalRR) {
        return (averageIntervalRR - intervalRR) / averageIntervalRR;
    }

    private static float compensatoryPause(float intervalRR, float averageIntervalRR) {
        return (intervalRR - averageIntervalRR) / averageIntervalRR;
    }

    public static List<Sample> detectPVCs(List<Sample> intervalsRR, float averageIntervalRR) {
        for (int i = 1; i < intervalsRR.size(); i++) {
            if (prematurity(intervalsRR.get(i - 1).getValue(), averageIntervalRR) > 0.15f &&
                    compensatoryPause(intervalsRR.get(i).getValue(), averageIntervalRR) > 0.15f) {
                intervalsRR.get(i).setPVC(true);
            }
        }
        return intervalsRR;
    }
}
