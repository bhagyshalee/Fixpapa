package com.fixpapa.ffixpapa.Services.NotificationClasses;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class MyFirebaseMessegingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String Id;
    String setSatus = "";
    Intent gotoIntent;
    private int count = 0;
    String ACCESS_TOKEN,LOGIN_TYPE,USER_ID;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //String message = remoteMessage.getData().get("message");


       // Log.d(TAG, "NotificationFragment Message Body: " + remoteMessage.getData().toString());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "mghxdxfgbhdhfgh" + remoteMessage.getData().toString());
            Log.d(TAG, "background" + remoteMessage.getNotification().getBody().toString() + " " + remoteMessage.getNotification().getTitle().toString());

            getJobDetail(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData().get("jobId"));
            //jobId = remoteMessage.getData().get("jobId");
            // Log.d(TAG, " " + jobId);
           /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                createChannel();
            }else {
                sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData().get("jobId"));
                jobId = remoteMessage.getData().get("jobId");
                Log.d(TAG, " " + jobId);
            }*/

        }
        int badge_count = 0;

    }

    private void getJobDetail(final String message, final String title, final String jobId) {
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");
        USER_ID = sharedPref.getString("USER_ID","");

        gotoIntent = new Intent();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                int addtionalChar = 0;
                if (response.isSuccessful()) {


                  //  Log.e("ghnhtnfgbfg", "" + LOGIN_TYPE + " " + response.body().getSuccess().getData().getStatus());
                    if (LOGIN_TYPE.equals("vendor")) {
                        if (response.body().getSuccess().getData().getStatus().equals("pending")) {
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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsVendor");

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
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

                        }  else {
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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

                                }
                                else
                                {
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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted");

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
                            if (response.body().getSuccess().getData().getBill().getDiscount() != null) {
                                bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                            }
                            bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                            bundle.putString("jobId", response.body().getSuccess().getData().getId());
                            bundle.putString("cusId", USER_ID);
                            bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                            bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                            if (response.body().getSuccess().getData().getSchedule() != null) {
                                bundle.putString("scheduledOn", response.body().getSuccess().getData().getSchedule().getEStartDate());
                            }
                            if ( response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                                bundle.putString("modePayment", response.body().getSuccess().getData().getTransaction().getModeOfPayment());
                            }

                            bundle.putString("status", response.body().getSuccess().getData().getStatus());

                            // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                            gotoIntent.putExtras(bundle);
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.PaymentCustomer");


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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                            gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                if ( response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                                    bundle.putString("modePayment", response.body().getSuccess().getData().getTransaction().getModeOfPayment());
                                }
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewSite");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

                                }
                            }  else {
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
                                    gotoIntent.putExtras(bundle);
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs");

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
                                if ( response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                                    bundle.putString("modePayment", response.body().getSuccess().getData().getTransaction().getModeOfPayment());
                                }
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.OffSiteView");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs");

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
                                        gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                    gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating");

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
                                gotoIntent.putExtras(bundle);
                                gotoIntent.setClassName(MyFirebaseMessegingService.this, "com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs");


                            }
                        }
                    }
                    sendNotification(message, title, jobId, gotoIntent);

                } else {
                    sendNotification(message, title, jobId, gotoIntent);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(MyFirebaseMessegingService.this, "" + jsonObjError.getString("message"));
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

        Intent intentNew = null;
        NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;
            // gotoIntent = new Intent();
            //gotoIntent.setClassName(this, "com.fixpapa.newfixpapa.UserPart.UserNotification");//Start activity when user taps on notification.

         /*   if (LOGIN_TYPE.equals("vendor")) {
                intentNew = new Intent(MyFirebaseMessegingService.this, VendorHomeScreen.class);
                if (jobId!=null) {
                    intentNew.putExtra("jobId", "" + jobId);
                    intentNew.putExtra("noti", true);
                }
                //startActivity(intentNew);
            } else if (LOGIN_TYPE.equals("customer")) {
                intentNew= new Intent(MyFirebaseMessegingService.this, UserHomeScreen.class);
                if (jobId!=null) {
                    intentNew.putExtra("jobId", "" + jobId);
                    intentNew.putExtra("noti", true);
                }
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               // startActivity(i);
                
            } else if (LOGIN_TYPE.equals("engineer")) {

                 intentNew = new Intent(MyFirebaseMessegingService.this, EngineerHomeScreen.class);
                if (jobId!=null) {
                    intent.putExtra("jobId", "" + jobId);
                    intent.putExtra("noti", true);
                }
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(intent);
                
            }*/
            // gotoIntent=getJobDetail(jobId);
            // Log.d("intent_data",getJobDetail(jobId)+"");
            //getJobDetail(message,title,jobId);
            contentIntent = PendingIntent.getActivity(this,
                    (int) (Math.random() * 100), gotoIntent,
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
            if (notificationManager != null) {
                notificationManager.notify(count, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        if (arr.length == 2) {
//            try {


            new generatePictureStyleNotification(this, getString(R.string.app_name), arr[0], arr[1]).execute();
//                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(arr[1]).getContent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } else {
//            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
//            notiStyle.setSummaryText(arr[0]);

            NotificationManager notificationManager = null;
            Notification notification = null;
            try {
                notificationManager = (NotificationManager) this .getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = null;

                Intent gotoIntent = new Intent();
                gotoIntent.setClassName(this, "com.advocosoft.dothatapp.activities.MainActivity");//Start activity when user taps on notification.

                contentIntent = PendingIntent.getActivity(this,
                        (int) (Math.random() * 100), gotoIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        this);
                notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(arr[0]).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(arr[0])
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(arr[0]))
                        .setContentIntent(contentIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(arr[1])
                        .build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //This will generate seperate notification each time server sends.
        }*/
    }

  /*  @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }*/


}
