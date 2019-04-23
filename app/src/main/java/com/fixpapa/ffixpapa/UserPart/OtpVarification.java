package com.fixpapa.ffixpapa.UserPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OtpVarification extends AppCompatActivity implements View.OnClickListener {

    Button submitOTP;
    TextView resendOtp;
    EditText otpCode;
    ImageView verificationBack;
    String text = "";
    boolean delete = false;
    String userId, userOtp, userValue;
    int loginStatus = 0;
    DataBaseHandler baseHandler;
    String LOGIN_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userOtp = bundle.getString("userOtp");
            userId = bundle.getString("userId");
            userValue = bundle.getString("uservalue");

        }

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE", "");

        baseHandler = new DataBaseHandler(OtpVarification.this);
        submitOTP = (Button) findViewById(R.id.submitOTP);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        otpCode = (EditText) findViewById(R.id.otpCode);
        //otpCode.setText(userOtp);
        verificationBack = (ImageView) findViewById(R.id.verificationBack);
        submitOTP.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        verificationBack.setOnClickListener(this);
        Log.e("fgbfgbfghf", "" + userOtp + " " + userId);
        if (userOtp.equals("")) {
            if (isOnline(true, OtpVarification.this)) {
                Log.e("fgbfgb", "" + userOtp);
                resendOtpMethod();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == submitOTP) {
            if (otpCode.getText().toString().isEmpty()) {
                otpCode.setError(getText(R.string.otp_empty_text));
                otpCode.requestFocus();
            } else if (otpCode.getText().length() < 4) {
                otpCode.setError(getText(R.string.otp_valid_text));
                otpCode.requestFocus();
            } else if (isOnline(true, OtpVarification.this)) {
                submitVarificationOpt();
            }
        }
        if (v == verificationBack) {
            finish();
        }
        if (v == resendOtp) {
            if (isOnline(true, OtpVarification.this)) {
                resendOtpMethod();
            }

        }
    }

    private void resendOtpMethod() {
        HashMap hashMap = new HashMap();
        hashMap.put("peopleId", userId);
        showProgress(OtpVarification.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.resendOtp(hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    userOtp = String.valueOf(response.body().getSuccess().getData().getMobOtp().getOtp());
                    // otpCode.setText(userOtp);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        System.out.println("error response " + jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
            }
        });
    }

    private void submitVarificationOpt() {
        HashMap hashMap = new HashMap();
        hashMap.put("peopleId", userId);
        hashMap.put("otp", userOtp);
        showProgress(OtpVarification.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.verifyOtp(hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    baseHandler.addSearchDataUpdate(response.body().getSuccess().getData().getId(), response.body().getSuccess().getData().getFullName(), response.body().getSuccess().getData().getGstNumber(), response.body().getSuccess().getData().getImage(), response.body().getSuccess().getData().getMobile(), response.body().getSuccess().getData().getEmail());
                    SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
//                    editorValues.putString("ACCESS_TOKEN", userAccessToken);
                    editorValues.putString("USER_ID", response.body().getSuccess().getData().getId());
                    editorValues.putString("USER_EMAIL", response.body().getSuccess().getData().getEmail());
                    editorValues.putString("USER_NAME", response.body().getSuccess().getData().getFullName());
                    editorValues.putString("USER_MOBILE_NO", response.body().getSuccess().getData().getMobile());
                    editorValues.putString("USER_GST", response.body().getSuccess().getData().getGstNumber());
//                    editorValues.putString("USER_ADDRESS", userAddress);
//                    editorValues.putString("USER_STREET", userStreet);
//                    editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
//                    editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
//                    editorValues.putString("USER_TYPE", userType);
                    editorValues.putString("USER_IMAGE",  response.body().getSuccess().getData().getImage());
//                    editorValues.putString("LOGIN_TYPE", getLoginType);
                    editorValues.apply();
                    editorValues.commit();
                    if (userValue != null) {
                        finish();
                    } else {

                        loginStatus = isLogin(OtpVarification.this);

                        if (loginStatus != 0) {
                            if (LOGIN_TYPE.equals("")) {
                                Intent i = new Intent(OtpVarification.this, UserHomeScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            if (LOGIN_TYPE.equals("vendor")) {
                                Intent i = new Intent(OtpVarification.this, VendorHomeScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } else if (LOGIN_TYPE.equals("customer")) {
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();

                                Intent i = new Intent(OtpVarification.this, UserHomeScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } else if (LOGIN_TYPE.equals("engineer")) {

                                Intent intent = new Intent(OtpVarification.this, EngineerHomeScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(OtpVarification.this, UserLogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(OtpVarification.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("error response " + jsonObject.toString());
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
            }
        });
    }
}
