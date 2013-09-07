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

        // Query the db for the given alarm with aid
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS + " WHERE " + FIELD_ALARMS_KEY + " = " + aid, null);

        // Go to the first result or result null if it doesn't exist
        if(!cursor.moveToFirst()) {
            db.close();
            return null;
        }

        // Create a new alarm object and return it
        Alarm alarmInfo = new Alarm(cursor.getInt(0), Uri.parse(cursor.getString(1)), cursor.getLong(2));

        db.close();
        return alarmInfo;
    }


    public ArrayList<Alarm> selectAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the db for all alarms
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS, null);

        // Add each result to an ArrayList
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        // Go to the first result or result null if it doesn't exist
        if(!cursor.moveToFirst()) {
            db.close();
            return alarms;
        }

        do{
            int aid = cursor.getInt(0);
            String packageString = cursor.getString(1);
            long time = cursor.getLong(2);
            alarms.add(new Alarm(aid, Uri.parse(packageString), time));
        } while(cursor.moveToNext());

        db.close();
        return alarms;
    }

    public Alarm selectAlarmFromPackageUri(Uri packageUri) {
        if (packageUri == null)
            return null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS + " WHERE " + FIELD_ALARMS_PACKAGE + " = \"" + packageUri.toString() + "\"", null);

        // If no results, just close the connection and return an empty list
        if(!cursor.moveToFirst()) {
            db.close();
            return null;
        }

        // Create a new alarm object and return it
        Alarm alarmInfo = new Alarm(cursor.getInt(0), Uri.parse(cursor.getString(1)), cursor.getLong(2));

        db.close();
        return alarmInfo;
    }

    public int insertAlarm(Alarm alarmInfo) {
        if (alarmInfo == null) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Get info from the alarm
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, alarmInfo.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, alarmInfo.getTime());

        // This bit is based on the idea that we'd only ever want an entry for an app once.  That entry should then be updated
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS + " WHERE " + FIELD_ALARMS_PACKAGE + " = \"" + alarmInfo.getPackageUri().toString() + "\"", null);

        int aid;
        if (!cursor.moveToFirst()) {
            // Insert the values into the db
            aid = (int)db.insert(TABLE_ALARMS, null, values);
        } else {
            updateAlarm(alarmInfo);
            aid = alarmInfo.getAid();
        }
        db.close();

        return aid;
    }

    public int updateAlarm(Alarm alarmInfo) {
        if (alarmInfo == null)
            return Constants.FAILURE;

        SQLiteDatabase db = this.getWritableDatabase();

        if (alarmInfo.getAid() < 0) {
            return  Constants.FAILURE;
        }

        // Get info from the alarm
        ContentValues values = new ContentValues();
        values.put(FIELD_ALARMS_PACKAGE, alarmInfo.getPackageUri().toString());
        values.put(FIELD_ALARMS_DATE, alarmInfo.getTime());

        db.update(TABLE_ALARMS, values, FIELD_ALARMS_KEY + " = " + alarmInfo.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }

    public int deleteAlarm(Alarm alarmInfo) {
        if (alarmInfo == null)
            return Constants.FAILURE;

        SQLiteDatabase db = this.getWritableDatabase();

        // Check that the alarm has a valid aid
        if(alarmInfo.getAid() < 0) return Constants.FAILURE;

        db.delete(TABLE_ALARMS, FIELD_ALARMS_KEY + " = " + alarmInfo.getAid(), null);
        db.close();

        return Constants.SUCCESS;
    }
}