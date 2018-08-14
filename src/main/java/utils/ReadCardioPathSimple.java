package utils;


import com.opencsv.CSVReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadCardioPathSimple {

    public static ECGSignal load(String path, int channels, int samplingFrequency, String filename) {

        int channel = 0;
        int bajt;
        int i = 0;
        try {
            FileInputStream readSource = new FileInputStream(path + File.separator + filename);

            int size = readSource.available() / channels;
            ECGSignal signal = new ECGSignal(channels, size, samplingFrequency);
            BufferedInputStream readBuffer = new BufferedInputStream(readSource);

            while ((bajt = readBuffer.read()) != -1) {
                if (channel == channels) {
                    ++i;
                    channel = 0;
                }
                if (i >= size) {
                    break;
                }
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

    public static float[] loadCSV(String path, int signalNumber, int start, int stop) {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> records = csvReader.readAll();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}