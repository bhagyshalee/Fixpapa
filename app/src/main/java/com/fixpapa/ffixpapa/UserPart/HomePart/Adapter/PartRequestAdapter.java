package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;

import java.util.List;

public class PartRequestAdapter extends RecyclerView.Adapter<PartRequestAdapter.ViewHolder> {
    private Context context;
    List<Integer> servicesPrice;
    List<AddPart> addPart;

    public PartRequestAdapter(List<AddPart> addPart, Context context) {
        super();
        this.addPart = addPart;
        this.context = context;
        this.servicesPrice = servicesPrice;

    }

    @Override
    public PartRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_totalprice, parent, false);
        PartRequestAdapter.ViewHolder viewHolder = new PartRequestAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final PartRequestAdapter.ViewHolder holder, final int position) {
        holder.servicePrduct.setText(addPart.get(position).getPartName());
        holder.servicePrice.setText(addPart.get(position).getPartCost()+"/-");
        }

    @Override
    public int getItemCount() {
        return addPart.size();
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
