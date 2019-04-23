package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.OfficeSetupPostAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AmcCategory;
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
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtcPurchase;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.setDatePickerHidePrevious;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.OfficeSetupPostAdapter.getSelectedCheck;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.OfficeSetupPostAdapter.getSelectedSystem;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.OfficeSetupPostAdapter.getSelectedUnits;

public class OfficeSetupPost extends AppCompatActivity {
    RecyclerView categoryOfficeRecycler;
    EditText officeAddress, descriptionOffice, officeBudget;
    Button btnOfficePost;
    List<AmcCategory> amc;
    List<String> listUnit;
    OfficeSetupPostAdapter officeSetupPostAdapter;
    String amcId, officeSetupName;
    JSONObject jsonObjectmain;
    JsonArray jsonElements;
    TextView startDate, endDate;
    ImageView crossImage;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_setup_post);
        getUserDetailId(OfficeSetupPost.this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        amc = (List<AmcCategory>) bundle.getSerializable("officeSetupCat");
        listUnit = (List<String>) bundle.getSerializable("officeSetupUnit");
        amcId = bundle.getString("officeSetupId");
        officeSetupName = bundle.getString("officeSetupName");

        categoryOfficeRecycler = (RecyclerView) findViewById(R.id.categoryOfficeRecycler);
        officeAddress = (EditText) findViewById(R.id.officeAddress);
        officeBudget = (EditText) findViewById(R.id.officeBudget);
        descriptionOffice = (EditText) findViewById(R.id.descriptionOffice);
        btnOfficePost = (Button) findViewById(R.id.btnOfficePost);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        endDate = (TextView) findViewById(R.id.endDate);
        startDate = (TextView) findViewById(R.id.startDate);
        titleText = (TextView) findViewById(R.id.titleText);

        categoryOfficeRecycler.setHasFixedSize(true);
        categoryOfficeRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OfficeSetupPost.this);
        categoryOfficeRecycler.setLayoutManager(layoutManager);
        officeSetupPostAdapter = new OfficeSetupPostAdapter(listUnit, amc, OfficeSetupPost.this);
        categoryOfficeRecycler.setAdapter(officeSetupPostAdapter);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePickerHidePrevious(OfficeSetupPost.this, startDate);

            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePickerHidePrevious(OfficeSetupPost.this, endDate);
            }
        });
        titleText.setText(officeSetupName);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOfficePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonObjectmain = new JSONObject();
                jsonElements = new JsonArray();

                Log.e("beargesar", "" + getSelectedCheck.size() + " " + getSelectedSystem.size() + " " + getSelectedUnits.size());

                for (int i = 0; i < getSelectedCheck.size(); i++) {
                    if (getSelectedCheck.get(i).isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("servicesId", "" + getSelectedSystem.get(i));
                        jsonObject.addProperty("noOfSystems", "" + getSelectedUnits.get(i));
                        jsonElements.add(jsonObject);
                    }
                   // Log.e("gergertdss", "" + getSelectedSystem.get(i) + " " + getSelectedUnits.get(i));

                }

                int myNum = 0;

                try {
                    myNum = Integer.parseInt(officeBudget.getText().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                if (jsonElements == null || descriptionOffice.getText().toString().isEmpty()
                        || officeAddress.getText().toString().isEmpty() || startDate.getText().toString().isEmpty() || endDate.getText().toString().isEmpty() || btnOfficePost.getText().toString().isEmpty()) {
                    showToast(OfficeSetupPost.this, "All fields are mandatory");
                } else if (isOnline(true, OfficeSetupPost.this)) {
                    String getDateStart = getDateFromUtcPurchase(startDate.getText().toString().trim());
                    String getDateEnd = getDateFromUtcPurchase(endDate.getText().toString().trim());
                    sendAmcRequest(amcId, descriptionOffice.getText().toString().trim(), officeAddress.getText().toString().trim(),
                            getDateStart, getDateEnd, myNum);
                }
            }

        });
    }


    private void sendAmcRequest(String amcId, String description, String address, String startDate, String endDate, int budget) {

        final JsonObject requestBody = new JsonObject();
        requestBody.add("bidDetail", jsonElements);
        requestBody.addProperty("description", description);
        requestBody.addProperty("estiBudget", budget);
        requestBody.addProperty("address", "" + address);
        requestBody.addProperty("bidId", amcId);
        requestBody.addProperty("startDate", startDate);
        requestBody.addProperty("endDate", endDate);
        showProgress(OfficeSetupPost.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> successModelCall = apiInterface.sendOfficeSetupRequest(ACCESS_TOKEN, requestBody);
        successModelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(OfficeSetupPost.this, "" + response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OfficeSetupPost.this, "" + jsonObjError.getString("message"));
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
}