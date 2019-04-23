package com.fixpapa.ffixpapa.VendorPart;

import android.content.Context;
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
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.BlockEngineerAdapter;
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

public class BlockEngineer extends AppCompatActivity {
    RecyclerView engineerBlockRecycler;
    BlockEngineerAdapter blockEngineerAdapter;
    ImageView crossImage;
    TextView valueStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_engineer);
        getUserDetailId(BlockEngineer.this);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        engineerBlockRecycler = (RecyclerView) findViewById(R.id.engineerBlockRecycler);
        valueStatus = (TextView) findViewById(R.id.valueStatus);

        engineerBlockRecycler.setHasFixedSize(true);
        engineerBlockRecycler.setLayoutManager(new LinearLayoutManager(BlockEngineer.this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(BlockEngineer.this, DividerItemDecoration.VERTICAL);
        engineerBlockRecycler.addItemDecoration(itemDecor);
        getAllEngineers();
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getAllEngineers() {
        showProgress(BlockEngineer.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
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
                        blockEngineerAdapter = new BlockEngineerAdapter(response.body().getSuccess().getData(), BlockEngineer.this);
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
                        showToast(BlockEngineer.this, "" + jsonObjError.getString("message"));
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
