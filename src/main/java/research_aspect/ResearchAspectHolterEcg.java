package research_aspect;

import filter_iirj.ButterworthFilter;
import model.Sample;
import parameters.*;
import qrs.*;
import utils.ECGSignal;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

import java.util.Arrays;
import java.util.List;

public class ResearchAspectHolterEcg {

    private static final String ECG_HOLTER_DATA_DIRECTORY = "C:\\Users\\Damian\\Desktop\\holter-ecg\\";

    private static String[] folderNames = {"Save002"}; //"Save011", "Save023", "Save031", "Save052", "Save065", "Save076", "Save078", "Save091", "Save124"};
    private static String crecgFile = "crecg.dat";
    private static int SAMPLING_FREQUENCY = 128;
    private static int TWO_MINUTES = 120 * SAMPLING_FREQUENCY;


    public static void main(String[] args) {


        for (int i = 0; i < folderNames.length; i++) {
            String path = ECG_HOLTER_DATA_DIRECTORY + folderNames[i];
            int channels = ReadCardioPathNumberOfChannels.load(path);
            ECGSignal ecgSignal = ReadCardioPathSimple.load(ECG_HOLTER_DATA_DIRECTORY + folderNames[i] + "\\" + crecgFile, channels, SAMPLING_FREQUENCY);

            float[] inputSignal = Arrays.copyOfRange(ecgSignal.getChannel(0), TWO_MINUTES, 921600);

            System.out.println(folderNames[i] + ": canceling DC drift");
            inputSignal = Normalization.cancelDC(inputSignal);


            QrsDetection qrsDetection = new QrsDetection(inputSignal);

            System.out.println(folderNames[i] + ": filtering signal");
            float[] filteredSignal = ButterworthFilter.filter(inputSignal, SAMPLING_FREQUENCY);
            System.out.println(folderNames[i] + ": differentiation signal");
            float[] derivativeSignal = Derivative.derivative(filteredSignal, SAMPLING_FREQUENCY);
            System.out.println(folderNames[i] + ": squaring signal");
            float[] squaringSignal = Squaring.squaring(derivativeSignal);
            System.out.println(folderNames[i] + ": integrating signal");
            float[] integrationSignal = Integration.integration(squaringSignal, SAMPLING_FREQUENCY);

            System.out.println(folderNames[i] + ": QRS detecting");
            List<Sample> peaks = qrsDetection.detect(integrationSignal, SAMPLING_FREQUENCY);

            System.out.println(folderNames[i] + ": getting intervalsRR");
            List<Sample> intervalsRR = HeartRateVariability.getIntervalsRR(peaks, SAMPLING_FREQUENCY);
            float averageIntervalsRR = HeartRate.averageIntervalRR(intervalsRR);
            intervalsRR = PrematureVentricularContractions.detectPVCs(intervalsRR, averageIntervalsRR);

            float heartRate = HeartRate.getHeartRate(averageIntervalsRR);
            float SDNN = HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR);
            float TO = HeartRateTurbulence.averageTO(intervalsRR);
            float TS = HeartRateTurbulence.averageTS(intervalsRR);
            float AC = Acceleration.acceleration(Acceleration.signalAveraging(intervalsRR));
            float DC = Deceleration.deceleration(Deceleration.signalAveraging(intervalsRR));

            System.out.println(folderNames[i] + ":");
            System.out.println("SDNN: " + SDNN);
            System.out.println("TO: " + TO);
            System.out.println("TS: " + TS);
            System.out.println("AC: " + AC);
            System.out.println("DC: " + DC);

        }


    }
}
