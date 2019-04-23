package com.fixpapa.ffixpapa.EngineerPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.EngineerPart.Adapter.PartAdapter;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class CreateBill extends AppCompatActivity {

    TextView serviceTotalValue, additionalPartValue, totalPriceValue;
    EditText additionalServiceCharges, discountCharges;
    ImageView signatureImage, crossImage;
    byte[] byteArray;
    Button generateButton;
    String jobId;
    int addAllValue = 0;
    int serviceTotal;
    int addServiceCost, discountText;
    int serviceAdpartCost, totalPrice, discount, totalPriceValueV;
    String addStr, scheduledStart, scheduledEnd, customerName, customerId, engineerId;
    List<AddPart> addParts;
    RecyclerView recyclerParts;
    PartAdapter partAdapter;
    private static int SPLASH_TIME_OUT = 500;
    int totalpriceLast = 0, serviceTotalLast = 0, additionalchargeLast = 0, discountLast = 0;

    String  ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        addParts = new ArrayList<>();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
          ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        assert bundle != null;

        jobId = bundle.getString("jobId");
        serviceTotal = bundle.getInt("serviceTotal");
        serviceAdpartCost = bundle.getInt("AdditionalPart");
        addServiceCost = bundle.getInt("additionalCharge");
        //addParts = (List<AddPart>) bundle.getSerializable("AdditionalPartDetail");
        scheduledStart = bundle.getString("scheduledStart");
        scheduledEnd = bundle.getString("scheduledEnd");
        customerId = bundle.getString("customerId");
        engineerId = bundle.getString("enginId");
        totalPrice = bundle.getInt("totalPrice");
        discount = bundle.getInt("discount");

        Log.e("dvsaaaaaaaae", "" + totalPrice + " " + addServiceCost);
        serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
        additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
        totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
        additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
        discountCharges = (EditText) findViewById(R.id.discountCharges);
        signatureImage = (ImageView) findViewById(R.id.signatureImage);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        generateButton = (Button) findViewById(R.id.generateButton);
        recyclerParts = (RecyclerView) findViewById(R.id.recyclerParts);
        additionalServiceCharges.addTextChangedListener(new CustomWatcher());
        discountCharges.addTextChangedListener(new CustomWatcher());


  /*  additionalServiceCharges.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.e("dfbfdbdfsvs",hasFocus+"");
            if (!hasFocus)
            {
                additionalServiceCharges.setCursorVisible(false);
            }
            else
            {
                additionalServiceCharges.setCursorVisible(true);
            }
        }
    });
        additionalServiceCharges.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("dfbfdbdfsvs",hasFocus+"");
                if (!hasFocus)
                {
                    additionalServiceCharges.setCursorVisible(false);
                }
                else
                {
                    additionalServiceCharges.setCursorVisible(true);
                }
            }
        });
*/

        signatureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateBill.this, DigitalSugnature.class);
                startActivityForResult(intent, 0);
            }
        });

        recyclerParts.setHasFixedSize(true);
        recyclerParts.setNestedScrollingEnabled(false);
        recyclerParts.setLayoutManager(new LinearLayoutManager(CreateBill.this));

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!serviceTotalValue.getText().toString().isEmpty()) {
                        serviceTotalLast = Integer.parseInt(serviceTotalValue.getText().toString().trim());
                    }
                    if (!totalPriceValue.getText().toString().isEmpty()) {
                        totalpriceLast = Integer.parseInt(totalPriceValue.getText().toString().trim());
                    }
                    if (!additionalServiceCharges.getText().toString().isEmpty()) {
                        additionalchargeLast = Integer.parseInt(additionalServiceCharges.getText().toString().trim());
                    }

                    if (!discountCharges.getText().toString().isEmpty()) {
                        discountLast = Integer.parseInt(discountCharges.getText().toString().trim());
                    }
                    //totalpriceLast = totalpriceLast + additionalchargeLast- discount;
                   // totalpriceLast = totalpriceLast ;
                    Log.e("ebheyert",""+totalpriceLast);
                } catch (NumberFormatException e) {
                }

                if (signatureImage.getDrawable() == null) {
                    showToast(CreateBill.this, "signature is must");
                } else if (totalpriceLast < 0) {
                    showToast(CreateBill.this, "Total price not valid");
                } else {
                    generateBill(jobId, serviceTotalLast, additionalchargeLast, totalpriceLast, discountLast);

                }
            }
        });


        getJobDetail(jobId);
        //  discountCharges.
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("vdsvsvxv", "" + newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
            Log.e("dfvklnfdlvbndf", "fd");
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
            Log.e("dfvklnfdlvbndf", "fd");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {
            if (data != null) {
                byteArray = data.getByteArrayExtra("imageC");
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                signatureImage.setImageBitmap(bitmap);

            } else {
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(CreateBill.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
       finish();
    }


    private void generateBill(String jobId, int budget, int addtionalCharge, int totall, int discount) {
        showProgress(CreateBill.this);

        JsonArray jsonArray = new JsonArray();

        JsonObject requestBody = null;

        if (addParts.size() != 0) {
            for (int i = 0; i < addParts.size(); i++) {
                requestBody = new JsonObject();
                requestBody.addProperty("partName", addParts.get(i).getPartName());
                requestBody.addProperty("partNumber", addParts.get(i).getPartNumber());
                requestBody.addProperty("partCost", addParts.get(i).getPartCost());
                jsonArray.add(requestBody);
            }
        }

        final JsonObject hashMap = new JsonObject();
        hashMap.addProperty("totalAmount", budget);
        hashMap.addProperty("addServiceCost", addtionalCharge);
        hashMap.addProperty("discount", discount);
        hashMap.add("addPart", jsonArray);
        hashMap.addProperty("total", totall);
        // Log.e("fdhvfdsdfsdv", "" + hashMap);
        MultipartBody.Part filePartmultipleImages = null;
        if (byteArray != null) {
            RequestBody requestBodywq = RequestBody.create(MediaType.parse("image/*"), byteArray);
            filePartmultipleImages = MultipartBody.Part.createFormData("custSign", "signature.jpg", requestBodywq);
        } else {
            RequestBody requestBodyqw = RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.ic_user));
            filePartmultipleImages = MultipartBody.Part.createFormData("custSign", String.valueOf(R.drawable.ic_user), requestBodyqw);
        }

        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(hashMap));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.generateBill(ACCESS_TOKEN, jobId, objdata, filePartmultipleImages);

        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                   /* int addiAmt=0;
                    for (int i=0;i>=response.body().getSuccess().getData().getBill().getAddPart().size();i++){
                        addiAmt+=response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                    }*/

                    Log.e("CreateBilll", "" + response.body().getSuccess());
                    Intent intent = new Intent(CreateBill.this, PaymentEngineerOffsite.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("serviceTotal", response.body().getSuccess().getData().getTotalPrice());
                    //bundle.putInt("AdditionalPart", addiAmt);
                    bundle.putInt("additionalCharge", response.body().getSuccess().getData().getBill().getAddServiceCost());
                    bundle.putSerializable("AdditionalPartDetail", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                    bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                    bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                    bundle.putString("status", response.body().getSuccess().getData().getStatus());
                    //bundle.putString("status", response.body().getSuccess().getData().getStatus());
                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getEndDate());
                    bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                    bundle.putString("customerId", response.body().getSuccess().getData().getCustomer().getId());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                    // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(CreateBill.this, "" + jsonObjError.getString("message"));
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

    public class CustomWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           /* serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
            additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
            totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
            additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
            discountCharges = (EditText) findViewById(R.id.discountCharges);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                        serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                        addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                        discountText = Integer.parseInt(discountCharges.getText().toString());
                        addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                        addAllValue = addAllValue - discountText;
                   *//* if (serviceTotal + serviceAdpartCost + addServiceCost < discountText) {
                        showToast(CreateBill.this, "please enter discount is less than total amount");
                    }*//*
                    } catch (NumberFormatException e) {
                    }
                    addStr = String.valueOf(addAllValue);

                    totalPriceValue.setText(addStr);
                }

            }, SPLASH_TIME_OUT);*/
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          /*  serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
            additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
            totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
            additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
            discountCharges = (EditText) findViewById(R.id.discountCharges);
            if (s == additionalServiceCharges.getEditableText()) {
                Log.e("gbdbdfb", "");

                try {
                    serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                    serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                    addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                    discountText = Integer.parseInt(discountCharges.getText().toString());
                    addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                    addAllValue = addAllValue - discountText;
                } catch (NumberFormatException e) {
                }
                addStr = String.valueOf(addAllValue);
                totalPriceValue.setText(addStr);

            } else if (s == discountCharges.getEditableText()) {
                try {
                    serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                    serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                    addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                    discountText = Integer.parseInt(discountCharges.getText().toString());
                    addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                    addAllValue = addAllValue - discountText;
                   *//* if (serviceTotal + serviceAdpartCost + addServiceCost < discountText) {
                        showToast(CreateBill.this, "please enter discount is less than total amount");
                    }*//*
                } catch (NumberFormatException e) {
                }
                addStr = String.valueOf(addAllValue);

                totalPriceValue.setText(addStr);
            }*/
           /* serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
            additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
            totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
            additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
            discountCharges = (EditText) findViewById(R.id.discountCharges);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                        serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                        addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                        discountText = Integer.parseInt(discountCharges.getText().toString());
                        addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                        addAllValue = addAllValue - discountText;
                   *//* if (serviceTotal + serviceAdpartCost + addServiceCost < discountText) {
                        showToast(CreateBill.this, "please enter discount is less than total amount");
                    }*//*
                    } catch (NumberFormatException e) {
                    }
                    addStr = String.valueOf(addAllValue);

                    totalPriceValue.setText(addStr);
                }

            }, SPLASH_TIME_OUT);*/
        }

        @Override
        public void afterTextChanged(Editable s) {
            serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
            additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
            totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
            additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
            discountCharges = (EditText) findViewById(R.id.discountCharges);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                        serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());



                        if (TextUtils.isEmpty(additionalServiceCharges.getText())){
                            addServiceCost = 0;
                        }else {
                            addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                        }
                        if (TextUtils.isEmpty(discountCharges.getText())){
                            discountText = 0;
                        }else {
                            discountText = Integer.parseInt(discountCharges.getText().toString());
                        }

                        addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                        addAllValue = addAllValue - discountText;
                   /* if (serviceTotal + serviceAdpartCost + addServiceCost < discountText) {
                        showToast(CreateBill.this, "please enter discount is less than total amount");
                    }*/
                    } catch (NumberFormatException e) {
                    }
                    addStr = String.valueOf(addAllValue);

                    totalPriceValue.setText(addStr);
                }

            }, SPLASH_TIME_OUT);
        }
    }


