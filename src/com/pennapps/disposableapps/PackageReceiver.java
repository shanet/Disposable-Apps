package com.pennapps.disposableapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri packageUri = intent.getData();

        // Start the app install activity
        intent = new Intent(context, AppInstalledActivity.class);
        intent.putExtra("packageUri", packageUri);
        context.startActivity(intent);
    }
}
