package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel.GetEngineerData;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class FreeEngineerAdapter extends RecyclerView.Adapter<FreeEngineerAdapter.ViewHolder> {
    private Context context;
    List<GetEngineerData> services;
    String jobId;

    public FreeEngineerAdapter(String jobId, List<GetEngineerData> services, Context context) {
        this.services = services;
        this.context = context;
        this.jobId = jobId;
    }

    @Override
    public FreeEngineerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_block_engineer, parent, false);
        FreeEngineerAdapter.ViewHolder viewHolder = new FreeEngineerAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final FreeEngineerAdapter.ViewHolder holder, final int position) {
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
        holder.ButtonBlock.setVisibility(View.GONE);

        holder.layoutEngineerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignedEngineer(jobId, category.getId());
            }
        });
    }

    private void assignedEngineer(String jobId, String id) {
        showProgress(context);
        HashMap hashMap = new HashMap();
        hashMap.put("requestjobId", jobId);
        hashMap.put("engineerId", id);
        SharedPreferences sharedPref = context.getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.assignEngineer(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SmallSucess>() {
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(context, "Job successfully assigned");
                    ((Activity) context).finish();
                    Intent intent=new Intent(context, VendorHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

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
            public void onFailure(Call<SmallSucess> call, Throwable t) {
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
        Button ButtonBlock;
        ImageView engineerImage;
        LinearLayout layoutEngineerSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            engineerMobileNo = (TextView) itemView.findViewById(R.id.engineerMobileNo);
            engineerMailId = (TextView) itemView.findViewById(R.id.engineerMailId);
            engineerName = (TextView) itemView.findViewById(R.id.engineerName);
            ButtonBlock = (Button) itemView.findViewById(R.id.ButtonBlock);
            engineerImage = (ImageView) itemView.findViewById(R.id.engineerImage);
            layoutEngineerSelect = (LinearLayout) itemView.findViewById(R.id.layoutEngineerSelect);


        }
    }
}
