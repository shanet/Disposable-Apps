package com.pennapps.disposableapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Joseph on 9/6/13.
 */
public class PackageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Uri packageUri = intent.getData();
            Log.i("Enter", "Enters here");
            Toast.makeText(context, "App Installed or removed!!!!!.", Toast.LENGTH_LONG).show();
            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
            uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
        }
}
