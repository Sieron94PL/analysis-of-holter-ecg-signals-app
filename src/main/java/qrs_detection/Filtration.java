package qrs_detection;

import uk.me.berndporr.iirj.Butterworth;

public class Filtration {


    public static float[] filter(float[] input) {
        Butterworth butterworth = new Butterworth();
        butterworth.bandPass(3, 128, 10, 10);

        float[] output = new float[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = (float) butterworth.filter(input[i]);
        }

        return output;
    }

}
