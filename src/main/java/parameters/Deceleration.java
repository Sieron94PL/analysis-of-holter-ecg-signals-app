package parameters;

import model.Sample;

import java.util.List;

public class Deceleration {

    public static float deceleration(List<Sample> intervalsRR) {
        float X0 = 0.0f;
        float X1 = 0.0f;
        float X_1 = 0.0f;
        float X_2 = 0.0f;
        int size = 0;

        for (int i = 2; i < intervalsRR.size() - 1; i++) {
            if (intervalsRR.get(i).getValue() > intervalsRR.get(i - 1).getValue()) {
                X0 += intervalsRR.get(i).getValue();
                X1 += intervalsRR.get(i + 1).getValue();
                X_1 += intervalsRR.get(i - 1).getValue();
                X_2 += intervalsRR.get(i - 2).getValue();
                size++;
            }
        }
        X0 /= size;
        X1 /= size;
        X_1 /= size;
        X_2 /= size;

        return (X0 + X1 - X_1 - X_2) / 4;
    }



}
