package researchaspect;

import filters.iirj.ButterworthFilter;
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

    private static String[] folderNames = {"Save002", "Save011", "Save023", "Save031", "Save052", "Save065", "Save076", "Save078", "Save091", "Save124"};
    private static String crecgFile = "crecg.dat";
    private static int SAMPLING_FREQUENCY = 128;
    private static int TWO_MINUTES = 120 * SAMPLING_FREQUENCY;
    private static int ONE_HOUR = SAMPLING_FREQUENCY * 60 * 60;


    public static void main(String[] args) {


        for (int i = 0; i < folderNames.length; i++) {
            String path = ECG_HOLTER_DATA_DIRECTORY + folderNames[i];
            int channels = ReadCardioPathNumberOfChannels.load(path);
            ECGSignal ecgSignal = ReadCardioPathSimple.load(ECG_HOLTER_DATA_DIRECTORY + folderNames[i] + "\\" + crecgFile, channels, SAMPLING_FREQUENCY);

            int start, stop, hours = ecgSignal.getChannelLength(0) / ONE_HOUR;

            float HR = 0.0f, SDNN = 0.0f, TO = 0.0f, TS = 0.0f, AC = 0.0f, DC = 0.0f;

            for (int j = 1; j <= hours; j++) {
                start = (j - 1) * ONE_HOUR;
                stop = j * ONE_HOUR;

                if (stop > hours * ONE_HOUR) {
                    stop = ecgSignal.getChannelLength(0);
                }
                if ((j - 1) == 0) {
                    start = TWO_MINUTES;
                }

                float[] inputSignal = Arrays.copyOfRange(ecgSignal.getChannel(0), start, stop);
                inputSignal = Normalization.cancelDC(inputSignal);
                QrsDetection qrsDetection = new QrsDetection(inputSignal);
                float[] filteredSignal = ButterworthFilter.filter(inputSignal, SAMPLING_FREQUENCY);
                float[] derivativeSignal = Derivative.derivative(filteredSignal, SAMPLING_FREQUENCY);
                float[] squaringSignal = Squaring.squaring(derivativeSignal);
                float[] integrationSignal = Integration.integration(squaringSignal, SAMPLING_FREQUENCY);

                List<Sample> peaks = qrsDetection.detect(integrationSignal, SAMPLING_FREQUENCY);
                List<Sample> intervalsRR = HeartRateVariability.getIntervalsRR(peaks, SAMPLING_FREQUENCY);
                float averageIntervalsRR = HeartRate.averageIntervalRR(intervalsRR);
                intervalsRR = PrematureVentricularContractions.detectPVCs(intervalsRR, averageIntervalsRR);
                intervalsRR = HeartRateTurbulence.calculateTS(HeartRateTurbulence.calculateTO(intervalsRR));

                HR += HeartRate.getHeartRate(averageIntervalsRR);
                SDNN += HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR);
                TO += HeartRateTurbulence.averageTO(intervalsRR);
                TS += HeartRateTurbulence.averageTS(intervalsRR);
                AC += Acceleration.acceleration(Acceleration.signalAveraging(intervalsRR));
                DC += Deceleration.deceleration(Deceleration.signalAveraging(intervalsRR));

                System.out.println(folderNames[i] + " (hour: " + j + "): ");
                System.out.println("HR: " + HeartRate.getHeartRate(averageIntervalsRR));
                System.out.println("SDNN: " + HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR));
                System.out.println("TO: " + HeartRateTurbulence.averageTO(intervalsRR));
                System.out.println("TS: " + HeartRateTurbulence.averageTS(intervalsRR));
                System.out.println("AC: " + Acceleration.acceleration(Acceleration.signalAveraging(intervalsRR)));
                System.out.println("DC: " + Deceleration.deceleration(Deceleration.signalAveraging(intervalsRR)));

            }

            System.out.println(folderNames[i] + " overall: ");
            System.out.println("HR: " + (HR / hours * 1.0f));
            System.out.println("SDNN: " + (SDNN / hours * 1.0f));
            System.out.println("TO: " + (TO / hours * 1.0f));
            System.out.println("TS: " + (TS / hours * 1.0f));
            System.out.println("AC: " + (AC / hours * 1.0f));
            System.out.println("DC: " + (DC / hours * 1.0f));

        }


    }
}
