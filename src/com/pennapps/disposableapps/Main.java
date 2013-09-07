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
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity {
    ArrayList<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupAlarmsList();

        registerAPKInstallReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAlarmsList();
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
        registerForContextMenu(alarmsList);
    }

    private void registerAPKInstallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_INSTALL_PACKAGE);
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        intentFilter.addDataScheme("package");
        registerReceiver(new PackageReceiver(), intentFilter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get the selected alarm
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        Alarm selectedAlarm = alarms.get(menuInfo.position);
        
        switch (item.getItemId()) {
            case R.id.cancelAlarm:
                // TODO: this.
                //DialogUtils.showDeleteDialog(this, selectedAlarm);
                return true;
                
            default:
                return false;
        }
    }

    /*public static void showDeleteDialog(final Context context, final Alarm alarm) {
        // Show the dialog to confirm the deletion of alarm
        new AlertDialog.Builder(context)
        .setTitle(R.string.deleteRelayTitle)
        .setMessage(String.format(context.getString(R.string.deleteDialog), alarm.getName()))
        .setIcon(R.drawable.error_icon)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Delete the alarm from the db
                new Database(context).deleteRelay(alarm);
                Toast.makeText(context, R.string.deletedRelay, Toast.LENGTH_SHORT).show();
                
                // Tell the main activity to reload the relays
                ((Main)context).reloadRelaysAndGroupsFromDatabase();
            }
        })
        .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        })
        .show();
    }*/
}
