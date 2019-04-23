package com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.MyJobFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.Adapter.EngineerTodayAdapter;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtcToday;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class TodayFragement extends Fragment {

    RecyclerView bookingRecycler;
    EngineerTodayAdapter bookingOpenAdapter;
    TextView logOutMsgShow;
    int loginStatus = 0;
    List<SampleData> newJobsData;
    int valueGett = 0;
    SwipeRefreshLayout setOnRefreshListener;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_open, container, false);
        context = getActivity();
        loginStatus = isLogin(getActivity());
        bookingRecycler = (RecyclerView) v.findViewById(R.id.bookingRecycler);
        logOutMsgShow = (TextView) v.findViewById(R.id.logOutMsgShow);
        bookingRecycler.setHasFixedSize(true);
        bookingRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        bookingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        newJobsData = new ArrayList<>();
        if (loginStatus != 0) {
            getTodayData();
        } else {
            logOutMsgShow.setVisibility(View.VISIBLE);
        }
        setOnRefreshListener = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setOnRefreshListener.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setOnRefreshListener.setRefreshing(false);
                        if (loginStatus!=0){
                            if (isOnline(true, getActivity())) {
                                getTodayData();
                            }
                        }

                    }
                }, 2000);
            }
        });
        return v;
    }

    private void getTodayData() {
        showProgress(getActivity());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> modelCall = apiInterface.getEngineerJobs(ACCESS_TOKEN);
        modelCall.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful()) {
                    newJobsData.clear();
                    if (response.body().getSuccess().getData().size() != 0) {

                        Date d = new Date();
                        CharSequence s = DateFormat.format("dd MM yyyy", d.getTime());

                        for (int i = 0; i < response.body().getSuccess().getData().size(); i++) {
                            Log.e("sgrwwwwwwwwww", "" + getDateFromUtcToday(response.body().getSuccess().getData().get(i).getStartDate())
                                    + "  " + s.toString());

                            // newJobsData.clear();
                            if (getDateFromUtcToday(response.body().getSuccess().getData().get(i).getStartDate()).compareTo(s.toString()) == 0) {
                                SampleData sampleData = new SampleData();
                                sampleData.setOrderId(response.body().getSuccess().getData().get(i).getOrderId());
                                sampleData.setCategory(response.body().getSuccess().getData().get(i).getCategory());
                                sampleData.setStartDate(response.body().getSuccess().getData().get(i).getStartDate());
                                sampleData.setEndDate(response.body().getSuccess().getData().get(i).getEndDate());
                                sampleData.setTotalPrice(response.body().getSuccess().getData().get(i).getTotalPrice());
                                sampleData.setStatus(response.body().getSuccess().getData().get(i).getStatus());
                                sampleData.setProblems(response.body().getSuccess().getData().get(i).getProblems());
                                sampleData.setProduct(response.body().getSuccess().getData().get(i).getProduct());

                                sampleData.setBrand(response.body().getSuccess().getData().get(i).getBrand());
                                sampleData.setAddress(response.body().getSuccess().getData().get(i).getAddress());
                                sampleData.setImage(response.body().getSuccess().getData().get(i).getImage());
                                sampleData.setProblemDes(response.body().getSuccess().getData().get(i).getProblemDes());
                                sampleData.setId(response.body().getSuccess().getData().get(i).getId());
                                sampleData.setVendorAssigned(response.body().getSuccess().getData().get(i).getIsVendorAssigned());
                                sampleData.setEngineerAssigned(response.body().getSuccess().getData().get(i).getIsEngineerAssigned());
                                sampleData.setVendorAssignedDate(response.body().getSuccess().getData().get(i).getVendorAssignedDate());
                                sampleData.setSchedule(response.body().getSuccess().getData().get(i).getSchedule());
                                sampleData.setSiteType(response.body().getSuccess().getData().get(i).getSiteType());
                                // sampleData.setCustomerName(response.body().getSuccess().getData().get(i).getCustomer().getFullName());
                                //sampleData.setCustomerId(response.body().getSuccess().getData().get(i).getCustomer().getId());

                                sampleData.setPick(response.body().getSuccess().getData().get(i).getPick());
                                sampleData.setBill(response.body().getSuccess().getData().get(i).getBill());
                                sampleData.setSchedule(response.body().getSuccess().getData().get(i).getSchedule());
                                sampleData.setCustomerMobile(response.body().getSuccess().getData().get(i).getCustomer().getMobile());
                                sampleData.setEngineerId(response.body().getSuccess().getData().get(i).getEngineerId());
                                sampleData.setCustomer(response.body().getSuccess().getData().get(i).getCustomer());

                                newJobsData.add(sampleData);
                                valueGett = 1;
                                logOutMsgShow.setVisibility(View.GONE);
                            } else if (valueGett == 0) {
                                logOutMsgShow.setVisibility(View.VISIBLE);
                                logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));
                            }

                        }
                        bookingOpenAdapter = new EngineerTodayAdapter(newJobsData,context);
                        bookingRecycler.setAdapter(bookingOpenAdapter);
                    } else {
                        Activity activity = getActivity();
                        if (activity != null) {

                            logOutMsgShow.setVisibility(View.VISIBLE);
                            logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));
                        }
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
                Log.e("vfdvdfvfd", t + "");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLogin(getActivity()) != 0) {
            getTodayData();
        }

    }
}
