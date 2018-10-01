package model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HeartRateTurbulenceData {
    private SimpleIntegerProperty peakNo;
    private SimpleStringProperty time;
    private SimpleFloatProperty TO;
    private SimpleFloatProperty TS;

    public HeartRateTurbulenceData(int peakNo, String time, float TO, float TS) {
        this.peakNo = new SimpleIntegerProperty(peakNo);
        this.time = new SimpleStringProperty(time);
        this.TO = new SimpleFloatProperty(TO);
        this.TS = new SimpleFloatProperty(TS);
    }

    public int getPeakNo() {
        return peakNo.get();
    }

    public SimpleIntegerProperty peakNoProperty() {
        return peakNo;
    }

    public void setPeakNo(int peakNo) {
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

    public float getTO() {
        return TO.get();
    }

    public SimpleFloatProperty TOProperty() {
        return TO;
    }

    public void setTO(float TO) {
        this.TO.set(TO);
    }

    public float getTS() {
        return TS.get();
    }

    public SimpleFloatProperty TSProperty() {
        return TS;
    }

    public void setTS(float TS) {
        this.TS.set(TS);
    }
}
