package com.fixpapa.ffixpapa.EngineerPart;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.Adapter.AddpartAdapter;
import com.fixpapa.ffixpapa.EngineerPart.Adapter.JobStatusAdapter;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Offsitestatus;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OffSiteView extends AppCompatActivity implements View.OnClickListener {

    ImageView crossImage;
    TextView vendorViewJobDevice, vendorViewId, vendorViewExpected, productCondition, specifyDamage, ProductConfig,brandNameShow;
    Button deliverProductButton, viewJobButton, updateJobStatus, requestAdditionalPart;
    List<Problems> problemsList;
    String categoryName = "", brandName = "", productName = "", jobId, budget, dateTimeStart, dateTimeEnd, jobOrderId,
            customerName, timeDuration, customerId, jobDescription, dateVendor, scheduleStatus, engineerId;
    RecyclerView addPartRecycler, jobStatusRecycler;
    PickModel pickModel;
    List<AddPart> addPart;
    List<AddPart> addPartAdpterLL;
    private List<Offsitestatus> offsiteStatus;
    JobStatusAdapter jobStatusAdapter;
    AddpartAdapter addpartAdapter;
    AddressesModel addressesModel;
    List<String> listImage;
    int serviceTotal, addServiceCost, serviceAdpartCost;
    BillModel billModel;
    String  ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_site_view);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        offsiteStatus = new ArrayList<Offsitestatus>();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        addPartAdpterLL = new ArrayList<>();
        addPart = new ArrayList<>();
        if (bundle != null) {
            pickModel = (PickModel) bundle.getSerializable("pick");
            addPart = (List<AddPart>) bundle.getSerializable("AdditionalPartDetail");
            billModel = (BillModel) bundle.getSerializable("billModel");
            categoryName = bundle.getString("sendCategory");
            productName = bundle.getString("subCategory");
            brandName = bundle.getString("brand");
            jobId = bundle.getString("jobId");
            budget = bundle.getString("budget");
            customerName = bundle.getString("customerName");
            customerId = bundle.getString("customerId");
            dateTimeStart = bundle.getString("dateTimeStart");
            dateTimeEnd = bundle.getString("dateTimeEnd");
            jobOrderId = bundle.getString("jonOrderId");
            // timeDuration = bundle.getString("timeDuration");

            problemsList = (List<Problems>) bundle.getSerializable("sendIssues");
            addressesModel = (AddressesModel) bundle.getSerializable("address");
            listImage = (List<String>) bundle.getSerializable("jobImage");
            jobDescription = bundle.getString("jobDescription");
            dateVendor = bundle.getString("dateVendorAssign");
            scheduleStatus = bundle.getString("scheduleStatus");
            serviceTotal = bundle.getInt("serviceTotal");
            addServiceCost = bundle.getInt("serviceAdpartCost");
            serviceAdpartCost = bundle.getInt("addServiceCost");
            engineerId = bundle.getString("enginId");

        }

        crossImage = (ImageView) findViewById(R.id.crossImage);
        vendorViewJobDevice = (TextView) findViewById(R.id.vendorViewJobDevice);
        vendorViewId = (TextView) findViewById(R.id.vendorViewId);
        vendorViewExpected = (TextView) findViewById(R.id.vendorViewExpected);
        productCondition = (TextView) findViewById(R.id.productCondition);
        specifyDamage = (TextView) findViewById(R.id.specifyDamage);
        ProductConfig = (TextView) findViewById(R.id.ProductConfig);
        brandNameShow = (TextView) findViewById(R.id.brandNameShow);
        deliverProductButton = (Button) findViewById(R.id.deliverProductButton);
        viewJobButton = (Button) findViewById(R.id.viewJobButton);
        requestAdditionalPart = (Button) findViewById(R.id.requestAdditionalPart);
        updateJobStatus = (Button) findViewById(R.id.updateJobStatus);
        jobStatusRecycler = (RecyclerView) findViewById(R.id.jobStatusRecycler);
        addPartRecycler = (RecyclerView) findViewById(R.id.addPartRecycler);
        updateJobStatus.setOnClickListener(this);
        requestAdditionalPart.setOnClickListener(this);
        viewJobButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);
        deliverProductButton.setOnClickListener(this);

       /* if (categoryName.equals(productName)) {
            vendorViewJobDevice.setText(productName);
        } else if (productName.equals(brandName)) {
            vendorViewJobDevice.setText(brandName);
        }*/
        vendorViewId.setText(jobId);
        brandNameShow.setText(brandName);
        vendorViewExpected.setText("" + pickModel.getDuration());
        productCondition.setText(pickModel.getProCondition());
        specifyDamage.setText(pickModel.getDamage());
        ProductConfig.setText(pickModel.getDetails());
        vendorViewJobDevice.setText(categoryName);
        jobStatusRecycler.setHasFixedSize(true);
        jobStatusRecycler.setNestedScrollingEnabled(false);
        jobStatusRecycler.addItemDecoration(new DividerItemDecoration(OffSiteView.this, DividerItemDecoration.VERTICAL));
        jobStatusRecycler.setLayoutManager(new LinearLayoutManager(OffSiteView.this));
        offsiteStatus = pickModel.getOffsiteStatus();
        jobStatusAdapter = new JobStatusAdapter(offsiteStatus, OffSiteView.this);
        jobStatusRecycler.setAdapter(jobStatusAdapter);

        addPartRecycler.setHasFixedSize(true);
        addPartRecycler.setNestedScrollingEnabled(false);
        addPartRecycler.addItemDecoration(new DividerItemDecoration(OffSiteView.this, DividerItemDecoration.VERTICAL));
        addPartRecycler.setLayoutManager(new LinearLayoutManager(OffSiteView.this));
      /* if (addPart.size() == 0) {
            addPartRecycler.setVisibility(View.GONE);
        }*/
       if (jobId!=null) {
           if (isOnline(true,OffSiteView.this)) {
               getJobDetail(jobId);
           }
       }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(OffSiteView.this, EngineerHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
       finish();
    }

    @Override
    public void onClick(View v) {
        if (v == deliverProductButton) {
            updateDeliverStatus();
        }
        if (v == viewJobButton) {
            Intent intent = new Intent(OffSiteView.this, EngineerViewJobs.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("sendIssues", (ArrayList<Problems>) problemsList);
            bundle.putSerializable("sendCategory", categoryName);
            bundle.putSerializable("subCategory", productName);
            bundle.putSerializable("brand", brandName);
            bundle.putSerializable("address", (AddressesModel) addressesModel);
            bundle.putSerializable("jobImage", (ArrayList<String>) listImage);
            bundle.putString("jobId", jobId);
            bundle.putString("budget", budget);
            bundle.putString("dateTimeStart", dateTimeStart);
            bundle.putString("dateTimeEnd", dateTimeEnd);
            bundle.putString("dateVendorAssign", dateVendor);
            bundle.putString("jobDescription", jobDescription);
            bundle.putString("jonOrderId", jobOrderId);
            bundle.putString("scheduleStatus", scheduleStatus);
            bundle.putString("showButton", "no");
            bundle.putInt("serviceTotal", serviceTotal);
            bundle.putInt("serviceAdpartCost", serviceAdpartCost);
            bundle.putInt("addServiceCost", addServiceCost);
            bundle.putString("enginId", engineerId);

            intent.putExtras(bundle);
            startActivity(intent);

        }
        if (v == crossImage) {
           /* Intent intent = new Intent(OffSiteView.this, EngineerHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();
        }
        if (v == requestAdditionalPart) {
            try {
                final Dialog dialog = new Dialog(OffSiteView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popup_request);
                dialog.setCanceledOnTouchOutside(false);
                final EditText cost, partName, issueDetail;
                Button btnSend;
                ImageView crossImage;

                cost = (EditText) dialog.findViewById(R.id.cost);
                partName = (EditText) dialog.findViewById(R.id.partName);
                issueDetail = (EditText) dialog.findViewById(R.id.issueDetail);
                btnSend = (Button) dialog.findViewById(R.id.btnSend);
                crossImage = (ImageView) dialog.findViewById(R.id.crossImage);
                dialog.show();

                crossImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        if (issueDetail.getText().toString().isEmpty()) {
                            issueDetail.setError(getResources().getString(R.string.required_empty_error));
                            issueDetail.requestFocus();
                        } else if (partName.getText().toString().isEmpty()) {
                            partName.setError(getResources().getString(R.string.required_empty_error));
                            partName.requestFocus();
                        } else if (cost.getText().toString().isEmpty()) {
                            cost.setError(getResources().getString(R.string.required_empty_error));
                            cost.requestFocus();
                        } else {
                            dialog.dismiss();
                            addPartRequest(issueDetail.getText().toString().trim(), partName.getText().toString().trim(), Integer.parseInt(cost.getText().toString().trim()));
                        }
                    }
                });


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == updateJobStatus) {
            try {
                final Dialog dialog = new Dialog(OffSiteView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popup_update_status);
                dialog.setCanceledOnTouchOutside(false);
                final EditText statusDetail;
                Button btnUpdateStatus;
                ImageView crossImage;

                statusDetail = (EditText) dialog.findViewById(R.id.statusDetail);
                btnUpdateStatus = (Button) dialog.findViewById(R.id.btnUpdateStatus);
                crossImage = (ImageView) dialog.findViewById(R.id.crossImage);
                dialog.show();

                crossImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        if (statusDetail.getText().toString().isEmpty()) {
                            statusDetail.setError(getResources().getString(R.string.required_empty_error));
                            statusDetail.requestFocus();
                        } else {
                            dialog.dismiss();
                            changeJobStatus(statusDetail.getText().toString().trim());
                        }
                    }
                });


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    private void updateDeliverStatus() {
        showProgress(OffSiteView.this);
        HashMap jsonObject = new HashMap();

        jsonObject.put("status", "outForDelivery");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.updateDeliverStatus(ACCESS_TOKEN, jobId, jsonObject);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {

                    int addtionalChar = 0;
                    Intent intent = new Intent(OffSiteView.this, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                    bundle.putSerializable("sendCategory", categoryName);
                    bundle.putSerializable("subCategory", productName);
                    bundle.putSerializable("brand", brandName);
                    bundle.putInt("serviceTotal", response.body().getSuccess().getData().getBill().getTotalAmount());
                    for (int i = 0; i < response.body().getSuccess().getData().getBill().getAddPart().size(); i++) {
                        addtionalChar += response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                    }
                    bundle.putInt("AdditionalPart", addtionalChar);
                    bundle.putSerializable("AdditionalPartDetail", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                    bundle.putInt("additionalCharge", response.body().getSuccess().getData().getBill().getAddServiceCost());
                    bundle.putInt("discount", response.body().getSuccess().getData().getBill().getDiscount());
                    bundle.putInt("totalPrice", response.body().getSuccess().getData().getBill().getTotal());
                    bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                    bundle.putString("jobId", response.body().getSuccess().getData().getId());
                    bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                    bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                    bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                    bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                    bundle.putString("enginId", response.body().getSuccess().getData().getEngineerId());
                    bundle.putString("showButton", "yes");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OffSiteView.this, "" + jsonObjError.getString("message"));
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

    private void addPartRequest(String issueDetail, String partName, int cost) {
        showProgress(OffSiteView.this);

        JsonObject jsonObject = new JsonObject();

        JsonObject bfbf = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        jsonObject.addProperty("partName", issueDetail);
        jsonObject.addProperty("partNumber", partName);
        jsonObject.addProperty("partCost", cost);
        if (addPart.size() != 0) {
            for (int i = 0; i < addPart.size(); i++) {
                JsonObject jsonObjectT = new JsonObject();
                jsonObjectT.addProperty("partName", addPart.get(i).getPartName());
                jsonObjectT.addProperty("partNumber", addPart.get(i).getPartNumber());
                jsonObjectT.addProperty("partCost", addPart.get(i).getPartCost());
              //  Log.e("ytjjtjjuty", "" + jsonObjectT);
                jsonElements.add(jsonObjectT);
            }
        }
        else
        {
            //addPart.set(0,setPartCost(""));
        }
        jsonElements.add(jsonObject);

        bfbf.add("addPart", jsonElements);
        //bfbf.add("addPart", addPart);
        Log.e("uykykyuky", "" + bfbf);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.addpartRequest(ACCESS_TOKEN, jobId, bfbf);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    // addPart.clear();
                    //   addPartAddall.addAll(addPartAdpterLL);
                    //addPartAdpterLL=addPart;
                    addPart = response.body().getSuccess().getData().getBill().getAddPart();
                    billModel= response.body().getSuccess().getData().getBill();
                    //  addPartAdpterLL.addAll(addPartAdpterLL);

                    //  addPartAddall.addAll(addPart);
                    for (int i = 0; i < addPart.size(); i++) {
                        Log.e("cdklcdkl", "" + addPart.get(i).getPartName());
                    }
                    addpartAdapter = new AddpartAdapter(billModel, addPart, OffSiteView.this);
                    addPartRecycler.setAdapter(addpartAdapter);
                    addpartAdapter.notifyDataSetChanged();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OffSiteView.this, "" + jsonObjError.getString("message"));
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

    private void changeJobStatus(String status) {
        showProgress(OffSiteView.this);
        JsonObject jsonObject = new JsonObject();
        HashMap bcbcb = new HashMap();


        jsonObject.addProperty("text", status);
        bcbcb.put("offsiteStatus", jsonObject.toString());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.updateJobStatus(ACCESS_TOKEN, jobId, bcbcb);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    offsiteStatus = response.body().getSuccess().getData().getPick().getOffsiteStatus();
                    jobStatusAdapter = new JobStatusAdapter(offsiteStatus, OffSiteView.this);
                    jobStatusRecycler.setAdapter(jobStatusAdapter);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OffSiteView.this, "" + jsonObjError.getString("message"));
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
                Log.e("bcbcbcb", "" + t);
            }
        });
    }

    private void getJobDetail(String JOB_ID) {
        showProgress(OffSiteView.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, JOB_ID);
        call.enqueue(new Callback<SmallSucess>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                 close();
                if (response.isSuccessful()) {
                    addpartAdapter = new AddpartAdapter(response.body().getSuccess().getData().getBill(), response.body().getSuccess().getData().getBill().getAddPart(), OffSiteView.this);
                    addPartRecycler.setAdapter(addpartAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OffSiteView.this, "" + jsonObjError.getString("message"));
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
