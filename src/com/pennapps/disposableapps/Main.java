package com.pennapps.disposableapps;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_INSTALL_PACKAGE);
        intentFilter.addDataScheme("package");
        registerReceiver(new PackageReceiver(), intentFilter);

        startActivity(new Intent(this, AppInstalledActivity.class));
    }


    public void scheduleApkUninstallation(final long milliseconds) {
        PendingIntent uninstallIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, UninstallReceiver.class), 0);
        
        if(uninstallIntent != null) {
            AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
            am.cancel(uninstallIntent);
            am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + milliseconds, uninstallIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
