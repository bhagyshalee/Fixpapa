package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.BookingFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.HomeFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.UserProfileFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.PartRequest;
import com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UserHomeScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    public static ViewPager viewPager;
    public static Activity fa;
    ImageView view1, view2, view3, view4;
    View v1, v2, v3, v4;
    boolean getnoti;
    String jobId;
    DataBaseHandler dataBaseHandler;
    private TabLayout tabLayout;
    String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fa = this;
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        dataBaseHandler = new DataBaseHandler(UserHomeScreen.this);
        getUserDetailId(UserHomeScreen.this);

        if (getIntent().getExtras() != null) {
            jobId = getIntent().getExtras().getString("jobId");
            getnoti = getIntent().getExtras().getBoolean("noti");
        }

        v1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1 = v1.findViewById(R.id.icon);
        view1.setImageResource(R.drawable.home_icon_click);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        v2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2 = v2.findViewById(R.id.icon);
        view2.setImageResource(R.drawable.category_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        v3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3 = v3.findViewById(R.id.icon);
        view3.setImageResource(R.drawable.view_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        v4 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view4 = v4.findViewById(R.id.icon);
        view4.setImageResource(R.drawable.profile_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        PagerAdpterr adapter = new PagerAdpterr(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(UserHomeScreen.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserHomeScreen.this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (ActivityCompat.checkSelfPermission(UserHomeScreen.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserHomeScreen.this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
            }
        }

        if (jobId != null) {
            if (getnoti) {
                getJobDetail(jobId);
            }
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            view1.setImageResource(R.drawable.home_icon_click);
        }
        if (tab.getPosition() == 1) {
            view2.setImageResource(R.drawable.category_icon_click);
        }
        if (tab.getPosition() == 2) {
            view3.setImageResource(R.drawable.view_icon_click);
        }
        if (tab.getPosition() == 3) {
            view4.setImageResource(R.drawable.profile_icon_click);
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
            view2.setImageResource(R.drawable.category_icon);
        }
        if (tab.getPosition() == 2) {
            view3.setImageResource(R.drawable.view_icon);
        }
        if (tab.getPosition() == 3) {
            view4.setImageResource(R.drawable.profile_icon);

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

    private void getJobDetail(final String jobId) {
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
         USER_ID = sharedPref.getString("USER_ID","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                int addtionalChar = 0;
                if (response.isSuccessful()) {

//                    Log.e("ghnhtnfgbfg", "" + LOGIN_TYPE + " " + response.body().getSuccess().getData().getStatus());
                    if (response.body().getSuccess().getData().getStatus().equals("canceled")) {

                        Intent intent = new Intent(UserHomeScreen.this, EngineerViewJobs.class);
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
                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                        bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                        bundle.putString("showButton", "no");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        // gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                    } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {
                        Intent intent = new Intent(UserHomeScreen.this, EngineerViewJobs.class);
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
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                        bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                        bundle.putString("showButton", "no");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        // gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                    } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                        if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                            boolean getRateStatus = false;
                            for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("engineer")) {
                                    getRateStatus = true;
                                }
                            }

                            if (getRateStatus) {
                                Intent intent = new Intent(UserHomeScreen.this, EngineerCompleteJobs.class);
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
                                intent.putExtras(bundle);
                                // gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(UserHomeScreen.this, ReviewRating.class);
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
                                intent.putExtras(bundle);
                                startActivity(intent);
                                // gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                            }
                        } else {
                            Intent intent = new Intent(UserHomeScreen.this, ReviewRating.class);
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
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                        }
                    } else if (response.body().getSuccess().getData().getStatus().equals("on the way")) {
                        Intent intent = new Intent(UserHomeScreen.this, ViewJobsAccpted.class);
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
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("status", String.valueOf(response.body().getSuccess().getData().getStatus()));
                        bundle.putString("assignedStatus", "assigned");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        // gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                    } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {

                        if (response.body().getSuccess().getData().getSiteType().equals("Offsite")) {
                            if (response.body().getSuccess().getData().getBill().getClientResponse().equals("requested")) {
                                Intent intent = new Intent(UserHomeScreen.this, PartRequest.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("jobId", response.body().getSuccess().getData().getId());
                                if (response.body().getSuccess().getData().getBill() != null) {
                                    bundle.putSerializable("addPart", (BillModel) response.body().getSuccess().getData().getBill());
                                }
                                bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                                bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                                if (response.body().getSuccess().getData().getBrand() != null) {
                                    bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                                }
                                intent.putExtras(bundle);
                                startActivity(intent);
                                // gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.UserPart.PartRequest");
                                Log.e("fvndfvdfbfd", "" + response.body().getSuccess().getData().getSiteType());

                            } else if (response.body().getSuccess().getData().getBill().getClientResponse().equals("decline")) {
                                Intent intent = new Intent(UserHomeScreen.this, ViewJobsAccpted.class);
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
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("status", String.valueOf(response.body().getSuccess().getData().getStatus()));
                                bundle.putString("assignedStatus", "assigned");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                //gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
                            } else {
                                Intent intent = new Intent(UserHomeScreen.this, ViewJobsAccpted.class);
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
                                bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                                bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                                bundle.putString("status", String.valueOf(response.body().getSuccess().getData().getStatus()));
                                bundle.putString("assignedStatus", "assigned");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    } else if (response.body().getSuccess().getData().getStatus().equals("billGenerated") ||
                            response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                        Intent intent = new Intent(UserHomeScreen.this, PaymentCustomer.class);
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
                        bundle.putString("cusId", USER_ID);
                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                        bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                        if (response.body().getSuccess().getData().getSchedule() != null) {
                            bundle.putString("scheduledOn", response.body().getSuccess().getData().getSchedule().getEStartDate());
                        }
                        bundle.putString("customerName", response.body().getSuccess().getData().getFullName());
                        bundle.putString("modePayment", response.body().getSuccess().getData().getTransaction().getModeOfPayment());
                        // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        // gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.PaymentCustomer");


                    } else if (response.body().getSuccess().getData().getStatus().equals("canceled")) {
                        Intent intent = new Intent(UserHomeScreen.this, EngineerViewJobs.class);
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
                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                        bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                        bundle.putString("showButton", "no");
                        intent.putExtras(bundle);
                        startActivity(intent);
                          /*  gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");
*/
                    } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {
                        Intent intent = new Intent(UserHomeScreen.this, EngineerViewJobs.class);

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
                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                        bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                        bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                        bundle.putString("showButton", "no");
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                        if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                            boolean getRateStatus = false;
                            for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("engineer")) {
                                    getRateStatus = true;
                                }
                            }

                            if (getRateStatus) {
                                Intent intent = new Intent(UserHomeScreen.this, EngineerCompleteJobs.class);
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
                                  /*  gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");
*/
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(UserHomeScreen.this, ReviewRating.class);
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
                                gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");
*/
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(UserHomeScreen.this, ReviewRating.class);
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
                            gotoIntent.setClassName(UserHomeScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");
*/
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(UserHomeScreen.this, ViewJobsAccpted.class);
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
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("status", String.valueOf(response.body().getSuccess().getData().getStatus()));
                        bundle.putString("assignedStatus", "assigned");
                       /* gotoIntent.putExtras(bundle);
                        gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
*/
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UserHomeScreen.this, "" + jsonObjError.getString("message"));
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

    public class PagerAdpterr extends FragmentStatePagerAdapter {

        int tabCount;

        public PagerAdpterr(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HomeFragment tab1 = new HomeFragment();
                    return tab1;
                case 1:
                    CategoryFragment tab2 = new CategoryFragment();
                    return tab2;
                case 2:
                    BookingFragment tab3 = new BookingFragment();
                    return tab3;
                case 3:
                    UserProfileFragment tab4 = new UserProfileFragment();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;

        }
    }

}
