package com.example.beacondetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnStartService, btnStopService;
    String TAG = "WABBLER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    public void startService() {
        if(PermissionsHelper.checkLocationPermission(MainActivity.this)) {
            Log.d(TAG, "startService: permission granted");
            Intent serviceIntent = new Intent(this, ForegroundBLE.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }else{
            Log.d(TAG, "startService: permission not granted");
            PermissionsHelper.requestLocationPermission(MainActivity.this);
        }
    }
    public void stopService() {
        if(PermissionsHelper.checkLocationPermission(MainActivity.this)) {
            Intent serviceIntent = new Intent(this, ForegroundBLE.class);
            stopService(serviceIntent);
        }else{
            PermissionsHelper.requestLocationPermission(MainActivity.this);
        }
    }


}