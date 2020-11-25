package com.example.beacondetector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionsHelper {

    boolean permission_granted;
    static String TAG = "WABBLER";

    public static boolean checkLocationPermission(Context context){

        //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //                boolean x = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_PRIVILEGED) == PERMISSION_GRANTED;
        //                Log.d(TAG, "checkLocationPermission: "+ x);
        //
        //                return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_PRIVILEGED) == PERMISSION_GRANTED;
        //            }else{
        //            }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }


    public static void requestLocationPermission(Context context){
        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                        (Activity)context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.BLUETOOTH_PRIVILEGED},
                        0);
            }else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ActivityCompat.requestPermissions(
                            (Activity)context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_PRIVILEGED},
                            0);
                }else{
                    ActivityCompat.requestPermissions(
                            (Activity)context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                }

            }
        } catch (Exception e){

            Log.d(TAG, "requestLocationPermission: "+ e);
        }
    }
}
