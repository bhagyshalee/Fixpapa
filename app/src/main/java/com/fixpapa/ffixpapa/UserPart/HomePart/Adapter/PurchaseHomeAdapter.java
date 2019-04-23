package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Newpurchase;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Product;
import com.fixpapa.ffixpapa.UserPart.HomePart.PurchasePost;
import com.fixpapa.ffixpapa.UserPart.UserLogin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Config.mainCategoryId;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;

public class PurchaseHomeAdapter extends RecyclerView.Adapter<PurchaseHomeAdapter.ViewHolder> {
    private Context context;
    List<Newpurchase> services;
    int loginStatus;

    public PurchaseHomeAdapter(List<Newpurchase> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        getUserDetailId(context);
    }

    @Override
    public PurchaseHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_home, parent, false);
        PurchaseHomeAdapter.ViewHolder viewHolder = new PurchaseHomeAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final PurchaseHomeAdapter.ViewHolder holder, final int position) {

        final Newpurchase category = services.get(position);
        holder.serviceTypeName.setText(category.getName());
        Picasso.with(context)
                .load(Image_BASE_URL + category.getImage())
                .placeholder(R.drawable.icon_fixpapa)
                .error(R.drawable.icon_fixpapa)
                .into(holder.serviceTypeImage);
        holder.homeLayout.setOnClickListener(new View.OnClickListener() {
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
                    mainCategoryId = services.get(position).getId();

                    if (category.getProducts().size() == 1) {
                        if (category.getProducts().get(0).getName().equals(holder.serviceTypeName.getText().toString())) {
                            productId=category.getProducts().get(0).getId();
                            Intent intent = new Intent(context, PurchasePost.class);
                            Bundle bundle = new Bundle();
                         /*   for (int i=0;i<category.getProducts().size();i++) {
                                bundle.putSerializable("getBrands", (ArrayList<Brand>) category.getProducts().get(0).getBrand());
                            }*/
                            bundle.putSerializable("getProducts", (ArrayList<Product>) services.get(0).getProducts());

                            //bundle.putSerializable("getIssues", (ArrayList<Problem>) services.get(position).getProblems());
                            bundle.putString("getMainCatName", category.getName());
                            bundle.putString("getMainCatId", category.getId());
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                        } else {
                            Intent intent = new Intent(context, PurchasePost.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("getProducts", (ArrayList<Product>) services.get(position).getProducts());
                          /*  for (int i=0;i<category.getProducts().size();i++) {
                                bundle.putSerializable("getBrands", (ArrayList<Brand>) category.getProducts().get(i).getBrand());
                            }*/
                            bundle.putString("getMainCatName", category.getName());
                            bundle.putString("getMainCatId", category.getId());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } else if (category.getProducts().size() > 1 && category.getProducts().get(0).getBrandIds().size() == 0) {

                        Intent intent = new Intent(context, PurchasePost.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("getProducts", (ArrayList<Product>) services.get(position).getProducts());
                       /* for (int i=0;i<category.getProducts().size();i++) {
                            bundle.putSerializable("getBrands", (ArrayList<Brand>) category.getProducts().get(i).getBrand());
                        }*/
                        bundle.putString("getMainCatName", category.getName());
                        bundle.putString("getMainCatId", category.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    } else {
                        Intent intent = new Intent(context, PurchasePost.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("getProducts", (ArrayList<Product>) services.get(position).getProducts());
                     /*   for (int i=0;i<category.getProducts().size();i++) {
                            bundle.putSerializable("getBrands", (ArrayList<Brand>) category.getProducts().get(i).getBrand());
                        }*/
                        bundle.putString("getMainCatName", category.getName());
                        bundle.putString("getMainCatId", category.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

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
