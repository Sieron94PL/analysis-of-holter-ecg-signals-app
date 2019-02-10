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

    private static boolean isQRS(float[] input, float thresholdValue) {
        for (int i = 0; i < 5; i++) {
            if (input[i] > thresholdValue)
                return false;
        }
        for (int i = 5; i < 10; i++) {
            if (input[i] < thresholdValue)
                return false;
        }
        return true;
    }

    private Sample findPeak(List<Sample> samples) {
        float max = samples.get(0).getValue();
        int id = samples.get(0).getId();
        for (int i = 1; i < samples.size(); i++) {
            if (samples.get(i).getValue() > max) {
                max = samples.get(i).getValue();
                id = samples.get(i).getId();
            }
        }
        for (int i = id - 10; i < id + 10; i++) {
            if (ecgSignal[i] > max) {
                max = ecgSignal[i];
                id = i;
            }
        }
        return new Sample(id, max);
    }

    public float thresholdValue(float[] input, int start, int stop) {
        return 0.2f * Math.max(Arrays.copyOfRange(input, start, stop));
    }

    public List<Sample> detect(float[] input, float samplingFrequency) {
        List<Sample> temp = new ArrayList<>();
        List<Sample> peaks = new ArrayList<>();
        input = Normalization.normalize(input);
        int start = 0;
        int stop = java.lang.Math.round(5.0f * samplingFrequency);
        stop = (input.length - 1 > stop) ? stop : input.length - 1;
        float thresholdValue = thresholdValue(input, start, stop);

        for (int i = 5; i < input.length - 5; i++) {
            if (input[i] > thresholdValue) {
                if (isQRS(Arrays.copyOfRange(input, i - 5, i + 5), thresholdValue)) {

                    while (input[i] / Math.max(input) > thresholdValue && i < input.length - 1) {
                        temp.add(new Sample(i, input[i] / Math.max(input)));
                        i++;
                    }
                    peaks.add(findPeak(temp));
                    temp.clear();

                    if (i > stop) {
                        stop = i + java.lang.Math.round(5.0f * samplingFrequency);
                        stop = (input.length - 1 > stop) ? stop : input.length - 1;
                        if (stop - i > 0) {
                            thresholdValue = thresholdValue(input, i, stop);
                        }
                    }
                }
            }
        }
        return peaks;
    }
}
