package com.fixpapa.ffixpapa.UserPart;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.SelectAddressesAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.DateTime;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class SelectAddress extends AppCompatActivity implements View.OnClickListener {

    TextView addNewAddress, selectedAddress;
    RecyclerView addressRecycler;
    LinearLayout qualityLayout, guaranteeLayout, timeArrivalLayout, localSupportLayout;
    Button buttonNextAddress;
    List<AddressesModel> addedList;
    JSONArray jsonArray = new JSONArray();
    SelectAddressesAdapter addressesAdapter;
    JSONObject subObjectlist;
    TextView userLocation;
    TextView userStreetName;
    ImageView closeDialog;
    Button saveAddressButton;
    double latitude, longitude;
    ImageView backImage;
    ArrayList<String> issueList;
    int loginStatus=0;
    JSONObject innerAddressObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        Utility.getUserDetailId(SelectAddress.this);

        issueList = (ArrayList<String>) getIntent().getSerializableExtra("issueValueList");
        qualityLayout = (LinearLayout) findViewById(R.id.qualityLayout);
        guaranteeLayout = (LinearLayout) findViewById(R.id.guaranteeLayout);
        timeArrivalLayout = (LinearLayout) findViewById(R.id.timeArrivalLayout);
        localSupportLayout = (LinearLayout) findViewById(R.id.localSupportLayout);
        addNewAddress = (TextView) findViewById(R.id.addNewAddress);
        selectedAddress = (TextView) findViewById(R.id.selectedAddress);
        addressRecycler = (RecyclerView) findViewById(R.id.addressRecycler);
        buttonNextAddress = (Button) findViewById(R.id.buttonNextAddress);
        backImage = (ImageView) findViewById(R.id.backImage);

        addNewAddress.setOnClickListener(this);
        backImage.setOnClickListener(this);
        buttonNextAddress.setOnClickListener(this);
        addressRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SelectAddress.this, LinearLayoutManager.HORIZONTAL, false);
        addressRecycler.setLayoutManager(layoutManager);
       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SelectAddress.this,
                layoutManager.getOrientation());
        addressRecycler.addItemDecoration(dividerItemDecoration);*/
        if (isOnline(true, SelectAddress.this)) {
            loginStatus = isLogin(SelectAddress.this);
            if (loginStatus != 0) {
                getAllAddresses();
            } else {
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backImage) {
            finish();
        }
        if (v == qualityLayout) {

        }
        if (v == guaranteeLayout) {

        }
        if (v == timeArrivalLayout) {

        }
        if (v == localSupportLayout) {

        }
        if (v == buttonNextAddress) {
            if (selectedAddress.getText().toString().isEmpty())
            {
                showToast(SelectAddress.this,"Please Enter Address");
            }
            else {
                Intent intent = new Intent(SelectAddress.this, DateTime.class);
                intent.putExtra("issueValue", issueList);
                startActivity(intent);
            }
        }
        if (v == addNewAddress) {
            try {
                final Dialog dialog = new Dialog(SelectAddress.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.activity_edit_add_address);
                dialog.setCanceledOnTouchOutside(false);

                userLocation = (TextView) dialog.findViewById(R.id.userLocation);
                userStreetName = (TextView) dialog.findViewById(R.id.userStreetName);
                closeDialog = (ImageView) dialog.findViewById(R.id.closeDialog);
                saveAddressButton = (Button) dialog.findViewById(R.id.saveAddressButton);
                dialog.show();
                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                userLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                            .build(SelectAddress.this);
                            startActivityForResult(intent, 19);
                        } catch (Exception e) {
                            Log.e("Address", e.getStackTrace().toString());
                        }
                    }
                });
                saveAddressButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (userLocation.getText().toString().isEmpty())
                        {
                            userLocation.setError("Please enter your location");
                            userLocation.requestFocus();
                            showToast(SelectAddress.this,"Please Select Your Location");
                        }
                        else {
                            dialog.dismiss();
                            selectedAddress.setText(userStreetName.getText().toString().trim() + " " + userLocation.getText().toString());
                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                if (place!=null) {
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    userLocation.setText(place.getAddress());

                    JSONObject subObject = new JSONObject();
                    JSONObject latlong = new JSONObject();
                    innerAddressObject = new JSONObject();
                    try {
                        latlong.put("lat", latitude);
                        latlong.put("lng", longitude);
                        innerAddressObject.put("value", userLocation.getText().toString().trim());
                        innerAddressObject.put("location", latlong);
                        innerAddressObject.put("street", userStreetName.getText().toString().trim());
                        SharedPreferences.Editor editor = getSharedPreferences("SELECTED_ADDRESS", MODE_PRIVATE).edit();
                        editor.putString("jsonAdd", innerAddressObject.toString());
                        editor.apply();
                        //innerAddressObject=subObject.optString(String.valueOf(subObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                {
                    showToast(SelectAddress.this,"address not found");
                }

            }
        }else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            // TODO: Handle the error.
            Log.i("", status.getStatusMessage());
            showToast(SelectAddress.this,"address not found");

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
            showToast(SelectAddress.this,"address not found");
        }
    }

    private void getAllAddresses() {
        showProgress(SelectAddress.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> modelCall = apiInterface.getAddresses(ACCESS_TOKEN);
        modelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {

                    addedList = response.body().getSuccess().getData().getAddresses();

                    JSONObject latlonglist = new JSONObject();
                    subObjectlist = new JSONObject();

                    for (int i = 0; i < addedList.size(); i++) {
                        try {
                            latlonglist.put("lat", addedList.get(i).getLocation().getLat());
                            latlonglist.put("lng", addedList.get(i).getLocation().getLng());
                            subObjectlist.put("value", addedList.get(i).getValue());
                            subObjectlist.put("location", latlonglist);
                            subObjectlist.put("street", addedList.get(i).getStreet());
                            jsonArray.put(subObjectlist);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    addressesAdapter = new SelectAddressesAdapter(addedList, SelectAddress.this,selectedAddress);
                    addressRecycler.setAdapter(addressesAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(SelectAddress.this, "" + jsonObjError.getString("message"));
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
