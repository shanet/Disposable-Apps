package com.pennapps.disposableapps;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class AppInstalledActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_installed);

        Uri packageUri = getIntent().getParcelableExtra("packageUri");

        // TODO: on button click set the uninstall timer
        //setUninstallImter();
    }

    private void setUninstallTimer(final long milliseconds, final Uri packageUri) {
        final Intent intent = new Intent(this, UninstallReceiver.class);
        intent.putExtra("packageUri", packageUri);

        PendingIntent uninstallIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        
        if(uninstallIntent != null) {
            AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
            am.cancel(uninstallIntent);
            am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + milliseconds, uninstallIntent);
        }
    }
}
