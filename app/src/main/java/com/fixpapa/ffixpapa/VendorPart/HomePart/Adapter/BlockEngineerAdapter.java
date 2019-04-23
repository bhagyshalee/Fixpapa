package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel.GetEngineerData;
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

public class BlockEngineerAdapter extends RecyclerView.Adapter<BlockEngineerAdapter.ViewHolder> {
    private Context context;
    List<GetEngineerData> services;
   String getEngineerId="";
   boolean activeStatus;
    BlockEngineerAdapter adapter;

    public BlockEngineerAdapter(List<GetEngineerData> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @Override
    public BlockEngineerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_block_engineer, parent, false);
        BlockEngineerAdapter.ViewHolder viewHolder = new BlockEngineerAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final BlockEngineerAdapter.ViewHolder holder, final int position) {
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

        if (category.getActive()) {
            holder.ButtonBlock.setText(context.getResources().getText(R.string.block_text));
        }
        else
        {
            holder.ButtonBlock.setText(context.getResources().getText(R.string.unblock_text));
        }

        holder.ButtonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEngineerId=category.getId();
               if (holder.ButtonBlock.getText().equals(context.getResources().getText(R.string.unblock_text)))
               {
                   activeStatus=true;
               }
               else
               {
                   activeStatus=false;
               }
                inactiveEngineer(getEngineerId,holder.ButtonBlock,position);
            }
        });

    }

    private void inactiveEngineer(String engId, final Button button, final int posi) {
        showProgress(context);
        HashMap jsonObject=new HashMap();
        jsonObject.put("peopleId",engId);
        jsonObject.put("active",activeStatus);
        SharedPreferences sharedPref = context.getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.activeInactiveEngineers(ACCESS_TOKEN,jsonObject);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful())
                {
                    if (activeStatus)
                    {
                        Log.e("xcvbcbcvv",""+activeStatus+"");
                        button.setText(context.getResources().getText(R.string.block_text));
                    }
                    else
                    {
                        Log.e("xcvbcbcvv",""+activeStatus+"");
                        button.setText(context.getResources().getText(R.string.unblock_text));
                    }
                    //((Activity)context).finish();

                    /*BlockEngineer blockEngineer=new BlockEngineer();
                    blockEngineer.getAllEngineers();*/

/*
                    Intent intent=new Intent(context,BlockEngineer.class);
                   context.startActivity(intent);*/
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
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
                Log.e("bfgfgbfg",""+activeStatus+"");
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

        public ViewHolder(View itemView) {
            super(itemView);

            engineerMobileNo = (TextView) itemView.findViewById(R.id.engineerMobileNo);
            engineerMailId = (TextView) itemView.findViewById(R.id.engineerMailId);
            engineerName = (TextView) itemView.findViewById(R.id.engineerName);
            ButtonBlock = (Button) itemView.findViewById(R.id.ButtonBlock);
            engineerImage = (ImageView) itemView.findViewById(R.id.engineerImage);


        }
    }
}
