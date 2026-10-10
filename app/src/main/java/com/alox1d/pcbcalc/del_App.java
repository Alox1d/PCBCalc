package com.alox1d.pcbcalc;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.alox1d.pcbcalc.utils.del_AppDatabase;

public class del_App extends Application {

    public static del_App instance;

    private del_AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, del_AppDatabase.class, "pcbcalc.db")
                .allowMainThreadQueries().build();
    }

    public static del_App getInstance() {
        return instance;
    }

    public del_AppDatabase getDatabase() {
        return database;
    }
}