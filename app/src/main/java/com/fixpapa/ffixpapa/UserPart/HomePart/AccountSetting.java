package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.ChangePass.Succ;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class AccountSetting extends AppCompatActivity {

    LinearLayout changeEmailLayout, changeMobileLayout, changePasswordLayout;
    DataBaseHandler dataBaseHandler;
    String ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        getUserDetailId(AccountSetting.this);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

        dataBaseHandler=new DataBaseHandler(AccountSetting.this);
        changeEmailLayout = (LinearLayout) findViewById(R.id.changeEmailLayout);
        changeMobileLayout = (LinearLayout) findViewById(R.id.changeMobileLayout);
        changePasswordLayout = (LinearLayout) findViewById(R.id.changePasswordLayout);
        changeEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSetting.this, UpdateEmailMobile.class);
                intent.putExtra("email", "email");
                startActivity(intent);
            }
        });
        changeMobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSetting.this, UpdateEmailMobile.class);
                intent.putExtra("email", "mobile");
                startActivity(intent);
            }
        });
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(AccountSetting.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.design_change_password);
                    dialog.setCanceledOnTouchOutside(false);
                    final EditText oldPassword, newPassword, confirmPassword;
                    Button submitButton;
                    ImageView crossDialog;
                    oldPassword = (EditText) dialog.findViewById(R.id.oldPassword);
                    newPassword = (EditText) dialog.findViewById(R.id.newPassword);
                    confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword);
                    submitButton = (Button) dialog.findViewById(R.id.submitButton);
                    crossDialog = (ImageView) dialog.findViewById(R.id.crossDialog);
                    dialog.show();
                    crossDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (oldPassword.getText().toString().isEmpty()) {
                                oldPassword.setError(getString(R.string.required_empty_error));
                                oldPassword.requestFocus();
                            } else if (newPassword.getText().toString().isEmpty()) {
                                newPassword.setError(getString(R.string.required_empty_error));
                                newPassword.requestFocus();
                            } else if (confirmPassword.getText().toString().isEmpty()) {
                                confirmPassword.setError(getString(R.string.required_empty_error));
                                confirmPassword.requestFocus();
                            } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                confirmPassword.setError(getString(R.string.confirm_password_error));
                                confirmPassword.requestFocus();
                            }
                            else if (isOnline(true, AccountSetting.this)) {
                                dialog.dismiss();
                                changePassword(oldPassword.getText().toString().trim(), newPassword.getText().toString().trim());
                            }
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        });

    }

    private void changePassword(String old, String newp) {
        showProgress(AccountSetting.this);
        JSONObject jsonObject = new JSONObject();
        HashMap hashMap = new HashMap();

        hashMap.put("newPassword", newp);
        hashMap.put("oldPassword", old);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Succ> successModelCall = apiInterface.changePassword(ACCESS_TOKEN, hashMap);
        successModelCall.enqueue(new Callback<Succ>() {
            @Override
            public void onResponse(Call<Succ> call, Response<Succ> response) {
                close();
               // Log.e("fifowef","hhhhhhhhhhh");
                if (response.isSuccessful()) {
                    showToast(AccountSetting.this, "successfully password changed");
                    dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);
                    logoutCall();
                   /* Intent intent=new Intent(AccountSetting.this,UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(AccountSetting.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Succ> call, Throwable t) {
                close();
            }
        });
    }
    private void logoutCall() {
        showProgress(AccountSetting.this);
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> call=apiInterface.logoutAllServices(ACCESS_TOKEN);
        call.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful())
                {
                    Intent intent=new Intent(AccountSetting.this, UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(AccountSetting.this, "" + jsonObjError.getString("message"));
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

}
