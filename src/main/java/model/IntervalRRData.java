package model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class IntervalRRData {
    private SimpleIntegerProperty beatNo;
    private SimpleFloatProperty duration;

    public IntervalRRData(SimpleIntegerProperty beatNo, SimpleFloatProperty duration) {
        this.beatNo = beatNo;
        this.duration = duration;
    }

    public int getBeatNo() {
        return beatNo.get();
    }

    public SimpleIntegerProperty beatNoProperty() {
        return beatNo;
    }

    public void setBeatNo(int beatNo) {
        this.beatNo.set(beatNo);
    }

    public float getDuration() {
        return duration.get();
    }

    public SimpleFloatProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }
}
