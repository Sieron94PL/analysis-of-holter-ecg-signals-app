package qrs_detection;

public class QRSDetection {

    public static float[] qrsDetection(float[] input) {
        return Integration.integration
                (Power.power(
                        Differentiation.differentiation(
                                Filtration.filter(input))));

    }

}
