package com.example.bikesigntracker;

import java.io.Serializable;

public class CycleSign extends Sign implements Serializable {
    private ECycleSignType type = ECycleSignType.FORWARD;


    public CycleSign(int count, String label, boolean isPrinted, ECycleSignType type) {
        super(count, label, isPrinted);
        this.type = type;
    }

    public CycleSign(int count, String label, boolean isPrinted) {
        super(count, label, isPrinted);
    }

    public ECycleSignType getType() {
        return type;
    }

    public void setType(ECycleSignType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CycleSign{" +
                "type=" + type +
                "label="+ this.getLabel() +
                '}';
    }
}