package filters.iirj;

import uk.me.berndporr.iirj.Butterworth;

public class ButterworthFilter {

    public static float[] filter(float[] input, float samplingFrequency) {
        Butterworth butterworth = new Butterworth();
        float[] output = new float[input.length];

        /*Bandpass filter 5-11Hz.*/
        butterworth.bandPass(2, samplingFrequency, 8, 6);
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }

        return output;
    }

    public static float[] cascadeFilter(float[] input) {
        Butterworth butterworth = new Butterworth();
        float[] output = new float[input.length];
        /*Low pass filter cutoffFrequency = 11 Hz.*/
        butterworth.lowPass(2, 128, 11);
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }
        /*High pass filter cutoffFrequency = 5 Hz.*/
        butterworth.highPass(2, 128, 5);
        for (int i = 0; i < output.length; i++) {
            output[i] = (float) butterworth.filter(output[i]);
        }
        return output;
    }
}
