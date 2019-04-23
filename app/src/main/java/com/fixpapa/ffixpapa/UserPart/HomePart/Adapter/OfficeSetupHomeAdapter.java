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
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AmcCategory;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Bid;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.OfficeSetupPost;
import com.fixpapa.ffixpapa.UserPart.UserLogin;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;

public class OfficeSetupHomeAdapter extends RecyclerView.Adapter<OfficeSetupHomeAdapter.ViewHolder> {
    private Context context;
    List<Bid> services;
    int loginStatus;

    public OfficeSetupHomeAdapter(List<Bid> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        getUserDetailId(context);
    }

    @Override
    public OfficeSetupHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_home, parent, false);
        OfficeSetupHomeAdapter.ViewHolder viewHolder = new OfficeSetupHomeAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(OfficeSetupHomeAdapter.ViewHolder holder, final int position) {


        Bid category=services.get(position);
            holder.serviceTypeName.setText(category.getName());
            Picasso.with(context)
                    .load(Image_BASE_URL+category.getImage())
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
                        Intent intent = new Intent(context, OfficeSetupPost.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("officeSetupCat", (ArrayList<AmcCategory>) services.get(position).getServices());
                        bundle.putSerializable("officeSetupUnit", (ArrayList<String>) services.get(position).getNoOfSystems());
                        bundle.putString("officeSetupId", services.get(position).getId());
                        bundle.putString("officeSetupName", services.get(position).getName());
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
