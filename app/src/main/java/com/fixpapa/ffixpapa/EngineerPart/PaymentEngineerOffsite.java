package com.fixpapa.ffixpapa.EngineerPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.JobIndispute.SuccessI;
import com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class PaymentEngineerOffsite extends AppCompatActivity {

    int serviceTotal, AdditionalPart, additionalCharge, totalPrice, discount;
    TextView totalPriceValue, additionalServiceCharges, additionalPartValue, serviceTotalValue, discountValue;
    Button jobCompleteButton;
    ImageView crossImage;
    String jobId, custId, enginId, statusPayment, typePayment, modeOfPayment = "Cash Payment", customerName,
            orderId, scheduledStart, scheduleEnd, customerId, status = "",getPaymentStatus;
    LinearLayout layoutPaymentType;
    EditText commentData;
    RadioGroup paymentTypeGroup, paymentStatusGroup;
    RadioButton typeRadioButton, statusRadioButton, radioButtonComplete, radioButtonPending, RBCashPay, RBOnlinePay, RBChequePay;
    boolean isPaymentDone = true;
    ArrayList<AddPart> addPart;
String ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_engineer_offsite);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            serviceTotal = bundle.getInt("serviceTotal");
            addPart = (ArrayList<AddPart>) bundle.getSerializable("AdditionalPartDetail");
            AdditionalPart = bundle.getInt("AdditionalPart");
            additionalCharge = bundle.getInt("additionalCharge");
            discount = bundle.getInt("discount");
            totalPrice = bundle.getInt("totalPrice");
            jobId = bundle.getString("jobId");
            custId = bundle.getString("cusId");
            enginId = bundle.getString("enginId");
            orderId = bundle.getString("orderId");
            scheduledStart = bundle.getString("scheduledStart");
            scheduleEnd = bundle.getString("scheduledEnd");
            customerId = bundle.getString("customerId");
            customerName = bundle.getString("customerName");
            status = bundle.getString("status");
        }
        totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
        additionalServiceCharges = (TextView) findViewById(R.id.additionalServiceCharges);
        additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
        serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
        discountValue = (TextView) findViewById(R.id.discountValue);
        jobCompleteButton = (Button) findViewById(R.id.jobCompleteButton);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        layoutPaymentType = (LinearLayout) findViewById(R.id.layoutPaymentType);
        commentData = (EditText) findViewById(R.id.commentData);
        paymentStatusGroup = (RadioGroup) findViewById(R.id.paymentStatusGroup);
        paymentTypeGroup = (RadioGroup) findViewById(R.id.paymentTypeGroup);
        radioButtonComplete = (RadioButton) findViewById(R.id.radioButtonComplete);
        radioButtonPending = (RadioButton) findViewById(R.id.radioButtonPending);
        RBCashPay   = (RadioButton) findViewById(R.id.RBCashPay);
        RBOnlinePay = (RadioButton) findViewById(R.id.RBOnlinePay);
        RBChequePay = (RadioButton) findViewById(R.id.RBChequePay);

        // totalPrice = totalPrice + serviceTotal;
        serviceTotalValue.setText("" + serviceTotal);
        // for (int i=0;i<)
        additionalPartValue.setText("" + AdditionalPart);
        additionalServiceCharges.setText("" + additionalCharge);
        discountValue.setText("" + discount);
        totalPriceValue.setText("" + totalPrice);

        radioButtonComplete.setChecked(true);
        paymentStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = paymentStatusGroup.getCheckedRadioButtonId();
                statusRadioButton = (RadioButton) findViewById(selectedId);
                Log.e("fnjnjf", "" + enginId);
                statusPayment = statusRadioButton.getText().toString().trim();
                if (statusPayment.equals("Complete")) {
                    commentData.setVisibility(View.GONE);
                    layoutPaymentType.setVisibility(View.VISIBLE);
                    isPaymentDone = true;
                }
                if (statusPayment.equals("Pending")) {
                    commentData.setVisibility(View.VISIBLE);
                    layoutPaymentType.setVisibility(View.GONE);
                    isPaymentDone = false;
                }

            }
        });
        paymentTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = paymentTypeGroup.getCheckedRadioButtonId();
                typeRadioButton = (RadioButton) findViewById(selectedId);
                Log.e("fnjnjf", "" + enginId);
                typePayment = typeRadioButton.getText().toString().trim();
                if (typePayment.equals("Cash Payment")) {
                    modeOfPayment = "cash";
                }
                if (typePayment.equals("Online Payment")) {
                    modeOfPayment = "online";
                }
                if (typePayment.equals("cheque")) {
                    modeOfPayment = "cheque";
                }

            }
        });

        jobCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPaymentDone) {
                    if (getPaymentStatus.equals("paymentDone")) {
                        completeJob(jobId, custId, enginId, isPaymentDone, modeOfPayment, "");
                    }
                    else
                    {
                        showToast(PaymentEngineerOffsite.this,"Payment is not Transferred by customer");
                    }
                } else {

                   pendingJob(jobId, custId, enginId, isPaymentDone, modeOfPayment, commentData.getText().toString().trim());
                    //Log.e("fnjnjfhgmnghfg", jobId+" "+enginId+" "+custId+" "+isPaymentDone+" "+modeOfPayment+" "+commentData.getText().toString().trim());
                }

            }
        });
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(PaymentEngineerOffsite.this, EngineerHomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
*/
                finish();
            }
        });
        getJobDetail(jobId);
    }

    private void pendingJob(final String jobId, final String custId, final String enginId, boolean isPaymentDone, String modeOfPayment, String comment) {
        showProgress(PaymentEngineerOffsite.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestjobId", jobId);
        jsonObject.addProperty("customerId", custId);
        jsonObject.addProperty("engineerId", enginId);
        jsonObject.addProperty("isPaymentDone", isPaymentDone);
        jsonObject.addProperty("modeOfPayment", modeOfPayment);
        jsonObject.addProperty("comment", comment);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessI> call = apiInterface.completeJobPen(ACCESS_TOKEN, jsonObject);
        call.enqueue(new Callback<SuccessI>() {
            @Override
            public void onResponse(Call<SuccessI> call, Response<SuccessI> response) {
                close();
                if (response.isSuccessful()) {
                    Bundle bundle = new Bundle();
                    Log.e("PaymentEngineerOffsite", "" + response.body().getSuccess());
                    bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                    bundle.putInt("totalAmount", response.body().getSuccess().getData().getBill().getTotal());
                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getEndDate());
                    bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                    Intent intent = new Intent(PaymentEngineerOffsite.this, ReviewRating.class);
                    intent.putExtras(bundle);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentEngineerOffsite.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SuccessI> call, Throwable t) {
                close();
                Log.e("mbmvbmvmvm", "" + t);
            }
        });
    }

    private void completeJob(final String jobId, final String custId, final String enginId, boolean isPaymentDone, String modeOfPayment, String comment) {
        showProgress(PaymentEngineerOffsite.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestjobId", jobId);
        jsonObject.addProperty("customerId", custId);
        jsonObject.addProperty("engineerId", enginId);
        jsonObject.addProperty("isPaymentDone", isPaymentDone);
        jsonObject.addProperty("modeOfPayment", modeOfPayment);
        jsonObject.addProperty("comment", comment);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.completeJob(ACCESS_TOKEN, jsonObject);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    Log.e("PaymentEngineerOffsite", "" + response.body().getSuccess());
                    Bundle bundle = new Bundle();
                    bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                    bundle.putInt("totalAmount", response.body().getSuccess().getData().getBill().getTotal());
                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getEndDate());
                    bundle.putString("completedOn", response.body().getSuccess().getData().getCompleteJob().getCompletedAt());
                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineer().getId());
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());


                    Intent intent = new Intent(PaymentEngineerOffsite.this, ReviewRating.class);
                    intent.putExtras(bundle);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentEngineerOffsite.this, "" + jsonObjError.getString("message"));
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
                Log.e("mbmvbmvmvm", "" + t);
            }
        });
    }

    private void getJobDetail(String jobId) {
        showProgress(PaymentEngineerOffsite.this);
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
                    int totall = 0;
                    serviceTotalValue.setText("" + response.body().getSuccess().getData().getTotalPrice());
                    for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                        totall += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                    }
                    additionalPartValue.setText("" + totall);
                    if (response.body().getSuccess().getData().getBill().getAddServiceCost() != null) {
                        additionalServiceCharges.setText("" + response.body().getSuccess().getData().getBill().getAddServiceCost());
                    } else {
                        additionalServiceCharges.setText("" + 0);
                    }
                    int totalo = 0;
                    if (response.body() != null) {
                        totalo = response.body().getSuccess().getData().getTotalPrice() + totall + response.body().getSuccess().getData().getBill().getAddServiceCost();
                    }
                    if (response.body().getSuccess().getData().getBill().getDiscount() != null) {
                        discountValue.setText("" + response.body().getSuccess().getData().getBill().getDiscount());
                        totalo = totalo - response.body().getSuccess().getData().getBill().getDiscount();
                    }

                    totalPriceValue.setText("" + totalo);
                    Log.e("svgrrrrrrrrrrrrr", response.body().getSuccess().getData().getBill().getTotal() + "");
                    getPaymentStatus=response.body().getSuccess().getData().getStatus();
                    if (response.body().getSuccess().getData().getStatus().equals("paymentDone")) {

                        if (response.body().getSuccess().getData().getTransaction().getModeOfPayment().equals("cash")) {
                            RBCashPay.setChecked(true);
                            RBCashPay.setClickable(false);
                            RBChequePay.setClickable(false);
                            RBOnlinePay.setClickable(false);

                        } else if (response.body().getSuccess().getData().getTransaction().getModeOfPayment().equals("cheque")) {
                            RBChequePay.setChecked(true);
                            RBCashPay.setClickable(false);
                            RBChequePay.setClickable(false);
                            RBOnlinePay.setClickable(false);
                        } else if (response.body().getSuccess().getData().getTransaction().getModeOfPayment().equals("online")) {
                            RBOnlinePay.setChecked(true);
                            RBCashPay.setClickable(false);
                            RBChequePay.setClickable(false);
                            RBOnlinePay.setClickable(false);
                        }
                    } else {
                        radioButtonComplete.setChecked(true);
                    }
                    // }

                    //showToast(OrderDetail.this, "" + response.body().getSuccess().getMsg().getReplyCode());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentEngineerOffsite.this, "" + jsonObjError.getString("message"));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      /*  Intent intent = new Intent(PaymentEngineerOffsite.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
        finish();
    }
}
