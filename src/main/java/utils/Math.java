package utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Math {

    public final static float THRESHOLD_QRS_DETECTION = 0.1f;
    public final static float ONE_MINUTE = toMillisecond(60.0f);
    public final static float THRESHOLD_PVC_DETECTION = 0.1f;

    public static float mean(float[] input) {
        float sum = 0.0f;
        for (int i = 0; i < input.length; i++) {
            sum += input[i];
        }
        return sum / input.length;
    }

    public static float max(float[] input) {
        float max = input[0];
        for (int i = 1; i < input.length; i++) {
            if (max < java.lang.Math.abs(input[i])) {
                max = java.lang.Math.abs(input[i]);
            }
        }
        return max;
    }

    public static float min(float[] input) {
        float min = input[0];
        for (int i = 1; i < input.length; i++) {
            if (min > java.lang.Math.abs(input[i])) {
                min = java.lang.Math.abs(input[i]);
            }
        }
        return min;
    }

    public static float samplingPeriod(float samplingFrequency) {
        return 1.0f / samplingFrequency;
    }

    public static int secondsToSample(float second, float samplingFrequency) {
        return java.lang.Math.round(second * samplingFrequency);
    }

    public static float sampleToSecond(int sampleId, float samplingFrequency) {
        return sampleId / samplingFrequency;
    }

    public static String secondsToLocalTime(int seconds) {
        return LocalTime.MIN.plusSeconds(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static int localTimeToSeconds(String localTime) {
        return LocalTime.parse(localTime).toSecondOfDay();
    }

    public static float toMillisecond(float value) {
        return 1000.0f * value;
    }

}
