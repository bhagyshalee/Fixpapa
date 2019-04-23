package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.Model.UserLoginData.SucessLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Config.brandId;
import static com.fixpapa.ffixpapa.Services.Rest.Config.brandName;
import static com.fixpapa.ffixpapa.Services.Rest.Config.imagesArray;
import static com.fixpapa.ffixpapa.Services.Rest.Config.mainCategoryId;
import static com.fixpapa.ffixpapa.Services.Rest.Config.problemDescription;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productId;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productName;
import static com.fixpapa.ffixpapa.Services.Rest.Config.totalPrice;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeToUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuePrice;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuecategoryId;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuecreatedAt;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueid;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueprobContent;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueupdatedAt;

public class DateTime extends AppCompatActivity implements View.OnClickListener {

    DatePicker datePickerValue;
    Spinner selectTimeSlot;
    Button buttonPostAddress;

    String emailMobileStatus = "", selectedAddress, selectedTime;
    ImageView facebookLogin, googleLogin, crossDialog, backImage;
    EditText loginEmail, loginPassword;
    LoginButton login_button;
    String userId, userName, userEmail, userType, userMobile, userGstNo, userAddress, userStreet, userAccessToken, userImage, getLoginType;
    boolean mobileVerify, emailVerify;
    double userLatitude, userLongitude;
    Dialog dialog;
    DataBaseHandler baseHandler;
    int day, month, year;
    String dateStart, dateEnd, concatStr;

    int loginStatus = 0;
    Date dateSelect = null, dateCurrent = null;
    Date afterDate = null, date1 = null, date2 = null, date3 = null, date4 = null, date5 = null, date6 = null;

