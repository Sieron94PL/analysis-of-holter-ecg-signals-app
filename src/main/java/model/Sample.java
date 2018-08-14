package model;

public class Sample {

    private int id;
    private float value;
    private boolean isPVC;

    public Sample(int id, float value) {
        this.id = id;
        this.value = value;
        this.isPVC = false;
    }

    public void setPVC(boolean PVC) {
        isPVC = PVC;
    }

    public boolean isPVC() {
        return isPVC;
    }

    public int getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

}
