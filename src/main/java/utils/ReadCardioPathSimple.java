package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadCardioPathSimple {

    public static ECGSignal load(String path, int channels) {

        int channel = 0;
        int bajt;

        int i = 0;

        try {

            FileInputStream readSource = new FileInputStream(path + File.separator + "crecg.dat");
            int size = readSource.available() / channels;
            ECGSignal signal = new ECGSignal(1, size, 128);

            BufferedInputStream readBuffer = new BufferedInputStream(readSource);

            while ((bajt = readBuffer.read()) != -1) {
                if (channel == channels) {
                    ++i;
                    channel = 0;
                }

                if (i >= size)
                    break;
                if (channel == 0)
                    signal.setSample(channel, i, (float) bajt);
                ++channel;
            }

            readBuffer.close();
            return signal;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}