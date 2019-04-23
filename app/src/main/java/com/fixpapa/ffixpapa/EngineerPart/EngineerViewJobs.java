package com.fixpapa.ffixpapa.EngineerPart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class EngineerViewJobs extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton, rejectJobButton;
    List<Problems> problemsList;
    List<AddPart> addParts;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName, jobOrderId, customerName,
            dateVendor, scheduleStatus = "", customerId, engineerId;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle, showTitleText;

    List<AditionPart> listStr;
    JSONObject addService;
    String showStatus;
    int serviceTotal, addServiceCost, serviceAdpartCost, totalPrice=0, discount,getPayAmt=0;
    LinearLayout layoutButton, showTitleTextLayout;
    CancelModel cancelModel;
    CompleteJobModel completedJobDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        listStr = new ArrayList<>();
        addService = new JSONObject();
        problemsList = (List<Problems>) bundle.getSerializable("sendIssues");
        cancelModel = (CancelModel) bundle.getSerializable("cancelJobDetail");
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");
        addressList = (AddressesModel) bundle.getSerializable("address");
        imageList = (List<String>) bundle.getSerializable("jobImage");
        addParts = (List<AddPart>) bundle.getSerializable("AdditionalPartDetail");

        jobId = bundle.getString("jobId");
        budget = bundle.getString("budget");
        dateTimeStart = bundle.getString("dateTimeStart");
        dateTimeEnd = bundle.getString("dateTimeEnd");
        jobDescription = bundle.getString("jobDescription");
        jobOrderId = bundle.getString("jonOrderId");
        getPayAmt = bundle.getInt("payAmt");
        customerName = bundle.getString("customerName");
        dateVendor = bundle.getString("dateVendorAssign");
        scheduleStatus = bundle.getString("scheduleStatus");
        customerId = bundle.getString("customerId");
        showStatus = bundle.getString("showButton");
        serviceTotal = bundle.getInt("serviceTotal");
        serviceAdpartCost = bundle.getInt("AdditionalPart");
        addServiceCost = bundle.getInt("additionalCharge");
        totalPrice = bundle.getInt("totalPrice");
        completedJobDate = (CompleteJobModel) bundle.getSerializable("completedDate");
        engineerId = bundle.getString("enginId");
        discount = bundle.getInt("discount");
        Log.e("svdsvdsvdsvdsvdsv", "" + getPayAmt);

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
        showTitleText = (TextView) findViewById(R.id.showTitleText);
        vendorViewJobDescription = (TextView) findViewById(R.id.vendorViewJobDescription);
        productTitle = (TextView) findViewById(R.id.productTitle);
        brandTitle = (TextView) findViewById(R.id.brandTitle);
        layoutButton = (LinearLayout) findViewById(R.id.layoutButton);
        showTitleTextLayout = (LinearLayout) findViewById(R.id.showTitleTextLayout);

        vendorJobIssues = (RecyclerView) findViewById(R.id.vendorJobIssues);
        rejectJobButton = (Button) findViewById(R.id.rejectJobButton);
        acceptJobButton = (Button) findViewById(R.id.acceptJobButton);
        acceptJobButton.setText("Customer Issue");
        rejectJobButton.setText("Create Bill");


        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(EngineerViewJobs.this));

        if (showStatus.equals("yes")) {
            showTitleTextLayout.setVisibility(View.GONE);
            layoutButton.setVisibility(View.VISIBLE);
        } else {
            if (completedJobDate != null) {
                if (scheduleStatus.equals("completed")) {
                    showTitleTextLayout.setVisibility(View.VISIBLE);
                    if(getPayAmt==0)
                    {
                        showTitleText.setText("Job successfully completed on"+getDateFromUtc(completedJobDate.getCompletedAt())+"\n" + "Reason: " + completedJobDate.getComment());

                    }else
                    {
                        showTitleText.setText("Job successfully completed on"+getDateFromUtc(completedJobDate.getCompletedAt())+"\n" +"Total Price "+getPayAmt+"\n" + "Reason: " + completedJobDate.getComment());
                    }

                } else if (scheduleStatus.equals("indispute")) {
                    showTitleTextLayout.setVisibility(View.VISIBLE);
                    showTitleText.setText("Job placed in Dispute by engineer " + getDateFromUtc(completedJobDate.getCompletedAt())+" "+
                            "\n" + "Reason: " + completedJobDate.getComment());
                }
            } else if (cancelModel != null) {
                showTitleTextLayout.setVisibility(View.VISIBLE);
                if (getPayAmt!=0) {
                    showTitleText.setText("Job cancelled by " + cancelModel.getCancelledBy() + " on " + getDateFromUtc(cancelModel.getCancelledDate()) + "\n" +"Total Price "+getPayAmt+"\n"+ "Reason: " + cancelModel.getReason());
                }else
                {
                    showTitleText.setText("Job cancelled by " + cancelModel.getCancelledBy() + " on " + getDateFromUtc(cancelModel.getCancelledDate()) +"\n"+ "Reason: " + cancelModel.getReason());

                }
            }

            layoutButton.setVisibility(View.GONE);
        }
        showTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (completedJobDate != null) {
                    if (scheduleStatus.equals("completed")) {
                        showPopup("Job successfully completed on"+"\n"+getDateFromUtc(completedJobDate.getCompletedAt()), "Total Price "+getPayAmt,  "Reason: " + completedJobDate.getComment());
                    }
                    else if (scheduleStatus.equals("indispute")) {
                        showPopup("Job placed in Dispute by engineer  on" +"\n"+ getDateFromUtc(completedJobDate.getCompletedAt()),  "","\n" + "Reason: " + completedJobDate.getComment());
                    }
                }else if (cancelModel != null) {
                    showTitleTextLayout.setVisibility(View.VISIBLE);
                    if (getPayAmt!=0) {
                        //showTitleText.setText("Job cancelled by " + cancelModel.getCancelledBy() + " on " + getDateFromUtc(cancelModel.getCancelledDate()) + "\n" +"Total Price "+getPayAmt+"\n"+ "Reason: " + cancelModel.getReason());
                       showPopup("Job cancelled by " + cancelModel.getCancelledBy() + " on "+"\n" + getDateFromUtc(cancelModel.getCancelledDate()), "Total Price "+getPayAmt, "\n" + "Reason: " + cancelModel.getReason());

                    }else
                    {
                        showPopup("Job cancelled by " + cancelModel.getCancelledBy() + " on "+"\n" + getDateFromUtc(cancelModel.getCancelledDate()), "",  "Reason: " + cancelModel.getReason());
                    }
                }
            }
        });

        rejectJobButton.setOnClickListener(this);
        acceptJobButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);
        Log.e("ymtjhtktyk", productName + " " + brandName);
        if (categoryName.equals(productName)) {
            vendorViewProduct.setVisibility(View.GONE);
            productTitle.setVisibility(View.GONE);
        }
        if (productName != null && brandName != null) {
            if (productName.equals(brandName)) {
                vendorViewBrand.setVisibility(View.GONE);
                brandTitle.setVisibility(View.GONE);
            }
        }


        vendorViewJobDevice.setText(categoryName);
        vendorViewProduct.setText(productName);

        vendorViewBudget.setText(budget);
        vendorViewJobDescription.setText(jobDescription);
        vendorViewBrand.setText(brandName);

        vendorViewAddress.setText(addressList.getStreet() + "" + addressList.getValue());

        vendorViewDate.setText(getDateFromUtc(dateVendor));

        vendorViewTime.setText(getTimeFromUtc(dateTimeStart) + " - " + getTimeFromUtc(dateTimeEnd));

        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {

                Picasso.with(EngineerViewJobs.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(EngineerViewJobs.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, EngineerViewJobs.this);
        vendorJobIssues.setAdapter(issuesAdapter);


/*
        siteRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().equals("Off Site")) {
                    setRadioString = "offsite";
                    layoutOffSite.setVisibility(View.VISIBLE);
                    layoutOnSite.setVisibility(View.GONE);
                    generateBillButton.setText(getResources().getString(R.string.pic_jobcat_text));
                } else {
                    setRadioString = "onsite";
                    layoutOffSite.setVisibility(View.GONE);
                    layoutOnSite.setVisibility(View.VISIBLE);
                    generateBillButton.setText(getResources().getString(R.string.generate_bill_text));
                }

            }
        });
*/

    }

    private void showPopup(String setPoup,String price,String datee) {
        try {
            final Dialog dialog = new Dialog(EngineerViewJobs.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.design_showdetail_completed);
            dialog.setCanceledOnTouchOutside(false);
            final TextView jobCompletedOn,jobCompletedTotalPr,jobCompletedDate;
            Button buttonOk;
            jobCompletedTotalPr = (TextView) dialog.findViewById(R.id.jobCompletedTotalPr);
            jobCompletedOn = (TextView) dialog.findViewById(R.id.jobCompletedOn);
            jobCompletedDate = (TextView) dialog.findViewById(R.id.jobCompletedDate);
            buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
            dialog.show();
            Log.e("dsvsvvvvvvvvvvvvv",""+setPoup+" "+price+" "+datee);
            if (getPayAmt==0) {
                jobCompletedOn.setVisibility(View.GONE);
            }
            jobCompletedTotalPr.setText(setPoup);
            jobCompletedOn.setText(price);
            jobCompletedDate.setText(datee);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v == acceptJobButton) {
            try {
                final Dialog dialog = new Dialog(EngineerViewJobs.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.design_customer_issue);
                dialog.setCanceledOnTouchOutside(false);

                final EditText editTextComment;
                Button buttonDone, buttonCancel;
                editTextComment = (EditText) dialog.findViewById(R.id.editTextComment);
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
                        if (editTextComment.getText().toString().isEmpty()) {
                            showToast(EngineerViewJobs.this, "Please enter the reason");
                        } else {
                            dialog.dismiss();
                            cancelJob(jobId, editTextComment.getText().toString(), "");
                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        if (v == rejectJobButton) {
            Intent intent = new Intent(EngineerViewJobs.this, CreateBill.class);
            Bundle bundle = new Bundle();
            bundle.putString("jobId", jobId);
            bundle.putInt("serviceTotal", serviceTotal);
            bundle.putInt("AdditionalPart", serviceAdpartCost);
            bundle.putInt("additionalCharge", addServiceCost);
            bundle.putSerializable("AdditionalPartDetail", (ArrayList<AddPart>) addParts);
            bundle.putInt("totalPrice", totalPrice);
            bundle.putString("scheduledStart", dateTimeStart);
            bundle.putString("scheduledEnd", dateTimeEnd);
            bundle.putString("customerName", customerName);
            bundle.putString("customerId", customerId);
            bundle.putInt("discount", discount);
            bundle.putString("enginId", engineerId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (v == crossImage) {
          /*  Intent intent = new Intent(EngineerViewJobs.this, EngineerHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();
        }

    }

    private void cancelJob(String jobId, final String reason, String comment) {
        showProgress(EngineerViewJobs.this);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
     String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");
        String LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE", "");

        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("reason", reason);
        hashMap.put("realm", LOGIN_TYPE);
        hashMap.put("comment", comment);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.cancelJobByVendor(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(EngineerViewJobs.this, response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                    Intent intent = new Intent(EngineerViewJobs.this, EngineerHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewJobs.this, "" + jsonObjError.getString("message"));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    /*    Intent intent = new Intent(EngineerViewJobs.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
        finish();
    }
}