package com.alox1d.pcbcalc.models;


public class CalcRowItem {
    private int nameId;
    private double value;
    private String unitOfMeasurement;

    public int getHelpImageId() {
        return helpImageId;
    }

    private int helpImageId;// help-circle

    private boolean isEditTextBlocked;
    private String name;

    public CalcRowItem(int nameId, double value, String unitOfMeasurement) {
        this.nameId = nameId;
        this.value = value;
        this.unitOfMeasurement = unitOfMeasurement;
        //this.helpImageId = helpImageId; // help-circle
    }
    public CalcRowItem(String name, double value, String unitOfMeasurement, boolean isEditTextBlocked) {
        this.name = name;
        this.value = value;
        this.unitOfMeasurement = unitOfMeasurement;
        this.isEditTextBlocked = isEditTextBlocked;
    }

    public int getNameId() {
        return nameId;
    }

    public double getValue() {
        return value;
    }

    public String getUnitOfMeasurement(){
        return unitOfMeasurement;
    }
    public boolean isEditTextBlocked() {
        return isEditTextBlocked;
    }

    public String getName() {
        return name;
    }
}
