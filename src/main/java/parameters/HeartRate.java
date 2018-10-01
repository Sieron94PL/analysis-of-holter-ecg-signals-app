package parameters;

import model.Sample;

import java.util.List;

public class HeartRate {

    public static float averageIntervalRR(List<Sample> intervalsRR) {
        float sum = 0.0f;
        for (int i = 0; i < intervalsRR.size(); i++) {
            sum += intervalsRR.get(i).getValue();
        }
        return sum / intervalsRR.size();
    }

    public static float getHeartRate(float averageIntervalRR) {
        return Math.round(utils.Math.ONE_MINUTE / averageIntervalRR);
    }

}
