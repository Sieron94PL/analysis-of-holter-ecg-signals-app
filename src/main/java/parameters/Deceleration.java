package parameters;

import model.Sample;

import java.util.ArrayList;
import java.util.List;

public class Deceleration {

    private static boolean isDeceleration(float currentValue, float previousValue) {
        return currentValue > previousValue;
    }

    public static List<Sample> signalAveraging(List<Sample> intervalsRR) {
        float X0 = 0.0f;
        float X1 = 0.0f;
        float X2 = 0.0f;
        float X3 = 0.0f;
        float X4 = 0.0f;
        float X5 = 0.0f;
        float X6 = 0.0f;
        float X_1 = 0.0f;
        float X_2 = 0.0f;
        float X_3 = 0.0f;
        float X_4 = 0.0f;
        float X_5 = 0.0f;
        float X_6 = 0.0f;
        int decelerations = 0;

        for (int i = 6; i < intervalsRR.size() - 6; i++) {
            if (isDeceleration(intervalsRR.get(i).getValue(), intervalsRR.get(i - 1).getValue())) {
                X0 += intervalsRR.get(i).getValue();
                X1 += intervalsRR.get(i + 1).getValue();
                X_1 += intervalsRR.get(i - 1).getValue();
                X_2 += intervalsRR.get(i - 2).getValue();

                X_3 += intervalsRR.get(i - 3).getValue();
                X_4 += intervalsRR.get(i - 4).getValue();
                X_5 += intervalsRR.get(i - 5).getValue();
                X_6 += intervalsRR.get(i - 6).getValue();
                X2 += intervalsRR.get(i + 2).getValue();
                X3 += intervalsRR.get(i + 3).getValue();
                X4 += intervalsRR.get(i + 4).getValue();
                X5 += intervalsRR.get(i + 5).getValue();
                X6 += intervalsRR.get(i + 6).getValue();
                decelerations++;
            }
        }

        X0 /= decelerations;
        X1 /= decelerations;
        X2 /= decelerations;
        X3 /= decelerations;
        X4 /= decelerations;
        X5 /= decelerations;
        X6 /= decelerations;
        X_1 /= decelerations;
        X_2 /= decelerations;
        X_3 /= decelerations;
        X_4 /= decelerations;
        X_5 /= decelerations;
        X_6 /= decelerations;

        List<Sample> signalAveraging = new ArrayList<>();
        signalAveraging.add(new Sample(-6, X_6));
        signalAveraging.add(new Sample(-5, X_5));
        signalAveraging.add(new Sample(-4, X_4));
        signalAveraging.add(new Sample(-3, X_3));
        signalAveraging.add(new Sample(-2, X_2));
        signalAveraging.add(new Sample(-1, X_1));
        signalAveraging.add(new Sample(0, X0));
        signalAveraging.add(new Sample(1, X1));
        signalAveraging.add(new Sample(2, X2));
        signalAveraging.add(new Sample(3, X3));
        signalAveraging.add(new Sample(4, X4));
        signalAveraging.add(new Sample(5, X5));
        signalAveraging.add(new Sample(6, X6));

        return signalAveraging;

    }

    private static float findById(List<Sample> signalAveraging, int id) {
        for (int i = 0; i < signalAveraging.size(); i++) {
            if (signalAveraging.get(i).getId() == id) {
                return signalAveraging.get(i).getValue();
            }
        }
        return -1.0f;
    }

    public static float deceleration(List<Sample> signalAveraging) {
        float X0 = findById(signalAveraging, 0);
        float X1 = findById(signalAveraging, 1);
        float X_1 = findById(signalAveraging, -1);
        float X_2 = findById(signalAveraging, -2);

        return (X0 + X1 - X_1 - X_2) / 4;
    }


}
