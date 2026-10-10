package com.alox1d.pcbcalc.models;

public class ChartItem {
    private double x;
    private double y;
    public ChartItem(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
