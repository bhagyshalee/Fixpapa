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
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;

import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class AddpartAdapter extends RecyclerView.Adapter<AddpartAdapter.ViewHolder> {
    private Context context;
    List<AddPart> services;
    BillModel billModel;

    public AddpartAdapter(BillModel billModel, List<AddPart> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.billModel = billModel;
    }

    @Override
    public AddpartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_addpart, parent, false);
        AddpartAdapter.ViewHolder viewHolder = new AddpartAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final AddpartAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final AddPart category = services.get(position);
        holder.adpartName.setText(category.getPartName());
        holder.adpartNumber.setText(category.getPartNumber());
        holder.adPartCost.setText("" + category.getPartCost() + "/-");
        Log.e("gerjgiore",""+category.getPartName());
        holder.adpartStatus.setText(billModel.getClientResponse());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView adpartName, adpartNumber, adPartCost, adpartStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            adpartName = (TextView) itemView.findViewById(R.id.adpartName);
            adpartNumber = (TextView) itemView.findViewById(R.id.adpartNumber);
            adPartCost = (TextView) itemView.findViewById(R.id.adPartCost);
            adpartStatus = (TextView) itemView.findViewById(R.id.adpartStatus);


        }
    }
}