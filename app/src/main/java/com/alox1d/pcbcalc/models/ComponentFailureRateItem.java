package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class ComponentFailureRateItem implements Serializable {
    private Long id;
    private String componentName;
    private Double value;
    private int componentLoadingCoeffItemId;



    public ComponentFailureRateItem(long id, String componentName, Double value, int componentLoadingCoeffItemId){
        this.id = id;
        this.componentName = componentName;
        this.value = value;
        this.componentLoadingCoeffItemId = componentLoadingCoeffItemId;
    }
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getComponentLoadingCoeffItemId() {
        return componentLoadingCoeffItemId;
    }

    public void setComponentLoadingCoeffItemId(int componentLoadingCoeffItemId) {
        this.componentLoadingCoeffItemId = componentLoadingCoeffItemId;
    }
}
