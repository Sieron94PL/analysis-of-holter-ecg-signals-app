package parameters;

import model.Sample;

import java.util.List;

public class HeartRateTurbulence {

    private static float TO(float RR1, float RR2, float RR_1, float RR_2) {
        return (((RR1 + RR2) - (RR_2 + RR_1)) / (RR_2 + RR_1)) * 100.0f;
    }

    private static float TS(float RR4, float RR3, float RR1, float RR0) {
        return (2 * RR4 + RR3 - RR1 - 2 * RR0) / 10;
    }

    public static float averageTO(List<Sample> intervalsRR) {
        float sum = 0.0f;
        int sizePVCs = 0;
        for (int i = 0; i < intervalsRR.size(); i++) {
            if (intervalsRR.get(i).isPVC()) {
                sum += intervalsRR.get(i).getTO();
                sizePVCs++;
            }
        }
        return sum / sizePVCs;
    }

    public static float averageTS(List<Sample> intervalsRR) {
        float sum = 0.0f;
        int sizePVCs = 0;
        for (int i = 0; i < intervalsRR.size(); i++) {
            if (intervalsRR.get(i).isPVC()) {
                sum += intervalsRR.get(i).getTS();
                sizePVCs++;
            }
        }
        return sum / sizePVCs;
    }

    public static List<Sample> calculateTO(List<Sample> intervalsRR) {
        for (int i = 2; i < intervalsRR.size() - 2; i++) {
            if (intervalsRR.get(i).isPVC()) {
                float RR1 = intervalsRR.get(i + 1).getValue();
                float RR2 = intervalsRR.get(i + 2).getValue();
                float RR_1 = intervalsRR.get(i - 2).getValue();
                float RR_2 = intervalsRR.get(i - 3).getValue();
                intervalsRR.get(i).setTO(TO(RR1, RR2, RR_1, RR_2));
            }
        }
        return intervalsRR;
    }

    public static List<Sample> calculateTS(List<Sample> intervalsRR) {
        float max;
        for (int i = 0; i < intervalsRR.size() - 15; i++) {
            if (intervalsRR.get(i).isPVC()) {
                max = 0.0f;
                for (int j = i; j < i + 15; j++) {
                    float RR0 = intervalsRR.get(j + 1).getValue();
                    float RR1 = intervalsRR.get(j + 2).getValue();
                    float RR3 = intervalsRR.get(j + 4).getValue();
                    float RR4 = intervalsRR.get(j + 5).getValue();
                    if (max < TS(RR4, RR3, RR1, RR0)) {
                        max = TS(RR4, RR3, RR1, RR0);
                        intervalsRR.get(i).setTS(max);
                    }
                }
            }
        }
        return intervalsRR;
    }


}
