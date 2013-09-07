package com.pennapps.disposableapps;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Joseph on 9/7/13.
 */
public class AlarmInfo {
    private int aid;
    private Date alarmDate;
    private Uri packageUri;

    public AlarmInfo(int aid, Uri uri, Date date)
    {
        this.aid = aid;
        alarmDate = date;
        packageUri = uri;
    }

    public void setAid(int aid)
    {
        this.aid = aid;
    }

    public int getAid()
    {
        return aid;
    }

    public void setAlarmDate(Date date)
    {
        alarmDate = date;
    }

    public Date getAlarmDate()
    {
        return alarmDate;
    }

    public void setPackageUri(Uri uri)
    {
        packageUri = uri;
    }

    public Uri getPackageUri()
    {
        return packageUri;
    }
}
