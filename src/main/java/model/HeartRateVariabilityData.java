package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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

    public int getBeatNo() {
        return beatNo.get();
    }

    public SimpleIntegerProperty beatNoProperty() {
        return beatNo;
    }

    public void setBeatNo(int beatNo) {
        this.beatNo.set(beatNo);
    }

    public void setPeakNo(int peakNo) {
        this.peakNo.set(peakNo);
    }

    public Integer getPeakNo() {
        return peakNo.get();
    }

    public SimpleIntegerProperty peakNoProperty() {
        return peakNo;
    }

    public void setPeakNo(Integer peakNo) {
        this.peakNo.set(peakNo);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public float getDuration() {
        return duration.get();
    }

    public SimpleFloatProperty durationProperty() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration.set(duration);
    }

    public boolean isIsPVC() {
        return isPVC.get();
    }

    public SimpleBooleanProperty isPVCProperty() {
        return isPVC;
    }

    public void setIsPVC(boolean isPVC) {
        this.isPVC.set(isPVC);
    }
}
