package com.fixpapa.ffixpapa.EngineerPart.HomePart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewSite;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.MyJobsFragment;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.TaskListFragment;
import com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack;
import com.fixpapa.ffixpapa.EngineerPart.OffSiteView;
import com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.LocationService;
import com.fixpapa.ffixpapa.Services.NotificationClasses.MyFirebaseMessegingService;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.UserProfileFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.PaymentCustomer;
import com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.ShowAlertDialog;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class EngineerHomeScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView view1, view2, view3;
    View v1, v2, v3, v4;
    Intent  intentService;
    boolean getnoti;
    String jobId;
    int currentAPIVersion = Build.VERSION.SDK_INT;
    DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dataBaseHandler=new DataBaseHandler(EngineerHomeScreen.this);
        getUserDetailId(EngineerHomeScreen.this);

        if (getIntent().getExtras()!=null) {
            jobId = getIntent().getExtras().getString("jobId");
            getnoti = getIntent().getExtras().getBoolean("noti");
        }

       // giveRunTimePermission(EngineerHomeScreen.this,EngineerHomeScreen.this);


        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(EngineerHomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EngineerHomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }


        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ShowAlertDialog(EngineerHomeScreen.this);
        }

        if (jobId != null) {
            if (getnoti) {
                getJobDetail(jobId);
            }
        }


        tabLayout = (TabLayout) findViewById(R.id.vendorTabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        v1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1=v1.findViewById(R.id.icon);
        view1.setImageResource(R.drawable.home_icon_click);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        v2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2=v2.findViewById(R.id.icon);
        view2.setImageResource(R.drawable.ongoing);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        v3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3=v3.findViewById(R.id.icon);
        view3.setImageResource(R.drawable.user);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        viewPager = (ViewPager) findViewById(R.id.vendorPager);
        viewPager.setOffscreenPageLimit(4);
        PagerAdpter adapter = new PagerAdpter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        //stopService(intentService);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    if (currentAPIVersion >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(EngineerHomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EngineerHomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            view1.setImageResource(R.drawable.home_icon_click);
        }
        if (tab.getPosition() == 1) {
            view2.setImageResource(R.drawable.ongoing_click);
        }
        if (tab.getPosition() == 2) {
            view3.setImageResource(R.drawable.user_click);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.e("unselectTab", "" + tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            view1.setImageResource(R.drawable.home_icon);
        }
        if (tab.getPosition() == 1) {
            view2.setImageResource(R.drawable.ongoing);
        }
        if (tab.getPosition() == 2) {
            view3.setImageResource(R.drawable.user);
        }

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("stateScrolled", "sc" + position);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e("stateScroll", "" + state);
    }

    public class PagerAdpter extends FragmentStatePagerAdapter

    {

        int tabCount;

        public PagerAdpter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MyJobsFragment tabNewJobs= new MyJobsFragment();
                    return tabNewJobs;
                case 1:
                    TaskListFragment tab2 = new TaskListFragment();
                    return tab2;
                case 2:
                    UserProfileFragment tab3 = new UserProfileFragment();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;

        }
    }

    private void getJobDetail(final String jobId) {
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                int addtionalChar = 0;
                if (response.isSuccessful()) {

                    {


                        if (response.body().getSuccess().getData().getSiteType().equals("Onsite")) {

                            if (response.body().getSuccess().getData().getStatus().equals("billGenerated") || response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                                Intent intent = new Intent(EngineerHomeScreen.this, PaymentEngineerOffsite.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal", response.body().getSuccess().getData().getBill().getTotalAmount());
                                for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                                    addtionalChar += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putInt("additionalCharge", response.body().getSuccess().getData().getBill().getAddServiceCost());
                                bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                                bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                if (response.body().getSuccess().getData().getSchedule() != null) {
                                    bundle.putString("scheduledOn", response.body().getSuccess().getData().getSchedule().getEStartDate());
                                }
                                bundle.putString("customerName", response.body().getSuccess().getData().getFullName());
                                bundle.putString("customerId", response.body().getSuccess().getData().getCustomerId());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                //gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");

                            } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewSite.class);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("customerName", response.body().getSuccess().getData().getFullName());
                                bundle.putString("customerId", response.body().getSuccess().getData().getCustomerId());
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("timeDuration", String.valueOf(response.body().getSuccess().getData().getDuration()));
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                if (response.body().getSuccess().getData().getStatus().equals("scheduled")) {
                                    bundle.putSerializable("Schedule", (ScheduleModel) response.body().getSuccess().getData().getSchedule());
                                }
                              /*  gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewSite");*/
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("on the way")) {
                                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    ShowAlertDialog(EngineerHomeScreen.this);
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("latitude", String.valueOf(response.body().getSuccess().getData().getAddress().getLocation().getLat()));
                                    bundle.putString("longitude", String.valueOf(response.body().getSuccess().getData().getAddress().getLocation().getLng()));
                                    bundle.putString("street", String.valueOf(response.body().getSuccess().getData().getAddress().getStreet()));
                                    bundle.putString("value", String.valueOf(response.body().getSuccess().getData().getAddress().getValue()));
                                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                    bundle.putString("otp", String.valueOf(response.body().getSuccess().getData().getOtp()));

                                    bundle.putSerializable("pick", (PickModel) response.body().getSuccess().getData().getPick());
                                    bundle.putSerializable("addpart", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                                    bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                    bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                    bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                    bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                    bundle.putString("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                    bundle.putString("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                    if (response.body().getSuccess().getData().getBrand() != null) {
                                        bundle.putString("brand", response.body().getSuccess().getData().getBrand().getName());
                                    }
                                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                    bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                                    bundle.putString("customerId", response.body().getSuccess().getData().getCustomerId());
                                    bundle.putString("customerMobile", response.body().getSuccess().getData().getCustomer().getMobile());
                                    bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                    bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                    bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                    bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                    bundle.putString("timeDuration", String.valueOf(response.body().getSuccess().getData().getDuration()));
                                    bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                    if (response.body().getSuccess().getData().getStatus().equals("scheduled")) {
                                        bundle.putSerializable("Schedule", (ScheduleModel) response.body().getSuccess().getData().getSchedule());
                                    }
                                    // gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack");
                                    Intent intent = new Intent(EngineerHomeScreen.this, MapEngineerTrack.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }


                            } else if (response.body().getSuccess().getData().getStatus().equals("canceled")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("showButton", "no");
                             /*   gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("showButton", "no");
                              /*  gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                                if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                                    boolean getRateStatus = false;
                                    for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                        if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("customer")) {
                                            getRateStatus = true;
                                        }
                                    }

                                    if (getRateStatus) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                        bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                        bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                        if (response.body().getSuccess().getData().getBrand() != null) {
                                            bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                        }
                                        bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                        bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                        bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                        bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                        bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                        bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                        bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                       /* gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                        Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("customerName", response.body().getSuccess().getData().getEngineer().getFullName());
                                        bundle.putInt("totalAmount", response.body().getSuccess().getData().getTotalPrice());
                                        bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                        bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                                        bundle.putString("scheduledEnd", response.body().getSuccess().getData().getStartDate());
                                        bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                                        bundle.putString("cusId", response.body().getSuccess().getData().getCustomer().getId());
                                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                                        bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                       /* gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");*/
                                        Intent intent = new Intent(EngineerHomeScreen.this, ReviewRating.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("customerName", response.body().getSuccess().getData().getEngineer().getFullName());
                                    bundle.putInt("totalAmount", response.body().getSuccess().getData().getTotalPrice());
                                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getStartDate());
                                    bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomer().getId());
                                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                 /*   gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");*/
                                    Intent intent = new Intent(EngineerHomeScreen.this, ReviewRating.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand()!=null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                                bundle.putString("customerMobile", response.body().getSuccess().getData().getCustomer().getMobile());
                                if (response.body().getSuccess().getData().getStatus().equals("scheduled")) {
                                    bundle.putSerializable("Schedule", (ScheduleModel) response.body().getSuccess().getData().getSchedule());
                                }
                             /*   gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "e com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs");
*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewPendingJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);


                            }
                        } else if (response.body().getSuccess().getData().getSiteType().equals("Offsite")) {
                            if (response.body().getSuccess().getData().getStatus().equals("billGenerated") || response.body().getSuccess().getData().getStatus().equals("paymentDone")) {

                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal", response.body().getSuccess().getData().getBill().getTotalAmount());
                                bundle.putInt("AdditionalPart", response.body().getSuccess().getData().getBill().getAddServiceCost());
                                bundle.putInt("additionalCharge", response.body().getSuccess().getData().getBill().getAddServiceCost());
                                if (response.body().getSuccess().getData().getBill().getDiscount() != null) {
                                    bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                                }
                                bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                                bundle.putString("scheduledEnd", response.body().getSuccess().getData().getEndDate());
                                bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                                bundle.putString("customerId", response.body().getSuccess().getData().getCustomerId());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                              /*  gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, PaymentEngineerOffsite.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("outForDelivery")) {

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("showButton", "yes");
                              /*  gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pick", (PickModel) response.body().getSuccess().getData().getPick());
                                bundle.putSerializable("billModel", (BillModel) response.body().getSuccess().getData().getBill());
                                bundle.putSerializable("addpart", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                                bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putString("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putString("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("timeDuration", String.valueOf(response.body().getSuccess().getData().getDuration()));
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putInt("serviceTotal", response.body().getSuccess().getData().getTotalPrice());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                                    bundle.putInt("serviceAdpartCost", response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost());
                                }
                                bundle.putInt("addServiceCost", response.body().getSuccess().getData().getBill().getAddServiceCost());

                              /*  gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.OffSiteView");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, OffSiteView.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (response.body().getSuccess().getData().getStatus().equals("canceled")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("showButton", "no");
                            /*    gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);


                            } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("showButton", "no");
                            /*    gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);


                            } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                                if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                                    boolean getRateStatus = false;
                                    for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                        if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("customer")) {
                                            getRateStatus = true;
                                        }
                                    }

                                    if (getRateStatus) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                        bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                        bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                        if (response.body().getSuccess().getData().getBrand() != null) {
                                            bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                        }
                                        bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                        bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                        bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                        bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                        bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                        bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                        bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                       /* gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");*/
                                        Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewJobs.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("customerName", response.body().getSuccess().getData().getEngineer().getFullName());
                                        bundle.putInt("totalAmount", response.body().getSuccess().getData().getTotalPrice());
                                        bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                        bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                                        bundle.putString("scheduledEnd", response.body().getSuccess().getData().getStartDate());
                                        bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                                        bundle.putString("cusId", response.body().getSuccess().getData().getCustomer().getId());
                                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                                        bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                       /* gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");*/
                                        Intent intent = new Intent(EngineerHomeScreen.this, ReviewRating.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("customerName", response.body().getSuccess().getData().getEngineer().getFullName());
                                    bundle.putInt("totalAmount", response.body().getSuccess().getData().getTotalPrice());
                                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getStartDate());
                                    bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomer().getId());
                                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                  /*  gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(EngineerHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");
*/
                                    Intent intent = new Intent(EngineerHomeScreen.this, ReviewRating.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand()!=null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                                bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                                bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                                bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                                bundle.putString("customerMobile", response.body().getSuccess().getData().getCustomer().getMobile());
                                if (response.body().getSuccess().getData().getStatus().equals("scheduled")) {
                                    bundle.putSerializable("Schedule", (ScheduleModel) response.body().getSuccess().getData().getSchedule());
                                }
                             /*   gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "e com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs");
*/
                                Intent intent = new Intent(EngineerHomeScreen.this, EngineerViewPendingJobs.class);
                                intent.putExtras(bundle);
                                startActivity(intent);


                            }
                        }
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerHomeScreen.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SmallSucess> call, Throwable t) {
                // close();
            }
        });
    }

}
