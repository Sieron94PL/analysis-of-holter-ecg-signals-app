package researchaspect;

import filters.iirj.ButterworthFilter;
import model.Sample;
import parameters.HeartRate;
import parameters.HeartRateTurbulence;
import parameters.HeartRateVariability;
import parameters.PrematureVentricularContractions;
import qrs.*;
import utils.ECGSignal;
import utils.ReadCardioPathNumberOfChannels;
import utils.ReadCardioPathSimple;

import java.util.Arrays;
import java.util.List;

public class PeakDetectionHolterEcg {

    private static final String ECG_HOLTER_DATA_DIRECTORY = "C:\\Users\\Damian\\Desktop\\holter-ecg\\";
    private static String crecgFile = "crecg.dat";
    private static final String XLS_RESULTS_DIRECTORY = ECG_HOLTER_DATA_DIRECTORY + "Results\\";
    private static String[] folderNames = {"Save091", "Save124", "Save078", "Save076", "Save065", "Save031", "Save052", "Save023", "Save011", "Save002"};
    private static int SAMPLING_FREQUENCY = 128;
    private static final int ONE_HOUR = SAMPLING_FREQUENCY * 60 * 60;
    private static final int HALF_HOUR = SAMPLING_FREQUENCY * 60 * 30;
    private static final int ONE_MINUTE = SAMPLING_FREQUENCY * 60;
    private static int[] signalStarts = {
            (12 * ONE_HOUR),
            (18 * ONE_HOUR),
            ((2 * ONE_HOUR) + (5 * ONE_MINUTE)),
            (2 * ONE_HOUR),
            HALF_HOUR,
            (12 * ONE_HOUR),
            (6 * ONE_HOUR),
            (13 * ONE_HOUR),
            (7 * ONE_HOUR),
            (5 * ONE_HOUR)};

    public static void main(String[] args) throws Exception {

        DataHelper dataHelper = new DataHelper();

        for (int i = 0; i < folderNames.length; i++) {
            String path = ECG_HOLTER_DATA_DIRECTORY + folderNames[i];
            int channels = ReadCardioPathNumberOfChannels.load(path);
            ECGSignal ecgSignal = ReadCardioPathSimple.load(ECG_HOLTER_DATA_DIRECTORY + folderNames[i] + "\\" + crecgFile, channels, SAMPLING_FREQUENCY);

            float[] inputSignal = Arrays.copyOfRange(ecgSignal.getChannel(0), signalStarts[i], signalStarts[i] + HALF_HOUR);
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

            dataHelper.intervalsRRToXls(intervalsRR, XLS_RESULTS_DIRECTORY + folderNames[i] + ".xlsx");
            System.out.println("SDNN: " + HeartRateVariability.SDNN(intervalsRR, averageIntervalsRR));
        }

    }
}
