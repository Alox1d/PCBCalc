package com.alox1d.pcbcalc.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ExpandedRecyclerParentItem implements Serializable {
    private String parentName;
    private ArrayList<ExpandedRecyclerChildItem> childDataItems;
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public ArrayList<ExpandedRecyclerChildItem> getChildDataItems() {
        return childDataItems;
    }

    public void setChildDataItems(ArrayList<ExpandedRecyclerChildItem> childDataItems) {
        this.childDataItems = childDataItems;
    }
}
