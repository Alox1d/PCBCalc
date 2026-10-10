package com.alox1d.pcbcalc.models;

import java.util.ArrayList;

public class ListViewItem {
    private int nameId;
    private StringWithTag value;
    public ListViewItem(int nameId, StringWithTag value) {
        this.nameId = nameId;
        this.value = value;
    }

    public int getNameId() {
        return nameId;
    }

    public StringWithTag getValue() {
        return value;
    }


}
