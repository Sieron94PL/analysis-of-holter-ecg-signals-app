package qrs;


import model.Sample;
import utils.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QrsDetection {

    private float[] ecgSignal;

    public QrsDetection(float[] ecgSignal) {
        this.ecgSignal = ecgSignal;
    }

    private static boolean isQRS(float[] input) {
        for (int i = 0; i < 5; i++) {
            if (input[i] > Math.THRESHOLD_VALUE)
                return false;
        }
        for (int i = 5; i < 10; i++) {
            if (input[i] < Math.THRESHOLD_VALUE)
                return false;
        }
        return true;
    }

    private Sample getPeak(List<Sample> samples) {
        float max = samples.get(0).getValue();
        int id = samples.get(0).getId();

        for (int i = 1; i < samples.size(); i++) {
            if (samples.get(i).getValue() > max) {
                max = samples.get(i).getValue();
                id = samples.get(i).getId();
            }
        }

        max = ecgSignal[id - 5];
        for (int i = id - 5; i < id + 5; i++) {
            if (ecgSignal[i] > max) {
                max = ecgSignal[i];
                id = i;
            }
        }

        return new Sample(id, max);
    }

    public List<Sample> detect(float[] input) {
        List<Sample> temp = new ArrayList<>();
        List<Sample> peaks = new ArrayList<>();

        input = Normalization.normalize(input);

        for (int i = 5; i < input.length; i++) {

            if (input[i] / Math.max(input) > Math.THRESHOLD_VALUE) {

                if (isQRS(Arrays.copyOfRange(input, i - 5, i + 5))) {
                    while (input[i] / Math.max(input) > Math.THRESHOLD_VALUE && i < input.length - 1) {
                        temp.add(new Sample(i, input[i] / Math.max(input)));
                        i++;
                    }
                    peaks.add(getPeak(temp));
                    temp.clear();
                }
            }
        }

        return peaks;
    }

}
