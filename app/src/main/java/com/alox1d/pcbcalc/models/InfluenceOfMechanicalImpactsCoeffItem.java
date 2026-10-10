package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class InfluenceOfMechanicalImpactsCoeffItem implements Serializable {
    private Long id;
    private String conditionsName;
    private Double K1;
    private Double K2;



    public InfluenceOfMechanicalImpactsCoeffItem(long id, String conditionsName, Double K1, Double K2){
        this.id = id;
        this.conditionsName = conditionsName;
        this.K1 = K1;
        this.K2 = K2;
    }
    public String getConditionsName() {
        return conditionsName;
    }

    public void setConditionsName(String conditionsName) {
        this.conditionsName = conditionsName;
    }

    public Double getK1() {
        return K1;
    }

    public void setK1(Double k1) {
        K1 = k1;
    }

    public Double getK2() {
        return K2;
    }

    public void setK2(Double k2) {
        K2 = k2;
    }
}
