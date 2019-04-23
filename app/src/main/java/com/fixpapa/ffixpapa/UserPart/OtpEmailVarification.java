package com.fixpapa.ffixpapa.UserPart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class OtpEmailVarification extends AppCompatActivity implements View.OnClickListener {

    Button submitOTP;
    TextView resendOtp,textEmailConfirm;
    EditText otpCode;
    ImageView verificationBack;
    String text = "";
    boolean delete = false;
    String userId, userOtp,userValue;
    int loginStatus=0;
    DataBaseHandler baseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);
        baseHandler = new DataBaseHandler(OtpEmailVarification.this);
        submitOTP = (Button) findViewById(R.id.submitOTP);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        textEmailConfirm = (TextView) findViewById(R.id.textEmailConfirm);
        textEmailConfirm.setText(getResources().getString(R.string.verification_email_text));
        otpCode = (EditText) findViewById(R.id.otpCode);
        otpCode.setText(userOtp);
        resendOtp.setVisibility(View.GONE);
        verificationBack = (ImageView) findViewById(R.id.verificationBack);
        submitOTP.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        verificationBack.setOnClickListener(this);

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
            }  else if (isOnline(true, OtpEmailVarification.this)) {
                checkOtpForEmail(otpCode.getText().toString());
            }
        }
        if (v==verificationBack)
        {
            finish();
        }

    }

    private void checkOtpForEmail(String otp) {
        showProgress(OtpEmailVarification.this);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        HashMap hashMap = new HashMap();
        hashMap.put("otp",otp);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateEmail(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    baseHandler.addSearchDataUpdate(response.body().getSuccess().getData().getId(), response.body().getSuccess().getData().getFullName(), response.body().getSuccess().getData().getGstNumber(), response.body().getSuccess().getData().getImage(),response.body().getSuccess().getData().getMobile(),response.body().getSuccess().getData().getEmail());
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
                    finish();
                    Intent intent=new Intent(OtpEmailVarification.this,UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    showToast(OtpEmailVarification.this,""+response.body().getSuccess().getMsg().getReplyCode());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(OtpEmailVarification.this,""+jsonObjError.getString("message"));
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
}
