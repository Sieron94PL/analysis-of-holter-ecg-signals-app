package parameters;

import model.Sample;

import java.util.List;

public class HeartRateTurbulence {

    private static float TO(float RR1, float RR2, float RR_1, float RR_2) {
        return (((RR1 + RR2) - (RR_2 + RR_1)) / (RR_2 + RR_1)) * 100.0f;
    }

    public static void calculate(List<Sample> intervalsRR) {
        float TO = 0.0f;
        for (int i = 2; i < intervalsRR.size(); i++) {
            if(intervalsRR.get(i).isPVC()){
                float RR1 = intervalsRR.get(i + 1).getValue();
                float RR2 = intervalsRR.get(i + 2).getValue();
                float RR_1 = intervalsRR.get(i - 2).getValue();
                float RR_2 = intervalsRR.get(i - 3).getValue();
                TO = TO(RR1, RR2, RR_1, RR_2);
                System.out.println(TO);
            }

        }
    }


}
