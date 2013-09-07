package com.pennapps.disposableapps;

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

    private static final String FIELD_ALARMS_KEY      = "aid";
    private static final String FIELD_ALARMS_PACKAGE  = "package";
    private static final String FIELD_ALARMS_DATE     = "date";

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


    public Alarm selectAlarm(int aid) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the db for the given relay with rid
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS + " WHERE " + FIELD_ALARMS_KEY + " = " + aid, null);

        // Go to the first result or result null if it doesn't exist
        if(!cursor.moveToFirst()) {
            db.close();
            return null;
        }

        // Create a new Alarm object and return it
        Alarm Alarm = new Alarm(cursor.getInt(0), Uri.parse(cursor.getString(1)), new Date(cursor.getInt(2)));

        db.close();
        return Alarm;
    }


    public ArrayList<Alarm> selectAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the db for all relays
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS, null);

        // Add each result to an ArrayList
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        // If no results, just close the connection and return an empty list
        if(!cursor.moveToFirst()) {
            db.close();
            return alarms;
        }

        do{
            alarms.add(new Alarm(cursor.getInt(0), Uri.parse(cursor.getString(1)), new Date(cursor.getInt(2))));
        } while(cursor.moveToNext());

        db.close();
        return alarms;
    }


    public int insertAlarm(Alarm Alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get info from the relay
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, Alarm.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, Alarm.getAlarmDate().getTime());

        // Insert the values into the db
        int aid = (int)db.insert(TABLE_ALARMS, null, values);
        db.close();

        return aid;
    }


    public int updateAlarm(Alarm Alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (Alarm.getAid() < 0)
            return  Constants.FAILURE;

        // Get info from the relay
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, Alarm.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, Alarm.getAlarmDate().getTime());

        db.update(TABLE_ALARMS, values, FIELD_ALARMS_KEY + " = " + Alarm.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }


    public int deleteAlarm(Alarm Alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check that the relay has a valid rid
        if(Alarm.getAid() < 0) return Constants.FAILURE;

        db.delete(TABLE_ALARMS, FIELD_ALARMS_KEY + " = " + Alarm.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }
}