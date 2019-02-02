package model;

import java.util.List;

public class Sample {

    private int id;
    private float value;
    private boolean isPVC;
    private float TO;
    private float TS;
    private float HR;

    public float getHR() {
        return HR;
    }

    public void setHR(float HR) {
        this.HR = HR;
    }

    public Sample() {}

    public Sample(int id, float value) {
        this.id = id;
        this.value = value;
        this.isPVC = false;
    }

    public static Sample findById(int id, List<Sample> samples) {
        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i).getId() == id)
                return samples.get(i);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public void setPVC(boolean PVC) {
        isPVC = PVC;
    }

    public boolean isPVC() {
        return isPVC;
    }

    public float getTO() {
        return TO;
    }

    public void setTO(float TO) {
        this.TO = TO;
    }

    public float getTS() {
        return TS;
    }

    public void setTS(float TS) {
        this.TS = TS;
    }

}
