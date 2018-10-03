package parameters;

import model.Sample;
import qrs.Normalization;
import utils.Math;

import java.util.ArrayList;
import java.util.List;

public class PrematureVentricularContractions {

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
