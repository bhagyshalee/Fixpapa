package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.AmcPostAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AmcCategory;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.AmcPostAdapter.getCehckBox;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.AmcPostAdapter.getSelectedPosi;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.AmcPostAdapter.getSelectedStatus;

public class AmcPost extends Activity {
    RecyclerView categoryAmcRecycler;
    Spinner typesOfAmc;
    EditText amcAddress, descriptionAmc, amcBudget;
    Button btnAmcPost;
    List<AmcCategory> amc;
    List<String> listUnit;
    AmcPostAdapter amcPostAdapter;
    String amcId, amcName;
    JSONObject jsonObjectmain;
    JsonArray jsonElements;
    ImageView crossImage;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amc_post);
        getUserDetailId(AmcPost.this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        amc = (List<AmcCategory>) bundle.getSerializable("amcCat");
        listUnit = (List<String>) bundle.getSerializable("amcUnit");
        amcId = bundle.getString("amcId");
        amcName = bundle.getString("amcName");

        categoryAmcRecycler = (RecyclerView) findViewById(R.id.categoryAmcRecycler);
        typesOfAmc = (Spinner) findViewById(R.id.typesOfAmc);
        amcAddress = (EditText) findViewById(R.id.amcAddress);
        amcBudget = (EditText) findViewById(R.id.amcBudget);
        descriptionAmc = (EditText) findViewById(R.id.descriptionAmc);
        btnAmcPost = (Button) findViewById(R.id.btnAmcPost);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        titleText = (TextView) findViewById(R.id.titleText);

        categoryAmcRecycler.setHasFixedSize(true);
        categoryAmcRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AmcPost.this);
        categoryAmcRecycler.setLayoutManager(layoutManager);
        amcPostAdapter = new AmcPostAdapter(listUnit, amc, AmcPost.this);
        categoryAmcRecycler.setAdapter(amcPostAdapter);
        titleText.setText(amcName);

        String[] plants = new String[]{
                "Type Of AMC",
                "Comprehensive",
                "Non-comprehensive"
        };
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AmcPost.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        typesOfAmc.setAdapter(spinnerArrayAdapter);

        btnAmcPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonObjectmain = new JSONObject();
                jsonElements = new JsonArray();
                for (int i = 0; i < getCehckBox.size(); i++) {
                    if (getCehckBox.get(i).isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("categoryId", "" + getSelectedStatus.get(i));
                        jsonObject.addProperty("noOfUnits", "" + getSelectedPosi.get(i));
                        jsonElements.add(jsonObject);
                    }
                    Log.e("erhgergsdg", "" + getCehckBox.size());
                }

                int myNum = 0;

                try {
                    myNum = Integer.parseInt(amcBudget.getText().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                if (jsonElements == null || typesOfAmc.getSelectedItem().toString().isEmpty() || descriptionAmc.getText().toString().isEmpty()
                        || amcAddress.getText().toString().isEmpty() || amcBudget.getText().toString().isEmpty()) {
                    showToast(AmcPost.this, "All fields are mandatory");
                } else if (isOnline(true, AmcPost.this)) {
                    sendAmcRequest(amcId, typesOfAmc.getSelectedItem().toString().trim(),
                            descriptionAmc.getText().toString().trim(), amcAddress.getText().toString().trim(), myNum);
                }
            }

        });

    }

    private void sendAmcRequest(String amcId, String spinnerStr, String description, String address, int budget) {

        final JsonObject requestBody = new JsonObject();
        requestBody.add("amcDetail", jsonElements);
        requestBody.addProperty("typeOfAmc", spinnerStr);
        requestBody.addProperty("description", description);
        requestBody.addProperty("estiBudget", budget);
        requestBody.addProperty("address", address);
        requestBody.addProperty("amcId", amcId);
        showProgress(AmcPost.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> successModelCall = apiInterface.sendAmcRequest(ACCESS_TOKEN, requestBody);
        successModelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(AmcPost.this, "" + response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(AmcPost.this, "" + jsonObjError.getString("message"));
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
