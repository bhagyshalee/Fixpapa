package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {
    private Context context;
    List<Problems> services;
    List<String> stringList;
    int loginStatus;

    public IssuesAdapter(List<Problems> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();


    }

    @Override
    public IssuesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_totalprice, parent, false);
        IssuesAdapter.ViewHolder viewHolder = new IssuesAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(IssuesAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final Problems category = services.get(position);

        holder.servicePrduct.setText("" + category.getProbContent());
        holder.servicePrice.setText("" + category.getPrice());

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
