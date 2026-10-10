package com.alox1d.pcbcalc.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface del_EmployeeDao {

    @Query("SELECT * FROM del_Employee")
    List<del_Employee> getAll();

    @Query("SELECT * FROM del_Employee WHERE id = :id")
    del_Employee getById(long id);

    @Insert
    void insert(del_Employee delEmployee);

    @Update
    void update(del_Employee delEmployee);

    @Delete
    void delete(del_Employee delEmployee);

}