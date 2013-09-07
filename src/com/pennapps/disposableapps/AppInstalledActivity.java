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
    private Database database;

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
                    // Save the alarm info in the db
                    final Alarm alarm = new Alarm(0, packageUri, System.currentTimeMillis() + uninstallTime);
                    alarm.setAid(database.insertAlarm(alarm));

                    // Set the timer to uninstall the app
                    Utils.setUninstallTimer(AppInstalledActivity.this, alarm.getAid(), uninstallTime, packageUri);

                    Toast.makeText(AppInstalledActivity.this, AppInstalledActivity.this.getString(R.string.timerSet), Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

    }
}
