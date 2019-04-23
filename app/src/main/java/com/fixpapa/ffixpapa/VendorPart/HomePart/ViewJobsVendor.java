package com.fixpapa.ffixpapa.VendorPart.HomePart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.NewJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
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
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ViewJobsVendor extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton, rejectJobButton;
    List<Problems> problemsList;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle;
    NewJobsFragment newJobsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        newJobsFragment = new NewJobsFragment();


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
        jobDescription = bundle.getString("jobDescription");
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
        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(ViewJobsVendor.this));

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

        if (brandName==null)
        {
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


        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {

                Picasso.with(ViewJobsVendor.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(ViewJobsVendor.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, ViewJobsVendor.this);
        vendorJobIssues.setAdapter(issuesAdapter);


    }

    @Override
    public void onClick(View v) {
        if (v == crossImage) {
            finish();
        }
        if (v == acceptJobButton) {
            acceptRequest();
        }
        if (v == rejectJobButton) {
            rejectRequest();
        }
    }

    private void rejectRequest() {
        showProgress(ViewJobsVendor.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("status", "requested");
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> vendorCall = apiInterface.acceptVendor(ACCESS_TOKEN, hashMap);

        vendorCall.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    finish();
                    Intent intent=new Intent(ViewJobsVendor.this,VendorHomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    showToast(ViewJobsVendor.this, "Job Rejected");

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ViewJobsVendor.this, "" + jsonObjError.getString("message"));

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

    private void acceptRequest() {
        showProgress(ViewJobsVendor.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("status", "vendorAccepted");

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> vendorCall = apiInterface.acceptVendor(ACCESS_TOKEN, hashMap);

        vendorCall.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    finish();
                    Intent intent=new Intent(ViewJobsVendor.this,VendorHomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    showToast(ViewJobsVendor.this, "Job Accepted");

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ViewJobsVendor.this, "" + jsonObjError.getString("message"));

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
                Log.e("bncbcbcdf",""+t);
            }
        });

    }
}
