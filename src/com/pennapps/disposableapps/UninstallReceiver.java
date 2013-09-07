package com.pennapps.disposableapps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class UninstallReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri packageUri;

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            // No need to open uninstall intent because the app is already getting removed
            packageUri = intent.getData();
        } else {
            packageUri = intent.getParcelableExtra("packageUri");
            startUninstallIntent(context, packageUri);
        }

        Database db = new Database(context);

        Alarm alarm = db.selectAlarmFromPackageUri(packageUri);
        db.deleteAlarm(alarm);
    }

    private void startUninstallIntent(final Context context, final Uri packageUri) {
        final Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }
}
