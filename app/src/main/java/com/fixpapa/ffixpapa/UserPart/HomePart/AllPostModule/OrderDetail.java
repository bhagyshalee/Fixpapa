package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.fixpapa.ffixpapa.EngineerPart.EngineerInfo;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OrderDetail extends AppCompatActivity {

    TextView productName, orderId, scheduledOn, totalAmountValue;
    String getStatus;
    boolean vendorAss;
    VerticalStepView step_view;
    int setStatusSetting = 0;
    ImageView backImage;
    List<String> list0;
    Button engineerInfoButton, cancelJobButton;
    String selectedReason, jobId, engineerId,jobOrderId;
String LOGIN_TYPE,ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        list0 = new ArrayList<>();
        final Intent intent = this.getIntent();
        final Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            JOB_ID = bundle.getString("jobId");
        }

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
         ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
         LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");


        Log.e("vklsd", "" + JOB_ID);
        productName = (TextView) findViewById(R.id.productName);
        orderId = (TextView) findViewById(R.id.orderId);
        scheduledOn = (TextView) findViewById(R.id.scheduledOn);
        totalAmountValue = (TextView) findViewById(R.id.totalAmountValue);
        backImage = (ImageView) findViewById(R.id.backImage);
        engineerInfoButton = (Button) findViewById(R.id.engineerInfoButton);
        cancelJobButton = (Button) findViewById(R.id.cancelJobButton);
        step_view = (VerticalStepView) findViewById(R.id.step_view);
        list0.add(0,"Job Placed");
        list0.add(1,"FP Assigned Engineer");
        list0.add(2,"FP Expert on the way");
        list0.add(3,"Job in process");
        list0.add(4,"Job Completed");
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getJobDetail();
        engineerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderDetail.this, EngineerInfo.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("engineerId", engineerId);
                bundle1.putString("jobId", jobId);
                bundle1.putBoolean("showUpdation", false);
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });



        cancelJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LOGIN_TYPE.equals("customer")) {
                    try {
                        final Dialog dialog = new Dialog(OrderDetail.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.cancel_reason_customer);
                        dialog.setCanceledOnTouchOutside(false);

                        TextView orderId;
                        final EditText otherReasonText;
                        ImageView crossImage;
                        final RadioGroup radioGroup;
                        Button sendCancelButton;
                        final RadioButton radioButtonOne, radioButtonTwo, radioButtonThree, radioButtonFour, radioButtonFive;
                        orderId = (TextView) dialog.findViewById(R.id.orderId);
                        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                        otherReasonText = (EditText) dialog.findViewById(R.id.otherReasonText);
                        radioButtonOne = (RadioButton) dialog.findViewById(R.id.radioButtonOne);
                        radioButtonTwo = (RadioButton) dialog.findViewById(R.id.radioButtonTwo);
                        radioButtonThree = (RadioButton) dialog.findViewById(R.id.radioButtonThree);
                        radioButtonFour = (RadioButton) dialog.findViewById(R.id.radioButtonFour);
                        radioButtonFive = (RadioButton) dialog.findViewById(R.id.radioButtonFive);
                        sendCancelButton = (Button) dialog.findViewById(R.id.sendCancelButton);
                        orderId.setText("Order ID: " + jobOrderId);
                        crossImage = (ImageView) dialog.findViewById(R.id.crossImage);
                        dialog.show();
                        crossImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.radioButtonOne:
                                        selectedReason = radioButtonOne.getText().toString();
                                        break;
                                    case R.id.radioButtonTwo:
                                        selectedReason = radioButtonTwo.getText().toString();
                                        break;
                                    case R.id.radioButtonThree:
                                        selectedReason = radioButtonThree.getText().toString();
                                        break;
                                    case R.id.radioButtonFour:
                                        selectedReason = radioButtonFour.getText().toString();
                                        break;
                                    case R.id.radioButtonFive:
                                        selectedReason = radioButtonFive.getText().toString();
                                        break;
                                }
                            }
                        });
                        sendCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selectedReason.equals("")) {
                                    showToast(OrderDetail.this, "Please Select The Reason");
                                } else {
                                    dialog.dismiss();
                                    cancelJob(jobId, selectedReason, otherReasonText.getText().toString());
                                }
                            }
                        });

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }
                if (LOGIN_TYPE.equals("vendor")) {
                    try {
                        final Dialog dialog = new Dialog(OrderDetail.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.cancel_reason_vendor);
                        dialog.setCanceledOnTouchOutside(false);

                        TextView orderId;
                        final EditText otherReasonText;
                        ImageView crossImage;
                        final RadioGroup radioGroup;
                        Button sendCancelButton;
                        final RadioButton radioButtonOne, radioButtonTwo, radioButtonThree, radioButtonFour, radioButtonFive, radioButtonSix;
                        orderId = (TextView) dialog.findViewById(R.id.orderId);
                        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                        otherReasonText = (EditText) dialog.findViewById(R.id.otherReasonText);
                        radioButtonOne = (RadioButton) dialog.findViewById(R.id.radioButtonOne);
                        radioButtonTwo = (RadioButton) dialog.findViewById(R.id.radioButtonTwo);
                        radioButtonThree = (RadioButton) dialog.findViewById(R.id.radioButtonThree);
                        radioButtonFour = (RadioButton) dialog.findViewById(R.id.radioButtonFour);
                        radioButtonFive = (RadioButton) dialog.findViewById(R.id.radioButtonFive);
                        sendCancelButton = (Button) dialog.findViewById(R.id.sendCancelButton);
                        crossImage = (ImageView) dialog.findViewById(R.id.crossImage);
                        dialog.show();
                        crossImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.radioButtonOne:
                                        selectedReason = radioButtonOne.getText().toString();
                                        break;
                                    case R.id.radioButtonTwo:
                                        selectedReason = radioButtonTwo.getText().toString();
                                        break;
                                    case R.id.radioButtonThree:
                                        selectedReason = radioButtonThree.getText().toString();
                                        break;
                                    case R.id.radioButtonFour:
                                        selectedReason = radioButtonFour.getText().toString();
                                        break;
                                    case R.id.radioButtonFive:
                                        selectedReason = radioButtonFive.getText().toString();
                                        break;

                                }
                            }
                        });
                        sendCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selectedReason.equals("")) {
                                    showToast(OrderDetail.this, "Please Select The Reason");
                                } else {
                                    dialog.dismiss();
                                    cancelJob(jobId, selectedReason, otherReasonText.getText().toString());
                                }
                            }
                        });

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void cancelJob(String jobId, final String reason, String comment) {
        showProgress(OrderDetail.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("reason", reason);
        hashMap.put("realm", LOGIN_TYPE);
        hashMap.put("comment", comment);
        Log.e("fvcccccsdagbfdvb",""+hashMap);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.cancelJobByVendor(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(OrderDetail.this, response.body().getSuccess().getMsg().getReplyMessage());
                    if (LOGIN_TYPE.equals("engineer")) {

                        Intent intent = new Intent(OrderDetail.this, EngineerHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else  if (LOGIN_TYPE.equals("customer"))
                    {
                        Intent intent = new Intent(OrderDetail.this, UserHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else if (LOGIN_TYPE.equals("vendor"))
                    {

                        Intent intent = new Intent(OrderDetail.this, VendorHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OrderDetail.this, "" + jsonObjError.getString("message"));
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
            }
        });
    }


    private void getJobDetail() {
        showProgress(OrderDetail.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, JOB_ID);
        call.enqueue(new Callback<SmallSucess>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    jobId=response.body().getSuccess().getData().getId();
                    jobOrderId=response.body().getSuccess().getData().getOrderId();
                    Log.e("dbfff",""+(list0.size()));
                  /*  getStatus = "" + response.body().getSuccess().getData().getStatus();
                    jobId = response.body().getSuccess().getData().getId();
                    Log.e("gbjigbiohiofd", jobId + "");
                    if (getStatus.equals("vendorAccepted") || getStatus.equals("pending") || getStatus.equals("scheduled") || getStatus.equals("requested")) {
                        setStatusSetting = 5;
                    } else if (getStatus.equals("on the way")) {
                        setStatusSetting = 3;
                    } else if (getStatus.equals("inprocess")) {
                        setStatusSetting = 2;
                    } else if (getStatus.equals("completed")) {
                        setStatusSetting = 1;
                    }*/
                  String setStatus=response.body().getSuccess().getData().getStatus();

                    list0.clear();
                    list0.add(0, "Job Placed" +/*"\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate())+", "+getTimeFromUtc(response.body().getSuccess().getData().getStartDate())  +*/ "\n" + "Your order has been Placed and fix papa expert will be assign shortly"+"\n");
                    list0.add(1, "FP Assigned Engineer");
                    list0.add(2, "on the way");
                    list0.add(3, "In Process");
                    list0.add(4, "Job Completed");
                    setStatusSetting = 5;

                   /* if (!response.body().getSuccess().getData().getIsEngineerAssigned() && response.body().getSuccess().getData().getStatus().equals("vendorAccepted") || response.body().getSuccess().getData().getStatus().equals("pending") || response.body().getSuccess().getData().getStatus().equals("scheduled") || response.body().getSuccess().getData().getStatus().equals("requested")) {
                        list0.clear();
                        list0.add(0, "Job Placed" + "\n" + "Your order has been Placed and fix papa expert will be assign shortly");
                        list0.add(1, "FP Assigned Engineer");
                        list0.add(2, "on the way");
                        list0.add(3, "In Process");
                        list0.add(4, "Job Completed");
                        setStatusSetting = 5;

                        Log.e("dbffffrehn", "" + setStatus);
                    } else*/ if (response.body().getSuccess().getData().getIsEngineerAssigned()) {
                        list0.clear();
                        Log.e("dbffffreum", "" + setStatus);
                        list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                        list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                        list0.add(2, "on the way");
                        list0.add(3, "In Process");
                        list0.add(4, "Job Completed");
                        setStatusSetting = 4;

                        if (response.body().getSuccess().getData().getIsEngineerAssigned() && response.body().getSuccess().getData().getStatus().equals("on the way")) {
                            list0.clear();
                            Log.e("dbffffree", "" + setStatus);
                            list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                            list0.add(1, "FP Assigned  Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                            list0.add(2, "FP expert on the way" + "\n" + response.body().getSuccess().getData().getOtp() + " is your job OTP");
                            list0.add(3, "In Process");
                            list0.add(4, "Job Completed");
                            setStatusSetting = 3;
                        } else if (setStatus.equals("inprocess")) {
                            Log.e("dbfffferwe", "" + setStatus);
                            list0.clear();
                            if (response.body().getSuccess().getData().getSiteType().equals("Offsite")) {
                                list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                                list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                                list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                                list0.add(3, "In Process" + "\n" + "FP Expert working on offsite to fix problem");
                                list0.add(4, "Job Completed");
                                setStatusSetting = 2;
                            } else {
                                list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                                list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                                list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                                list0.add(3, "In Process" + "\n" + "FP Expert working to fix problem");
                                list0.add(4, "Job Completed");
                                setStatusSetting = 2;
                            }
                        } else if (setStatus.equals("billGenerated")) {
                            Log.e("dbffffs", "" + setStatus);
                            list0.clear();
                            list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                            list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                            list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                            list0.add(3, "In Process" + "\n" + "FP Expert completed work and generated job bill");
                            list0.add(4, "Job Completed");
                            setStatusSetting = 2;
                            if (LOGIN_TYPE.equals("customer"))
                            {
                                cancelJobButton.setVisibility(View.GONE);
                            }
                        } else if (setStatus.equals("paymentDone")) {
                            Log.e("dbffffsd", "" + setStatus);
                            list0.clear();
                            list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                            list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                            list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                            list0.add(3, "In Process" + "\n" + "Payment made successfully");
                            list0.add(4, "Job Completed");
                            setStatusSetting = 2;
                            if (LOGIN_TYPE.equals("vendor"))
                            {
                                cancelJobButton.setVisibility(View.GONE);
                            }
                        } else if (setStatus.equals("outForDelivery")) {
                            list0.clear();
                            Log.e("dbffffres", "" + setStatus);
                            list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                            list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                            list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                            list0.add(3, "In Process" + "\n" + "Your product is out for deliver");
                            list0.add(4, "Job Completed");
                            setStatusSetting = 2;
                        } else if (setStatus.equals("completed")) {
                            Log.e("dbffff", "" + setStatus);
                            cancelJobButton.setVisibility(View.GONE);
                            list0.clear();
                            list0.add(0, "Job Placed" + "\n" + "placed on: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate()));
                            list0.add(1, "FP Assigned Engineer" + "\n" + "job assigned to " + response.body().getSuccess().getData().getEngineer().getFullName());
                            list0.add(2, "FP expert reach to destination" + "\n" + "Job started");
                            list0.add(3, "In Process" + "\n" + "Probelm fixed by FP Expert");
                            list0.add(4, "Job Completed" + "\n" + "Job successfully completed on: "+getDateFromUtc(response.body().getSuccess().getData().getCompleteJob().getCompletedAt())+ "\n"+"Total paid amount "+response.body().getSuccess().getData().getBill().getTotal());
                            setStatusSetting = 1;
                        }

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        step_view.setStepsViewIndicatorComplectingPosition(list0.size() - setStatusSetting)
                                .reverseDraw(false)//default is true
                                .setStepViewTexts(list0)
                                .setLinePaddingProportion(0.85f)
                                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(OrderDetail.this, R.color.lightGray))
                                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(OrderDetail.this, R.color.lightGray))
                                .setStepViewComplectedTextColor(ContextCompat.getColor(OrderDetail.this, R.color.colorPrimary))
                                .setStepViewUnComplectedTextColor(ContextCompat.getColor(OrderDetail.this, R.color.lightGray))
                                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(OrderDetail.this, R.drawable.radio_on))
                                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(OrderDetail.this, R.drawable.radio_off))
                                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(OrderDetail.this, R.drawable.radio_on))
                                .setTextSize(12);

                    }
                    engineerId = response.body().getSuccess().getData().getEngineerId();
                    productName.setText("" + response.body().getSuccess().getData().getCategory().getName());
                    orderId.setText("Order ID : " + response.body().getSuccess().getData().getOrderId());
                    scheduledOn.setText("Scheduled On: " + getDateFromUtc(response.body().getSuccess().getData().getStartDate()) + "  " + getTimeFromUtc(response.body().getSuccess().getData().getStartDate())+"-"+getTimeFromUtc(response.body().getSuccess().getData().getEndDate()));
                    totalAmountValue.setText("" + response.body().getSuccess().getData().getTotalPrice());

                    if (response.body().getSuccess().getData().getVendorAssigned() && response.body().getSuccess().getData().getEngineerAssigned()) {
                        engineerInfoButton.setVisibility(View.VISIBLE);
                    } else {
                        engineerInfoButton.setVisibility(View.GONE);
                    }
                    if (response.body().getSuccess().getData().getProduct().getName().equals(response.body().getSuccess().getData().getCategory().getName())) {
                        productName.setText(response.body().getSuccess().getData().getProduct().getName());
                    }
                    if (response.body().getSuccess().getData().getBrand() != null) {
                        if (response.body().getSuccess().getData().getProduct().getName().equals(response.body().getSuccess().getData().getBrand().getName())) {
                            productName.setText(response.body().getSuccess().getData().getBrand().getName());
                        }
                    }
                    //showToast(OrderDetail.this, "" + response.body().getSuccess().getMsg().getReplyCode());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OrderDetail.this, "" + jsonObjError.getString("message"));
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
            }
        });

    }

}
