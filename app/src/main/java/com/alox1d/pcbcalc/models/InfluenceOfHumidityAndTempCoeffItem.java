package com.alox1d.pcbcalc.models;

import java.io.Serializable;

public class InfluenceOfHumidityAndTempCoeffItem implements Serializable {
    private Long id;
    private String humidity;
    private String temperature;
    private Double correctionCoeff;



    public InfluenceOfHumidityAndTempCoeffItem(long id, String humidity, String temperature, Double correctionCoeff){
        this.id = id;
        this.humidity = humidity;
        this.temperature = temperature;
        this.correctionCoeff = correctionCoeff;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Double getCorrectionCoeff() {
        return correctionCoeff;
    }

    public void setCorrectionCoeff(Double correctionCoeff) {
        this.correctionCoeff = correctionCoeff;
    }
}
