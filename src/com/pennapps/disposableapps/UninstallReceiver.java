package com.pennapps.disposableapps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UninstallReceiver extends BroadcastReceiver {
    
    @Override public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, "Got uninstall", Toast.LENGTH_SHORT).show();

    }
}
