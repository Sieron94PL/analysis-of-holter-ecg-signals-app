package model;

import javafx.beans.property.SimpleStringProperty;

public class IntervalRR {
    private SimpleStringProperty beatNo;
    private SimpleStringProperty duration;

    public IntervalRR(String beatNo, String duration) {
        this.beatNo = new SimpleStringProperty(beatNo);
        this.duration = new SimpleStringProperty(duration);
    }

    public String getBeatNo() {
        return beatNo.get();
    }

    public SimpleStringProperty beatNoProperty() {
        return beatNo;
    }

    public void setBeatNo(String beatNo) {
        this.beatNo.set(beatNo);
    }

    public String getDuration() {
        return duration.get();
    }

    public SimpleStringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

}
