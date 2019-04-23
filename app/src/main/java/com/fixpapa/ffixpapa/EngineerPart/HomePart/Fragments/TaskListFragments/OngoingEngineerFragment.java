package com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.TaskListFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fixpapa.ffixpapa.EngineerPart.Adapter.EngineerTaskListOngoingAdapter;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs;
import com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.LocationService;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OngoingEngineerFragment extends Fragment {

    RecyclerView bookingRecycler;
    EngineerTaskListOngoingAdapter bookingOpenAdapter;
    TextView logOutMsgShow;
    int loginStatus = 0;
    List<SampleData> newJobsData;
    int valueGett=0;
    SwipeRefreshLayout setOnRefreshListener;
    double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_open, container, false);

        loginStatus = isLogin(getActivity());
        newJobsData=new ArrayList<>();
        bookingRecycler = (RecyclerView) v.findViewById(R.id.bookingRecycler);
        logOutMsgShow = (TextView) v.findViewById(R.id.logOutMsgShow);
        bookingRecycler.setHasFixedSize(true);
        bookingRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        bookingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> modelCall = apiInterface.getEngineerAllJobs(ACCESS_TOKEN);
        modelCall.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                if (response.isSuccessful()) {
                    newJobsData.clear();
                    if (response.body().getSuccess().getData().size() != 0) {

                        Log.e("vxvvv",response.body().getSuccess().getData()+"");
                        for(int i=0;i<response.body().getSuccess().getData().size();i++) {

                            if (response.body().getSuccess().getData().get(i).getStatus().equals("inprocess") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("billGenerated") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("paymentDone") ||
                            response.body().getSuccess().getData().get(i).getStatus().equals("on the way") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("outForDelivery"))
                            {
                                SampleData sampleData=new SampleData();
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
                                sampleData.setCustomerName(response.body().getSuccess().getData().get(i).getCustomer().getFullName());
                                sampleData.setCustomerId(response.body().getSuccess().getData().get(i).getCustomer().getId());

                                sampleData.setPick(response.body().getSuccess().getData().get(i).getPick());
                                sampleData.setBill(response.body().getSuccess().getData().get(i).getBill());
                                sampleData.setSchedule(response.body().getSuccess().getData().get(i).getSchedule());
                                sampleData.setCustomerMobile(response.body().getSuccess().getData().get(i).getCustomer().getMobile());
                                sampleData.setEngineerId(response.body().getSuccess().getData().get(i).getEngineerId());
                                sampleData.setCustomer(response.body().getSuccess().getData().get(i).getCustomer());

                                newJobsData.add(sampleData);

                                valueGett=1;
                                logOutMsgShow.setVisibility(View.GONE);
                            }
                            else if (valueGett==0)
                            {
                                Activity activity = getActivity();
                                if(activity != null) {

                                    logOutMsgShow.setVisibility(View.VISIBLE);
                                    logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));
                                }
                            }

                            bookingOpenAdapter = new EngineerTaskListOngoingAdapter(newJobsData, getActivity());
                            bookingRecycler.setAdapter(bookingOpenAdapter);
                        }
                    } else {
                        logOutMsgShow.setVisibility(View.VISIBLE);
                        logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(getActivity(), "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessModelVendor> call, Throwable t) {
                Log.e("bvcbcvbcb",""+t);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isLogin(getActivity()) != 0) {
            getTodayData();
        }

    }
}
