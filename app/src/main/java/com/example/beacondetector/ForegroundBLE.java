package com.example.beacondetector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class ForegroundBLE  extends Service implements BeaconConsumer {

    private static final String TAG = "WABBLER" ;
    private BeaconManager beaconManager;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
         beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.setRegionStatePersistenceEnabled(false);



        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        beaconManager.enableForegroundServiceScanning(notification,1);
        beaconManager.setForegroundScanPeriod(1100);
        beaconManager.setForegroundBetweenScanPeriod(0);
        beaconManager.bind(this);

        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
//        RangeNotifier rangeNotifier = new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                if (beacons.size() > 0) {
//                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
//                    Beacon firstBeacon = beacons.iterator().next();
//                    Log.d(TAG, "The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
//                }
//            }
//
//        };
//        try {
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            beaconManager.addRangeNotifier(rangeNotifier);
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            beaconManager.addRangeNotifier(rangeNotifier);
//        } catch (RemoteException e) {
//            Log.d(TAG, "onBeaconServiceConnect: EXCEPTION"+ e);
//        }

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
                Toast.makeText(getApplicationContext(), "I just saw a beacon for the first time!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                Toast.makeText(getApplicationContext(), "I no longer see a beacon", Toast.LENGTH_LONG).show();

            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }
}
