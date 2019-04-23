package com.fixpapa.ffixpapa.Services;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.ArrayMap;
import android.util.Log;

import com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack;
import com.fixpapa.ffixpapa.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack.jobId;
import static com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack.custLat;
import static com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack.custLng;
import static com.fixpapa.ffixpapa.Services.Rest.Config.DISTANCE;
import static com.fixpapa.ffixpapa.Services.Rest.Config.DURATION;

/**
 * Created by advosoft on 7/23/2018.
 */

public class LocationService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
   public double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 900;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    SharedPreferences.Editor editor;
   // String jobId;
   // double cusLat,cusLng;
    boolean canGetLocation = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onStartCommand");
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 1, notify_interval);
        intent = new Intent(str_receiver);

    }

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean finish = intent.getBooleanExtra("finish", false);
       // tripId = intent.getStringExtra("id");
        //Log.d("tripId", intent.getStringExtra("id"));
        if (finish) {
            stopForeground(true);
            stopSelf();
        }
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        context = getApplicationContext();
        Intent playIntent = new Intent(getApplicationContext(), SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                playIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name) + " is running")
                .setContentText(getResources().getString(R.string.app_name) + " is running")
                .setSmallIcon(R.drawable.luncher_icon)
                //                //.setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build();


        startForeground(1001,
                notification);

        return START_STICKY;
    }
*/

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }



    @Override
    public void onProviderDisabled(String provider) {
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            /*if (isNetworkEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }*/

            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            }
            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                getLastKnownLocation();
                if (locationManager != null) {

                    LocationManager locationManager =
                            (LocationManager) this.getSystemService( LOCATION_SERVICE );
                    List<String> providers = locationManager.getProviders( true );
                    Location bestLocation = null;
                    for( String provider : providers ){
                        Location l = locationManager.getLastKnownLocation( provider );
                        if( l == null ){
                            continue;
                        }
                        if( bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy() ){
                            bestLocation = l; // Found best last known location;
                        }
                    }
                    if (bestLocation!=null) {
                        fn_update(bestLocation);
                    }
                  /*  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        this.canGetLocation = true;
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }*/
                }
            }


        }

    }

    private Location getLastKnownLocation(){
        if( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            return null;
        }
        LocationManager locationManager =
                (LocationManager) this.getSystemService( LOCATION_SERVICE );
        List<String> providers = locationManager.getProviders( true );
        Location bestLocation = null;
        for( String provider : providers ){
            Location l = locationManager.getLastKnownLocation( provider );
            if( l == null ){
                continue;
            }
            if( bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy() ){
                bestLocation = l; // Found best last known location;
            }
        }
        return bestLocation;
    }
    private void buildAlertMessageNoGps() {
        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("fnhrters",""+e);
        }
    }
    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location) {
        // MyApplication.setDouble(latitude, (location.getLatitude()));
        // MyApplication.setDouble(longitude, (location.getLongitude()));\


        latitude=location.getLatitude();
        longitude=location.getLongitude();

        String latt,lngg;
        latt= String.valueOf(latitude);
        lngg= String.valueOf(longitude);
        editor = getSharedPreferences("LO", MODE_PRIVATE).edit();
        editor.putString("lat", latt);
        editor.putString("lng", lngg);
        editor.apply();
        editor.commit();
        final String destination = String.valueOf("" + custLat + "," + custLng);
        final String source = String.valueOf("" + latitude + "," + longitude);
        SharedPreferences shared = getSharedPreferences("LO", MODE_PRIVATE);
        String lattt = shared.getString("lat", "");
        String lnggg = shared.getString("lng", "");
        Log.e("hreertfbfgbfg",""+lattt+" "+lnggg);
       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
                if (jobId != null) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("locations").child(jobId);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    Map<String, Object> jsonParams = new ArrayMap<>();
                    jsonParams.put("destination", destination);
                    jsonParams.put("source", source);
                    jsonParams.put("lat",latitude);
                    jsonParams.put("lng", longitude);
                    jsonParams.put("rem_distance", DISTANCE);
                    jsonParams.put("rem_duration", DURATION);
                    jsonParams.put("timestamp", tsLong);
                    mDatabase.updateChildren(jsonParams);

                }

         /*   }
        }, 100000);*/


        intent.putExtra("location", location);
        intent.putExtra("latutide", location.getLatitude() + "");
        intent.putExtra("longitude", location.getLongitude() + "");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        //sendBroadcast(intent);

    }
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        Log.e("fdsbbs", ""+longitude);
        return longitude;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }



}
