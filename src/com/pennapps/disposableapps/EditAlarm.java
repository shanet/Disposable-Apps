package com.pennapps.disposableapps;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EditAlarm extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_installed);

        final Uri packageUri = getIntent().getParcelableExtra("packageUri");

        try {
            final PackageManager pm = getPackageManager();
            final ApplicationInfo ai = pm.getApplicationInfo(packageUri.toString().replace("package:", ""), 0);
            final String appTitle = pm.getApplicationLabel(ai).toString();
            this.setTitle(String.format(getResources().getString(R.string.appInstalledActivityTitle), appTitle));
        } catch (PackageManager.NameNotFoundException nnfe) {

        }

        // Get the relay to edit
        final Database db = new Database(this);

        final Spinner timeSpinner  = (Spinner) findViewById(R.id.timeSpinner);
        Button editAlarmButton     = (Button) findViewById(R.id.timerButton);

        editAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
                final long uninstallTime = Long.parseLong(getResources().getStringArray(R.array.timeSpinnerMilliseconds)[timeSpinner.getSelectedItemPosition()]);

                // Skip the uninstall timer if the forever option was selected
                if(uninstallTime != 0) {
                    // Save the alarm info in the db
                    Alarm alarm = db.selectAlarmFromPackageUri(packageUri);

                    if (alarm == null) {
                        alarm = new Alarm(0, packageUri, System.currentTimeMillis() + uninstallTime);
                        alarm.setAid(db.insertAlarm(alarm));
                    } else {
                        alarm.setTime(System.currentTimeMillis() + uninstallTime);
                        db.updateAlarm(alarm);
                    }
                    // Set the timer to uninstall the app
                    Utils.setUninstallTimer(EditAlarm.this, alarm.getAid(), uninstallTime, packageUri);

                    Toast.makeText(EditAlarm.this, EditAlarm.this.getString(R.string.timerSet), Toast.LENGTH_SHORT).show();
                } else {
                    Alarm alarm = db.selectAlarmFromPackageUri(packageUri);
                    if (alarm != null) {
                        db.deleteAlarm(alarm);

                        Utils.removeUninstallTimer(EditAlarm.this, packageUri);
                    }
                }

                finish();
            }
        });
    }
}