    JSONArray issuesListArray;
    JSONObject innerAddressObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        getUserDetailId(DateTime.this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;

        SharedPreferences prefs = getSharedPreferences("SELECTED_ADDRESS", MODE_PRIVATE);
        try {
            innerAddressObject = new JSONObject(prefs.getString("jsonAdd", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        baseHandler = new DataBaseHandler(DateTime.this);
        dialog = new Dialog(DateTime.this);
        loginStatus = isLogin(DateTime.this);

        datePickerValue = (DatePicker) findViewById(R.id.datePickerValue);
        selectTimeSlot = (Spinner) findViewById(R.id.selectTimeSlot);
        buttonPostAddress = (Button) findViewById(R.id.buttonPostAddress);
        backImage = (ImageView) findViewById(R.id.backImage);
        buttonPostAddress.setOnClickListener(this);
        backImage.setOnClickListener(this);

        Date date = new Date();
        date.setHours(date.getHours() + 4);
        Date currentDate = new Date();
        issuesListArray = new JSONArray();
        SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        System.out.println(simpDate.format(date));
        String getAfterFour = simpDate.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        try {
            afterDate = sdf.parse(simpDate.format(date));
            date1 = sdf.parse("9:00 AM");
            date2 = sdf.parse("11:00 AM");
            date3 = sdf.parse("1:00 PM");
            date4 = sdf.parse("3:00 PM");
            date5 = sdf.parse("5:00 PM");
            date6 = sdf.parse("5:00 AM");

        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
        }
      //  Log.e("fdvfdgrgrewt", date + "  \n    " + getAfterFour + " \n   " + afterDate.toString() + "  \n  " + date1.toString() + " \n   " + date2.toString() + " \n   " + date3.toString() + "  \n  " + date4.toString() + "  \n  " + date5.toString() + " \n   " + date6.toString());

     /*   String[] plants = new String[]{
                "Select Time Slot",
                "9:00  AM    -   11:00 AM",
                "11:00 AM     -   1:00 PM",
                "1:00  PM     -   3:00 PM",
                "3:00  PM     -   5:00 PM",
                "5:00  PM     -   7:00 PM"
        };
        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
*/


        datePickerValue.setMinDate(System.currentTimeMillis() + 1000);
        day = datePickerValue.getDayOfMonth();
        month = datePickerValue.getMonth() + 1;
        year = datePickerValue.getYear();

       /* String strDate = sdfd.format(dateSelect);
        String strDatee = sdfd.format(dateCurrent);
*/
        JSONObject jsonObject = new JSONObject();
        if (listIssueid.size()!=0) {
            for (int i = 0; i < listIssueid.size(); i++) {
                try {
                    jsonObject.put("id", listIssueid.get(i));
                    jsonObject.put("categoryId", listIssuecategoryId.get(i));
                    jsonObject.put("probContent", listIssueprobContent.get(i));
                    jsonObject.put("price", listIssuePrice.get(i));
                    jsonObject.put("createdAt", listIssuecreatedAt.get(i));
                    jsonObject.put("updatedAt", listIssueupdatedAt.get(i));
                    issuesListArray.put(jsonObject);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }


        GregorianCalendar gc = new GregorianCalendar();
        String s = String.valueOf(android.text.format.DateFormat.format("dd/M/yyyy", gc.getTime()));

        SimpleDateFormat sdfd = new SimpleDateFormat("dd/M/yyyy",Locale.ENGLISH);
        try {
            dateSelect = sdfd.parse((day) + "/" + (month) + "/" + year);
            dateCurrent = sdfd.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strDate = sdfd.format(dateSelect);
        String strDatee = sdfd.format(dateCurrent);

        /* try {*/
          /* for (int i = 0; i < plantsList.size(); i++) {
               plantsList.set(i, "Slots not available today");
           }*/
       // if (afterDate != null && dateCurrent !=null && date1!=null && date2!=null) {
            List<String> plantsListF = new ArrayList<>();
            if (dateSelect.compareTo(dateCurrent) == 0) {
                if (afterDate.before(date1)) {
                    if (afterDate.before(date6)) {
                   /* datePickerValue.setMinDate(System.currentTimeMillis()+1000);
                    day = datePickerValue.getDayOfMonth()+1;
                    month = datePickerValue.getMonth() + 1;
                    year = datePickerValue.getYear();*/
                        plantsListF.add("Slots not available today");
                    } else {
                        plantsListF.add("Select Time Slot");
                        plantsListF.add("9:00 AM    -   11:00 AM");
                        plantsListF.add("11:00 AM     -   1:00 PM");
                        plantsListF.add("1:00 PM     -   3:00 PM");
                        plantsListF.add("3:00 PM     -   5:00 PM");
                        plantsListF.add("5:00 PM     -   7:00 PM");
                    }

                } else if (afterDate.before(date2)) {
                    plantsListF.add("Select Time Slot");
                    plantsListF.add("11:00 AM     -   1:00 PM");
                    plantsListF.add("1:00 PM     -   3:00 PM");
                    plantsListF.add("3:00 PM     -   5:00 PM");
                    plantsListF.add("5:00 PM     -   7:00 PM");

                } else if (afterDate.before(date3)) {
                    plantsListF.add("Select Time Slot");
                    plantsListF.add("1:00 PM     -   3:00 PM");
                    plantsListF.add("3:00 PM     -   5:00 PM");
                    plantsListF.add("5:00 PM     -   7:00 PM");

                } else if (afterDate.before(date4)) {
                    plantsListF.add("Select Time Slot");
                    plantsListF.add("3:00 PM     -   5:00 PM");
                    plantsListF.add("5:00 PM     -   7:00 PM");

                } else if (afterDate.before(date5)) {

                    plantsListF.add("Select Time Slot");
                    plantsListF.add("5:00 PM     -   7:00 PM");

                } else {
                    plantsListF.add("Slots not available today");
                }
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DateTime.this, R.layout.design_spinner, R.id.textSpinner, plantsListF) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
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
                            tv.setTextSize(15);
                            //tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                            tv.setPadding(15, 15, 15, 15);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                            tv.setPadding(15, 15, 15, 15);
                            // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                selectTimeSlot.setAdapter(spinnerArrayAdapter);
            } else {

                String[] plants = new String[]{
                        "Select Time Slot",
                        "9:00 AM    -   11:00 AM",
                        "11:00 AM    -   1:00 PM",
                        "1:00 PM     -   3:00 PM",
                        "3:00 PM     -   5:00 PM",
                        "5:00 PM     -   7:00 PM"
                };

                List<String> plantsListt = new ArrayList<>(Arrays.asList(plants));
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DateTime.this, R.layout.design_spinner, R.id.textSpinner, plantsListt) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
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
                            tv.setTextSize(15);
                            // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                            tv.setPadding(15, 15, 15, 15);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                            tv.setPadding(15, 15, 15, 15);
                            // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                selectTimeSlot.setAdapter(spinnerArrayAdapter);

          //  }
        }

        datePickerValue.init(datePickerValue.getYear(), datePickerValue.getMonth(), datePickerValue.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                GregorianCalendar gc = new GregorianCalendar();
                String s = String.valueOf(android.text.format.DateFormat.format("dd/M/yyyy", gc.getTime()));

                SimpleDateFormat sdfd = new SimpleDateFormat("dd/M/yyyy",Locale.ENGLISH);
                try {
                    dateSelect = sdfd.parse((arg3) + "/" + (arg2 + 1) + "/" + arg1);
                    dateCurrent = sdfd.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String strDate = sdfd.format(dateSelect);
                String strDatee = sdfd.format(dateCurrent);
                   /*for (int i = 0; i < plantsList.size(); i++) {
                       plantsList.set(i, "Slots not available today");
                   }*/

                List<String> plantsListS = new ArrayList<>();
                if (dateSelect.compareTo(dateCurrent) == 0) {
                    if (afterDate.before(date1)) {
                        if (afterDate.before(date6)) {
                            plantsListS.add("Slots not available today");
                        } else {
                            plantsListS.add("Select Time Slot");
                            plantsListS.add("9:00 AM    -   11:00 AM");
                            plantsListS.add("11:00 AM    -  1:00 PM");
                            plantsListS.add("1:00 PM     -   3:00 PM");
                            plantsListS.add("3:00 PM     -   5:00 PM");
                            plantsListS.add("5:00 PM     -   7:00 PM");
                        }
                    } else if (afterDate.before(date2)) {
                        plantsListS.add("Select Time Slot");
                        plantsListS.add("11:00 AM     -   1:00 PM");
                        plantsListS.add("1:00 PM     -   3:00 PM");
                        plantsListS.add("3:00 PM     -   5:00 PM");
                        plantsListS.add("5:00 PM     -   7:00 PM");

                    } else if (afterDate.before(date3)) {
                        plantsListS.add("Select Time Slot");
                        plantsListS.add("1:00 PM     -   3:00 PM");
                        plantsListS.add("3:00 PM     -   5:00 PM");
                        plantsListS.add("5:00 PM     -   7:00 PM");
                    } else if (afterDate.before(date4)) {
                        plantsListS.add("Select Time Slot");
                        plantsListS.add("3:00 PM     -   5:00 PM");
                        plantsListS.add("5:00 PM     -   7:00 PM");
                    } else if (afterDate.before(date5)) {

                        plantsListS.add("Select Time Slot");
                        plantsListS.add("5:00 PM     -   7:00 PM");
                    } else {
                        plantsListS.add("Slots not available today");
                    }
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DateTime.this, R.layout.design_spinner, R.id.textSpinner, plantsListS) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
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
                                tv.setTextSize(15);
                                //tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                                tv.setPadding(15, 15, 15, 15);
                                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                                tv.setPadding(15, 15, 15, 15);
                                //tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                    selectTimeSlot.setAdapter(spinnerArrayAdapter);
                } else {
                    String[] plants = new String[]{
                            "Select Time Slot",
                            "9:00 AM    -   11:00 AM",
                            "11:00 AM    -   1:00 PM",
                            "1:00 PM     -   3:00 PM",
                            "3:00 PM     -   5:00 PM",
                            "5:00 PM     -   7:00 PM"
                    };

                    List<String> plantsListt = new ArrayList<>(Arrays.asList(plants));
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DateTime.this, R.layout.design_spinner, R.id.textSpinner, plantsListt) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
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
                                tv.setTextSize(15);
                                // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                                tv.setPadding(15, 15, 15, 15);
                                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                                tv.setPadding(15, 15, 15, 15);
                                // tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
                    selectTimeSlot.setAdapter(spinnerArrayAdapter);
                }

            }
        });


     /*  }catch (Exception e)
       {
           Log.e("indexOutOfBound",""+e);


       }*/


    }

    @Override
    public void onClick(View v) {
        selectedTime = selectTimeSlot.getSelectedItem().toString();

        if (v == backImage) {
            finish();
        }

        if (v == buttonPostAddress) {

            if (selectedTime.equals("Select Time Slot") || selectedTime.equals("Slots not available today")) {
                showToast(DateTime.this, "Please Select Time Slot");
            } else if (loginStatus == 0) {
                showDialogLogin();
            } else {
                String[] parts = selectedTime.split("-");
                String first = parts[0];
                String second = parts[1];

                day = datePickerValue.getDayOfMonth();
                month = datePickerValue.getMonth() + 1;
                year = datePickerValue.getYear();


                dateStart = year + "-" + month + "-" + day + " " + first;
                dateEnd = year + "-" + month + "-" + day + " " + second;

                String startdate = getTimeToUtc(dateStart);
                String endDate = getTimeToUtc(dateEnd);


                Log.e("fgfgnfgnfgn",""+startdate+" "+getTimeToUtc(dateStart));
                postFixProblem(mainCategoryId, productId, brandId, problemDescription, startdate, endDate);
            }



        }
    }

    private void showDialogLogin() {
        try {

            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            // dialog.requestWindowFeature(Dialog.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.design_fixproblem_login);
            dialog.setCanceledOnTouchOutside(false);

            loginEmail = (EditText) dialog.findViewById(R.id.loginEmail);
            loginPassword = (EditText) dialog.findViewById(R.id.loginPassword);
            Button loginButton = (Button) dialog.findViewById(R.id.loginButton);
            facebookLogin = (ImageView) dialog.findViewById(R.id.facebookLogin);
            googleLogin = (ImageView) dialog.findViewById(R.id.googleLogin);
            login_button = (LoginButton) dialog.findViewById(R.id.login_button);

            facebookLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_button.performClick();
                }
            });

