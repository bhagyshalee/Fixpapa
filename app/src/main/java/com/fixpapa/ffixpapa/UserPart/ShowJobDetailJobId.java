package com.fixpapa.ffixpapa.UserPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

public class ShowJobDetailJobId extends AppCompatActivity {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton, rejectJobButton;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle;
    LinearLayout layoutButton;
    String jobId;
    IssuesAdapter issuesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        jobId = bundle.getString("jobId");

        getUserDetailId(ShowJobDetailJobId.this);
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

        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(ShowJobDetailJobId.this));
        layoutButton.setVisibility(View.GONE);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getJobDetail(jobId);
    }

    private void getJobDetail(String jobId) {
        showProgress(ShowJobDetailJobId.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    //showToast(ShowJobDetailJobId.this, "" + response.body().getSuccess().getMsg().getReplyCode());


                    if (response.body().getSuccess().getData().getCategory().getName().equals(response.body().getSuccess().getData().getProduct().getName())) {
                        vendorViewProduct.setVisibility(View.GONE);
                        productTitle.setVisibility(View.GONE);
                    }
                    if (response.body().getSuccess().getData().getProduct() != null && response.body().getSuccess().getData().getBrand() != null) {
                        if (response.body().getSuccess().getData().getProduct().getName().equals(response.body().getSuccess().getData().getBrand().getName())) {
                            vendorViewBrand.setVisibility(View.GONE);
                            brandTitle.setVisibility(View.GONE);
                        }
                    }
                    vendorViewJobDevice.setText(response.body().getSuccess().getData().getCategory().getName());
                    vendorViewProduct.setText(response.body().getSuccess().getData().getProduct().getName());

                    vendorViewBudget.setText("" + response.body().getSuccess().getData().getTotalPrice());
                    vendorViewJobDescription.setText(response.body().getSuccess().getData().getProblemDes());
                    if (response.body().getSuccess().getData().getBrand()!=null) {
                        vendorViewBrand.setText(response.body().getSuccess().getData().getBrand().getName());
                    }

                    vendorViewAddress.setText(response.body().getSuccess().getData().getAddress().getStreet() + "" + response.body().getSuccess().getData().getAddress().getValue());

                    vendorViewDate.setText(getDateFromUtc(response.body().getSuccess().getData().getVendorAssignedDate()));

                    vendorViewTime.setText(getTimeFromUtc(response.body().getSuccess().getData().getStartDate()) + " - " + getTimeFromUtc(response.body().getSuccess().getData().getEndDate()));

                    for (int i = 0; i < response.body().getSuccess().getData().getImage().size(); i++) {
                        if (i == 0) {

                            Picasso.with(ShowJobDetailJobId.this)
                                    .load(Image_BASE_URL + response.body().getSuccess().getData().getImage().get(0))
                                    .placeholder(R.drawable.icon_fixpapa)
                                    .error(R.drawable.icon_fixpapa)
                                    .into(productImagesOne);

                        }
                        if (i == 1) {
                            Picasso.with(ShowJobDetailJobId.this)
                                    .load(Image_BASE_URL + response.body().getSuccess().getData().getImage().get(1))
                                    .placeholder(R.drawable.icon_fixpapa)
                                    .error(R.drawable.icon_fixpapa)
                                    .into(productImagesTwo);

                        }
                    }
                    issuesAdapter = new IssuesAdapter(response.body().getSuccess().getData().getProblems(), ShowJobDetailJobId.this);
                    vendorJobIssues.setAdapter(issuesAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ShowJobDetailJobId.this, "" + jsonObjError.getString("message"));
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