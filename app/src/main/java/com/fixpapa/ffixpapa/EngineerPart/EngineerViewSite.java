package com.fixpapa.ffixpapa.EngineerPart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.Adapter.EngineerAdditionalPartAdapter;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.NewJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.EngineerPart.Adapter.EngineerAdditionalPartAdapter.additionalServices;
import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class EngineerViewSite extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo, signatureImage, signatureImageOffSite;
    RecyclerView vendorJobIssues, additionalPartsRecycler;
    Button generateBillButton, customerIssueButton, pickProductButton, customerIssueButton1;
    List<Problems> problemsList;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName, jobOrderId, customerName,
            timeDuration, dateVendor, scheduleStatus = "", setRadioString = "", customerId, engineerId, scheduleStart;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle, amountValue, additionalPartsValue;
    NewJobsFragment newJobsFragment;
    RadioGroup siteRadioGroup;
    RadioButton radioButtonOnSite;
    LinearLayout layoutOffSite, layoutOnSite, layoutButton, pickLayout;
    EditText additionalCharges, productCondition, expectedDays, specifyDamage, productDetail, discountField;
    List<AditionPart> listStr;
    EngineerAdditionalPartAdapter adapterAdditional;
    JSONObject addService;
    Bitmap bitmap;
    File digitalSignatureFile;
    byte[] byteArray;
    int additionalCost = 0, totalAmt = 0;
    int bud = 0, addcharge = 0, discount = 0;

    String LOGIN_TYPE, ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_view_jobs);

        newJobsFragment = new NewJobsFragment();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        listStr = new ArrayList<>();
        addService = new JSONObject();
        problemsList = (List<Problems>) bundle.getSerializable("sendIssues");
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");
        addressList = (AddressesModel) bundle.getSerializable("address");
        imageList = (List<String>) bundle.getSerializable("jobImage");
        jobId = bundle.getString("jobId");
        budget = bundle.getString("budget");
        dateTimeStart = bundle.getString("dateTimeStart");
        dateTimeEnd = bundle.getString("dateTimeEnd");
        jobDescription = bundle.getString("jobDescription");
        jobOrderId = bundle.getString("jonOrderId");
        customerName = bundle.getString("customerName");
        dateVendor = bundle.getString("dateVendorAssign");
        scheduleStatus = bundle.getString("scheduleStatus");
        customerId = bundle.getString("customerId");
        timeDuration = bundle.getString("timeDuration");
        engineerId = bundle.getString("enginId");

        crossImage = (ImageView) findViewById(R.id.crossImage);
        productImagesOne = (ImageView) findViewById(R.id.productImagesOne);
        productImagesTwo = (ImageView) findViewById(R.id.productImagesTwo);
        vendorViewJobDevice = (TextView) findViewById(R.id.vendorViewJobDevice);
        vendorViewProduct = (TextView) findViewById(R.id.vendorViewProduct);
        vendorViewBrand = (TextView) findViewById(R.id.vendorViewBrand);
        vendorViewDate = (TextView) findViewById(R.id.vendorViewDate);
        vendorViewTime = (TextView) findViewById(R.id.vendorViewTime);
        vendorViewBudget = (TextView) findViewById(R.id.vendorViewBudget);
        vendorViewAddress = (TextView) findViewById(R.id.vendorViewAddress);
        additionalPartsValue = (TextView) findViewById(R.id.additionalPartsValue);
        amountValue = (TextView) findViewById(R.id.amountValue);
        vendorViewJobDescription = (TextView) findViewById(R.id.vendorViewJobDescription);
        productTitle = (TextView) findViewById(R.id.productTitle);
        brandTitle = (TextView) findViewById(R.id.brandTitle);
        siteRadioGroup = (RadioGroup) findViewById(R.id.siteRadioGroup);
        radioButtonOnSite = (RadioButton) findViewById(R.id.radioButtonOnSite);
        layoutOffSite = (LinearLayout) findViewById(R.id.layoutOffSite);
        layoutOnSite = (LinearLayout) findViewById(R.id.layoutOnSite);
        layoutButton = (LinearLayout) findViewById(R.id.layoutButton);
        pickLayout = (LinearLayout) findViewById(R.id.pickLayout);
        signatureImage = (ImageView) findViewById(R.id.signatureImage);
        signatureImageOffSite = (ImageView) findViewById(R.id.signatureImageOffSite);
        additionalCharges = (EditText) findViewById(R.id.additionalCharges);
        additionalPartsRecycler = (RecyclerView) findViewById(R.id.additionalPartsRecycler);
        productCondition = (EditText) findViewById(R.id.productCondition);
        expectedDays = (EditText) findViewById(R.id.expectedDays);
        specifyDamage = (EditText) findViewById(R.id.specifyDamage);
        productDetail = (EditText) findViewById(R.id.productDetail);
        discountField = (EditText) findViewById(R.id.discountField);

        vendorJobIssues = (RecyclerView) findViewById(R.id.vendorJobIssues);
        generateBillButton = (Button) findViewById(R.id.generateBillButton);
        customerIssueButton = (Button) findViewById(R.id.customerIssueButton);
        customerIssueButton1 = (Button) findViewById(R.id.customerIssueButton1);
        pickProductButton = (Button) findViewById(R.id.pickProductButton);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE", "");

        vendorJobIssues.setHasFixedSize(true);
        additionalPartsRecycler.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        additionalPartsRecycler.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(EngineerViewSite.this));
        additionalPartsRecycler.setLayoutManager(new LinearLayoutManager(EngineerViewSite.this));

        amountValue.setText(budget + "/-");
        generateBillButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);
        pickProductButton.setOnClickListener(this);
        customerIssueButton.setOnClickListener(this);
        customerIssueButton1.setOnClickListener(this);
        signatureImage.setOnClickListener(this);
        signatureImageOffSite.setOnClickListener(this);
        additionalPartsValue.setOnClickListener(this);


        if (productName.equals(categoryName)) {
            vendorViewProduct.setVisibility(View.GONE);
            productTitle.setVisibility(View.GONE);
        }

        if (productName.equals(brandName)) {
            vendorViewBrand.setVisibility(View.GONE);
            brandTitle.setVisibility(View.GONE);
        }

        if (brandName == null) {
            vendorViewBrand.setVisibility(View.GONE);
            brandTitle.setVisibility(View.GONE);
        }

        vendorViewJobDevice.setText(categoryName);
        vendorViewProduct.setText(productName);

        vendorViewBudget.setText(budget);
        vendorViewJobDescription.setText(jobDescription);
        vendorViewBrand.setText(brandName);

        vendorViewAddress.setText(addressList.getStreet() + "" + addressList.getValue());

        vendorViewDate.setText(getDateFromUtc(dateVendor));

        vendorViewTime.setText(getTimeFromUtc(dateTimeStart) + " - " + getTimeFromUtc(dateTimeEnd));

        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {

                Picasso.with(EngineerViewSite.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(EngineerViewSite.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, EngineerViewSite.this);
        vendorJobIssues.setAdapter(issuesAdapter);


        siteRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().equals("Off Site")) {
                    setRadioString = "offsite";
                    layoutOffSite.setVisibility(View.VISIBLE);
                    layoutOnSite.setVisibility(View.GONE);
                    layoutButton.setVisibility(View.GONE);
                    pickLayout.setVisibility(View.VISIBLE);
                } else {
                    setRadioString = "onsite";
                    layoutOffSite.setVisibility(View.GONE);
                    layoutOnSite.setVisibility(View.VISIBLE);
                    layoutButton.setVisibility(View.VISIBLE);
                    pickLayout.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == customerIssueButton1) {
            try {
                final Dialog dialog = new Dialog(EngineerViewSite.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.design_customer_issue);
                dialog.setCanceledOnTouchOutside(false);

                final EditText editTextComment;
                Button buttonDone, buttonCancel;
                editTextComment = (EditText) dialog.findViewById(R.id.editTextComment);
                buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
                buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                dialog.show();
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextComment.getText().toString().isEmpty()) {
                            showToast(EngineerViewSite.this, "Please enter the reason");
                        } else {
                            dialog.dismiss();
                            cancelJob(jobId, editTextComment.getText().toString(), "");
                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == customerIssueButton) {
            try {
                final Dialog dialog = new Dialog(EngineerViewSite.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.design_customer_issue);
                dialog.setCanceledOnTouchOutside(false);

                final EditText editTextComment;
                Button buttonDone, buttonCancel;
                editTextComment = (EditText) dialog.findViewById(R.id.editTextComment);
                buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
                buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                dialog.show();
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextComment.getText().toString().isEmpty()) {
                            showToast(EngineerViewSite.this, "Please enter the reason");
                        } else {
                            dialog.dismiss();
                            cancelJob(jobId, editTextComment.getText().toString(), "");
                        }
                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == pickProductButton) {
            if (productCondition.getText().toString().isEmpty()) {
                productCondition.setError(getResources().getString(R.string.required_empty_error));
                productCondition.requestFocus();
            } else if (expectedDays.getText().toString().isEmpty()) {
                expectedDays.setError(getResources().getString(R.string.required_empty_error));
                expectedDays.requestFocus();
            } else if (specifyDamage.getText().toString().isEmpty()) {
                specifyDamage.setError(getResources().getString(R.string.required_empty_error));
                specifyDamage.requestFocus();
            } else if (productDetail.getText().toString().isEmpty()) {
                productDetail.setError(getResources().getString(R.string.required_empty_error));
                productDetail.requestFocus();
            } else {
                if (signatureImage.getDrawable() == null) {
                    showToast(EngineerViewSite.this, "signature is must");
                } else {
                    pickJob(productCondition.getText().toString(), specifyDamage.getText().toString(), productDetail.getText().toString()
                            , expectedDays.getText().toString());
                }

            }
        }
        if (v == generateBillButton) {

            try {
                if (budget != null) {
                    bud = Integer.parseInt(budget);
                }
                if (!additionalCharges.getText().toString().isEmpty()) {
                    addcharge = Integer.parseInt(additionalCharges.getText().toString());
                }
                if (!discountField.getText().toString().isEmpty()) {
                    discount = Integer.parseInt(discountField.getText().toString());
                }
                Log.e("fdbfdbrthrge", discountField.getText().toString());
                if (signatureImage.getDrawable() == null) {
                    showToast(EngineerViewSite.this, "signature is must");
                } else {
                    generateBill(jobId, bud, addcharge, discount);
                }
            } catch (NumberFormatException ex) { // handle your exception
            }

        }
        if (v == signatureImage) {
            Intent intent = new Intent(EngineerViewSite.this, DigitalSugnature.class);
            startActivityForResult(intent, 0);
        }
        if (v == additionalPartsValue) {
            try {
                final Dialog dialog = new Dialog(EngineerViewSite.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popup_additional_part);
                dialog.setCanceledOnTouchOutside(false);
                ImageView crossDialog;
                final EditText partName, partNumber, cost;
                Button buttonAdd;
                crossDialog = (ImageView) dialog.findViewById(R.id.crossDialog);
                partName = (EditText) dialog.findViewById(R.id.partName);
                partNumber = (EditText) dialog.findViewById(R.id.partNumber);
                cost = (EditText) dialog.findViewById(R.id.cost);
                buttonAdd = (Button) dialog.findViewById(R.id.buttonAdd);
                dialog.show();

                crossDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {

                        if (partName.getText().toString().isEmpty()) {
                            partName.setError(getResources().getString(R.string.required_empty_error));
                            partName.requestFocus();
                        } else if (partNumber.getText().toString().isEmpty()) {
                            partNumber.setError(getResources().getString(R.string.required_empty_error));
                            partNumber.requestFocus();
                        } else if (cost.getText().toString().isEmpty()) {
                            cost.setError(getResources().getString(R.string.required_empty_error));
                            cost.requestFocus();
                        } else if (isOnline(true, EngineerViewSite.this)) {
                            dialog.dismiss();
                            AditionPart aditionPart = new AditionPart();
                            aditionPart.setPartName(partName.getText().toString().trim());
                            aditionPart.setPartNumber(partNumber.getText().toString().trim());
                            aditionPart.setCost(cost.getText().toString().trim());
                            listStr.add(aditionPart);
                            adapterAdditional = new EngineerAdditionalPartAdapter(listStr, EngineerViewSite.this);
                            additionalPartsRecycler.setAdapter(adapterAdditional);
                            additionalPartsRecycler.setBackground(getResources().getDrawable(R.drawable.edittext_border));

                        }
                    }
                });


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == signatureImageOffSite) {
            Intent intent = new Intent(EngineerViewSite.this, DigitalSugnature.class);
            startActivityForResult(intent, 0);
        }
        if (v == crossImage) {
            Intent intent = new Intent(EngineerViewSite.this, EngineerHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void pickJob(String procon, String damage, String detail, String dura) {
        showProgress(EngineerViewSite.this);

        JSONObject hashMap = new JSONObject();
        try {
            hashMap.put("proCondition", procon);
            hashMap.put("damage", damage);
            hashMap.put("details", detail);
            hashMap.put("duration", dura);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        Call<SmallSucess> call = apiInterface.pickPro(ACCESS_TOKEN, jobId, objdata, filePartmultipleImages);

        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {

                    Intent intent = new Intent(EngineerViewSite.this, OffSiteView.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pick", (PickModel) response.body().getSuccess().getData().getPick());
                    bundle.putSerializable("AdditionalPartDetail", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                    bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                    bundle.putSerializable("billModel", (BillModel) response.body().getSuccess().getData().getBill());
                    bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                    bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                    bundle.putString("sendCategory", response.body().getSuccess().getData().getCategory().getName());
                    bundle.putString("subCategory", response.body().getSuccess().getData().getProduct().getName());
                    if (response.body().getSuccess().getData().getBrand() != null) {
                        bundle.putString("brand", response.body().getSuccess().getData().getBrand().getName());
                    }
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                    bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                    bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                    bundle.putString("timeDuration", String.valueOf(response.body().getSuccess().getData().getDuration()));
                    bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                    bundle.putInt("serviceTotal", response.body().getSuccess().getData().getTotalPrice());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                    for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                        bundle.putInt("serviceAdpartCost", response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost());
                    }
                    bundle.putInt("addServiceCost", response.body().getSuccess().getData().getBill().getAddServiceCost());

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();


                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewSite.this, "" + jsonObjError.getString("message"));
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


    private void generateBill(String jobId, int budget, int addtionalCharge, int discountAmt) {
        showProgress(EngineerViewSite.this);
        JsonArray jsonArray = new JsonArray();
        if (additionalServices != null) {

            for (int i = 0; i < additionalServices.size(); i++) {
                JsonObject requestBody = null;
                requestBody = new JsonObject();
                requestBody.addProperty("partName", additionalServices.get(i).getPartName());
                requestBody.addProperty("partNumber", additionalServices.get(i).getPartNumber());
                requestBody.addProperty("partCost", Integer.parseInt(additionalServices.get(i).getCost()));
                additionalCost += Integer.parseInt(additionalServices.get(i).getCost());
                jsonArray.add(requestBody);
            }
        }
        totalAmt = budget + addtionalCharge + additionalCost;
        totalAmt = totalAmt - discountAmt;
        final JsonObject hashMap = new JsonObject();
        hashMap.addProperty("totalAmount", budget);
        hashMap.addProperty("addServiceCost", addtionalCharge);
        hashMap.add("addPart", jsonArray);
        hashMap.addProperty("total", totalAmt);
        hashMap.addProperty("discount", discountAmt);

        Log.e("fgnfgnfgn", "" + hashMap.toString());

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
                    int addtionalChar = 0;
                    Intent intent = new Intent(EngineerViewSite.this, PaymentEngineerOffsite.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putInt("serviceTotal", response.body().getSuccess().getData().getTotalPrice());

                    for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                        addtionalChar += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                    }

                    bundle.putInt("AdditionalPart", addtionalChar);
                    bundle.putInt("additionalCharge", response.body().getSuccess().getData().getBill().getAddServiceCost());
                    bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                    bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                    bundle.putString("cusId", response.body().getSuccess().getData().getCustomerId());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                    bundle.putString("orderId", response.body().getSuccess().getData().getOrderId());
                    bundle.putString("status", response.body().getSuccess().getData().getStatus());
                    bundle.putString("scheduledStart", response.body().getSuccess().getData().getStartDate());
                    bundle.putString("scheduledEnd", response.body().getSuccess().getData().getEndDate());
                    bundle.putString("customerName", response.body().getSuccess().getData().getCustomer().getFullName());
                    bundle.putString("customerId", response.body().getSuccess().getData().getCustomer().getId());
                    if (response.body().getSuccess().getData().getStatus().equals("paymentDone")) {
                        bundle.putString("modePayment", response.body().getSuccess().getData().getTransaction().getModeOfPayment());
                    }
                    // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewSite.this, "" + jsonObjError.getString("message"));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {
            if (data != null) {
                byteArray = data.getByteArrayExtra("imageC");
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                signatureImage.setImageBitmap(bitmap);
                signatureImageOffSite.setImageBitmap(bitmap);

            } else {
            }
        }

    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
       // additionalServices.clear();
        Log.e("fgbnjnjlgb","rrere");
    }*/

    private void cancelJob(String jobId, final String reason, String comment) {
        showProgress(EngineerViewSite.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("reason", reason);
        hashMap.put("realm", LOGIN_TYPE);
        hashMap.put("comment", comment);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.cancelJobByVendor(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(EngineerViewSite.this, response.body().getSuccess().getMsg().getReplyMessage());
                    finish();
                    Intent intent = new Intent(EngineerViewSite.this, EngineerHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewSite.this, "" + jsonObjError.getString("message"));
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
        Intent intent = new Intent(EngineerViewSite.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}