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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity {
    ArrayList<AlarmInfo> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupAlarmsList();

        registerAPKInstallReceiver();
    }

    private void setupAlarmsList() {
        ListView alarmsList = (ListView) findViewById(R.id.alarmsList);
        alarmsList.setEmptyView(findViewById(R.id.emptyListLayout));
        
        // Load all alarms from the db and set the list adapter
        Database database = new Database(this);
        alarms = database.selectAllAlarms();

        AlarmAdapter alarmAdapter = new AlarmAdapter(this, alarms);
        alarmsList.setAdapter(alarmAdapter);
        
        // Set the long click menu on the alarm list
        registerForContextMenu(alarmList);
    }

    private void registerAPKInstallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_INSTALL_PACKAGE);
        intentFilter.addDataScheme("package");
        registerReceiver(new PackageReceiver(), intentFilter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get the selected alarm
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        AlarmInfo selectedAlarm = alarms.get(menuInfo.position);
        
        switch (item.getItemId()) {
            case R.id.cancelAlarm:
                // TODO: this.
                //DialogUtils.showDeleteDialog(this, selectedAlarm);
                return true;
                
            default:
                return false;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
