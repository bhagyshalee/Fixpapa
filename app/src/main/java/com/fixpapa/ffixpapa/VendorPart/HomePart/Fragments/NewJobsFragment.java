package com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.NewJobAdapter;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.NewJobsData;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
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
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class NewJobsFragment extends Fragment {

    ToggleButton toggleButton;
    RecyclerView newJobsVendor;
    NewJobAdapter newJobAdapter;
    List<NewJobsData> overViewModules;
    TextView valueStatus;
    SwipeRefreshLayout setOnRefreshListener;
    Context context;
    String USER_ID, ACCESS_TOKEN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_jobs, container, false);
        context=getActivity();
        overViewModules = new ArrayList<>();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");
        USER_ID = sharedPref.getString("USER_ID", "");

//        getUserDetailId(context);

        toggleButton = (ToggleButton) v.findViewById(R.id.toggle);
        valueStatus = (TextView) v.findViewById(R.id.valueStatus);
        setOnRefreshListener = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setOnRefreshListener.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setOnRefreshListener.setRefreshing(false);
                        if (isLogin(getActivity())!=0){
                            if (isOnline(true, getActivity())) {
                                getAllNewJobs();
                            }
                        }

                    }
                }, 2000);
            }
        });
        newJobsVendor = (RecyclerView) v.findViewById(R.id.newJobsVendor);
        newJobsVendor.setHasFixedSize(true);
        newJobsVendor.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (isLogin(getActivity())!=0){
            if (isOnline(true, getActivity())) {
                getAllNewJobs();
            }
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("checkData", "" + buttonView + " : " + isChecked);
                checkVendorVisibility(isChecked);
            }
        });
        return v;
    }

    private void checkVendorVisibility(boolean getvisi) {
        showProgress(context);
        HashMap hashMap = new HashMap();
        hashMap.put("isAvailable", getvisi);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.vendorVisibility(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(context, ""+response.body().getSuccess().getMsg().getReplyMessage());
                    Log.e("fgbbfgb",""+response.body().getSuccess().getMsg().getReplyMessage());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(context, "" + jsonObjError.getString("message"));
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

    public void getAllNewJobs() {
        showProgress(context);
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("id", USER_ID + "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> call = apiInterface.getNewVendorJobs(ACCESS_TOKEN, USER_ID);
        call.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful()) {
                    //overViewModules.add(response.body());
                    if (response.body().getSuccess().getData().size() != 0) {
                        newJobAdapter = new NewJobAdapter(response.body().getSuccess().getData(),  context);
                        newJobsVendor.setAdapter(newJobAdapter);
                        valueStatus.setVisibility(View.GONE);
                    } else {
                        valueStatus.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(context, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SuccessModelVendor> call, Throwable t) {
                close();
                Log.e("fdvfdnv", "" + t);
            }
        });
    }

}
