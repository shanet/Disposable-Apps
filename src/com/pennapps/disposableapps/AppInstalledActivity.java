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

import java.util.Date;


public class AppInstalledActivity extends Activity {
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_installed);

        final Uri packageUri = getIntent().getParcelableExtra("packageUri");

        database = new Database(this);

        Button timerButton = (Button) findViewById(R.id.timerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
                final long uninstallTime = Long.parseLong(getResources().getStringArray(R.array.timeSpinnerMilliseconds)[timeSpinner.getSelectedItemPosition()]);

                // Skip the uninstall timer if the forever option was selected
                if(uninstallTime != 0) {
                    final Date alarmDate = new Date(System.currentTimeMillis() + uninstallTime);
                    final Alarm alarm = new Alarm(0, packageUri, alarmDate);

                    alarm.setAid(database.insertAlarm(alarm));

                    Utils.setUninstallTimer(AppInstalledActivity.this, uninstallTime, packageUri);
                }
                finish();
            }
        });

    }
}
