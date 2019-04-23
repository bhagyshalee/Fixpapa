package com.fixpapa.ffixpapa.EngineerPart;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;

import com.fixpapa.ffixpapa.Services.LocationService;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Config.DURATION;
import static com.fixpapa.ffixpapa.Services.Rest.Config.LATITUDE;
import static com.fixpapa.ffixpapa.Services.Rest.Config.LONGITUDE;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getCompleteAddressString;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class MapEngineerTrack extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    CardView engineerLocation, customerLocation;
    ImageView backIcon, upIcon, downIcon;
    TextView timeDuration, customerLocationValue, engineerLocationValue;

    LocationService locationService;
  //  double latitude, longitude;
    public static double custLat, custLng;
    Button viewOngoingJobs, startJobBtn;
    EditText otpValue;
    PickModel pickModel;
    List<AddPart> addPart;
    List<Problems> problemsList;
    List<String> listImage;
    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    // GPSTracker gpsTracker;
    Marker bikeMarker;
    long notify_interval = 60000;
    AddressesModel addressesModel;
    ImageView callImage;
    String getcustomerLocation, otp, categoryName, productName, brandName, budget, dateTimeStart, dateTimeEnd, jobOrderId,
            timeDurationstr, jobDescription, scheduleStatus, dateVendor, engineerId, customerName, customerMobile;
    public static String jobId;
    List<Double> latitudeV;
    List<Double> longitudeV;
    ArrayList points = new ArrayList();
    SharedPreferences shared;
    int setpoly = 0;
    SupportMapFragment mapFragment;
    Intent intentService;
   // double latitude,longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_engineer_track);
        intentService = new Intent(MapEngineerTrack.this, LocationService.class);
        startService(intentService);


        latitudeV = new ArrayList<>();
        longitudeV = new ArrayList<>();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        getcustomerLocation = bundle.getString("street") + " " + bundle.getString("value");
        custLat = Double.parseDouble(bundle.getString("latitude"));
        custLng = Double.parseDouble(bundle.getString("longitude"));
        otp = bundle.getString("otp");
        jobId = bundle.getString("jobId");
        //Log.e("eberbge",""+custLat+""+custLng);
        pickModel = (PickModel) bundle.getSerializable("pick");
        addPart = (List<AddPart>) bundle.getSerializable("addpart");
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");
        budget = bundle.getString("budget");
        dateTimeStart = bundle.getString("dateTimeStart");
        dateTimeEnd = bundle.getString("dateTimeEnd");
        jobOrderId = bundle.getString("jonOrderId");
        timeDurationstr = bundle.getString("timeDuration");
        problemsList = (List<Problems>) bundle.getSerializable("sendIssues");
        addressesModel = (AddressesModel) bundle.getSerializable("address");
        listImage = (List<String>) bundle.getSerializable("jobImage");
        jobDescription = bundle.getString("jobDescription");
        dateVendor = bundle.getString("dateVendorAssign");
        scheduleStatus = bundle.getString("scheduleStatus");
        engineerId = bundle.getString("enginId");
        customerName = bundle.getString("customerName");
        customerMobile = bundle.getString("customerMobile");
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        engineerLocation = (CardView) findViewById(R.id.engineerLocation);
        customerLocation = (CardView) findViewById(R.id.customerLocation);
        backIcon = (ImageView) findViewById(R.id.backIcon);
        upIcon = (ImageView) findViewById(R.id.upIcon);
        downIcon = (ImageView) findViewById(R.id.downIcon);
        timeDuration = (TextView) findViewById(R.id.timeDuration);
        customerLocationValue = (TextView) findViewById(R.id.customerLocationValue);
        engineerLocationValue = (TextView) findViewById(R.id.engineerLocationValue);
        startJobBtn = (Button) findViewById(R.id.startJobBtn);
        viewOngoingJobs = (Button) findViewById(R.id.viewOngoingJobs);
        otpValue = (EditText) findViewById(R.id.otpValue);
        callImage = (ImageView) findViewById(R.id.callImage);

        customerLocationValue.setText(getcustomerLocation);
        upIcon.setOnClickListener(this);
        downIcon.setOnClickListener(this);
        backIcon.setOnClickListener(this);
        viewOngoingJobs.setOnClickListener(this);
        startJobBtn.setOnClickListener(this);
        callImage.setOnClickListener(this);





        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MapEngineerTrack.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapEngineerTrack.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapEngineerTrack.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(MapEngineerTrack.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {

               //getLatLng(latitude,longitude);
            }
        } else {
             //getLatLng(latitude,longitude);
        }
        mapFragment.getMapAsync(this);

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    drawPoly();
                }
            });

        }
    }

    void animateMarker(final Location destination, final Marker marker) {

        Log.e("dsvdfvdfbfdbddfv", mMap + " " + marker);
        if (marker != null && mMap != null) {

            /*new code*/
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1555;

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
            Log.e("dsvdfvdfbfdbd", startPosition + "");
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 100f);
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float v = valueAnimator.getAnimatedFraction();
                    double lng = v * endPosition.longitude + (1 - v)
                            * startPosition.longitude;
                    double lat = v * endPosition.latitude + (1 - v)
                            * startPosition.latitude;
                    final LatLng newPos = new LatLng(lat, lng);
                    Location location1 = new Location("");
                    location1.setLatitude(startPosition.latitude);
                    location1.setLongitude(startPosition.longitude);


                    Location location2 = new Location("");
                    location2.setLatitude(newPos.latitude);
                    location2.setLongitude(newPos.longitude);
                    final float bear = location1.bearingTo(location2);
                    Log.e("distance", location1.distanceTo(location2) + "");
                    Log.e("accuracy", location2.getAccuracy() + "");
                    Log.e("total", location1.distanceTo(location2) - location2.getAccuracy() + "");
                    Log.e("update_bearing", bear + "");

                    if (location1.distanceTo(location2) - location2.getAccuracy() > 8) {

                        final Interpolator interpolator = new LinearInterpolator();
                        if (marker != null) {
                           // marker.remove();
                            marker.setPosition(newPos);
                            marker.setAnchor(0.5f, 0.5f);
                            marker.setRotation(bear);
                        }
                       /* if (setpoly==0)
                        {
                            getLatLngLatest(newPos.latitude,newPos.longitude);
                        }*/
                  /*  if (location1.distanceTo(location2) - location2.getAccuracy() > 10) {
                            if (getUpdatedValue==100){
                              *//*  getUpdatedValue=getUpdatedValue+1;
                                if (getUpdatedValue==200){*//*
                                drawPoly(newPos.latitude, newPos.longitude);
                            }

                        }*/
/*
                        if (location1.distanceTo(location2) - location2.getAccuracy() > 10) {
                            mMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(newPos)
                                                    //.zoom(13.5f)
                                                    .build()));
                            drawPoly(newPos.latitude, newPos.longitude);
                        }
*/

                    }
               /*   if (setpoly==0)
                  {
                      setpoly=1;
                      drawPoly(newPos.latitude,newPos.longitude);
                  }*/


              /*    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newPos));
                    }*/

/*
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                long elapsed = SystemClock.uptimeMillis() - start;
                                float t = interpolator.getInterpolation((float) elapsed / duration);

                                float rot = t * bear + (1 -t) * startRotation;
                                if (marker != null) {
                                    marker.remove();
                                    marker.setPosition(newPos);
                                    marker.setAnchor(0.5f, 0.5f);
                                    marker.setRotation(bear);
                                }
                              //  marker.setRotation(-rot > 180 ? rot/2 : rot);
                                if (t < 1.0) {
                                    // Post again 16ms later.
                                    handler.postDelayed(this, 16);
                                }
                            }
                        });
*/

/*
                        if (marker != null) {
                            marker.remove();
                            marker.setPosition(newPos);
                            marker.setAnchor(0.5f, 0.5f);
                            marker.setRotation(bear);
                        }
*/


                }
            });
            valueAnimator.start();
        } else {
            //Toast.makeText(MapEngineerTrack.this, "map null", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }



        singleEventCall();

        final FirebaseDatabase databaseOn = FirebaseDatabase.getInstance();
        DatabaseReference refOn = databaseOn.getReference().child("locations").child(jobId);
        refOn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, String> value1 = (Map<String, String>) dataSnapshot.getValue();
                    Log.i("dataSnapshot json", "dataSnapshot" + new JSONObject(value1));
                    JSONObject jsonObject = new JSONObject(value1);
                    Log.d("jsondata", jsonObject + "");

                    LATITUDE= Double.valueOf(jsonObject.optString("lat"));
                    LONGITUDE = Double.valueOf(jsonObject.optString("lng"));
                 /*   latitudeV.add(custLat);
                    latitudeV.add(latitude);
                    longitudeV.add(custLng);
                    longitudeV.add(longitude);

                    for (int i = 0; i < latitudeV.size(); i++) {
                        LatLng TutorialsPoint = new LatLng(latitudeV.get(i), longitudeV.get(i));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                        double latt = latitudeV.get(i);
                        double lngg = longitudeV.get(i);
                        LatLng position = new LatLng(latt, lngg);
                        points.add(position);

                    }

                    LatLng TutorialsPointsdv = new LatLng(custLat, custLng);
                    mMap.addMarker(new
                            MarkerOptions().position(TutorialsPointsdv));
                    int height = 50;
                    int width = 50;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bikeimg);
                    Bitmap b = bitmapdraw.getBitmap();
                    final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    final LatLng bikke = new LatLng(latitude, longitude);
                    bikeMarker = mMap.addMarker(new MarkerOptions().position(bikke)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );

                    if (points.size() >= 2) {
                        LatLng origin = (LatLng) points.get(0);
                        LatLng dest = (LatLng) points.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                    }
*/

                    LatLng TutorialsPointsdv = new LatLng(custLat, custLng);
                    mMap.addMarker(new
                            MarkerOptions().position(TutorialsPointsdv));
                    int height = 50;
                    int width = 50;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bikeimg);
                    Bitmap b = bitmapdraw.getBitmap();
                    final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    final LatLng bikke = new LatLng(LATITUDE, LONGITUDE);
                    bikeMarker = mMap.addMarker(new MarkerOptions().position(bikke)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );

                    mTimer = new Timer();
                    mTimer.schedule(new TimerTaskToGetLocation(), 1, notify_interval);

                   // engineerLocationValue.setText(getCompleteAddressString(latitude, longitude, MapEngineerTrack.this));
                   // LatLng update_src = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
