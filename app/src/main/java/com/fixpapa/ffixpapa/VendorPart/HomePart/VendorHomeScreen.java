package com.fixpapa.ffixpapa.VendorPart.HomePart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.UserProfileFragment;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.NewJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.OngoingJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.SettingFragment;
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
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class VendorHomeScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    ImageView view1, view2, view3, view4;
    View v1, v2, v3, v4;
    String jobId;
    boolean getnoti;
    String setNotiStatus;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dataBaseHandler = new DataBaseHandler(VendorHomeScreen.this);
        getUserDetailId(VendorHomeScreen.this);

        if (getIntent().getExtras()!=null) {
            jobId = getIntent().getExtras().getString("jobId");
            getnoti = getIntent().getExtras().getBoolean("noti");
        }

        tabLayout = (TabLayout) findViewById(R.id.vendorTabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
        view3.setImageResource(R.drawable.setting);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        v4 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view4 = v4.findViewById(R.id.icon);
        view4.setImageResource(R.drawable.profile_icon);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        viewPager = (ViewPager) findViewById(R.id.vendorPager);
        viewPager.setOffscreenPageLimit(4);
        PagerAdpter adapter = new PagerAdpter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);


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
            view3.setImageResource(R.drawable.setting_click);
        }
        if (tab.getPosition() == 3) {
            view4.setImageResource(R.drawable.profile_icon_click);
        }
    }

    private void getJobDetail(final String jobId) {
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
//        String LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                int addtionalChar = 0;
                if (response.isSuccessful()) {

//                    Log.e("ghnhtnfgbfg", "" + LOGIN_TYPE + " " + response.body().getSuccess().getData().getStatus());
                        if (response.body().getSuccess().getData().getVendorAssigned() && !response.body().getSuccess().getData().getEngineerAssigned() || !response.body().getSuccess().getData().getVendorAssigned() && !response.body().getSuccess().getData().getEngineerAssigned()) {
                            Intent gotoIntent = new Intent(VendorHomeScreen.this, ViewJobsVendor.class);

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
                            bundle.putString("assignedStatus", "notassign");
                            gotoIntent.putExtras(bundle);
                            //gotoIntent.setClassName(VendorHomeScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
                            startActivity(gotoIntent);
                        } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {
                            Intent intent = new Intent(VendorHomeScreen.this, ViewJobsAccpted.class);
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
                            // gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
                            startActivity(intent);


                        } else if (response.body().getSuccess().getData().getStatus().equals("canceled")) {
                            Intent intent = new Intent(VendorHomeScreen.this, EngineerViewJobs.class);
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
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                            //bundle.putString("status", String.valueOf(category.getStatus()));

                            bundle.putString("showButton", "no");
                            intent.putExtras(bundle);
                            //gotoIntent.setClassName(VendorHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");
                            startActivity(intent);
                        } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {

                            Intent gotoIntent = new Intent(VendorHomeScreen.this, EngineerViewJobs.class);

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
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());

                            //bundle.putString("status", String.valueOf(category.getStatus()));

                            bundle.putString("showButton", "no");
                            gotoIntent.putExtras(bundle);
                            // gotoIntent.setClassName(VendorHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");
                            // startActivity(intent);
                        } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {
                            Intent gotoIntent = new Intent(VendorHomeScreen.this, EngineerCompleteJobs.class);
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
                            gotoIntent.putExtras(bundle);
                            //  gotoIntent.setClassName(VendorHomeScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");
                            startActivity(gotoIntent);


                        }  else {
                            Intent gotoIntent = new Intent(VendorHomeScreen.this, ViewJobsAccpted.class);
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
                            gotoIntent.putExtras(bundle);
                            //gotoIntent.setClassName(VendorHomeScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
                            startActivity(gotoIntent);
                        }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(VendorHomeScreen.this, "" + jsonObjError.getString("message"));
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
            view3.setImageResource(R.drawable.setting);
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
                    NewJobsFragment tabNewJobs = new NewJobsFragment();
                    return tabNewJobs;
                case 1:
                    OngoingJobsFragment tab2 = new OngoingJobsFragment();
                    return tab2;
                case 2:
                    SettingFragment tab3 = new SettingFragment();
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
