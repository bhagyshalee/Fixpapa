package com.fixpapa.ffixpapa.UserPart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ForgotPassword extends AppCompatActivity {

    ImageView forgotBack;
    EditText forgotEmail;
    Button forgotSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotBack = (ImageView) findViewById(R.id.forgotBack);
        forgotEmail = (EditText) findViewById(R.id.forgotEmail);
        forgotSubmitButton = (Button) findViewById(R.id.forgotSubmitButton);
        forgotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgotSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forgotEmail.getText().toString().isEmpty())
                {
                    forgotEmail.setError(getString(R.string.email_empty_error));
                }
                else if (!isValidEmail(forgotEmail.getText().toString().trim())) {
                    forgotEmail.setError(getText(R.string.email_pattern_error));
                    forgotEmail.requestFocus();
                }
                else   if (isOnline(true, ForgotPassword.this))
                {
                    forgotPass(forgotEmail.getText().toString());
                }

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


    private void forgotPass(String s) {
        HashMap hashMap=new HashMap();
        hashMap.put("email",s);
        Utility.showProgress(ForgotPassword.this);
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call=apiInterface.resetPassword(hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                Utility.close();
                if (response.isSuccessful())
                {
                    Utility.showToast(ForgotPassword.this,""+response.body().getSuccess().getMsg().getReplyCode());
                    Intent intent = new Intent(ForgotPassword.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(ForgotPassword.this,""+jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Utility.close();
            }
        });
    }
}
