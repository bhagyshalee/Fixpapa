package com.fixpapa.ffixpapa.EngineerPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.OrderDetail;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
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

public class EngineerCompleteJobs extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton,rejectJobButton;
    List<Problems> problemsList;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName, jobOrderId, customerName,
            dateVendor, scheduleStatus = "", customerId,engineerId;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle;

    List<AditionPart> listStr;
    JSONObject addService;
    String showStatus;
    int serviceTotal,addServiceCost,serviceAdpartCost;
    LinearLayout layoutButton;


    @RequiresApi(api = Build.VERSION_CODES.M)
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
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");
        addressList = (AddressesModel) bundle.getSerializable("address");
        imageList = (List<String>) bundle.getSerializable("jobImage");
        jobId = bundle.getString("jobId");
        budget = bundle.getString("budget");
        dateTimeStart = bundle.getString("dateTimeStart");
        dateTimeEnd = bundle.getString("dateTimeEnd");
        jobDescription = bundle.getString("jobDescription");
        jobOrderId = bundle.getString("jonOrderId");
        customerName = bundle.getString("customerName");
        dateVendor = bundle.getString("dateVendorAssign");
        scheduleStatus = bundle.getString("scheduleStatus");
        customerId = bundle.getString("customerId");
        showStatus = bundle.getString("showButton");
        serviceTotal = bundle.getInt("serviceTotal");
        addServiceCost = bundle.getInt("serviceAdpartCost");
        serviceAdpartCost = bundle.getInt("addServiceCost");
        engineerId = bundle.getString("enginId");

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
        layoutButton = (LinearLayout) findViewById(R.id.layoutButton);

        vendorJobIssues = (RecyclerView) findViewById(R.id.vendorJobIssues);
        rejectJobButton = (Button) findViewById(R.id.rejectJobButton);
        acceptJobButton = (Button) findViewById(R.id.acceptJobButton);
        acceptJobButton.setText("Order Detail");
        //acceptJobButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        rejectJobButton.setVisibility(View.GONE);
        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(EngineerCompleteJobs.this));


        rejectJobButton.setOnClickListener(this);
        acceptJobButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);

        if (categoryName.equals(productName)) {
            vendorViewProduct.setVisibility(View.GONE);
            productTitle.setVisibility(View.GONE);
        }

        if (productName.equals(brandName)) {
            vendorViewBrand.setVisibility(View.GONE);
            brandTitle.setVisibility(View.GONE);
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

                Picasso.with(EngineerCompleteJobs.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(EngineerCompleteJobs.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, EngineerCompleteJobs.this);
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

    @Override
    public void onClick(View v) {
        if (v==acceptJobButton)
        {
            Intent intent=new Intent(EngineerCompleteJobs.this, OrderDetail.class);
            Bundle bundle=new Bundle();
            bundle.putString("jobId",jobId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (v==rejectJobButton)
        {
            Intent intent=new Intent(EngineerCompleteJobs.this,CreateBill.class);
            Bundle bundle=new Bundle();
            bundle.putString("jobId",jobId);
            bundle.putInt("serviceTotal",serviceTotal);
            bundle.putInt("AdditionalPart",serviceAdpartCost);
            bundle.putInt("additionalCharge",addServiceCost);
            bundle.putString("scheduledStart",dateTimeStart);
            bundle.putString("scheduledEnd",dateTimeEnd);
            bundle.putString("customerName",customerName);
            bundle.putString("customerId",customerId);
            bundle.putString("enginId",engineerId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (v == crossImage) {
            finish();
        }

    }

    private void cancelJob(String jobId, final String reason,String comment) {
        showProgress(EngineerCompleteJobs.this);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        String LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");

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
                    showToast(EngineerCompleteJobs.this, response.body().getSuccess().getMsg().getReplyCode());
                    finish();
                        Intent intent = new Intent(EngineerCompleteJobs.this, EngineerHomeScreen.class);
                        startActivity(intent);
                        finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerCompleteJobs.this, "" + jsonObjError.getString("message"));
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