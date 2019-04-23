package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.UserPart.OtpEmailVarification;
import com.fixpapa.ffixpapa.UserPart.OtpVarification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UpdateEmailMobile extends AppCompatActivity {
String ACCESS_TOKEN,USER_EMAIL,USER_ID,USER_MOBILE_NO;
    ImageView forgotBack;
    EditText forgotEmail;
    Button forgotSubmitButton;
    TextView titleOfActivity, instructionTag;
    String onStatus;
    DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotBack = (ImageView) findViewById(R.id.forgotBack);
        forgotEmail = (EditText) findViewById(R.id.forgotEmail);
        forgotSubmitButton = (Button) findViewById(R.id.forgotSubmitButton);
        titleOfActivity = (TextView) findViewById(R.id.titleOfActivity);
        instructionTag = (TextView) findViewById(R.id.instructionTag);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
         ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
         USER_EMAIL = sharedPref.getString("USER_EMAIL","");
         USER_ID = sharedPref.getString("USER_ID","");
         USER_MOBILE_NO = sharedPref.getString("USER_MOBILE_NO","");

        getUserDetailId(UpdateEmailMobile.this);
        dataBaseHandler = new DataBaseHandler(UpdateEmailMobile.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            onStatus = bundle.getString("email");
            if (onStatus.equals("email")) {
                titleOfActivity.setText("Update Email");
                instructionTag.setText(getText(R.string.enter_email_ifchange_text));
                forgotEmail.setText(USER_EMAIL);
                forgotEmail.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);

            } else {
                titleOfActivity.setText("Update Mobile");
                instructionTag.setText(getText(R.string.enter_mobile_ifchange_text));
                forgotEmail.setHint(getText(R.string.mobile_number_text));
                forgotEmail.setText(USER_MOBILE_NO);
                forgotEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
                forgotEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

            }
        }
        forgotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgotSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (forgotEmail.getText().toString().trim().isEmpty()) {
                    if (onStatus.equals("email")) {
                        forgotEmail.setError(getText(R.string.email_empty_error));
                        forgotEmail.requestFocus();
                    } else if (onStatus.equals("mobile")) {
                        forgotEmail.setError(getText(R.string.mobile_empty_error));
                        forgotEmail.requestFocus();
                    }
                } else if (forgotEmail.getText().toString().equals(USER_EMAIL)) {
                    showToast(UpdateEmailMobile.this, "Your previous email is same");
                } else if (forgotEmail.getText().toString().equals(USER_MOBILE_NO)) {
                    showToast(UpdateEmailMobile.this, "Your previous mobile is same");
                } else if (onStatus.equals("email")) {
                    if (!isValidEmail(forgotEmail.getText().toString())) {
                        forgotEmail.setError(getText(R.string.email_pattern_error));
                        forgotEmail.requestFocus();
                    }
                    else if (isOnline(true, UpdateEmailMobile.this))
                    {

                        onStatus = "emailId";
                        if (forgotEmail.getText().toString().equals(USER_EMAIL))
                        {
                            showToast(UpdateEmailMobile.this, "Email already exist");
                        }
                        else
                        {
                            updateEmail(onStatus);
                        }
                    }
                } else if (onStatus.equals("mobile")) {
                    if (forgotEmail.getText().length()<10 || forgotEmail.getText().length()>10) {
                        forgotEmail.setError(getText(R.string.contact_valid_error));
                        forgotEmail.requestFocus();
                    }
                    else
                    {
                        onStatus = "mobileNumber";

                        if (forgotEmail.getText().toString().equals(USER_MOBILE_NO))
                        {
                            showToast(UpdateEmailMobile.this, "Mobile already exist");
                        }
                        else
                        {
                            updateMobile(onStatus);
                        }


                    }
                }

            }
        });

    }

    private void updateMobile(String onStatus) {
        showProgress(UpdateEmailMobile.this);
        HashMap hashMap = new HashMap();
        hashMap.put(onStatus, forgotEmail.getText().toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateUserMobile(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    // Log.e("hifejk",""+response.body().getSuccess().getMsg().getReplyCode());
                    Intent intent = new Intent(UpdateEmailMobile.this, OtpVarification.class);
                    intent.putExtra("userOtp", ""+response.body().getSuccess().getData().getMobOtp().getOtp());
                    intent.putExtra("userId", "" + USER_ID);
                    intent.putExtra("uservalue", "" + "update");
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(UpdateEmailMobile.this,""+jsonObjError.getString("message"));
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

    private void updateEmail(String onStatus) {
        showProgress(UpdateEmailMobile.this);
        HashMap hashMap = new HashMap();
        hashMap.put(onStatus, forgotEmail.getText().toString());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateUserEmail(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    //dataBaseHandler.UpdateEmailMobile(USER_ID, response.body().getSuccess().getData().getEmailId(), USER_MOBILE_NO);
                    //forgotEmail.setText(forgotEmail.getText().toString().trim());
                    // showVerifyPopup();
                    showToast(UpdateEmailMobile.this, ""+response.body().getSuccess().getMsg().getReplyMessage());
                    Intent intent = new Intent(UpdateEmailMobile.this, OtpEmailVarification.class);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(UpdateEmailMobile.this,""+jsonObjError.getString("message"));
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

/*
    private void showVerifyPopup() {
        try {
            final Dialog dialog = new Dialog(UpdateEmailMobile.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.email_otp_verify);
            dialog.setCanceledOnTouchOutside(false);
            ImageView crossDialog;
            final EditText etOtp;
            Button buttonSubmit;
            crossDialog = (ImageView) dialog.findViewById(R.id.crossDialog);
            etOtp = (EditText) dialog.findViewById(R.id.etOtp);
            buttonSubmit = (Button) dialog.findViewById(R.id.buttonSubmit);
            dialog.show();

            crossDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if (etOtp.getText().toString().isEmpty()) {
                        etOtp.setError(getText(R.string.otp_empty_text));
                        etOtp.requestFocus();
                    } else if (etOtp.getText().length() < 4) {
                        etOtp.setError(getText(R.string.otp_valid_text));
                        etOtp.requestFocus();
                    } if (isOnline(true, UpdateEmailMobile.this))
                    {
                        checkOtpForEmail(etOtp.getText().toString());
                    }
                }
            });


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }
*/


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
