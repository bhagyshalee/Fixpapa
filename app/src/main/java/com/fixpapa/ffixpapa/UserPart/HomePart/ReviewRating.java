package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.RatingModel.SuccessMM;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ReviewRating extends AppCompatActivity {
    RatingBar ratingBar;
    Button btnSubmitReview;
    EditText commentWrite;
    TextView completedTime, completedDate, scheduleTime, scheduleDate, orderId, customerName, totalAmount;
    String customerNamestr, orderIdstr, scheduledStart, scheduledEnd, completedOnstr, cusIdstr, enginIdstr, jobId;
    int totalPriceIn;
    double ratingValue;
    String ACCESS_TOKEN,LOGIN_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating);
        getUserDetailId(ReviewRating.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerNamestr = bundle.getString("customerName");
            totalPriceIn = bundle.getInt("totalAmount");
            orderIdstr = bundle.getString("orderId");
            scheduledStart = bundle.getString("scheduledStart");
            scheduledEnd = bundle.getString("scheduledEnd");
            completedOnstr = bundle.getString("completedOn");
            cusIdstr = bundle.getString("cusId");
            enginIdstr = bundle.getString("enginId");
            jobId = bundle.getString("jobId");
        }
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
         ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
         LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        completedTime = (TextView) findViewById(R.id.completedTime);
        completedDate = (TextView) findViewById(R.id.completedDate);
        scheduleTime = (TextView) findViewById(R.id.scheduleTime);
        scheduleDate = (TextView) findViewById(R.id.scheduleDate);
        orderId = (TextView) findViewById(R.id.orderId);
        customerName = (TextView) findViewById(R.id.customerName);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        commentWrite = (EditText) findViewById(R.id.commentWrite);
        btnSubmitReview = (Button) findViewById(R.id.btnSubmitReview);

        if (LOGIN_TYPE.equals("customer"))
        {
            customerName.setText("Engineer Name: " + customerNamestr);
        }else
        {
            customerName.setText("Customer Name: " + customerNamestr);
        }

        totalAmount.setText("Total Amount: Rs " + totalPriceIn);
        scheduleDate.setText(getDateFromUtc(scheduledStart));
        scheduleTime.setText(getTimeFromUtc(scheduledEnd + " - " + scheduledEnd));
        completedDate.setText(getDateFromUtc(completedOnstr));
        completedTime.setText(getTimeFromUtc(completedOnstr));
        orderId.setText("OrderId: " + orderIdstr);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue = rating;

            }
        });


        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(ReviewRating.this);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("requestjobId", jobId);
                jsonObject.addProperty("userRating", ratingValue);
                jsonObject.addProperty("comment", "" + commentWrite.getText().toString().trim());
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<SuccessMM> call = apiInterface.giveRating(ACCESS_TOKEN, jsonObject);
                Log.e("dfgdf", "" + jsonObject);
                call.enqueue(new Callback<SuccessMM>() {
                    @Override
                    public void onResponse(Call<SuccessMM> call, Response<SuccessMM> response) {
                        close();
                        if (response.isSuccessful()) {
                            showToast(ReviewRating.this, "" + response.body().getSuccess().getMsg().getReplyMessage());
                            if (LOGIN_TYPE.equals("engineer")) {
                                Intent intent = new Intent(ReviewRating.this, EngineerHomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            if (LOGIN_TYPE.equals("vendor")) {
                                Intent intent = new Intent(ReviewRating.this, VendorHomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            if (LOGIN_TYPE.equals("customer")) {
                                Intent intent = new Intent(ReviewRating.this, UserHomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                JSONObject jsonObjError = jsonObject.getJSONObject("error");
                                showToast(ReviewRating.this, "" + jsonObjError.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessMM> call, Throwable t) {
                        close();
                        Log.e("vvxvxvxv", t + "");
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     /*   Intent intent = new Intent(ReviewRating.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
        finish();
    }
}
