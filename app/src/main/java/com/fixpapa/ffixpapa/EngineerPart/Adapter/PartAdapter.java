package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.ViewHolder> {
    private Context context;
    List<AddPart> services;
    List<String> stringList;
    int loginStatus;

    public PartAdapter(List<AddPart> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();


    }

    @Override
    public PartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_totalprice, parent, false);
        PartAdapter.ViewHolder viewHolder = new PartAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(PartAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final AddPart category = services.get(position);

        Log.e("fdjvghkdjf",""+category.getPartName());
        holder.servicePrduct.setText("" + category.getPartName());
        holder.servicePrice.setText("" + category.getPartCost());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView servicePrduct,servicePrice;

        public ViewHolder(View itemView) {
            super(itemView);

            servicePrduct = (TextView) itemView.findViewById(R.id.servicePrduct);
            servicePrice = (TextView) itemView.findViewById(R.id.servicePrice);
        }
    }
}
