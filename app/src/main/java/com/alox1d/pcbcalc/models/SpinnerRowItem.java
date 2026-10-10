package com.alox1d.pcbcalc.models;

import java.util.ArrayList;

public class SpinnerRowItem {
    private int nameId;
    private ArrayList<StringWithTag> values;
    public SpinnerRowItem(int nameId, ArrayList<StringWithTag> values) {
        this.nameId = nameId;
        this.values = values;
    }

    public int getNameId() {
        return nameId;
    }

    public ArrayList<StringWithTag> getValues() {
        return values;
    }


}
