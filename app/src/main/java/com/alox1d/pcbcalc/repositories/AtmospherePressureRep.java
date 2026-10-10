package com.alox1d.pcbcalc.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alox1d.pcbcalc.models.AtmospherePressureItem;
import com.alox1d.pcbcalc.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AtmospherePressureRep {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static String TABLE = "AtmospherePressure";
    private static String KEY_ID = "Id";
    private static String KEY_PRESSURE = "Pressure";
    private static String KEY_CORRECTION_COEFF = "CorrectionCoeff";




    public AtmospherePressureRep(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());

        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        open();
    }

    public AtmospherePressureRep open(){
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {KEY_ID, KEY_PRESSURE, KEY_CORRECTION_COEFF};
        return  database.query(TABLE, columns, null, null, null, null, null); // TODO джоин с rowQuery (что делать с моделью) ИЛИ вызов по foreign key; ДАЛЕЕ сделать UI по плану
    }

    public List<AtmospherePressureItem> getItems(){
        ArrayList<AtmospherePressureItem> items = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String pressure = cursor.getString(cursor.getColumnIndex(KEY_PRESSURE));
                Double value = cursor.getDouble(cursor.getColumnIndex(KEY_CORRECTION_COEFF));
                items.add(new AtmospherePressureItem(id, pressure,  value));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return  items;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, TABLE);
    }

//    public ComponentFailureRateItem getUser(long id){
//        ComponentFailureRateItem user = null;
//        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
//        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
//        if(cursor.moveToFirst()){
//            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
//            int year = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR));
//            user = new ComponentFailureRateItem(id, name, year);
//        }
//        cursor.close();
//        return  user;
//    }


}
