package com.alox1d.pcbcalc.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alox1d.pcbcalc.models.InfluenceOfHumidityAndTempCoeffItem;
import com.alox1d.pcbcalc.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfluenceOfhumidityCoeffsRep {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static String TABLE = "InfluenceOfhumidityCoeffs";
    private static String KEY_ID = "Id";
    private static String KEY_HUMIDITY_NAME = "Humidity";
    private static String KEY_TEMPERATURE_NAME = "Temperature";
    private static String KEY_CORRECTION_COEFF = "CorrectionCoeff";




    public InfluenceOfhumidityCoeffsRep(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());

        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        open();
    }

    public InfluenceOfhumidityCoeffsRep open(){
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
        String[] columns = new String[] {KEY_ID, KEY_HUMIDITY_NAME, KEY_TEMPERATURE_NAME, KEY_CORRECTION_COEFF};
        return  database.query(TABLE, columns, null, null, null, null, null);
    }

    public List<InfluenceOfHumidityAndTempCoeffItem> getItems(){
        ArrayList<InfluenceOfHumidityAndTempCoeffItem> items = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String nameHum = cursor.getString(cursor.getColumnIndex(KEY_HUMIDITY_NAME));
                String nameTemp = cursor.getString(cursor.getColumnIndex(KEY_TEMPERATURE_NAME));
                Double value = cursor.getDouble(cursor.getColumnIndex(KEY_CORRECTION_COEFF));
                items.add(new InfluenceOfHumidityAndTempCoeffItem(id, nameHum, nameTemp, value));
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
