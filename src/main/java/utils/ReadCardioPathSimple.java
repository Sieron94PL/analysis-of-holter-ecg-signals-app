package utils;


import com.opencsv.CSVReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadCardioPathSimple {

    public static ECGSignal load(String path, int channels, int samplingFrequency) {

        int channel = 0;
        int bit;
        int i = 0;
        try {
            FileInputStream readSource = new FileInputStream(path);

            int size = readSource.available() / channels;
            ECGSignal signal = new ECGSignal(channels, size, samplingFrequency);
            BufferedInputStream readBuffer = new BufferedInputStream(readSource);

            while ((bit = readBuffer.read()) != -1) {
                if (channel == channels) {
                    ++i;
                    channel = 0;
                }
                if (i >= size) {
                    break;
                }
                signal.setSample(channel, i, (float) bit);
                ++channel;
            }
            readBuffer.close();
            return signal;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float[] loadCSV(String path, int signalNumber, int start, int stop) {
        List<String[]> records = getRecords(path);
        float[] input = new float[stop - start];

        if (signalNumber <= records.get(0).length - 1 && signalNumber > 0) {
            for (int i = start; i < stop; i++) {
                String[] record = records.get(i);
                input[i - start] = Float.parseFloat(record[signalNumber]);
            }
            return input;
        } else {
            System.out.println("Invalid signal number");
            return null;
        }
    }


    public static List<String[]> getRecords(String path) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            return new CSVReader(reader).readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}