package com.fixpapa.ffixpapa.VendorPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.FreeEngineerAdapter;
import com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel.SucessEngineerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class FreeEngineer extends AppCompatActivity {
    RecyclerView engineerBlockRecycler;
    FreeEngineerAdapter blockEngineerAdapter;
    ImageView crossImage;
    TextView valueStatus;
    String jobOrder="",jobId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_engineer);
        setContentView(R.layout.activity_block_engineer);
        getUserDetailId(FreeEngineer.this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        jobOrder = bundle.getString("jobOrderId");
        jobId = bundle.getString("jobId");

        crossImage = (ImageView) findViewById(R.id.crossImage);
        engineerBlockRecycler = (RecyclerView) findViewById(R.id.engineerBlockRecycler);
        valueStatus = (TextView) findViewById(R.id.valueStatus);

        engineerBlockRecycler.setHasFixedSize(true);
        engineerBlockRecycler.setLayoutManager(new LinearLayoutManager(FreeEngineer.this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(FreeEngineer.this, DividerItemDecoration.VERTICAL);
        engineerBlockRecycler.addItemDecoration(itemDecor);
        getAllFreeEngineers();
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getAllFreeEngineers() {
        showProgress(FreeEngineer.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        String USER_ID = sharedPref.getString("USER_ID","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SucessEngineerModel> call = apiInterface.getAllFreeEngineers(ACCESS_TOKEN,USER_ID);
        call.enqueue(new Callback<SucessEngineerModel>() {
            @Override
            public void onResponse(Call<SucessEngineerModel> call, Response<SucessEngineerModel> response) {
                close();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().getData().size()!=0) {
                        blockEngineerAdapter = new FreeEngineerAdapter(jobId,response.body().getSuccess().getData(), FreeEngineer.this);
                        engineerBlockRecycler.setAdapter(blockEngineerAdapter);
                    }
                    else
                    {
                        valueStatus.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(FreeEngineer.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SucessEngineerModel> call, Throwable t) {
                close();
            }
        });

    }
}
