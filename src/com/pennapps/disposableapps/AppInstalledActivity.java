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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class AppInstalledActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_installed);

        final Uri packageUri = getIntent().getParcelableExtra("packageUri");

        Button timerButton = (Button) findViewById(R.id.timerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
                final long uninstallTime = Long.parseLong(getResources().getStringArray(R.array.timeSpinnerMilliseconds)[timeSpinner.getSelectedItemPosition()]);
                setUninstallTimer(uninstallTime, packageUri);
            }
        });
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
