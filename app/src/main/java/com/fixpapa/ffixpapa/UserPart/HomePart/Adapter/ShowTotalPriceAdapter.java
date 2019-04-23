package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;

import java.util.List;

public class ShowTotalPriceAdapter extends RecyclerView.Adapter<ShowTotalPriceAdapter.ViewHolder> {
    private Context context;
    List<String> services;
    List<Integer> servicesPrice;

    public ShowTotalPriceAdapter(List<Integer> servicesPrice,List<String> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.servicesPrice = servicesPrice;

    }

    @Override
    public ShowTotalPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_totalprice, parent, false);
        ShowTotalPriceAdapter.ViewHolder viewHolder = new ShowTotalPriceAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ShowTotalPriceAdapter.ViewHolder holder, final int position) {
        holder.servicePrduct.setText(services.get(position));
        holder.servicePrice.setText("Rs "+servicesPrice.get(position));
        }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicePrduct,servicePrice;

        public ViewHolder(View itemView) {
            super(itemView);

            servicePrice = (TextView) itemView.findViewById(R.id.servicePrice);
            servicePrduct = (TextView) itemView.findViewById(R.id.servicePrduct);
        }
    }
}
