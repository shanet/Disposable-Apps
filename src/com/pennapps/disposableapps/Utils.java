package com.pennapps.disposableapps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;

/**
 * Created by Joseph on 9/7/13.
 */
public abstract class Utils {
    public static void setUninstallTimer(Context context, final long milliseconds, final Uri packageUri) {
        final Intent intent = new Intent(context, UninstallReceiver.class);
        intent.putExtra("packageUri", packageUri);

        PendingIntent uninstallIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if(uninstallIntent != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            am.cancel(uninstallIntent);
            am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + milliseconds, uninstallIntent);
        }
    }

    public static void removeUninstallTimer(Context context, final Uri packageUri){
        final Intent intent = new Intent(context, UninstallReceiver.class);
        intent.putExtra("packageUri", packageUri);

        PendingIntent uninstallIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if(uninstallIntent != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            am.cancel(uninstallIntent);
        }
    }
}
