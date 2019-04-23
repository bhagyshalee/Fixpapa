package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel.GetEngineerData;
import com.fixpapa.ffixpapa.VendorPart.UpdateEngineerProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UpdateEngineerAdapter extends RecyclerView.Adapter<UpdateEngineerAdapter.ViewHolder> {
    private Context context;
    List<GetEngineerData> services;

    public UpdateEngineerAdapter(List<GetEngineerData> services, Context context) {
        this.services = services;
        this.context = context;
        Log.e("jkdfsjkdfsf", "mghmghmghmghm");

    }

    @Override
    public UpdateEngineerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_udpate_engineer, parent, false);
        UpdateEngineerAdapter.ViewHolder viewHolder = new UpdateEngineerAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(UpdateEngineerAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final GetEngineerData category = services.get(position);
        Picasso.with(context)
                .load(Image_BASE_URL + category.getImage())
                .placeholder(R.drawable.user_circle_img)
                .error(R.drawable.user_circle_img)
                .into(holder.engineerImage);
        holder.engineerName.setText(category.getFullName());
        holder.engineerMailId.setText(category.getEmail());
        holder.engineerMobileNo.setText(category.getMobile());
        holder.ButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateEngineerProfile.class);
                Bundle bundle = new Bundle();
                // bundle.putInt("getEngineerStatus",2);
                bundle.putString("engineerId", category.getId());
                bundle.putString("engineerName", category.getFullName());
                bundle.putString("engineerEmail", category.getEmail());
                bundle.putString("engineerMobile", category.getMobile());
                bundle.putInt("engineerExp", Integer.parseInt(category.getExp()));
                bundle.putString("engineerAddress", category.getAddress());
                bundle.putString("engineerImage", category.getImage());
                bundle.putString("loginStatus", "vendor");
                bundle.putSerializable("engineerexpId", (ArrayList<String>) category.getExpertiseIds());
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        holder.ButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(context);
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
                    oldPassword.setHint("Vendor Password");
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
                                oldPassword.setError(context.getResources().getString(R.string.required_empty_error));
                                oldPassword.requestFocus();
                            } else if (newPassword.getText().toString().isEmpty()) {
                                newPassword.setError(context.getResources().getString(R.string.required_empty_error));
                                newPassword.requestFocus();
                            } else if (confirmPassword.getText().toString().isEmpty()) {
                                confirmPassword.setError(context.getResources().getString(R.string.required_empty_error));
                                confirmPassword.requestFocus();
                            } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                confirmPassword.setError(context.getResources().getString(R.string.confirm_password_error));
                                confirmPassword.requestFocus();
                            } else if (isOnline(true, context)) {
                                dialog.dismiss();
                                changePassword(oldPassword.getText().toString().trim(), newPassword.getText().toString().trim(),
                                        category.getId());
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

    private void changePassword(String vendorPass, String newPass, String engineerId) {
        showProgress(context);
        HashMap hashMap = new HashMap();
        hashMap.put("engineerId", engineerId);
        hashMap.put("vendorPwd", vendorPass);
        hashMap.put("newPwd", newPass);
        SharedPreferences sharedPref = context.getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.changeEngineerPassword(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    //Log.e("fdfjd",""+response.body().getSuccess().getMsg().getReplyCode());
                    if (response.body() != null) {
                        showToast(context, response.body().getSuccess().getMsg().getReplyMessage());
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
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
            }
        });
    }


    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView engineerMobileNo, engineerMailId, engineerName;
        Button ButtonEditProfile, ButtonChangePassword;
        ImageView engineerImage;

        public ViewHolder(View itemView) {
            super(itemView);

            engineerMobileNo = (TextView) itemView.findViewById(R.id.engineerMobileNo);
            engineerMailId = (TextView) itemView.findViewById(R.id.engineerMailId);
            engineerName = (TextView) itemView.findViewById(R.id.engineerName);
            ButtonEditProfile = (Button) itemView.findViewById(R.id.ButtonEditProfile);
            ButtonChangePassword = (Button) itemView.findViewById(R.id.ButtonChangePassword);
            engineerImage = (ImageView) itemView.findViewById(R.id.engineerImage);


        }
    }
}
