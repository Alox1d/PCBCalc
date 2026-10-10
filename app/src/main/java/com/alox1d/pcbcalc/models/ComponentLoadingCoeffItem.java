package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class ComponentLoadingCoeffItem implements Serializable {
    private Long id;
    private String componentsName;
    private Double impulseModeValue;
    private Double staticModeValue;



    public ComponentLoadingCoeffItem(long id, String componentsName, Double impulseModeValue, Double staticModeValue){
        this.id = id;
        this.componentsName = componentsName;
        this.impulseModeValue = impulseModeValue;
        this.staticModeValue = staticModeValue;
    }
    public String getComponentsName() {
        return componentsName;
    }

    public void setComponentsName(String componentsName) {
        this.componentsName = componentsName;
    }

    public Double getImpulseModeValue() {
        return impulseModeValue;
    }

    public Double getStaticModeValue() {
        return staticModeValue;
    }

    public void setStaticModeValue(Double staticModeValue) {
        this.staticModeValue = staticModeValue;
    }

    public void setImpulseModeValue(Double value) {
        this.impulseModeValue = value;
    }
}
