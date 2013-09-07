package com.pennapps.disposableapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Joseph on 9/6/13.
 */
public class PackageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("Enter", "Enters here");
            Toast.makeText(context, "App Installed or removed!!!!!.", Toast.LENGTH_LONG).show();
        }
}
