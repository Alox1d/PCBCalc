package com.alox1d.pcbcalc.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class del_Employee {

    @PrimaryKey
    public long id;

    public String name;

    public int salary;
}