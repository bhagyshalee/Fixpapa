package com.fixpapa.ffixpapa.UserPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    String setSatus = "";
    String jobId = null, title, message;
    int addtionalChar = 0;
    Intent gotoIntent;
    private int count = 0;
    String LOGIN_TYPE,USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh_screen);
//        getUserDetailId(SplashScreen.this);
        // if (getIntent().getStringExtra("title")!=null) {
        //Log.e("vfhlfjdvbjf", "" + getIntent().getStringExtra("title"));
        //   }

       /* if (jobId!=null)
        {
            getJobDetail(jobId);
        }*/

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");
        USER_ID = sharedPref.getString("USER_ID","");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                 jobId=  getIntent().getStringExtra("jobId");
//                 message=  getIntent().getStringExtra("body");
//                 title=  getIntent().getStringExtra("title");
                if (getIntent().getExtras() != null) {
                    jobId = getIntent().getStringExtra("jobId");
                    message = getIntent().getStringExtra("body");
                    title = getIntent().getStringExtra("title");
                }

               // if (jobId == null) {
                    if (LOGIN_TYPE.equals("")) {
                        Intent i = new Intent(SplashScreen.this, UserHomeScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (LOGIN_TYPE.equals("vendor")) {
                        Intent i = new Intent(SplashScreen.this, VendorHomeScreen.class);
                        if (jobId!=null) {
                            i.putExtra("jobId", "" + jobId);
                            i.putExtra("noti", true);
                        }
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else if (LOGIN_TYPE.equals("customer")) {
                        Intent i = new Intent(SplashScreen.this, UserHomeScreen.class);
                        if (jobId!=null) {
                            i.putExtra("jobId", "" + jobId);
                            i.putExtra("noti", true);
                        }
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else if (LOGIN_TYPE.equals("engineer")) {

                        Intent intent = new Intent(SplashScreen.this, EngineerHomeScreen.class);
                        if (jobId!=null) {
                            intent.putExtra("jobId", "" + jobId);
                            intent.putExtra("noti", true);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
               /* } else {
                    getJobDetail(message, title, jobId);
                }*/


            }
        }, SPLASH_TIME_OUT);
    }

 /*   @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        jobId=  getIntent().getStringExtra("jobId");
        message=  getIntent().getStringExtra("body");
        title=  getIntent().getStringExtra("title");

    }*/

    private void getJobDetail(final String message, final String title, final String jobId) {
        gotoIntent = new Intent();
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
//        int highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                int addtionalChar = 0;
                if (response.isSuccessful()) {

                    Log.e("ghnhtnfgbfg", "" + LOGIN_TYPE + " " + response.body().getSuccess().getData().getStatus());
                    if (LOGIN_TYPE.equals("vendor")) {
                        if (response.body().getSuccess().getData().getVendorAssigned() && !response.body().getSuccess().getData().getEngineerAssigned() || !response.body().getSuccess().getData().getVendorAssigned() && !response.body().getSuccess().getData().getEngineerAssigned()) {
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
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                        } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {
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
                            gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");
                            //startActivity(intent);


                        } else if (response.body().getSuccess().getData().getStatus().equals("canceled")) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                            bundle.putSerializable("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                            bundle.putSerializable("subCategory", response.body().getSuccess().getData().getProduct().getName());
                            if (response.body().getSuccess().getData().getBrand() != null) {
                                bundle.putSerializable("brand", response.body().getSuccess().getData().getBrand().getName());
                            }
                            bundle.putSerializable("address", (AddressesModel)  response.body().getSuccess().getData().getAddress());
                            bundle.putSerializable("jobImage", (ArrayList<String>)  response.body().getSuccess().getData().getImage());
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
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                            // startActivity(intent);
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
                            bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                            bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                            bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                            bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());

                            //bundle.putString("status", String.valueOf(category.getStatus()));

                            bundle.putString("showButton", "no");
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                            //startActivity(intent);
                        } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {
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
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

                        } else {
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
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                        }


                    } else if (LOGIN_TYPE.equals("customer")) {

                        if (response.body().getSuccess().getData().getStatus().equals("canceled")) {

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
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                        } else if (response.body().getSuccess().getData().getStatus().equals("indispute")) {
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
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                        } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                            if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                                boolean getRateStatus = false;
                                for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                    if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("engineer")) {
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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                            }
                        } else if (response.body().getSuccess().getData().getStatus().equals("on the way")) {
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
                            gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                        } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {

                            if (response.body().getSuccess().getData().getSiteType().equals("Offsite")) {
                                if (response.body().getSuccess().getData().getBill().getClientResponse().equals("requested")) {
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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.UserPart.PartRequest");
                                    Log.e("fvndfvdfbfd", "" + response.body().getSuccess().getData().getSiteType());

                                } else if (response.body().getSuccess().getData().getBill().getClientResponse().equals("decline")) {

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
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                                }
                            }
                        } else if (response.body().getSuccess().getData().getStatus().equals("billGenerated") ||
                                response.body().getSuccess().getData().getStatus().equals("paymentDone")) {

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
                            bundle.putString("status", response.body().getSuccess().getData().getStatus());
                            // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.PaymentCustomer");


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
                            bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                            bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                            bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                            bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                            bundle.putSerializable("cancelJobDetail", (CancelModel) response.body().getSuccess().getData().getCancelJob());
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putString("showButton", "no");
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                            bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                            bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                            bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                            bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                            bundle.putSerializable("completedDate", (CompleteJobModel) response.body().getSuccess().getData().getCompleteJob());
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putString("showButton", "no");
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

                        } else if (response.body().getSuccess().getData().getStatus().equals("completed")) {

                            if (response.body().getSuccess().getData().getRatedetail().size() != 0) {
                                boolean getRateStatus = false;
                                for (int i = 0; i < response.body().getSuccess().getData().getRatedetail().size(); i++) {
                                    if (response.body().getSuccess().getData().getRatedetail().get(i).getForUser().equals("engineer")) {
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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                            }
                        } else {
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
                            gotoIntent.setClassName(getApplicationContext(), "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                        }


                    } else if (LOGIN_TYPE.equals("engineer")) {


                        if (response.body().getSuccess().getData().getSiteType().equals("Onsite")) {

                            if (response.body().getSuccess().getData().getStatus().equals("billGenerated") || response.body().getSuccess().getData().getStatus().equals("paymentDone")) {

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
                                bundle.putString("status", response.body().getSuccess().getData().getStatus());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");

                            } else if (response.body().getSuccess().getData().getStatus().equals("inprocess")) {

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewSite");

                            } else if (response.body().getSuccess().getData().getStatus().equals("on the way")) {

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                                }
                            } else {
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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");


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
                                bundle.putString("status", response.body().getSuccess().getData().getStatus());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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

                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.OffSiteView");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.putExtras(bundle);
                                        gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                                }
                            } else {
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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(SplashScreen.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");


                            }
                        }
                    }
                    sendNotification(message, title, jobId, gotoIntent);

                } else {
                    sendNotification(message, title, jobId, gotoIntent);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(SplashScreen.this, "" + jsonObjError.getString("message"));
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

    private void sendNotification(String message, String title, String jobId, Intent intent) {
        startActivity(intent);
        finish();

     /*   NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;
            contentIntent = PendingIntent.getActivity(this,
                    (int) (Math.random() * 100), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            notification = mBuilder.setSmallIcon(R.drawable.luncher_icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(contentIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(message)
                    .build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            count++;
            notificationManager.notify(count, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }


}
