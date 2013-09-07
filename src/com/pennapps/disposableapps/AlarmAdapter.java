package com.pennapps.disposableapps;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private Context context;
    private ArrayList<Alarm> alarms;
    private PackageManager pm;
    
    public AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.alarm_adapter, alarms);
        this.context = context;
        this.alarms = alarms;

        pm = context.getPackageManager();
    }
    
    public View getView(final int position, View view, ViewGroup parent) {
        // Inflate the view if it has not  been created yet
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_adapter, null);
        }
        
        TextView alarmNameLabel = (TextView) view.findViewById(R.id.alarmNameLabel);
        TextView timeLeftLabel = (TextView) view.findViewById(R.id.timeLeftLabel);

        ApplicationInfo ai = getApplicationInfo(alarms.get(position).getPackageUri());
        
        // Set the name on the label
        alarmNameLabel.setText(getApplicationLabel(ai));
        timeLeftLabel.setText(getApplicationLabel(ai));
        
        return view;
    }

    private ApplicationInfo getApplicationInfo(final Uri packageUri) {
        try {
            return pm.getApplicationInfo(packageUri.toString().replace("package:", ""), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private String getApplicationLabel(final ApplicationInfo ai) {
        return pm.getApplicationLabel(ai).toString();
    }

    private Drawable getApplicationIcon(final ApplicationInfo ai) {
        return pm.getApplicationIcon(ai);
    }
}
