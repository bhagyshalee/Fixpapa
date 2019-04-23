package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentPostAdapter;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentPostAdapter.getSelecCheck;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentPostAdapter.getSelectedRentCat;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentPostAdapter.getSelectedSystems;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentPostAdapter.getSelectedTimePeriod;

public class RentPost extends AppCompatActivity {
    RecyclerView categoryRentRecycler;
    EditText addressRent, descriptionRent, budgetRent;
    Button btnRentPost;
    List<String> amc,amcCatId;
    RentPostAdapter amcPostAdapter;
    JSONObject jsonObjectmain;
    JsonArray jsonElements;
    ImageView crossImage;
    String nameBr;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_post);
        getUserDetailId(RentPost.this);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        amc = (List<String>) bundle.getSerializable("amcCat");
        amcCatId = (List<String>) bundle.getSerializable("amcCatId");
        nameBr = bundle.getString("amcName");

        categoryRentRecycler = (RecyclerView) findViewById(R.id.categoryRentRecycler);
        addressRent = (EditText) findViewById(R.id.addressRent);

        budgetRent = (EditText) findViewById(R.id.budgetRent);
        descriptionRent = (EditText) findViewById(R.id.descriptionRent);
        btnRentPost = (Button) findViewById(R.id.btnRentPost);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        titleText = (TextView) findViewById(R.id.titleText);

        categoryRentRecycler.setHasFixedSize(true);
        categoryRentRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RentPost.this);
        categoryRentRecycler.setLayoutManager(layoutManager);
        amcPostAdapter = new RentPostAdapter(amc,amcCatId, RentPost.this);
        categoryRentRecycler.setAdapter(amcPostAdapter);


        titleText.setText(nameBr);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonObjectmain = new JSONObject();
                jsonElements = new JsonArray();
                for (int i = 0; i < getSelecCheck.size(); i++) {
                    if (getSelecCheck.get(i).isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("categoryId", "" + getSelectedRentCat.get(i));
                        jsonObject.addProperty("noOfUnits", "" + getSelectedSystems.get(i));
                        jsonObject.addProperty("timePeriod", "" + getSelectedTimePeriod.get(i));
                        jsonElements.add(jsonObject);
                    }

                }
                if (jsonElements == null || descriptionRent.getText().toString().isEmpty()
                        || addressRent.getText().toString().isEmpty() || budgetRent.getText().toString().isEmpty()) {
                    showToast(RentPost.this, "All fields are mandatory");
                } else if (isOnline(true, RentPost.this)) {
                    sendAmcRequest(descriptionRent.getText().toString().trim(), addressRent.getText().toString().trim(), Integer.parseInt(budgetRent.getText().toString()));
                }
            }

        });

    }


    private void sendAmcRequest(String description, String address, int budget) {

        final JsonObject requestBody = new JsonObject();
        requestBody.add("rentDetail", jsonElements);
        requestBody.addProperty("description", description);
        requestBody.addProperty("estiBudget", budget);
        requestBody.addProperty("address", address);

        showProgress(RentPost.this);
        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> successModelCall = apiInterface.sendRentRequest(ACCESS_TOKEN, requestBody);
        successModelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(RentPost.this, "" + response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(RentPost.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}