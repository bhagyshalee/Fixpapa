package com.fixpapa.ffixpapa.VendorPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.UpdateEngineerAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
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

public class UpdateEngineer extends AppCompatActivity {
    UpdateEngineerAdapter adapter;
    RecyclerView engineerBlockRecycler;
    ImageView crossImage;
    TextView valueStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_engineer);
        getUserDetailId(UpdateEngineer.this);
        engineerBlockRecycler = (RecyclerView) findViewById(R.id.engineerBlockRecycler);
        valueStatus = (TextView) findViewById(R.id.valueStatus);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        engineerBlockRecycler.setHasFixedSize(true);
        engineerBlockRecycler.setLayoutManager(new LinearLayoutManager(UpdateEngineer.this));
        getAllEngineers();
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(UpdateEngineer.this, VendorHomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void getAllEngineers() {
        showProgress(UpdateEngineer.this);
        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        String USER_ID = sharedPref.getString("USER_ID","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SucessEngineerModel> call = apiInterface.getAllEngineers(ACCESS_TOKEN,USER_ID);
        call.enqueue(new Callback<SucessEngineerModel>() {
            @Override
            public void onResponse(Call<SucessEngineerModel> call, Response<SucessEngineerModel> response) {
                close();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().getData().size()!=0) {
                        adapter = new UpdateEngineerAdapter(response.body().getSuccess().getData(), UpdateEngineer.this);
                        engineerBlockRecycler.setAdapter(adapter);
                    }
                    else
                    {
                        valueStatus.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UpdateEngineer.this, "" + jsonObjError.getString("message"));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(UpdateEngineer.this, VendorHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
