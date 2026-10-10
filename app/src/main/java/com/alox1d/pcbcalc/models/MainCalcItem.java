package com.alox1d.pcbcalc.models;


public class MainCalcItem {
    private int nameId;
    private int imageId;
    public MainCalcItem(int nameId, int imageId) {
        this.nameId = nameId;
        this.imageId = imageId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getImageId() {
        return imageId;
    }


}
