package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.SelectAddress;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ManageAddress extends AppCompatActivity implements View.OnClickListener {
    Button buttonNewAddress;
    RecyclerView recyclerManagedAdd;
    ImageView backImage;
    AddressesAdapter addressesAdapter;
    List<AddressesModel> addedList;
    double latitude, longitude;
    TextView userLocation;
    TextView userStreetName;
    ImageView closeDialog;
    Button saveAddressButton;
    JSONObject subObjectlist;
    JSONArray jsonArray = new JSONArray();
String ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);
        getUserDetailId(ManageAddress.this);
        addedList = new ArrayList<AddressesModel>();
        buttonNewAddress = (Button) findViewById(R.id.buttonNewAddress);
        backImage = (ImageView) findViewById(R.id.backImage);
        recyclerManagedAdd = (RecyclerView) findViewById(R.id.recyclerManagedAdd);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        buttonNewAddress.setOnClickListener(this);
        backImage.setOnClickListener(this);
        recyclerManagedAdd.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ManageAddress.this, LinearLayoutManager.VERTICAL, false);
        recyclerManagedAdd.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ManageAddress.this,
                layoutManager.getOrientation());
        recyclerManagedAdd.addItemDecoration(dividerItemDecoration);
          if (isOnline(true, ManageAddress.this)) {
              getAllAddresses();
        }
    }


    private void getAllAddresses() {
        showProgress(ManageAddress.this);
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
                    addressesAdapter = new AddressesAdapter(addedList, ManageAddress.this);
                    recyclerManagedAdd.setAdapter(addressesAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ManageAddress.this, "" + jsonObjError.getString("message"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                userLocation.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNewAddress) {
            try {
                final Dialog dialog = new Dialog(ManageAddress.this);
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
                                            .build(ManageAddress.this);
                            startActivityForResult(intent, 19);
                        } catch (Exception e) {
                            Log.e("Address", e.getStackTrace().toString());
                        }
                    }
                });
                saveAddressButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isOnline(true, ManageAddress.this)) {
                            if (userLocation.getText().toString().isEmpty())
                            {
                                userLocation.setError("Please enter your location");
                                userLocation.requestFocus();
                                showToast(ManageAddress.this,"Please Select Your Location");
                            }
                            else {
                                dialog.dismiss();
                                addNewAddress(userLocation.getText().toString().trim(), userStreetName.getText().toString().trim(), 1,500);
                            }


                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == backImage) {
            finish();
        }
    }

    private void addNewAddress(String location, String street, int getValues,int position) {
        showProgress(ManageAddress.this);
        HashMap hashMap2 = new HashMap();
        if (getValues == 1) {
            /*add*/
            JSONArray jsonArrayAdd=new JSONArray();
            for (int i = 0; i < addedList.size(); i++) {
                try {
                    JSONObject latlongAdd = new JSONObject();
                    JSONObject addressAdd = new JSONObject();
                    latlongAdd.put("lat", addedList.get(i).getLocation().getLat());
                    latlongAdd.put("lng", addedList.get(i).getLocation().getLng());
                    addressAdd.put("value", addedList.get(i).getValue());
                    addressAdd.put("location", latlongAdd);
                    addressAdd.put("street", addedList.get(i).getStreet());
                    jsonArrayAdd.put(addressAdd);
                } catch (JSONException e) {
                    Log.d("exception",e.toString());
                    e.printStackTrace();
                }
            }
            JSONObject latlongAdd1 = new JSONObject();
            JSONObject addressAdd1 = new JSONObject();
            try {

                latlongAdd1.put("lat", latitude);
                latlongAdd1.put("lng", longitude);
                addressAdd1.put("value", location);
                addressAdd1.put("location", latlongAdd1);
                addressAdd1.put("street", street);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArrayAdd.put(addressAdd1);
            hashMap2.put("addresses", jsonArrayAdd);

        } else  if (getValues == 2){
            /*edit*/
            JSONArray jsonArrayAdd=new JSONArray();
            for (int i = 0; i < addedList.size(); i++) {
                try {
                    JSONObject latlongAdd = new JSONObject();
                    JSONObject addressAdd = new JSONObject();
                    latlongAdd.put("lat", addedList.get(i).getLocation().getLat());
                    latlongAdd.put("lng", addedList.get(i).getLocation().getLng());
                    addressAdd.put("value", addedList.get(i).getValue());
                    addressAdd.put("location", latlongAdd);
                    addressAdd.put("street", addedList.get(i).getStreet());
                    jsonArrayAdd.put(addressAdd);
                } catch (JSONException e) {
                    Log.d("exception",e.toString());
                    e.printStackTrace();
                }
            }
            JSONObject latlongAdd1 = new JSONObject();
            JSONObject addressAdd1 = new JSONObject();
            try {

                latlongAdd1.put("lat", latitude);
                latlongAdd1.put("lng", longitude);
                addressAdd1.put("value", location);
                addressAdd1.put("location", latlongAdd1);
                addressAdd1.put("street", street);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArrayAdd.put(position,addressAdd1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hashMap2.put("addresses", jsonArrayAdd);

        }
        else if (getValues==3)
        {
            JSONArray jsonArrayDelete=new JSONArray();
            for (int i = 0; i < addedList.size(); i++) {
                try {
                    JSONObject latlongAdd = new JSONObject();
                    JSONObject addressAdd = new JSONObject();
                    latlongAdd.put("lat", addedList.get(i).getLocation().getLat());
                    latlongAdd.put("lng", addedList.get(i).getLocation().getLng());
                    addressAdd.put("value", addedList.get(i).getValue());
                    addressAdd.put("location", latlongAdd);
                    addressAdd.put("street", addedList.get(i).getStreet());
                    jsonArrayDelete.put(addressAdd);
                } catch (JSONException e) {
                    Log.d("exception",e.toString());
                    e.printStackTrace();
                }
            }
            ArrayList<Object> list = new ArrayList<>();
            int len = jsonArrayDelete.length();
            if (jsonArrayDelete != null) {
                for (int i=0;i<len;i++){
                    try {
                        list.add(jsonArrayDelete.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.remove(position);
            JSONArray jsArray = new JSONArray(list);
            hashMap2.put("addresses", jsArray);

        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> modelCall = apiInterface.manageAddresses(ACCESS_TOKEN, hashMap2);
        modelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    Log.d("response",response.toString());
                  /*  finish();
                    startActivity(new Intent(ManageAddress.this,ManageAddress.class));*/
                    getAllAddresses();

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(ManageAddress.this, "" + jsonObjError.getString("message"));
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
    class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {
        private Context context;
        List<AddressesModel> services;

        ImageView closeDialog;
        Button saveAddressButton;

        public AddressesAdapter(List<AddressesModel> services, Context context) {
            super();
            this.services = services;
            this.context = context;
        }

        @Override
        public AddressesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_managed_address, parent, false);
            ViewHolder viewHolder = new AddressesAdapter.ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(AddressesAdapter.ViewHolder holder, final int position) {
            final AddressesModel category = services.get(position);
            holder.addressAddress.setText(category.getStreet() + " " + category.getValue());
            //for (int i = 1; i < services.size() + 1; i++) {
            int i = position;
            i += 1;
            holder.addressTitle.setText("Address " + i);
            holder.addressDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //arrayListValue.remove(position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        jsonArray.remove(position);
                    }
                    addNewAddress("","", 3,position);

                }
            });

            holder.addressEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.activity_edit_add_address);
                        dialog.setCanceledOnTouchOutside(false);

                        userLocation = (TextView) dialog.findViewById(R.id.userLocation);
                        userStreetName = (TextView) dialog.findViewById(R.id.userStreetName);
                        closeDialog = (ImageView) dialog.findViewById(R.id.closeDialog);
                        saveAddressButton = (Button) dialog.findViewById(R.id.saveAddressButton);
                        userLocation.setText(category.getValue());
                        userStreetName.setText(category.getStreet());
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
                                // dialog.dismiss();
                                try {
                                    Intent intent =
                                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                                    .build(ManageAddress.this);
                                    startActivityForResult(intent, 19);
                                } catch (Exception e) {
                                    Log.e("Address", e.getStackTrace().toString());
                                }

                            }
                        });
                        saveAddressButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                addNewAddress(userLocation.getText().toString().trim(), userStreetName.getText().toString().trim(), 2,position);

                            }
                        });

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {

            return services.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView addressEdit, addressDelete;
            TextView addressTitle, addressAddress;

            public ViewHolder(View itemView) {
                super(itemView);

                addressEdit = (ImageView) itemView.findViewById(R.id.addressEdit);
                addressDelete = (ImageView) itemView.findViewById(R.id.addressDelete);
                addressTitle = (TextView) itemView.findViewById(R.id.addressTitle);
                addressAddress = (TextView) itemView.findViewById(R.id.addressAddress);


            }
        }
    }


}

