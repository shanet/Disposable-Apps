package com.pennapps.disposableapps;

/**
 * Created by Joseph on 9/7/13.
 */
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DisposableApps";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ALARMS  = "alarms";

    private static final String FIELD_ALARMS_KEY    = "aid";
    private static final String FIELD_ALARMS_PACKAGE  = "package";
    private static final String FIELD_ALARMS_DATE   = "date";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        // Create the alarms table
        db.execSQL("CREATE TABLE " + TABLE_ALARMS + "(" +
                FIELD_ALARMS_KEY      + " INTEGER PRIMARY KEY, " +
                FIELD_ALARMS_PACKAGE  + " TEXT, " +
                FIELD_ALARMS_DATE     + " INTEGER)");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        // Create new tables
        onCreate(db);
    }


    public AlarmInfo selectAlarmInfo(int aid) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the db for the given relay with rid
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS + " WHERE " + FIELD_ALARMS_KEY + " = " + aid, null);

        // Go to the first result or result null if it doesn't exist
        if(!cursor.moveToFirst()) {
            db.close();
            return null;
        }

        // Create a new alarminfo object and return it
        AlarmInfo alarmInfo = new AlarmInfo(cursor.getInt(0), Uri.parse(cursor.getString(1)), new Date(cursor.getInt(2)));

        db.close();
        return alarmInfo;
    }


    public ArrayList<AlarmInfo> selectAllAlarmInfos() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the db for all relays
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS, null);

        // Add each result to an ArrayList
        ArrayList<AlarmInfo> alarms = new ArrayList<AlarmInfo>();

        // If no results, just close the connection and return an empty list
        if(!cursor.moveToFirst()) {
            db.close();
            return alarms;
        }

        do{
            alarms.add(new AlarmInfo(cursor.getInt(0), Uri.parse(cursor.getString(1)), new Date(cursor.getInt(2))));
        } while(cursor.moveToNext());

        db.close();
        return alarms;
    }


    public int insertAlarmInfo(AlarmInfo alarmInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get info from the relay
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, alarmInfo.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, alarmInfo.getAlarmDate().getTime());

        // Insert the values into the db
        int aid = (int)db.insert(TABLE_ALARMS, null, values);
        db.close();

        return aid;
    }


    public int updateAlarmInfo(AlarmInfo alarmInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (alarmInfo.getAid() < 0)
            return  Constants.FAILURE;

        // Get info from the relay
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, alarmInfo.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, alarmInfo.getAlarmDate().getTime());

        db.update(TABLE_ALARMS, values, FIELD_ALARMS_KEY + " = " + alarmInfo.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }


    public int deleteAlarmInfo(AlarmInfo alarmInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check that the relay has a valid rid
        if(alarmInfo.getAid() < 0) return Constants.FAILURE;

        db.delete(TABLE_ALARMS, FIELD_ALARMS_KEY + " = " + alarmInfo.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }
}