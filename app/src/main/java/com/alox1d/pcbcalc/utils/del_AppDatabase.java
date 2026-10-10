package com.alox1d.pcbcalc.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alox1d.pcbcalc.models.del_Employee;
import com.alox1d.pcbcalc.models.del_EmployeeDao;

@Database(entities = {del_Employee.class}, version = 1)
public abstract class del_AppDatabase extends RoomDatabase {
    public abstract del_EmployeeDao employeeDao();
}
