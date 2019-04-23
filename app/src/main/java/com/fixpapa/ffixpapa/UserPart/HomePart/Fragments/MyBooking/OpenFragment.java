package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.MyBooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.BookingOngoingAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.UserLogin;
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


import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OpenFragment extends Fragment {

    RecyclerView bookingRecycler;
    BookingOngoingAdapter bookingOpenAdapter;
    TextView logOutMsgShow;
    int loginStatus = 0;
    String colorStatus;
    static int valueGett=0;
    List<SampleData> newJobsData;
    String customerType;
    SwipeRefreshLayout setOnRefreshListener;
    DataBaseHandler dataBaseHandler;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_open, container, false);

        context=getActivity();
        dataBaseHandler=new DataBaseHandler(context);
        loginStatus = isLogin(getActivity());
//        getUserDetailId(getActivity());


        bookingRecycler = (RecyclerView) v.findViewById(R.id.bookingRecycler);
        logOutMsgShow = (TextView) v.findViewById(R.id.logOutMsgShow);
        bookingRecycler.setHasFixedSize(true);
        newJobsData=new ArrayList<>();
        bookingRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        bookingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (loginStatus != 0) {
            if (isOnline(true, getActivity())) {
                getBookingData();
            }
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
                                getBookingData();
                            }
                        }

                    }
                }, 2000);
            }
        });

        return v;
    }

    private void getBookingData() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);

        Call<SuccessModelVendor> modelCall = apiInterface.getBookingDetail(ACCESS_TOKEN);
        modelCall.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                newJobsData.clear();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().getData().size() != 0) {
                        Log.e("dfbdvhfbdvfdvf",response.body().getSuccess().getData()+"");
                        for(int i=0;i<response.body().getSuccess().getData().size();i++) {
                            if (response.body().getSuccess().getData().get(i).getStatus().equals("requested") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("pending") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("vendorAccepted") ||
                                    response.body().getSuccess().getData().get(i).getStatus().equals("scheduled")) {
                                colorStatus="blue";
                                SampleData sampleData=new SampleData();
                                sampleData.setOrderId(response.body().getSuccess().getData().get(i).getOrderId());
                                sampleData.setCategory(response.body().getSuccess().getData().get(i).getCategory());
                                sampleData.setStartDate(response.body().getSuccess().getData().get(i).getStartDate());
                                sampleData.setEndDate(response.body().getSuccess().getData().get(i).getEndDate());
                                sampleData.setTotalPrice(response.body().getSuccess().getData().get(i).getTotalPrice());
                                sampleData.setStatus(response.body().getSuccess().getData().get(i).getStatus());
                                sampleData.setProblems(response.body().getSuccess().getData().get(i).getProblems());
                                sampleData.setProduct(response.body().getSuccess().getData().get(i).getProduct());
                             //   customerType=response.body().getSuccess().getData().get(i).get;
                                Log.e("fdvjfv",""+customerType);
                                sampleData.setBrand(response.body().getSuccess().getData().get(i).getBrand());
                                sampleData.setAddress(response.body().getSuccess().getData().get(i).getAddress());
                                sampleData.setImage(response.body().getSuccess().getData().get(i).getImage());
                                sampleData.setProblemDes(response.body().getSuccess().getData().get(i).getProblemDes());
                                sampleData.setId(response.body().getSuccess().getData().get(i).getId());
                                sampleData.setVendorAssigned(response.body().getSuccess().getData().get(i).getIsVendorAssigned());
                                sampleData.setEngineerAssigned(response.body().getSuccess().getData().get(i).getIsEngineerAssigned());
                                sampleData.setCreatedAt(response.body().getSuccess().getData().get(i).getCreatedAt());
                                sampleData.setSiteType(response.body().getSuccess().getData().get(i).getSiteType());
                                sampleData.setBill(response.body().getSuccess().getData().get(i).getBill());
                                sampleData.setModeOfPayment(response.body().getSuccess().getData().get(i).getModeOfPayment());
                                newJobsData.add(sampleData);
                                valueGett=1;
                                logOutMsgShow.setVisibility(View.GONE);
                            }
                            else if (valueGett==0)
                            {
                                  logOutMsgShow.setVisibility(View.VISIBLE);
                                    logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));

                            }
                        }
                        String open="open";
                        bookingOpenAdapter = new BookingOngoingAdapter(open,colorStatus,newJobsData, context);
                        bookingRecycler.setAdapter(bookingOpenAdapter);
                    }
                    else
                    {
                        logOutMsgShow.setVisibility(View.VISIBLE);
                        logOutMsgShow.setText(getResources().getText(R.string.notavaliableservice_text));
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        if (jsonObjError.getString("code").equals("AUTHORIZATION_REQUIRED")) {
                            // JSONObject jsonObDetail = jsonObjError.getJSONObject("details");

                            logoutCall();
                           // dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);
                        }else {
                            showToast(context, "" + jsonObjError.getString("message"));

                        }
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
                Log.e("cvbcbcb",""+t);
            }
        });
    }

    private void logoutCall() {
        showProgress(getActivity());
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);

        Call<SuccessModelVendor> call=apiInterface.logoutAllServices(ACCESS_TOKEN);
        call.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful())
                {
                    Intent intent = new Intent(getActivity(), UserLogin.class);
                    // intent.putExtra("userOtp", "");
                    // intent.putExtra("userId", jsonObDetail.getString("userId"));
                    startActivity(intent);
                    dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);
                   /* Intent intent=new Intent(getActivity(), UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                }
                else
                {
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
            getBookingData();
        }

    }
}
