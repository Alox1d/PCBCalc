package com.alox1d.pcbcalc.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ExpandedRecyclerChildItem implements Serializable {
    private int childNameId;
    private Double childValue;
    private ArrayList<StringWithTag> childValues;

    public void setChildNameId(int childNameId) {
        this.childNameId = childNameId;

    }

    public int getChildNameId() {
        return childNameId;
    }

    public void setChildValue(Double childValue) {
        this.childValue = childValue;
    }

    public Double getChildValue() {
        return childValue;
    }

    public ArrayList<StringWithTag> getChildValues() {
        return childValues;
    }

    public void setChildValues(ArrayList<StringWithTag> childValues) {
        this.childValues = childValues;
    }
}
