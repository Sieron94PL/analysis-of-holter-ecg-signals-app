package parameters;

import model.Sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Acceleration {

    private static boolean isAcceleration(float currentValue, float previousValue) {
        return currentValue < previousValue;
    }

    @SuppressWarnings("Duplicates")
    public static List<Sample> signalAveraging(List<Sample> intervalsRR) {
        float X0 = 0.0f;
        float X1 = 0.0f, X2 = 0.0f, X3 = 0.0f, X4 = 0.0f, X5 = 0.0f, X6 = 0.0f;
        float X_1 = 0.0f, X_2 = 0.0f, X_3 = 0.0f, X_4 = 0.0f, X_5 = 0.0f, X_6 = 0.0f;

        int accelerations = 0;

        List<Sample> signalAveraging = new ArrayList<>();

        for (int i = 6; i < intervalsRR.size() - 6; i++) {
            if (isAcceleration(intervalsRR.get(i).getValue(), intervalsRR.get(i - 1).getValue())) {
                X0 += intervalsRR.get(i).getValue();

                X_1 += intervalsRR.get(i - 1).getValue();
                X_2 += intervalsRR.get(i - 2).getValue();
                X_3 += intervalsRR.get(i - 3).getValue();
                X_4 += intervalsRR.get(i - 4).getValue();
                X_5 += intervalsRR.get(i - 5).getValue();
                X_6 += intervalsRR.get(i - 6).getValue();

                X1 += intervalsRR.get(i + 1).getValue();
                X2 += intervalsRR.get(i + 2).getValue();
                X3 += intervalsRR.get(i + 3).getValue();
                X4 += intervalsRR.get(i + 4).getValue();
                X5 += intervalsRR.get(i + 5).getValue();
                X6 += intervalsRR.get(i + 6).getValue();
                accelerations++;
            }
        }

        signalAveraging.add(new Sample(-6, X_6 / accelerations));
        signalAveraging.add(new Sample(-5, X_5 / accelerations));
        signalAveraging.add(new Sample(-4, X_4 / accelerations));
        signalAveraging.add(new Sample(-3, X_3 / accelerations));
        signalAveraging.add(new Sample(-2, X_2 / accelerations));
        signalAveraging.add(new Sample(-1, X_1 / accelerations));
        signalAveraging.add(new Sample(0, X0 / accelerations));
        signalAveraging.add(new Sample(1, X1 / accelerations));
        signalAveraging.add(new Sample(2, X2 / accelerations));
        signalAveraging.add(new Sample(3, X3 / accelerations));
        signalAveraging.add(new Sample(4, X4 / accelerations));
        signalAveraging.add(new Sample(5, X5 / accelerations));
        signalAveraging.add(new Sample(6, X6 / accelerations));

        return signalAveraging;

    }

    @SuppressWarnings("Duplicates")
    public static float acceleration(List<Sample> signalAveraging) {
        float X0 = Sample.findById(0, signalAveraging).getValue();
        float X1 = Sample.findById(1, signalAveraging).getValue();
        float X_1 = Sample.findById(-1, signalAveraging).getValue();
        float X_2 = Sample.findById(-2, signalAveraging).getValue();

        return (X0 + X1 - X_1 - X_2) / 4;
    }

}
