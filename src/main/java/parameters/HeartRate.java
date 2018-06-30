package parameters;

public class HeartRate {

    private static float averageIntervalRR(float[] intervalsRR) {
        float sum = 0.0f;
        for (int i = 0; i < intervalsRR.length; i++) {
            sum += intervalsRR[i];
        }
        return sum / intervalsRR.length;
    }

    public static float getHeartRate(float[] intervalsRR) {
        return Math.round(utils.Math.ONE_MINUTE / averageIntervalRR(intervalsRR));
    }
}
