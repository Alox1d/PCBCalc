package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class AtmospherePressureItem implements Serializable {
    private Long id;
    private String pressure;
    private Double correctionCoeff;



    public AtmospherePressureItem(long id, String pressure, Double correctionCoeff){
        this.id = id;
        this.pressure = pressure;
        this.correctionCoeff = correctionCoeff;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Double getCorrectionCoeff() {
        return correctionCoeff;
    }

    public void setCorrectionCoeff(Double correctionCoeff) {
        this.correctionCoeff = correctionCoeff;
    }
}
