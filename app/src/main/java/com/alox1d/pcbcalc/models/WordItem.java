package com.alox1d.pcbcalc.models;

public class WordItem {

    private int keyResourceId;
    private String value;
    private int valueResourceId;


    public WordItem(int keyResourceId, String value) {
        this.value = value;
        this.keyResourceId = keyResourceId;
    }
    public WordItem(int keyResourceId, int valueResourceId) {
        this.valueResourceId = valueResourceId;
        this.keyResourceId = keyResourceId;
    }


    public int getKeyResourceId() {
        return keyResourceId;
    }

    public String getValue() {
        return value;
    }

    public int getValueResourceId() {
        return valueResourceId;
    }
}
