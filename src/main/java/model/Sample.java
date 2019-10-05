package model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Sample {

    private int id;
    private float value;
    private boolean isPVC;
    private float TO;
    private float TS;
    private float HR;

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
}
