package com.pennapps.disposableapps;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<AlarmInfo> {
    private Context context;
    private ArrayList<AlarmInfo> alarms;
    
    public AlarmAdapter(Context context, ArrayList<AlarmInfo> alarms) {
        super(context, R.layout.alarm_adapter, alarms);
        this.context = context;
        this.alarms = alarms;
    }
    
    public View getView(final int position, View view, ViewGroup parent) {
        // Inflate the view if it has not  been created yet
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_adapter, null);
        }
        
        TextView alarmNameLabel = (TextView) view.findViewById(R.id.alarmNameLabel);
        TextView timeLeftLabel = (TextView) view.findViewById(R.id.timeLeftLabel);
        
        // Set the name on the label
        alarmNameLabel.setText(alarms.get(position).getPackageUri() + ":");
        timeLeftLabel.setText(alarms.get(position).getPackageUri() + ":");
        
        return view;
    }
}
