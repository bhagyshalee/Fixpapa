package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Product;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
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

import static com.fixpapa.ffixpapa.Services.Rest.Config.productId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtcPurchase;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.setDatePickerHidePrevious;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class PurchasePost extends AppCompatActivity implements View.OnClickListener {

   // List<Brand> brandsList;
    List<Product> productList;
    String getMainCatName, getMainCatId;
    EditText titleText, modelNumberText, budgetText, deliveryText, configurationText, otherText;
    LinearLayout layoutBrand, layoutProduct;
    Button buttonPostPurchase;
    Spinner brandSpinner, productSpinner, numberUnitSpinner, modePaymentSpinner;
    TextView brandNameShow, dateText;
    String brandId = "";
    String getDate;
    ImageView backImage;
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_post);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        productList = (List<Product>) bundle.getSerializable("getProducts");
       // brandsList = (List<Brand>) bundle.getSerializable("getBrands");
        getMainCatName = bundle.getString("getMainCatName");
        getMainCatId = bundle.getString("getMainCatId");

        titleText = (EditText) findViewById(R.id.titleText);
        modelNumberText = (EditText) findViewById(R.id.modelNumberText);
        dateText = (TextView) findViewById(R.id.dateText);
        budgetText = (EditText) findViewById(R.id.budgetText);
        deliveryText = (EditText) findViewById(R.id.deliveryText);
        brandNameShow = (TextView) findViewById(R.id.brandNameShow);
        configurationText = (EditText) findViewById(R.id.configurationText);
        otherText = (EditText) findViewById(R.id.otherText);
        layoutBrand = (LinearLayout) findViewById(R.id.layoutBrand);
        layoutProduct = (LinearLayout) findViewById(R.id.layoutProduct);
        buttonPostPurchase = (Button) findViewById(R.id.buttonPostPurchase);
        brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
        productSpinner = (Spinner) findViewById(R.id.productSpinner);
        numberUnitSpinner = (Spinner) findViewById(R.id.numberUnitSpinner);
        modePaymentSpinner = (Spinner) findViewById(R.id.modePaymentSpinner);
        backImage = (ImageView) findViewById(R.id.backImage);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

       /* if (brandsList!=null) {
            for (int j = 0; j < brandsList.size(); j++) {
               Log.e("dbhetryert",""+brandsList.get(j).getName());
            }
        }*/
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        brandNameShow.setText(getMainCatName);
        buttonPostPurchase.setOnClickListener(this);
        dateText.setOnClickListener(this);

        if (productList != null && productList.size()!=1) {
               List<String> productListLocal = new ArrayList<>();

                for (int i = 0; i < productList.size(); i++) {
                    productListLocal.add(productList.get(i).getName());
                }
                final List<String> plantsList = new ArrayList<>(productListLocal);
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PurchasePost.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return true;
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
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                productSpinner.setAdapter(spinnerArrayAdapter);

        } else {
            layoutProduct.setVisibility(View.GONE);
        }

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (productList != null) {
                    List<String> brandListLocal = new ArrayList<>();

                    //for (int i = 0; i < productList.size(); i++) {
                   // if (productList.get(0).getBrand() != null) {
                    if (productList.get(productSpinner.getSelectedItemPosition()).getBrand()!=null) {
                        layoutBrand.setVisibility(View.VISIBLE);
                        for (int j = 0; j < productList.get(productSpinner.getSelectedItemPosition()).getBrand().size(); j++) {
                            brandListLocal.add(productList.get(productSpinner.getSelectedItemPosition()).getBrand().get(j).getName());
                            //   }
                            //  }
                        }
                    }
                    else
                    {
                        layoutBrand.setVisibility(View.GONE);
                    }
                    final List<String> plantsList = new ArrayList<>(brandListLocal);
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PurchasePost.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return true;
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
                                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                    brandSpinner.setAdapter(spinnerArrayAdapter);

                }
                else {
                    layoutBrand.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (productList != null) {
                List<String> brandListLocal = new ArrayList<>();

                //for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(0).getBrand() != null) {
                        layoutBrand.setVisibility(View.VISIBLE);
                        for (int j = 0; j < productList.get(0).getBrand().size(); j++) {
                            brandListLocal.add(productList.get(0).getBrand().get(j).getName());
                        }
                  //  }
                }else
                    {
                        layoutBrand.setVisibility(View.GONE);
                    }
                final List<String> plantsList = new ArrayList<>(brandListLocal);
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PurchasePost.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return true;
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
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                brandSpinner.setAdapter(spinnerArrayAdapter);

        }
        else {
            layoutBrand.setVisibility(View.GONE);
        }


        String[] plants = new String[]{
                "Select",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"
        };

        String[] mode = new String[]{
                "Select",
                "Bank Transfer", "By Card", "By Cash"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PurchasePost.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
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
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        numberUnitSpinner.setAdapter(spinnerArrayAdapter);

        final List<String> modePayment = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerPayment = new ArrayAdapter<String>(PurchasePost.this, R.layout.design_spinner, R.id.textSpinner, mode) {
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
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerPayment.setDropDownViewResource(R.layout.design_spinner);
        modePaymentSpinner.setAdapter(spinnerPayment);


    }

    /*
         void setDatePicker(final Context context, final TextView et) {
            int mYear;
            int mMonth;
            int mDay;
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            et.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            getDate=et.getText().toString().trim();
                            setTime(context, et);

                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
            }
    */
/*
    void  setTime(Context context, final TextView textView)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setText(getDate+"  "+ selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
*/
    @Override
    public void onClick(View v) {
        if (v == dateText) {
            //setDatePicker(PurchasePost.this,dateText);
            setDatePickerHidePrevious(PurchasePost.this, dateText);

        }
        if (v == buttonPostPurchase) {
            if (productSpinner.getSelectedItemPosition() != -1) {
                productId = productList.get(productSpinner.getSelectedItemPosition()).getId();

            }

            if (brandSpinner.getSelectedItemPosition() != -1) {
                if (productList.size()==1)
                {
                    brandId = productList.get(0).getBrand().get(brandSpinner.getSelectedItemPosition()).getId();

                }
                else  {
                    brandId = productList.get(productSpinner.getSelectedItemPosition()).getBrand().get(brandSpinner.getSelectedItemPosition()).getId();
                }
            }
            if (numberUnitSpinner.getSelectedItem().toString().trim().equals("Select")) {
                showToast(PurchasePost.this, "Please select No. of Units");
            } else if (modePaymentSpinner.getSelectedItem().toString().trim().equals("Select")) {
                showToast(PurchasePost.this, "Please select Mode Of Payment");

            } else if (modelNumberText.getText().toString().trim().isEmpty()) {
                modelNumberText.setError("Please Enter Model No.");
                modelNumberText.requestFocus();
            } else if (dateText.getText().toString().trim().isEmpty()) {
                dateText.setError("Please Enter Date");
                dateText.requestFocus();
            } else if (budgetText.getText().toString().trim().isEmpty()) {
                budgetText.setError("Please Enter Budget");
                budgetText.requestFocus();
            } else if (deliveryText.getText().toString().trim().isEmpty()) {
                deliveryText.setError("Please Enter Delivery Address");
                deliveryText.requestFocus();
            } else if (configurationText.getText().toString().trim().isEmpty()) {
                configurationText.setError(getResources().getString(R.string.required_empty_error));
                configurationText.requestFocus();
            } else if (isOnline(true, PurchasePost.this)) {
                int noOfUnit = Integer.parseInt(numberUnitSpinner.getSelectedItem().toString());

                String getDateFormate = getDateFromUtcPurchase(dateText.getText().toString().trim());
                Log.e("dbdref", getDateFormate + "");
                postPurchase(productId, brandId, otherText.getText().toString().trim(), configurationText.getText().toString().trim(), deliveryText.getText().toString().trim(),
                        Integer.parseInt(budgetText.getText().toString().trim()), getDateFormate, modelNumberText.getText().toString().trim(),
                        titleText.getText().toString().trim(), getMainCatId, noOfUnit,
                        modePaymentSpinner.getSelectedItem().toString());
            }
        }
    }

    private void postPurchase(String productId, String brandId, String otherT, String configurationT, String deliveryAdd, int budget, String date, String modelNo, String title, String purchaseId, int noUnits, String modePayment) {
        showProgress(PurchasePost.this);

        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("newpurchaseId", purchaseId);
        requestBody.addProperty("productId", productId);
        requestBody.addProperty("brandId", brandId);
        requestBody.addProperty("purchaseDate", date);
        requestBody.addProperty("modelNumber", modelNo);
        requestBody.addProperty("noOfUnits", noUnits);
        requestBody.addProperty("configuration", configurationT);
        requestBody.addProperty("other", otherT);
        requestBody.addProperty("priceBudget", budget);
        requestBody.addProperty("deliveryAdd", deliveryAdd);
        requestBody.addProperty("modeOfPayment", modePayment);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> modelCall = apiInterface.sendPurchaseRequest(ACCESS_TOKEN, requestBody);

        modelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(PurchasePost.this, response.body().getSuccess().getMsg().getReplyMessage() + "");
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PurchasePost.this, "" + jsonObjError.getString("message"));
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
                Log.e("vvxvxvxv", requestBody + "");
            }
        });

    }
}
