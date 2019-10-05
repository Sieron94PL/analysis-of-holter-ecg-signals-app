package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartRateVariabilityData {

    private SimpleIntegerProperty beatNo;
    private SimpleIntegerProperty peakNo;
    private SimpleStringProperty time;
    private SimpleFloatProperty duration;
    private SimpleBooleanProperty isPVC;

    public HeartRateVariabilityData(int beatNo, int peakNo, String time, float duration, boolean isPVC) {
        this.beatNo = new SimpleIntegerProperty(beatNo);
        this.peakNo = new SimpleIntegerProperty(peakNo);
        this.time = new SimpleStringProperty(time);
        this.duration = new SimpleFloatProperty(duration);
        this.isPVC = new SimpleBooleanProperty(isPVC);
    }

}
