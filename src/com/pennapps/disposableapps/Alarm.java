package com.pennapps.disposableapps;

import android.net.Uri;

public class Alarm {
    private int aid;
    private long time;
    private Uri packageUri;

    public Alarm(int aid, Uri uri, long time) {
        this.aid = aid;
        this.time = time;
        packageUri = uri;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAid() {
        return aid;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setPackageUri(Uri uri) {
        packageUri = uri;
    }

    public Uri getPackageUri() {
        return packageUri;
    }

    public String toString() {
        return "Aid: " + aid + " package: " + packageUri.toString() + " time: " + time;
    }
}
