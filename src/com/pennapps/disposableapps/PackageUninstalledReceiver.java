package com.pennapps.disposableapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Joseph on 9/7/13.
 */
public class PackageUninstalledReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getAction() == null)
            return;

        Uri packageUri = intent.getData();
        Database db = new Database(context);

        Alarm alarm = db.selectAlarmFromPackageUri(packageUri);
        db.deleteAlarm(alarm);
    }
}
