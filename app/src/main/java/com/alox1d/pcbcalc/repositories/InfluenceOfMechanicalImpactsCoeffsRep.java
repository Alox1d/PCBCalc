package com.alox1d.pcbcalc.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alox1d.pcbcalc.models.ComponentFailureRateItem;
import com.alox1d.pcbcalc.models.InfluenceOfMechanicalImpactsCoeffItem;
import com.alox1d.pcbcalc.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfluenceOfMechanicalImpactsCoeffsRep {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static String TABLE = "InfluenceOfMechanicalImpactsCoeffs";
    private static String KEY_ID = "Id";
    private static String KEY_EXPLORATION_CONDITIONS_NAME = "ExploitationConditions";
    private static String KEY_K1 = "K1";
    private static String KEY_K2 = "K2";




    public InfluenceOfMechanicalImpactsCoeffsRep(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());

        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        open();
    }

    public InfluenceOfMechanicalImpactsCoeffsRep open(){
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
        String[] columns = new String[] {KEY_ID, KEY_EXPLORATION_CONDITIONS_NAME, KEY_K1, KEY_K2};
        return  database.query(TABLE, columns, null, null, null, null, null);
    }

    public List<InfluenceOfMechanicalImpactsCoeffItem> getItems(){
        ArrayList<InfluenceOfMechanicalImpactsCoeffItem> items = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_EXPLORATION_CONDITIONS_NAME));
                Double valueK1 = cursor.getDouble(cursor.getColumnIndex(KEY_K1));
                Double valueK2 = cursor.getDouble(cursor.getColumnIndex(KEY_K2));
                items.add(new InfluenceOfMechanicalImpactsCoeffItem(id, name, valueK1, valueK2));
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
