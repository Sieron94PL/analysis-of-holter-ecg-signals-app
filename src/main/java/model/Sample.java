package model;

public class Sample {

    private Long id;
    private float value;

    public Sample(Long id, float value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }
}
