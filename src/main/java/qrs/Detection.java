package qrs;


import model.Sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Detection {

    private final static float THRESHOLD_VALUE = 0.015f;

    public static float max(float[] input) {
        float max = 0.0f;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
            }
        }
        return max;
    }

    public static float[] normalize(float[] input) {
        float[] output = new float[input.length];
        float max = max(input);
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] / max;
        }
        return output;
    }

    private static boolean isQRS(float[] input) {
        for (int i = 0; i < 5; i++) {
            if (input[i] > THRESHOLD_VALUE) {
                return false;
            }
        }
        for (int i = 5; i < 10; i++) {
            if (input[i] < THRESHOLD_VALUE) {
                return false;
            }
        }
        return true;
    }

    private static int max(List<Sample> samples) {
        float max = samples.get(0).getValue();
        int id = samples.get(0).getId();
        for (int i = 1; i < samples.size(); i++) {
            if (samples.get(i).getValue() > max) {
                max = samples.get(i).getValue();
                id = samples.get(i).getId();
            }
        }
        return id;
    }


    public static List<Integer> detect(float[] input) {
        List<Sample> samples = new ArrayList<>();
        List<Integer> peaks = new ArrayList<>();
        input = normalize(input);
        for (int i = 5; i < input.length; i++) {
            if (input[i] / max(input) > THRESHOLD_VALUE) {
                if (isQRS(Arrays.copyOfRange(input, i - 5, i + 5))) {
                    while (input[i] / max(input) > THRESHOLD_VALUE && i < input.length - 1) {
                        Sample sample = new Sample(i, input[i] / max(input));
                        samples.add(sample);
                        i++;
                    }
                    peaks.add(max(samples));
                    samples.clear();
                }

            }
        }
        return peaks;
    }

}
