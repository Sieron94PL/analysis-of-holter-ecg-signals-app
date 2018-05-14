package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ECGSignal {

    private float[][] data;
    private int channelsNumber;

    private int samplingFrequency;

    String fullPath;

    public ECGSignal() {
    }

    public ECGSignal(int channelsNumber, int channelLength, int samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
        this.channelsNumber = channelsNumber;
        data = new float[channelsNumber][];

        for (int i = 0; i < channelsNumber; ++i)
            data[i] = new float[channelLength];
    }

    public float[] getChannel(int channel) {
        if (channel < 0 || channel >= channelsNumber) {
            return null;
        }

        return data[channel];
    }

    public int getChannelLength(int channel) {
        if (channel < 0 || channel >= channelsNumber) {
            return 0;
        }

        return data[channel].length;
    }

    public float getSample(int channel, int sample) {
        try {
            return data[channel][sample];
        } catch (Exception e) {
            return -1;
        }
    }

    public void setSample(int channel, int sample, float value) {
        data[channel][sample] = value;
    }

    public float getSampleuV(int channel, int sample) {
        return getSample(channel, sample) * 25.5f;
    }

    public ECGSignal clone() {
        ECGSignal signal = new ECGSignal(channelsNumber, data[0].length, samplingFrequency);
        for (int i = 0; i < channelsNumber; ++i)
            for (int j = 0; j < data[0].length; ++j)
                signal.data[i][j] = data[i][j];
        return signal;
    }

    public float getVabs(int channel, int sample, float dc) {
        return Math.abs(getSample(channel, sample) - dc);
    }

    public int getChannelsNumber() {
        return channelsNumber;
    }

    public void display(int start, int stop, int channel) {
        for (int i = start; i < stop; ++i) {
            System.out.println(getSample(channel, i));
        }
    }

    public void display(int start, int stop, int channel, String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (int i = start; i < stop; ++i) {
                out.write(String.valueOf((int) getSample(channel, i)));
                out.newLine();

                System.out.println();
            }

            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    public String getDescription() {
        return "ECG Signal Implementation";
    }

    public float[][] getAllData() {
        return data;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public int getSamplingFrequency() {
        return samplingFrequency;
    }


}
