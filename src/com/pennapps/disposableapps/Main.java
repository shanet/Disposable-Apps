package com.pennapps.disposableapps;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get the selected alarm
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        Alarm selectedAlarm = alarms.get(menuInfo.position);
        
        switch (item.getItemId()) {
            case R.id.cancelAlarm:
                showDeleteDialog(this, selectedAlarm);
                return true;
            case R.id.editAlarm:
                // TODO: this
                showEditDialog(this, selectedAlarm.getPackageUri());
                return true;
            case R.id.uninstallAlarmApp:
                return true;
            default:
                return false;
        }
    }

    public void showEditDialog(final Context context, final Uri packageUri){

        Intent intent = new Intent(context, EditAlarm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageUri", packageUri);
        context.startActivity(intent);

        // Tell the main activity to reload the alarms
        ((Main) context).setupAlarmsList();

    }


    public void showDeleteDialog(final Context context, final Alarm alarm) {

        PackageManager pm = getApplicationContext().getPackageManager();

        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(alarm.getPackageUri().toString().replace("package:", ""), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        CharSequence appName = pm.getApplicationLabel(ai);
        // Show the dialog to confirm the deletion of alarm
        new AlertDialog.Builder(context)
        .setTitle(R.string.cancelTimerTitle)
        .setMessage(String.format(context.getString(R.string.cancelTimerDialog), appName))
        .setIcon(R.drawable.action_about)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Delete the alarm from the db
                new Database(context).deleteAlarm(alarm);
                Toast.makeText(context, R.string.deletedAlarm, Toast.LENGTH_SHORT).show();

                // Remove pending alarm
                Utils.removeUninstallTimer(context, alarm.getPackageUri());

                // Tell the main activity to reload the relays
                ((Main) context).setupAlarmsList();
            }
        })
        .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        })
        .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.alarmsList) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.alarms_context_menu, menu);
        }
    }
}
