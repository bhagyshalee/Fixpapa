package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule.AmcPost;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Amc;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AmcCategory;
import com.fixpapa.ffixpapa.UserPart.UserLogin;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;

public class CategoryMaintainance extends RecyclerView.Adapter<CategoryMaintainance.ViewHolder> {
    private Context context;

    List<Amc> services;
    String typeData;
    int loginStatus;

    public CategoryMaintainance(List<Amc> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.typeData = typeData;
        getUserDetailId(context);
    }

    @Override
    public CategoryMaintainance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_category, parent, false);
        CategoryMaintainance.ViewHolder viewHolder = new CategoryMaintainance.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CategoryMaintainance.ViewHolder holder, final int position) {
        final Amc category=services.get(position);
        holder.categoryTitle.setText(category.getName());
        /*holder.serviceTypeName.setText(category.getName());
        Picasso.with(context)
                .load(Image_BASE_URL+category.getImage())
                .placeholder(R.drawable.about_us)
                .error(R.drawable.ic_user)
                .into(holder.serviceTypeImage);*/
        holder.layoutRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStatus = isLogin(context);
                if (loginStatus == 0) {
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
                    Intent intent = new Intent(context, AmcPost.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("amcCat", (ArrayList<AmcCategory>) services.get(position).getCategory());
                    bundle.putSerializable("amcUnit", (ArrayList<String>) services.get(position).getNoOfUnits());
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
        TextView categoryTitle;
        LinearLayout layoutRec;


        public ViewHolder(View itemView) {
            super(itemView);

            categoryTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            layoutRec = (LinearLayout) itemView.findViewById(R.id.layoutRec);
        }
        }
    }