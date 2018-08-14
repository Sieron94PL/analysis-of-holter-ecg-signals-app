package utils;

public class Math {

    public final static float THRESHOLD_VALUE = 0.15f;
    public final static float SAMPLING_FREQUENCY = 360.0f;
    public final static float SAMPLING_PERIOD = 1.0f / SAMPLING_FREQUENCY;
    public final static float ONE_MINUTE = toMillisecond(60.0f);

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

    public static int timeToSampleNumber(float time, float samplingFrequency) {
        float samplingPeriod = 1.0f / samplingFrequency;
        return java.lang.Math.round(time / samplingPeriod);
    }

    public static float sampleToTime(int sampleNumber, float samplingFrequency) {
        float samplingPeriod = 1.0f / samplingFrequency;
        return sampleNumber * samplingPeriod;
    }

    public static float toMillisecond(float value) {
        return 1000.0f * value;
    }
}
