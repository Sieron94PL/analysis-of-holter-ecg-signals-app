package filter_iirj;

import uk.me.berndporr.iirj.Butterworth;

public class ButterworthFilter {


    public static float[] filterBandPass(float[] input) {
        Butterworth butterworth = new Butterworth();
        butterworth.bandPass(2, 128, 9, 6);
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }
        return output;
    }

    public static float[] filterCascade(float[] input) {
        Butterworth butterworth = new Butterworth();
        butterworth.lowPass(2, 128, 15);
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }
        butterworth.highPass(2, 128, 5);
        for (int i = 0; i < output.length; i++) {
            output[i] = (float) butterworth.filter(output[i]);
        }
        return output;
    }
}
