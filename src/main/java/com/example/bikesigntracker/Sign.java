package com.example.bikesigntracker;

import java.io.Serializable;

public class Sign implements Serializable {
    private int count = 0;
    private String label = "";
    private boolean isPrinted = false;

    private Sign() {}

    public Sign(int count, String label, boolean isPrinted) {

        if(count == 0) {
            isPrinted = false;
        }

        this.count = count;
        this.label = label;
        this.isPrinted = isPrinted;
    }

    public int getCount() {
        return count;
    }

    public String getLabel() {
        return label;
    }

    public boolean getIsPrinted() {
        return isPrinted;
    }

    public void setLabel(String newLabel){
        label = newLabel;
    }

    public void setCount(int newCount) {
        count = newCount;
    }

    public void setIsPrinted(boolean isPrinted){
        this.isPrinted = isPrinted;
    }

    public int incrementCount(){
        count++;
        return count;
    }

    public int incrementCount(int incrementBy){
        count += incrementBy;
        return count;
    }

    public boolean switchIsPrinted(){
        isPrinted = !isPrinted;
        return isPrinted;
    }
}