/*  additionalServiceCharges.addTextChangedListener(generalTextWatcher);
            discountCharges.addTextChangedListener(generalTextWatcher);*//*


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == additionalServiceCharges.getEditableText()) {
                Log.e("gbdbdfb", "");
                serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
                additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
                totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
                additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
                try {
                    serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                    serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                    addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                    discountText = Integer.parseInt(discountCharges.getText().toString());
                    addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                    addAllValue = addAllValue - discountText;
                } catch (NumberFormatException e) {
                }
                addStr = String.valueOf(addAllValue);
                totalPriceValue.setText(addStr);

            } else if (s == discountCharges.getEditableText()) {
                serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
                additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
                totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
                additionalServiceCharges = (EditText) findViewById(R.id.additionalServiceCharges);
                discountCharges = (EditText) findViewById(R.id.discountCharges);
                try {
                    serviceTotal = Integer.parseInt(serviceTotalValue.getText().toString());
                    serviceAdpartCost = Integer.parseInt(additionalPartValue.getText().toString());
                    addServiceCost = Integer.parseInt(additionalServiceCharges.getText().toString());
                    discountText = Integer.parseInt(discountCharges.getText().toString());
                    addAllValue = serviceTotal + serviceAdpartCost + addServiceCost;
                    addAllValue = addAllValue - discountText;
                    if (serviceTotal + serviceAdpartCost + addServiceCost < discountText) {
                        showToast(CreateBill.this, "please enter discount is less than total amount");
                    }
                } catch (NumberFormatException e) {
                }
                addStr = String.valueOf(addAllValue);

                totalPriceValue.setText(addStr);
            }

        }

    };
*/

    private void getJobDetail(String jobId) {
        showProgress(CreateBill.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, jobId);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                   /* totalPrice = totalPrice + serviceTotal;
                    serviceTotalValue.setText("" + response.body().getSuccess().getData().getTotalPrice());
                  // for (int i=0;i<)
                    additionalPartValue.setText("" + AdditionalPart);
                    additionalServiceCharges.setText("" + additionalCharge);
                    discountValue.setText("" + discount);
                    totalPriceValue.setText("" + totalPrice);*/

                    // serviceTotalValue.setText("" + serviceTotal);
                  /*  int countVla = 0;
                    for (int i = 0; i < addParts.size(); i++) {
                        countVla += addParts.get(i).getPartCost();
                    }*/
                    // additionalPartValue.setText("" + countVla);
                    // additionalServiceCharges.setText("" + additionalServiceCharges);
                    //  discountCharges.setText("" + discount);
                  /*  totalPriceValueV = countVla + serviceTotal;
                    totalPriceValue.setText("" + totalPriceValueV);
                   */

                    int totall = 0, discountValue = 0;
                    serviceTotalValue.setText("" + response.body().getSuccess().getData().getTotalPrice());

                    if (response.body().getSuccess().getData().getBill().getClientResponse().equals("done")) {
                        for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                            totall += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                             Log.e("fhfhfhfhfhfhfh", "" + response.body().getSuccess().getData().getBill().getTotalAmount());
                            addParts=response.body().getSuccess().getData().getBill().getAddPart();

                        }
                    } else if (response.body().getSuccess().getData().getBill().getClientResponse().equals("decline")
                            || response.body().getSuccess().getData().getBill().getClientResponse().equals("requested")) {

                        totall = 0;
                        addParts.clear();
                        Log.e("dsvsdvsdv", "" + response.body().getSuccess().getData().getBill().getTotalAmount());
                    }

                    partAdapter = new PartAdapter(addParts, CreateBill.this);
                    recyclerParts.setAdapter(partAdapter);

                    additionalPartValue.setText("" + totall);
                    if (response.body().getSuccess().getData().getBill().getAddServiceCost() != null) {
                        if (response.body().getSuccess().getData().getBill().getAddServiceCost()!=0) {
                            additionalServiceCharges.setText("" + response.body().getSuccess().getData().getBill().getAddServiceCost());
                        }
                    } else {
                        additionalServiceCharges.setText("");

                    }
                    int totalo = 0;
                    if (response.body() != null) {
                        totalo = response.body().getSuccess().getData().getTotalPrice() + totall + response.body().getSuccess().getData().getBill().getAddServiceCost();
                    }
                    if (response.body().getSuccess().getData().getBill().getDiscount() != null) {
                        if(response.body().getSuccess().getData().getBill().getDiscount()!=0) {
                            discountCharges.setText("" + response.body().getSuccess().getData().getBill().getDiscount());
                        }
                        totalo = totalo - response.body().getSuccess().getData().getBill().getDiscount();
                    }

                    totalPriceValue.setText("" + totalo);
                    //  Log.e("svgrrrrrrrrrrrrr",response.body().getSuccess().getData().getBill().getTotal()+"");


                    //showToast(OrderDetail.this, "" + response.body().getSuccess().getMsg().getReplyCode());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(CreateBill.this, "" + jsonObjError.getString("message"));
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
