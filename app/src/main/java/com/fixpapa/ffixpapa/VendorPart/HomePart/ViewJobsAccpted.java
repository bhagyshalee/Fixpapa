package com.fixpapa.ffixpapa.VendorPart.HomePart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.OrderDetail;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.FreeEngineer;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.NewJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ViewJobsAccpted extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton, rejectJobButton;
    List<Problems> problemsList;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName,
            jobOrderId, jobStatus, selectedReason = "", strStatus;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle;
    NewJobsFragment newJobsFragment;

    String LOGIN_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        newJobsFragment = new NewJobsFragment();
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE", "");

        getUserDetailId(ViewJobsAccpted.this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        problemsList = (List<Problems>) bundle.getSerializable("sendIssues");
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");
        addressList = (AddressesModel) bundle.getSerializable("address");
        imageList = (List<String>) bundle.getSerializable("jobImage");
        jobId = bundle.getString("jobId");
        budget = bundle.getString("budget");
        dateTimeStart = bundle.getString("dateTimeStart");
        dateTimeEnd = bundle.getString("dateTimeEnd");
        strStatus = bundle.getString("status");
        jobDescription = bundle.getString("jobDescription");
        jobOrderId = bundle.getString("jonOrderId");
        jobStatus = bundle.getString("assignedStatus");
        crossImage = (ImageView) findViewById(R.id.crossImage);
        productImagesOne = (ImageView) findViewById(R.id.productImagesOne);
        productImagesTwo = (ImageView) findViewById(R.id.productImagesTwo);
        vendorViewJobDevice = (TextView) findViewById(R.id.vendorViewJobDevice);
        vendorViewProduct = (TextView) findViewById(R.id.vendorViewProduct);
        vendorViewBrand = (TextView) findViewById(R.id.vendorViewBrand);
        vendorViewDate = (TextView) findViewById(R.id.vendorViewDate);
        vendorViewTime = (TextView) findViewById(R.id.vendorViewTime);
        vendorViewBudget = (TextView) findViewById(R.id.vendorViewBudget);
        vendorViewAddress = (TextView) findViewById(R.id.vendorViewAddress);
        vendorViewJobDescription = (TextView) findViewById(R.id.vendorViewJobDescription);
        productTitle = (TextView) findViewById(R.id.productTitle);
        brandTitle = (TextView) findViewById(R.id.brandTitle);


        vendorJobIssues = (RecyclerView) findViewById(R.id.vendorJobIssues);
        acceptJobButton = (Button) findViewById(R.id.acceptJobButton);
        rejectJobButton = (Button) findViewById(R.id.rejectJobButton);

        if (jobStatus.equals("notassign")) {
            rejectJobButton.setText(getResources().getString(R.string.assign_engineer_text));
            acceptJobButton.setText(getResources().getString(R.string.cancel_jobs_text));
        }
        if (jobStatus.equals("assigned")) {
            rejectJobButton.setText(getResources().getString(R.string.order_detail_text));
            acceptJobButton.setText(getResources().getString(R.string.cancel_jobs_text));
        }

        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(ViewJobsAccpted.this));

        acceptJobButton.setOnClickListener(this);
        rejectJobButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);

        if (productName.equals(categoryName)) {
            vendorViewProduct.setVisibility(View.GONE);
            productTitle.setVisibility(View.GONE);
        }

        if (productName.equals(brandName)) {
            vendorViewBrand.setVisibility(View.GONE);
            brandTitle.setVisibility(View.GONE);
        }
        if (brandName == null) {
            vendorViewBrand.setVisibility(View.GONE);
            brandTitle.setVisibility(View.GONE);
        }
        vendorViewJobDevice.setText(categoryName);
        vendorViewProduct.setText(productName);

        vendorViewBudget.setText(budget);
        vendorViewJobDescription.setText(jobDescription);
        vendorViewBrand.setText(brandName);

        vendorViewAddress.setText(addressList.getStreet() + "" + addressList.getValue());

        vendorViewDate.setText(getDateFromUtc(dateTimeStart));

        vendorViewTime.setText(getTimeFromUtc(dateTimeStart) + " - " + getTimeFromUtc(dateTimeEnd));
        if (strStatus.equals("paymentDone") || strStatus.equals("completed")) {
            acceptJobButton.setVisibility(View.GONE);
        } else {
            acceptJobButton.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {

                Picasso.with(ViewJobsAccpted.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(ViewJobsAccpted.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, ViewJobsAccpted.this);
        vendorJobIssues.setAdapter(issuesAdapter);


    }

    @Override
    public void onClick(View v) {
        if (v == crossImage) {
            finish();
        }
        if (v == acceptJobButton) {
            if (LOGIN_TYPE.equals("customer")) {
                try {
                    final Dialog dialog = new Dialog(ViewJobsAccpted.this);
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
                           /* if (otherReasonText.getText().toString().isEmpty()) {
                                if (otherReasonText.getText().toString().isEmpty()) {
                                    otherReasonText.setError("Please Enter Valid Reason");
                                } else {
                                    selectedReason = otherReasonText.getText().toString().trim();
                                    dialog.dismiss();
                                    cancelJob(jobId, selectedReason);
                                }
                            } else*/
                            if (selectedReason.equals("")) {
                                showToast(ViewJobsAccpted.this, "Please Select The Reason");
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
                    final Dialog dialog = new Dialog(ViewJobsAccpted.this);
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
                           /* if (selectedReason.equals("Other (Text box required maximum words 100)")) {
                                if (otherReasonText.getText().toString().isEmpty()) {
                                    otherReasonText.setError("Please Enter Valid Reason");
                                } else {
                                    selectedReason = otherReasonText.getText().toString().trim();
                                    dialog.dismiss();

                                    cancelJob(jobId, selectedReason);
                                }
                            } else*/
                            if (selectedReason.equals("")) {
                                showToast(ViewJobsAccpted.this, "Please Select The Reason");
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
        if (v == rejectJobButton) {
            if (rejectJobButton.getText().toString().equals(getResources().getString(R.string.assign_engineer_text))) {
                Intent intent = new Intent(ViewJobsAccpted.this, FreeEngineer.class);
                intent.putExtra("jobOrderId", jobOrderId);
                intent.putExtra("jobId", jobId);
                startActivity(intent);
            }
            if (rejectJobButton.getText().toString().equals(getResources().getString(R.string.order_detail_text))) {
                Intent intent = new Intent(ViewJobsAccpted.this, OrderDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("sendCategory", categoryName);
                bundle.putString("subCategory", productName);
                bundle.putString("jobId", jobId);
                bundle.putString("brand", brandName);
                bundle.putString("orderId", jobOrderId);
                bundle.putString("totalPrice", budget);
                bundle.putString("date", dateTimeStart);
                bundle.putString("time", dateTimeEnd);
                bundle.putString("status", strStatus);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }

    private void cancelJob(String jobId, final String reason, String comment) {
        showProgress(ViewJobsAccpted.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("realm", LOGIN_TYPE);
        hashMap.put("reason", reason);
        hashMap.put("comment", comment);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> call = apiInterface.cancelJobByVendor(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(ViewJobsAccpted.this, "SuccessFully canceled job");
                    finish();
                    if (LOGIN_TYPE.equals("vendor")) {
                        Intent intent = new Intent(ViewJobsAccpted.this, VendorHomeScreen.class);
                        startActivity(intent);
                        finish();
                    } else if (LOGIN_TYPE.equals("customer")) {
                        Intent intent = new Intent(ViewJobsAccpted.this, UserHomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ViewJobsAccpted.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessModelVendor> call, Throwable t) {

                close();
            }
        });
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        rejectJobButton.setText(getResources().getString(R.string.order_detail_text));
    }*/
}
