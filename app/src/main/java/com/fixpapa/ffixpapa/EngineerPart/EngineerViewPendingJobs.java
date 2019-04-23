package com.fixpapa.ffixpapa.EngineerPart;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.ScheduleMod.SuccC;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.LocationService;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter.IssuesAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.NewJobsFragment;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.ShowAlertDialog;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtcToday;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeToUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class EngineerViewPendingJobs extends AppCompatActivity implements View.OnClickListener {
    ImageView crossImage, productImagesOne, productImagesTwo;
    RecyclerView vendorJobIssues;
    Button acceptJobButton, rejectJobButton;
    List<Problems> problemsList;
    AddressesModel addressList;
    List<String> imageList;
    String jobId, budget, dateTimeStart, dateTimeEnd, jobDescription, categoryName, productName, brandName, jobOrderId,
            dateVendor, scheduleStatus = "", engineerId, customerName, customerMobile;
    IssuesAdapter issuesAdapter;
    TextView vendorViewJobDevice, vendorViewProduct, vendorViewBrand, vendorViewDate, vendorViewTime, vendorViewBudget,
            vendorViewAddress, vendorViewJobDescription, productTitle, brandTitle;
    NewJobsFragment newJobsFragment;
    ScheduleModel scheduleModel;
    List<String> plantsList;
String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        newJobsFragment = new NewJobsFragment();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
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
        dateVendor = bundle.getString("dateVendorAssign");
        scheduleStatus = bundle.getString("scheduleStatus");
        engineerId = bundle.getString("enginId");
        customerName = bundle.getString("customerName");
        customerMobile = bundle.getString("customerMobile");

        if (scheduleStatus.equals("scheduled")) {
            scheduleModel = (ScheduleModel) bundle.getSerializable("Schedule");
        }

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
        vendorViewJobDescription = (TextView) findViewById(R.id.vendorViewJobDescription);
        productTitle = (TextView) findViewById(R.id.productTitle);
        brandTitle = (TextView) findViewById(R.id.brandTitle);


        vendorJobIssues = (RecyclerView) findViewById(R.id.vendorJobIssues);
        acceptJobButton = (Button) findViewById(R.id.acceptJobButton);
        rejectJobButton = (Button) findViewById(R.id.rejectJobButton);
        rejectJobButton.setText(getResources().getString(R.string.schedule_text));
        acceptJobButton.setText(getResources().getString(R.string.accept_now_text));
        if (scheduleStatus.equals("scheduled")) {
            rejectJobButton.setText(getResources().getString(R.string.scheduled_text));
        }


        try {
            Date mToday = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            String curTime = sdf.format(mToday);
            Date start = sdf.parse(getTimeFromUtc(dateTimeStart));
            Date end = sdf.parse(getTimeFromUtc(dateTimeEnd));
            Date userDate = sdf.parse(curTime);

            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }

            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());

            SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);
            String date = df.format(Calendar.getInstance().getTime());

            //String currentDateTimeString = outputFormat.getDateTimeInstance().format(new Date());
            Log.d("curTimedds", curTime+" "+getTimeFromUtc(dateTimeStart)+" "+getTimeFromUtc(dateTimeEnd));
            if (date.equals(getDateFromUtcToday(dateTimeStart))) {
                    acceptJobButton.setVisibility(View.VISIBLE);

            }else {
                acceptJobButton.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
        }


      /*  if (getDateFromUtc(dateTimeStart).equals(s.toString())) {
            if (dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
                acceptJobButton.setVisibility(View.VISIBLE);
            }
        }*/
        Log.e("rfgbgb", scheduleModel + "");

        vendorJobIssues.setHasFixedSize(true);
        vendorJobIssues.setNestedScrollingEnabled(false);
        vendorJobIssues.setLayoutManager(new LinearLayoutManager(EngineerViewPendingJobs.this));

        acceptJobButton.setOnClickListener(this);
        rejectJobButton.setOnClickListener(this);
        crossImage.setOnClickListener(this);

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

        vendorViewDate.setText(getDateFromUtc(dateTimeStart));

        vendorViewTime.setText(getTimeFromUtc(dateTimeStart) + " - " + getTimeFromUtc(dateTimeEnd));


        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {

                Picasso.with(EngineerViewPendingJobs.this)
                        .load(Image_BASE_URL + imageList.get(0))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesOne);

            }
            if (i == 1) {
                Picasso.with(EngineerViewPendingJobs.this)
                        .load(Image_BASE_URL + imageList.get(1))
                        .placeholder(R.drawable.icon_fixpapa)
                        .error(R.drawable.icon_fixpapa)
                        .into(productImagesTwo);

            }
        }
        issuesAdapter = new IssuesAdapter(problemsList, EngineerViewPendingJobs.this);
        vendorJobIssues.setAdapter(issuesAdapter);

        plantsList = new ArrayList<>();
        String time = getTimeFromUtc(dateTimeStart);
        Log.e("fdklnd", time);
        getSlots(time);

    }

    private void getSlots(String time) {
        String namepass[] = time.split(":");
        int name = Integer.parseInt(namepass[0]);
        String passs = namepass[1];

        String n[] = passs.split("00");
        String pass = n[1];

        String secondPart = name + ":30" + pass;
        plantsList.add(time + " - " + secondPart);
        int thirdPart;
        if (name == 12) {
            thirdPart = Integer.parseInt("1");
        } else {
            thirdPart = name + 1;
        }
        plantsList.add(secondPart + " - " + thirdPart + ":00" + pass);
        String fourth = thirdPart + ":30";
        plantsList.add(thirdPart + ":00" + pass + " - " + fourth + pass);
        int fift;
        if (thirdPart == 12) {
            fift = Integer.parseInt("1");
        } else {
            fift = thirdPart + 1;
        }
        plantsList.add(fourth + pass + " - " + fift + ":00" + pass);
    }

    @Override
    public void onClick(View v) {
        if (v == crossImage) {
            finish();
        }
        if (v == acceptJobButton) {
            acceptJobNow();
        }
        if (v == rejectJobButton) {
            if (rejectJobButton.getText().toString().equals(getResources().getString(R.string.schedule_text))) {
                try {
                    final Dialog dialog = new Dialog(EngineerViewPendingJobs.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.design_spinner_show);
                    dialog.setCanceledOnTouchOutside(false);

                    final Spinner popupSpinnerValue;
                    Button buttonDone, buttonCancel;
                    popupSpinnerValue = (Spinner) dialog.findViewById(R.id.popupSpinnerValue);
                    buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
                    buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);


                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EngineerViewPendingJobs.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return true;
                            } else {
                                return true;

                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                            if (position == 0) {
                                // Set the hint text color gray

                              //  tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                                tv.setPadding(15, 15, 15, 15);
                                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                                tv.setPadding(15, 15, 15, 15);
                             //   tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);

                            }

                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                    popupSpinnerValue.setAdapter(spinnerArrayAdapter);

                    dialog.show();
                    buttonDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            String namepass[] = popupSpinnerValue.getSelectedItem().toString().split("-");
                            Log.e("vsfdsfbdf",getDateFromUtc(dateTimeStart)+" ");
                            String startTime = namepass[0];
                            String endTime = namepass[1];
                            String dateee[] = dateTimeStart.split("T");
                            String DateS = dateee[0];

                            Log.e("vsfdsfbdf",getDateFromUtc(dateTimeStart)+" "+startTime);
                            String startdateUtc=getTimeToUtc(DateS+" "+startTime);
                            String enddateUtc=getTimeToUtc(DateS+" "+endTime);
                            Log.e("vsfdsfbdf",getDateFromUtc(dateTimeStart)+" "+startTime);
                            scheculedJob(jobId, DateS, startdateUtc, enddateUtc);
                        }
                    });
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (rejectJobButton.getText().toString().equals(getResources().getString(R.string.scheduled_text))) {
                try {
                    final Dialog dialog = new Dialog(EngineerViewPendingJobs.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.design_spinner_show);
                    dialog.setCanceledOnTouchOutside(false);

                    Spinner popupSpinnerValue;
                    Button buttonDone, buttonCancel;
                    TextView startDate, endDate, titlePopup;
                    LinearLayout layoutSpinner = (LinearLayout) dialog.findViewById(R.id.layoutSpinner);
                    popupSpinnerValue = (Spinner) dialog.findViewById(R.id.popupSpinnerValue);
                    buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
                    buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                    startDate = (TextView) dialog.findViewById(R.id.startDate);
                    endDate = (TextView) dialog.findViewById(R.id.endDate);
                    titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                    buttonDone.setVisibility(View.GONE);
                    layoutSpinner.setVisibility(View.GONE);
                    buttonCancel.setText(getResources().getString(R.string.ok_text));
                    startDate.setVisibility(View.VISIBLE);
                    endDate.setVisibility(View.VISIBLE);
                    titlePopup.setText("You Already Scheduled");
                    startDate.setText("Date: " + getDateFromUtc(scheduleModel.getEStartDate()));
                    endDate.setText("Time: " + getTimeFromUtc(scheduleModel.getEStartDate()) + " - " + getTimeFromUtc(scheduleModel.getEEndDate()));

                    dialog.show();

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }


        }
    }

    private void acceptJobNow() {
        showProgress(EngineerViewPendingJobs.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> modelCall = apiInterface.engineerAcceptJobRequest(ACCESS_TOKEN, hashMap);
        modelCall.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {

                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        ShowAlertDialog(EngineerViewPendingJobs.this);
                    }else {
                        showToast(EngineerViewPendingJobs.this, "job accepted successfully");
                        Intent intent = new Intent(EngineerViewPendingJobs.this, MapEngineerTrack.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude", String.valueOf(addressList.getLocation().getLat()));
                        bundle.putString("longitude", String.valueOf(addressList.getLocation().getLng()));
                        bundle.putString("street", String.valueOf(addressList.getStreet()));
                        bundle.putString("value", String.valueOf(addressList.getValue()));
                        bundle.putString("jobId", response.body().getSuccess().getData().getId());
                        bundle.putString("otp", String.valueOf(response.body().getSuccess().getData().getOtp()));
                        bundle.putSerializable("pick", (PickModel) response.body().getSuccess().getData().getPick());
                        bundle.putSerializable("addpart", (ArrayList<AddPart>) response.body().getSuccess().getData().getBill().getAddPart());
                        bundle.putSerializable("address", (AddressesModel) response.body().getSuccess().getData().getAddress());
                        bundle.putSerializable("jobImage", (ArrayList<String>) response.body().getSuccess().getData().getImage());
                        bundle.putSerializable("sendIssues", (ArrayList<Problems>) response.body().getSuccess().getData().getProblems());
                        bundle.putString("dateVendorAssign", String.valueOf(response.body().getSuccess().getData().getVendorAssignedDate()));
                        bundle.putString("jobDescription", String.valueOf(response.body().getSuccess().getData().getProblemDes()));
                        bundle.putString("sendCategory", categoryName);
                        bundle.putString("subCategory", productName);
                        bundle.putString("brand", brandName);
                        bundle.putString("customerName", customerName);
                        bundle.putString("customerMobile", response.body().getSuccess().getData().getCustomer().getMobile());
                        bundle.putString("customerId", response.body().getSuccess().getData().getCustomerId());
                        bundle.putString("budget", String.valueOf(response.body().getSuccess().getData().getTotalPrice()));
                        bundle.putString("dateTimeStart", String.valueOf(response.body().getSuccess().getData().getStartDate()));
                        bundle.putString("dateTimeEnd", String.valueOf(response.body().getSuccess().getData().getEndDate()));
                        bundle.putString("jonOrderId", String.valueOf(response.body().getSuccess().getData().getOrderId()));
                        bundle.putString("timeDuration", String.valueOf(response.body().getSuccess().getData().getDuration()));
                        bundle.putString("scheduleStatus", response.body().getSuccess().getData().getStatus());
                        bundle.putString("enginId", engineerId);
                        if (response.body().getSuccess().getData().getStatus().equals("scheduled")) {
                            bundle.putSerializable("Schedule", (ScheduleModel) scheduleModel);
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                        /*Intent intent = new Intent(EngineerViewPendingJobs.this, EngineerViewSite.class);
                        startActivity(intent);
                        showToast(EngineerViewPendingJobs.this, response.body().getSuccess().getMsg().getReplyMessage() + "");*/

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewPendingJobs.this, "" + jsonObjError.getString("message"));
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

    private void scheculedJob(String jobId, String date, String startTime, String endTime) {


        showProgress(EngineerViewPendingJobs.this);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("eStartDate", startTime);
        hashMap.put("eEndDate", endTime);
        Log.e("fdvfdvdfb",""+hashMap);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccC> modelCall = apiInterface.scheduleJobRequest(ACCESS_TOKEN, hashMap);
        modelCall.enqueue(new Callback<SuccC>() {
            @Override
            public void onResponse(Call<SuccC> call, Response<SuccC> response) {
                close();
                if (response.isSuccessful()) {

                    //rejectJobButton.setText(getResources().getString(R.string.scheduled_text));
                    showToast(EngineerViewPendingJobs.this, response.body().getSuccess().getMsg() + "");
                    Intent intent = new Intent(EngineerViewPendingJobs.this, EngineerHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerViewPendingJobs.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccC> call, Throwable t) {
                close();
            }
        });
    }


}
