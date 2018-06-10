package filter_iirj;

import uk.me.berndporr.iirj.Butterworth;

public class ButterworthFilter {


    public static float[] filter(float[] input) {
        Butterworth butterworth = new Butterworth();
        butterworth.bandPass(5, 128, 9, 8);
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }
        return output;
    }
}
