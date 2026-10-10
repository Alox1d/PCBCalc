package com.alox1d.pcbcalc.models;

import java.io.Serializable;

/**
 * Created by Alox1d on 17.04.2018.
 */

public class StringWithTag implements Serializable{ // Serializable - for intent.putExtra
    private String string;
    private Object tag;

    private int drawableId;

    public StringWithTag(Object tag, String string) { // only for spinner; tag is resource ID
        this.string = string;
        this.tag = tag;
    }
    public StringWithTag(int drawableId, Object tag, String string) { // only for spinner; tag is resource ID
        this.string = string;
        this.tag = tag;
        this.drawableId = drawableId;
    }

    @Override
    public String toString() {
        return string;
    }
    public Object getTag() {
        return tag;
    }
    public int getDrawableId() {
        return drawableId;
    }
}