/*
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition
                                    (new CameraPosition.Builder()
                                            .target(update_src)
                                            // .zoom(12.5f)
                                            .build()));
*/

                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     }


    private void singleEventCall() {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("locations").child(jobId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> value1 = (Map<String, String>) dataSnapshot.getValue();
               // Log.i("dataSnapshot json", "dataSnapshot" + new JSONObject(value1));
                JSONObject jsonObject = new JSONObject(value1);
                for (DataSnapshot snapm : dataSnapshot.getChildren()) {

//                    Double latitudee = snapm.child("lat").getValue(Double.class);
//                    Double longitudee = snapm.child("lng").getValue(Double.class);
//                    String name = snapm.child("rem_duration").getValue(String.class);
                    LATITUDE = Double.parseDouble(jsonObject.optString("lat"));
                    LONGITUDE = Double.parseDouble(jsonObject.optString("lng"));


                    if (LATITUDE != 0.0 && LONGITUDE != 0.0) {
                        if (custLat == LATITUDE && custLng == LONGITUDE) {
                            stopService(intentService);

                        } else {
                            Location location = new Location("");
                            location.setLatitude(Double.valueOf(LATITUDE));
                            location.setLongitude(Double.valueOf(LONGITUDE));
                            location.setBearing(getBearing(new LatLng(LATITUDE, LONGITUDE),
                                    new LatLng(custLat, custLng)));
                            // Log.e("fdvfdv", jobId + "");

                            animateMarker(location, bikeMarker);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void drawPoly() {

        latitudeV.add(custLat);
        latitudeV.add(LATITUDE);
        longitudeV.add(custLng);
        longitudeV.add(LONGITUDE);

        for (int i = 0; i < latitudeV.size(); i++) {
            LatLng TutorialsPoint = new LatLng(latitudeV.get(i), longitudeV.get(i));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(LATITUDE, LONGITUDE)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE, LONGITUDE), 15.0f));
            double latt = latitudeV.get(i);
            double lngg = longitudeV.get(i);
            LatLng position = new LatLng(latt, lngg);
            points.add(position);

        }

        if (points.size() >= 2) {
            LatLng origin = (LatLng) points.get(0);
            LatLng dest = (LatLng) points.get(1);
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
        engineerLocationValue.setText(getCompleteAddressString(LATITUDE, LONGITUDE, MapEngineerTrack.this));

    }


    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);
        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

   /* private void getLatLng(double latitude,double longitude) {

        shared = getSharedPreferences("LO", MODE_PRIVATE);
        String latt, lngg;
        latt = shared.getString("lat", "");
        lngg = shared.getString("lng", "");
        try {
            latitude = Double.parseDouble(latt);
            longitude = Double.parseDouble(lngg);
        } catch (Exception e) {
            mapFragment.getMapAsync(this);
            return;
        }
        latitudeV.add(custLat);
        latitudeV.add(latitude);
        longitudeV.add(custLng);
        longitudeV.add(longitude);

        engineerLocationValue.setText(getCompleteAddressString(latitude, longitude, MapEngineerTrack.this));
        //mapFragment.getMapAsync(this);

    }
*/

    @Override
    public void onClick(View v) {
        if (v == callImage) {

            try {
                final Dialog dialog = new Dialog(MapEngineerTrack.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.design_customer_design);
                dialog.setCanceledOnTouchOutside(false);

                final Spinner popupSpinnerValue;
                Button buttonDone, buttonCancel;
                popupSpinnerValue = (Spinner) dialog.findViewById(R.id.popupSpinnerValue);
                buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
                buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                dialog.show();
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (isPermissionGranted()) {
                            call_action();
                            dialog.dismiss();
//                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (v == viewOngoingJobs) {
            Intent intent = new Intent(MapEngineerTrack.this, EngineerViewJobs.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("sendIssues", (ArrayList<Problems>) problemsList);
            bundle.putSerializable("sendCategory", categoryName);
            bundle.putSerializable("subCategory", productName);
            bundle.putSerializable("brand", brandName);
            bundle.putSerializable("address", (AddressesModel) addressesModel);
            bundle.putSerializable("jobImage", (ArrayList<String>) listImage);
            bundle.putString("jobId", jobId);
            bundle.putString("budget", budget);
            bundle.putString("dateTimeStart", dateTimeStart);
            bundle.putString("dateTimeEnd", dateTimeEnd);
            bundle.putString("dateVendorAssign", dateVendor);
            bundle.putString("jobDescription", jobDescription);
            bundle.putString("jonOrderId", jobOrderId);
            bundle.putString("scheduleStatus", scheduleStatus);
            bundle.putString("enginId", engineerId);
            bundle.putString("showButton", "no");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (v == startJobBtn) {
            if (otpValue.getText().toString().isEmpty()) {
                showToast(MapEngineerTrack.this, "Please Enter OTP");
            } else {
                startJobApi(jobId, otpValue.getText().toString().trim());
            }
        }
        if (v == backIcon) {
            stopService(intentService);
            finish();
           /* Intent intent = new Intent(MapEngineerTrack.this, EngineerHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
        }
        if (v == downIcon) {
            upIcon.setVisibility(View.VISIBLE);
            engineerLocation.setVisibility(View.VISIBLE);
            customerLocation.setVisibility(View.VISIBLE);
            downIcon.setVisibility(View.GONE);
        }
        if (v == upIcon) {
            downIcon.setVisibility(View.VISIBLE);
            engineerLocation.setVisibility(View.GONE);
            customerLocation.setVisibility(View.GONE);
            upIcon.setVisibility(View.GONE);
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("sfdbbbbbbbbdjk", "" + "dfbg" + " ");
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    private void startJobApi(final String jobId, String otp) {
        showProgress(MapEngineerTrack.this);
        HashMap hashMap = new HashMap();
        //hashMap.put("requestjobId", jobId);
        hashMap.put("otp", otp);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
       String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> sucessEngineerModelCall = apiInterface.startJobEngineer(ACCESS_TOKEN, jobId, hashMap);
        sucessEngineerModelCall.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    stopService(intentService);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) problemsList);
                    bundle.putSerializable("sendCategory", categoryName);
                    bundle.putSerializable("subCategory", productName);
                    bundle.putSerializable("brand", brandName);
                    bundle.putSerializable("address", addressesModel);
                    bundle.putSerializable("jobImage", (ArrayList<String>) listImage);
                    bundle.putString("jobId", jobId);
                    bundle.putString("budget", budget);
                    bundle.putString("dateTimeStart", dateTimeStart);
                    bundle.putString("dateTimeEnd", dateTimeEnd);
                    bundle.putString("dateVendorAssign", dateVendor);
                    bundle.putString("jobDescription", jobDescription);
                    bundle.putString("jonOrderId", jobOrderId);
                    bundle.putString("enginId", engineerId);
                    bundle.putString("customerName", customerName);
                    bundle.putString("scheduleStatus", scheduleStatus);
                    Intent intent = new Intent(MapEngineerTrack.this, EngineerViewSite.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(MapEngineerTrack.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SmallSucess> call, Throwable t) {
                close();
                Log.e("fdvdfvbdf", "" + t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(intentService);
        finish();
      /*  Intent intent = new Intent(MapEngineerTrack.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }


    private class DownloadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //LocationService  gpsTracker = new LocationService(MapEngineerTrack.this,custLat,custLng);
                        //Log.e("custLngssssss",""+jobId);
                        // gpsTracker.fn_getlocation();
                        // GPSTracker gpsTracker=new GPSTracker(MapEngineerTrack.this);
                        //  gpsTracker.createDatabaseValue(latitude,longitude);
                        // gpsTracker.fn_update(latitude,longitude);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble(String.valueOf(point.get("lat")));
                    double lng = Double.parseDouble(String.valueOf(point.get("lng")));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(getResources().getColor(R.color.colorPrimary));
                lineOptions.geodesic(true);
                mMap.addPolyline(lineOptions);
                timeDuration.setText("" + DURATION);

            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=AIzaSyA8ULcLzfoc3hPDqkqeMuhSWzoqNdVYO54";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode+ "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void call_action() {
      /*  Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + customerMobile));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);*/

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+"+customerMobile, null));
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
/*
        if (requestCode == 3) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                call_action();
            } else {
                //Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
*/
/*
        if (requestCode == 1) {
            shared = getSharedPreferences("LO", MODE_PRIVATE);
            String latt, lngg;
            latt = shared.getString("lat", "");
            lngg = shared.getString("lng", "");
            try {
                latitude = Double.parseDouble(latt);
                longitude = Double.parseDouble(lngg);
            } catch (Exception e) {
                return;
            }
            latitudeV.add(custLat);
            latitudeV.add(latitude);
            longitudeV.add(custLng);
            longitudeV.add(longitude);

            engineerLocationValue.setText(getCompleteAddressString(latitude, longitude, MapEngineerTrack.this));
            mapFragment.getMapAsync(this);
        }
*/
        if (requestCode == 99) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                //proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MapEngineerTrack.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapEngineerTrack.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MapEngineerTrack.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(intentService);
    }
}
