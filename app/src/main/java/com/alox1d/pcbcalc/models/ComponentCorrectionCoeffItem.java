package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class ComponentCorrectionCoeffItem implements Serializable {
    private Long id;
    private String componentsType;
    private Double temperature;
    private Double impulseModeValue;
    private Double staticModeValue;



    public ComponentCorrectionCoeffItem(long id, String componentsType, Double temperature, Double impulseModeValue, Double staticModeValue){
        this.id = id;
        this.componentsType = componentsType;
        this.temperature = temperature;
        this.impulseModeValue = impulseModeValue;
        this.staticModeValue = staticModeValue;
    }
    public String getComponentsType() {
        return componentsType;
    }

    public void setComponentsType(String componentsType) {
        this.componentsType = componentsType;
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

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
