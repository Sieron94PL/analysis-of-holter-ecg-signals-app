package qrs;


import model.Sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QrsDetection {

    private final static float THRESHOLD_VALUE = 0.2f;

    private static boolean isQRS(float[] input) {
        for (int i = 0; i < 5; i++) {
            if (input[i] > THRESHOLD_VALUE)
                return false;
        }
        for (int i = 5; i < 10; i++) {
            if (input[i] < THRESHOLD_VALUE)
                return false;
        }
        return true;
    }

    public static Sample max(List<Sample> samples) {
        float max = samples.get(0).getValue();
        Long id = samples.get(0).getId();
        for (int i = 1; i < samples.size(); i++) {
            if (samples.get(i).getValue() > max) {
                max = samples.get(i).getValue();
                id = samples.get(i).getId();
            }
        }
        return new Sample(id, max);
    }


    public static List<Sample> detect(float[] input) {
        List<Sample> temp = new ArrayList<>();
        List<Sample> peaks = new ArrayList<>();

        input = Normalization.normalize(input);

        for (int i = 5; i < input.length; i++) {
            if (input[i] / Normalization.max(input) > THRESHOLD_VALUE) {
                if (isQRS(Arrays.copyOfRange(input, i - 5, i + 5))) {
                    while (input[i] / Normalization.max(input) > THRESHOLD_VALUE && i < input.length - 1) {
                        temp.add(new Sample(Long.valueOf(i), input[i] / Normalization.max(input)));
                        i++;
                    }
                    peaks.add(max(temp));
                    temp.clear();
                }

            }
        }
        return peaks;
    }

}
