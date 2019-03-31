package utils;

import java.time.format.DateTimeParseException;

public class Validator {

    public static boolean isNumber(String text) {
        String regex = "[0-9]+";
        return text.matches(regex);
    }

    public static boolean isLocalTime(String text) {
        try {
            Math.localTimeToSeconds(text);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidTimeRange(String fromTime, String toTime) {
        float fromTimeInSeconds = Math.localTimeToSeconds(fromTime);
        float toTimeInSeconds = Math.localTimeToSeconds(toTime);
        return (toTimeInSeconds - fromTimeInSeconds) > 0;
    }
}