            crossDialog = (ImageView) dialog.findViewById(R.id.crossDialog);
            dialog.show();
            crossDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = loginEmail.getText().toString().trim();
                    if (loginEmail.getText().toString().trim().isEmpty()) {
                        loginEmail.setError(getText(R.string.email_mobile_error));
                        loginEmail.requestFocus();
                    } else if (loginPassword.getText().toString().trim().isEmpty()) {
                        loginPassword.setError(getText(R.string.password_error));
                        loginPassword.requestFocus();
                    } else if (isOnline(true, DateTime.this)) {
                        if (!isValidEmail(s)) {
                            emailMobileStatus = "mobile";
                            userLogin();
                        } else {
                            emailMobileStatus = "email";
                            userLogin();

                        }
                    }
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void userLogin() {
        showProgress(DateTime.this);
        HashMap hashMap = new HashMap();

        hashMap.put("realm", "customer");
        hashMap.put(emailMobileStatus, loginEmail.getText().toString().trim());
        hashMap.put("password", loginPassword.getText().toString().trim());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<SucessLogin> dataCall = apiInterface.userLogin(hashMap);
        dataCall.enqueue(new Callback<SucessLogin>() {
            @Override
            public void onResponse(Call<SucessLogin> call, Response<SucessLogin> response) {
                close();
                if (response.isSuccessful()) {

                    userId = response.body().getSuccess().getUserId();
                    userAccessToken = response.body().getSuccess().getId();
                    userEmail = response.body().getSuccess().getUser().getEmail();
                    userName = response.body().getSuccess().getUser().getFullName();
                    userMobile = response.body().getSuccess().getUser().getMobile();
                    userType = response.body().getSuccess().getUser().getCustomerType();
                    userGstNo = response.body().getSuccess().getUser().getGstNumber();
                    mobileVerify = response.body().getSuccess().getUser().getMobileVerified();
                    emailVerify = response.body().getSuccess().getUser().getEmailVerified();
                    userAddress = response.body().getSuccess().getUser().getAddresses().get(0).getValue();
                    userStreet = response.body().getSuccess().getUser().getAddresses().get(0).getStreet();
                    userLatitude = response.body().getSuccess().getUser().getAddresses().get(0).getLocation().getLat();
                    userLongitude = response.body().getSuccess().getUser().getAddresses().get(0).getLocation().getLng();
                    userImage = response.body().getSuccess().getUser().getImage();
                    getLoginType = response.body().getSuccess().getUser().getRealm();

                    baseHandler.resetTables(DataBaseHandler.TABLE_USER);
                    baseHandler.addUser(userAccessToken, userId, userEmail, userName, userMobile, userGstNo, userAddress, userStreet, userLatitude, userLongitude, userType, userImage, getLoginType);

                    SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
                    editorValues.putString("ACCESS_TOKEN", userAccessToken);
                    editorValues.putString("USER_ID", userId);
                    editorValues.putString("USER_EMAIL", userEmail);
                    editorValues.putString("USER_NAME", userName);
                    editorValues.putString("USER_MOBILE_NO", userMobile);
                    editorValues.putString("USER_GST", userGstNo);
                    editorValues.putString("USER_ADDRESS", userAddress);
                    editorValues.putString("USER_STREET", userStreet);
                    editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
                    editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
                    editorValues.putString("USER_TYPE", userType);
                    editorValues.putString("USER_IMAGE", userImage);
                    editorValues.putString("LOGIN_TYPE", getLoginType);
                    editorValues.apply();
                    editorValues.commit();
                    getUserDetailId(DateTime.this);
                    dialog.dismiss();
                    loginStatus = isLogin(DateTime.this);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(DateTime.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SucessLogin> call, Throwable t) {
                close();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void postFixProblem(String mainCatId, String productId, String brandId,
                                String problemDescr, String startDate, String endDate) {

        showProgress(DateTime.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        List<MultipartBody.Part> photos = new ArrayList<>();
        MultipartBody.Part filePartmultipleImages;

        for (int i = 0; i < imagesArray.length(); i++) {
            File file = null;
            try {
                file = new File(String.valueOf(imagesArray.get(i)));
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                filePartmultipleImages = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                photos.add(filePartmultipleImages);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("categoryId", "" + mainCatId);
            jsonObject.put("productId", "" + productId);
            jsonObject.put("brandId", brandId + "");
            jsonObject.put("problems", issuesListArray);
            jsonObject.put("problemDes", problemDescr);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("address", innerAddressObject);
            jsonObject.put("totalPrice", totalPrice);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
       String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        String jsonObjData = String.valueOf(jsonObject);
        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

        Call<SucessLogin> getPostData = apiInterface.postFixProblem(ACCESS_TOKEN, photos, objdata);


        getPostData.enqueue(new Callback<SucessLogin>() {
            @Override
            public void onResponse(Call<SucessLogin> call, Response<SucessLogin> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(DateTime.this, "Job post successfully");

                   /* try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObjError = jsonObject.getJSONObject("success");
                        JSONObject jsonObjSucess = jsonObjError.getJSONObject("msg");
                        JSONObject msg = jsonObjSucess.getJSONObject("replyCode");
                        showToast(DateTime.this, "" + jsonObjSucess.getString("replyCode"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    productName = "";
                    brandName = "";
                    Intent intent = new Intent(DateTime.this, UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(DateTime.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SucessLogin> call, Throwable t) {
                close();
                Log.e("fail", "" + t);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
