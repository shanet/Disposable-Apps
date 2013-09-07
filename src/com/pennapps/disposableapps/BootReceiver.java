package com.pennapps.disposableapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/7/13.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DisposableApps", "Boot Completed");

        // Start the app install activity
        Database alarmDb = new Database(context);
        ArrayList<Alarm> alarms = alarmDb.selectAllAlarms();

        for (Alarm info : alarms) {
            long alarmDate = info.getTime();
            long currentDate = System.currentTimeMillis();

            long alarmSetDate = alarmDate - currentDate;

            // want to check for events that passed while shut off or otherwise
            if (alarmSetDate > 0) {
                Utils.setUninstallTimer(context, alarmSetDate, info.getPackageUri());
            } else {
                Utils.setUninstallTimer(context, 10000, info.getPackageUri());
            }
        }
    }
}
