package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.RentPost;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Rent;
import com.fixpapa.ffixpapa.UserPart.UserLogin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;

public class RentHomeAdapter extends RecyclerView.Adapter<RentHomeAdapter.ViewHolder> {
    private Context context;
    List<Rent> services;
    List<String> stringList;
    List<String> stringListId;
    int loginStatus;

    public RentHomeAdapter(List<Rent> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();
        stringListId = new ArrayList<>();
        getUserDetailId(context);

    }

    @Override
    public RentHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_home, parent, false);
        RentHomeAdapter.ViewHolder viewHolder = new RentHomeAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RentHomeAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        Rent category = services.get(position);
        holder.serviceTypeName.setText(category.getName());
        stringList.add(category.getName());
        stringListId.add(category.getId());
        Picasso.with(context)
                .load(Image_BASE_URL + category.getImage())
                .placeholder(R.drawable.icon_fixpapa)
                .error(R.drawable.icon_fixpapa)
                .into(holder.serviceTypeImage);
        holder.homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStatus = isLogin(context);
                if (loginStatus==0) {
                    try {
                        final Dialog dialog=new Dialog(context);
                        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.design_login_confirmation);
                        dialog.setCanceledOnTouchOutside(true);
                        TextView buttonNotNow,buttonLogin;
                        buttonNotNow=(TextView) dialog.findViewById(R.id.buttonNotNow);
                        buttonLogin=(TextView) dialog.findViewById(R.id.buttonLogin);
                        dialog.show();
                        buttonNotNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        buttonLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent=new Intent(context, UserLogin.class);
                                context.startActivity(intent);
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    Intent intent = new Intent(context, RentPost.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("amcCat", (ArrayList<String>) stringList);
                    bundle.putSerializable("amcCatId", (ArrayList<String>) stringListId);
                    // bundle.putSerializable("amcUnit", (ArrayList<String>) services.get(position).getName());
                    bundle.putString("amcId", services.get(position).getId());
                    bundle.putString("amcName", services.get(position).getName());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceTypeImage;
        TextView serviceTypeName;
        LinearLayout homeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            serviceTypeImage = (ImageView) itemView.findViewById(R.id.serviceTypeImage);
            serviceTypeName = (TextView) itemView.findViewById(R.id.serviceTypeName);
            homeLayout = (LinearLayout) itemView.findViewById(R.id.homeLayout);


        }
    }
}
