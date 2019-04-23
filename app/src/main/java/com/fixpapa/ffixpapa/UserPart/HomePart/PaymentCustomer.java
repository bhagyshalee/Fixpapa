package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.setDatePickerIn;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class PaymentCustomer extends AppCompatActivity {
String ACCESS_TOKEN,USER_EMAIL,USER_ID,USER_MOBILE_NO;
    String jobId, custId, enginId, statusPayment, typePayment, modeOfPayment = " ", customerName, email, mobile,status,
            orderId, scheduledStart, scheduleEnd, modePayment;
    int serviceTotal, AdditionalPart, additionalCharge, totalPrice, discount;
    TextView serviceTotalValue, additionalPartValue, additionalServiceCharges, serviceTaxValue, totalPriceValue, totalAllAmount,showChequeTitle;
    Button payButton;
    RadioGroup paymentTypeGroup;
    RadioButton typeRadioButton;
    ImageView crossImage;
    Spinner selectPaymentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_customer);
        Bundle bundle = getIntent().getExtras();
        getUserDetailId(PaymentCustomer.this);

        if (bundle != null) {
            serviceTotal = bundle.getInt("serviceTotal");
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
            customerName = bundle.getString("customerName");
            status = bundle.getString("status");
            modePayment = bundle.getString("modePayment");

        }
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
         ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        USER_EMAIL = sharedPref.getString("USER_EMAIL","");
         USER_ID = sharedPref.getString("USER_ID","");
         USER_MOBILE_NO = sharedPref.getString("USER_MOBILE_NO","");

        Log.e("vnjkdfvjk", "" + modePayment);
        serviceTotalValue = (TextView) findViewById(R.id.serviceTotalValue);
        additionalPartValue = (TextView) findViewById(R.id.additionalPartValue);
        additionalServiceCharges = (TextView) findViewById(R.id.additionalServiceCharges);
        serviceTaxValue = (TextView) findViewById(R.id.serviceTaxValue);
        totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
        totalAllAmount = (TextView) findViewById(R.id.totalAllAmount);
        showChequeTitle = (TextView) findViewById(R.id.showChequeTitle);
        payButton = (Button) findViewById(R.id.payButton);
        paymentTypeGroup = (RadioGroup) findViewById(R.id.paymentTypeGroup);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        selectPaymentMode = (Spinner) findViewById(R.id.selectPaymentMode);

        serviceTotalValue.setText("" + serviceTotal);
        additionalPartValue.setText("" + AdditionalPart);
        additionalServiceCharges.setText("" + additionalCharge);
        serviceTaxValue.setText("" + discount);
        totalPriceValue.setText("" + totalPrice);
        totalAllAmount.setText("Rs. : " + totalPrice);

        getJobDetail(jobId);

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] plants = new String[]{
                "Select Payment Mode",
                "cash",
                "online",
                "cheque"
        };
        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PaymentCustomer.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
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
                    tv.setTextSize(15);
                    tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                    tv.setPadding(15, 15, 15, 15);
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setPadding(15, 15, 15, 15);
                    tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        selectPaymentMode.setAdapter(spinnerArrayAdapter);

        selectPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("fgbgbrtsvsdvsv",""+view+" "+position);
                if (position==3)
                {
                    showChequeTitle.setVisibility(View.VISIBLE);
                }
                else
                {
                    showChequeTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (status!=null) {
            if (status.equals("paymentDone")) {
                payButton.setText("Paid");
                Log.e("fgbgbrtsv", "" + modePayment);
                if (modePayment.equals("cheque")) {

                    //selectPaymentMode.setSelection(1);
                    selectPaymentMode.setSelection(spinnerArrayAdapter.getPosition("cheque"));
                } else if (modePayment.equals("cash")) {
                    Log.e("fgbgbrtsvre", "" + status);
                   //selectPaymentMode.setSelection(2);
                    selectPaymentMode.setSelection(spinnerArrayAdapter.getPosition("cash"));
                } else if (modePayment.equals("online")) {
                    Log.e("fgbgbrtsvred", "" + status);
                    //selectPaymentMode.setSelection(3);
                    selectPaymentMode.setSelection(spinnerArrayAdapter.getPosition("online"));
                }
                selectPaymentMode.setEnabled(false);
            } else {
                payButton.setText("Pay");
            }

        }

/*
        paymentTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = paymentTypeGroup.getCheckedRadioButtonId();
                typeRadioButton = (RadioButton) findViewById(selectedId);

                typePayment = typeRadioButton.getText().toString().trim();
                if (typePayment.equals("Cash Payment")) {
                    modeOfPayment = "Cash Payment";
                }
                if (typePayment.equals("Online Payment")) {
                    modeOfPayment = "Online Payment";
                }

            }
        });
*/
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payButton.getText().equals("Pay")) {

                    modeOfPayment = selectPaymentMode.getSelectedItem().toString();
                    if (modeOfPayment.equals("cash")) {
                        cashPayment();
                    } else if (modeOfPayment.equals("online")) {
                        Intent intent1 = new Intent(PaymentCustomer.this, ShowPaytmScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", orderId);
                        bundle.putString("custmId", USER_ID);
                        bundle.putString("email", USER_EMAIL);
                        bundle.putString("mobile", USER_MOBILE_NO);
                        bundle.putInt("totalPrice", totalPrice);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        finish();
                        // paytmPayment();
                    } else if (modeOfPayment.equals("cheque")) {
                        showCheckPopup();
                    } else if (modeOfPayment.equals("Select Payment Mode")) {
                        showToast(PaymentCustomer.this, "Please select payment Mode");
                    }
                }else
                {
                    showToast(PaymentCustomer.this, "you have already done");
                }
            }
        });
    }

    private void showCheckPopup() {
        try {
            final Dialog dialog = new Dialog(PaymentCustomer.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.design_check_add);
            dialog.setCanceledOnTouchOutside(false);
            ImageView crossDialog;
            final EditText bankNumber;
            final TextView bankDate;
            final Spinner selectBankName;
            final Button buttonSubmit;
            crossDialog = (ImageView) dialog.findViewById(R.id.crossDialog);
            selectBankName = (Spinner) dialog.findViewById(R.id.selectBankName);
            bankNumber = (EditText) dialog.findViewById(R.id.bankNumber);
            bankDate = (TextView) dialog.findViewById(R.id.bankDate);
            buttonSubmit = (Button) dialog.findViewById(R.id.buttonSubmit);
            dialog.show();

            bankDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDatePickerIn(PaymentCustomer.this, bankDate);
                }
            });
            String[] plants = new String[]{
                    "Select Bank Name",
                    "Allahabad Bank", "Andhra Bank", "Axis Bank", "Bank of Bahrain and Kuwait", "Bank of Baroda - Corporate Banking",
                    "Bank of Baroda - Retail Banking", "Bank of India", "Bank of Maharashtra", "Canara Bank", "Central Bank of India",
                    "City Union Bank", "Corporation Bank", "Deutsche Bank", "Development Credit Bank", "Dhanlaxmi Bank",
                    "Federal Bank", "HDFC Bank",
                    "ICICI Bank", "IDBI Bank", "Indian Bank", "Indian Overseas Bank", "IndusInd Bank", "ING Vysya Bank",
                    "Jammu and Kashmir Bank", "Karnataka Bank Ltd", "Karnataka Bank Ltd", "Karur Vysya Bank", "Kotak Bank",
                    "Laxmi Vilas Bank", "Oriental Bank of Commerce", "Punjab National Bank - Corporate Banking",
                    "Punjab National Bank - Retail Banking", "Punjab & Sind Bank", "Shamrao Vitthal Co-operative Bank", "South Indian Bank",
                    "State Bank of Bikaner & Jaipur", "State Bank of Hyderabad", "State Bank of India", "State Bank of Mysore",
                    "State Bank of Patiala", "State Bank of Travancore", "Syndicate Bank", "Tamilnad Mercantile Bank Ltd.",
                    "UCO Bank", "Union Bank of India", "United Bank of India",
            };
            final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PaymentCustomer.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
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
                        tv.setTextSize(10);
                        tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                        tv.setPadding(15, 15, 15, 15);
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setPadding(15, 15, 15, 15);
                        tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                    }
                    return view;
                }
            };
            spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
            selectBankName.setAdapter(spinnerArrayAdapter);

            crossDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       String BankName = selectBankName.getSelectedItem().toString();
                        if (BankName.equals("Select Bank Name")) {
                            showToast(PaymentCustomer.this, "Please select bank name");
                        } else if (bankNumber.getText().toString().isEmpty()) {
                            bankNumber.setError(getResources().getString(R.string.required_empty_error));
                            bankNumber.requestFocus();
                        } else if (bankDate.getText().toString().isEmpty()) {
                            showToast(PaymentCustomer.this, "Please select Date");
                        } else {
                            dialog.dismiss();
                            submitBankDetail(orderId, BankName, bankNumber.getText().toString(), bankDate.getText().toString());
                        }

                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void submitBankDetail(String orderId, String bankName, String bankNumber, String bankDate) {
        showProgress(PaymentCustomer.this);
        HashMap hashMap = new HashMap();
        hashMap.put("orderId", orderId);
        hashMap.put("bankName", bankName);
        hashMap.put("chequeNumber", bankNumber);
        hashMap.put("chequeDate",bankDate);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.submitBankDetail(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    finish();
                    showToast(PaymentCustomer.this, response.body().getSuccess().getMsg().getReplyMessage() + "");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentCustomer.this, "" + jsonObjError.getString("message"));
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
                Log.e("fdnjlfjd",t+"");
            }
        });
    }

    private void cashPayment() {
        showProgress(PaymentCustomer.this);
        HashMap hashMap = new HashMap();
        hashMap.put("orderId", "" + orderId);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> smallSucessCall = apiInterface.cashPayment(ACCESS_TOKEN, hashMap);
        smallSucessCall.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(PaymentCustomer.this, response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentCustomer.this, "" + jsonObjError.getString("message"));
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

/*
    private void paytmPayment() {
        showProgress(PaymentCustomer.this);
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("MID", "FIXPAP78716680122755");
        requestBody.addProperty("ORDER_ID", orderId);
        requestBody.addProperty("CUST_ID", customerId);
        requestBody.addProperty("INDUSTRY_TYPE_ID", "Retail");
        requestBody.addProperty("CHANNEL_ID", "WEB");
        requestBody.addProperty("TXN_AMOUNT", "1");
        requestBody.addProperty("WEBSITE", "WEBSTAGING");
        requestBody.addProperty("EMAIL", email);
        requestBody.addProperty("MOBILE_NO", mobile);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.callPaytm(requestBody);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(PaymentCustomer.this, "" + response.body().getSuccess().getMsg().getReplyCode());
                    Intent intent1 = new Intent(PaymentCustomer.this, ShowPaytmScreen.class);
                    //  Bundle bundle = new Bundle();
                    //  bundle.putString("loadUrl", "http://139.59.71.150:3008/api/Transactions/payment?MID=FIXPAP78716680122755&ORDER_ID=FP-rJsOu75G7&INDUSTRY_TYPE_ID=Retail&CHANNEL_ID=WEB&TXN_AMOUNT=1&WEBSITE=WEBSTAGING&EMAIL=fpcust9%40mailinator.com&MOBILE_NO=914363636463");
                    //intent.putExtra("loadUrl","http://139.59.71.150:3008/api/Transactions/payment?MID=FIXPAP78716680122755&ORDER_ID=FP-rJsOu75G7&INDUSTRY_TYPE_ID=Retail&CHANNEL_ID=WEB&TXN_AMOUNT=1&WEBSITE=WEBSTAGING&EMAIL=fpcust9%40mailinator.com&MOBILE_NO=914363636463");
                    //intent.putExtras(bundle);
                    startActivity(intent1);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentCustomer.this, "" + jsonObjError.getString("message"));
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
*/

    private void getJobDetail(String jobId) {
        showProgress(PaymentCustomer.this);
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

                    if (response.body().getSuccess().getData().getBill().getClientResponse().equals("done")) {
                        for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                            totall += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                        }
                    } else if (response.body().getSuccess().getData().getBill().getClientResponse().equals("decline")
                            || response.body().getSuccess().getData().getBill().getClientResponse().equals("requested")) {

                        totall = 0;
                        Log.e("dsvsdvsdv", "" + response.body().getSuccess().getData().getBill().getTotalAmount());
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
                        serviceTaxValue.setText("" + response.body().getSuccess().getData().getBill().getDiscount());
                        totalo = totalo - response.body().getSuccess().getData().getBill().getDiscount();
                    }

                    totalPriceValue.setText("" + totalo);
                    Log.e("svgrrrrrrrrrrrrr", response.body().getSuccess().getData().getBill().getTotal() + "");
                    //  if (response.body().getSuccess().getData().getStatus().equals("paymentDone")) {

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PaymentCustomer.this, "" + jsonObjError.getString("message"));
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
