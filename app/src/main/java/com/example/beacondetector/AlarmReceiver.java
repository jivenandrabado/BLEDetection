package com.example.beacondetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String TAG = "WABBLER";
        Log.d(TAG, "onReceive: alarm received +++++++++++++++++++++++++++++++++++++++++++");
        Toast.makeText(context,"Alarm Received ++++ ", Toast.LENGTH_LONG).show();

        Intent serviceIntent = new Intent(context, ForegroundBLE.class);
        context.stopService(serviceIntent);

        ContextCompat.startForegroundService(context,serviceIntent);
    }
}
