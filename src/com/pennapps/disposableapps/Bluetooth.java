package com.pennapps.disposableapps;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Set;

public class Bluetooth extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    Integer REQ_BT_ENABLE = 1;
    private ArrayAdapter<String> mArrayAdapter;
    Set<BluetoothDevice> pairedDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mArrayAdapter = new ArrayAdapter<String>(this, R.id.bluetoothText);
        String Status;

        if(mBluetoothAdapter.isEnabled())
        {
            String mydeviceadress = mBluetoothAdapter.getAddress();
            String mydevicename = mBluetoothAdapter.getName();
            Status = "My Device Name is:" + mydevicename + "\nThe Address is:" + mydeviceadress;
            Toast.makeText(this, Status, Toast.LENGTH_LONG).show();
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQ_BT_ENABLE);
            Toast.makeText(this, "Enabling Bluetooth!!", Toast.LENGTH_LONG).show();
        }

        //Getting paired devices and saving them on an ArrayAdapter
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        //Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver Receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(Receiver, filter);

        //to make the device discoverable
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }
}