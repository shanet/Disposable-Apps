package com.pennapps.disposableapps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri packageUri = intent.getData();

        // Start the app install activity
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Database alarmDb = new Database(context);
            ArrayList<Alarm> alarms = alarmDb.selectAllAlarms();

            for (Alarm info : alarms) {
                long alarmDate = info.getAlarmDate().getTime();
                long currentDate = System.currentTimeMillis();

                long alarmSetDate = alarmDate - currentDate;

                if (alarmSetDate > 0) {
                     Utils.setUninstallTimer(context, alarmSetDate, info.getPackageUri());
                } else {
                    Utils.setUninstallTimer(context, 10000, info.getPackageUri());
                }
            }
        } else if (intent.getAction().equals(Intent.ACTION_INSTALL_PACKAGE) || intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            intent = new Intent(context, AppInstalledActivity.class);
            intent.putExtra("packageUri", packageUri);
            context.startActivity(intent);
        }
    }
}
