package com.fixpapa.ffixpapa.UserPart;

import android.annotation.TargetApi;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.Adapter.AddpartAdapter;
import com.fixpapa.ffixpapa.EngineerPart.OffSiteView;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.PartRequestAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;


public class PartRequest extends AppCompatActivity {
    TextView deviceName, totalAmountValue;
    RecyclerView recyclerParts;
    Button approvalBtn,approvalNoBtn;
    String jobId,categoryName,productName,brandName;
    BillModel addPart;
    PartRequestAdapter adapter;
    int adpartcost=0;
    String  ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_request);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
          ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        final Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        jobId = bundle.getString("jobId");
        addPart = (BillModel) bundle.getSerializable("addPart");
        categoryName = bundle.getString("sendCategory");
        productName = bundle.getString("subCategory");
        brandName = bundle.getString("brand");

        deviceName = (TextView) findViewById(R.id.deviceName);
        totalAmountValue = (TextView) findViewById(R.id.totalAmountValue);
        recyclerParts = (RecyclerView) findViewById(R.id.recyclerParts);
        approvalBtn = (Button) findViewById(R.id.approvalBtn);
        approvalNoBtn = (Button) findViewById(R.id.approvalNoBtn);


        recyclerParts.setHasFixedSize(true);
        recyclerParts.setNestedScrollingEnabled(false);
        recyclerParts.addItemDecoration(new DividerItemDecoration(PartRequest.this, DividerItemDecoration.VERTICAL));
        recyclerParts.setLayoutManager(new LinearLayoutManager(PartRequest.this));

      //  if (categoryName.equals(productName)) {
            deviceName.setText(categoryName);
       // }
        Log.e("dfbdfbhtr",""+addPart.toString());

       /* if (productName.equals(brandName)) {
            deviceName.setText(productName);
        }*/
        getJobDetail(jobId);
        approvalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPartResponse("done");
            }
        });
        approvalNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPartResponse("decline");
            }
        });

    }

    private void getJobDetail(String JOB_ID) {
        showProgress(PartRequest.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, JOB_ID);
        call.enqueue(new Callback<SmallSucess>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {

                    adapter=new PartRequestAdapter(response.body().getSuccess().getData().getBill().getAddPart(),PartRequest.this);
                    recyclerParts.setAdapter(adapter);
                    for (int i=0;i<response.body().getSuccess().getData().getBill().getAddPart().size();i++) {
                        adpartcost+=response.body().getSuccess().getData().getBill().getAddPart().get(i).getPartCost();
                    }
                    totalAmountValue.setText("" +adpartcost);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PartRequest.this, "" + jsonObjError.getString("message"));
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


    private void sendPartResponse(String statusGet) {
        showProgress(PartRequest.this);
       /* JsonObject jsonObject = new JsonObject();

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
                Log.e("ytjjtjjuty", "" + jsonObjectT);
                jsonElements.add(jsonObjectT);
            }
        }
        else
        {
            //addPart.set(0,setPartCost(""));
        }
        jsonElements.add(jsonObject);*/



        JsonObject bfbf = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        for (int i=0;i<addPart.getAddPart().size();i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("partName", addPart.getAddPart().get(i).getPartName());
            jsonObject.addProperty("partNumber", addPart.getAddPart().get(i).getPartNumber());
            jsonObject.addProperty("partCost", addPart.getAddPart().get(i).getPartCost());
            jsonElements.add(jsonObject);
        }

        bfbf.add("addPart", jsonElements);
        bfbf.addProperty("requestjobId", jobId);
        bfbf.addProperty("r_status", statusGet);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.partRequest(ACCESS_TOKEN, bfbf);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(PartRequest.this, "" + response.body().getSuccess().getMsg().getReplyMessage());
                   /* Intent intent=new Intent(PartRequest.this, UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PartRequest.this, "" + jsonObjError.getString("message"));
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
      /*  Intent intent=new Intent(PartRequest.this, UserHomeScreen.class);
        startActivity(intent);*/
        finish();
    }
}
