package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

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
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsVendor;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.NewJobsData;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class NewJobAdapter extends RecyclerView.Adapter<NewJobAdapter.ViewHolder> {
    private Context context;
    List<NewJobsData> services;
    List<String> stringList;
    int loginStatus;
    List<SuccessModelVendor> getAll;

    public NewJobAdapter(List<NewJobsData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();
    }

    @Override
    public NewJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_new_job, parent, false);
        NewJobAdapter.ViewHolder viewHolder = new NewJobAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NewJobAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final NewJobsData category = services.get(position);
        holder.jobTitle.setText("" + category.getCategory().getName());

        for (int i = 0; i < category.getProblems().size(); i++) {
            if (category.getProblems().size() == 1) {
                holder.jobDescription.setText(category.getProblems().get(i).getProbContent());
            } else {
                holder.jobDescription.setText(category.getProblems().get(i).getProbContent() + " and other Issues");
            }
        }
        if (category.getStartDate() != null) {
            holder.jobPlaced.setText("Placed On: " + getDateFromUtc(category.getCreatedAt()));
        }
        //holder.jobDescription.setText("" + category.getProblemDes());

      /*  if (services.get(position).getBrand()!=null) {
            holder.jobPlaced.setText("" + category.getBrand().getName());
        }
        else
        {
            holder.jobPlaced.setVisibility(View.GONE);
        }*/
        holder.jobPrice.setText("" + category.getTotalPrice()+" /-");
            holder.jobAddress.setText("" + category.getAddress().getStreet()+" "+
            category.getAddress().getValue());

        holder.layoutItemData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewJobsVendor.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                bundle.putSerializable("sendCategory", category.getCategory().getName());
                bundle.putSerializable("subCategory", category.getProduct().getName());
                if(category.getBrand()!=null) {
                    bundle.putSerializable("brand", category.getBrand().getName());
                }
                bundle.putSerializable("address", (AddressesModel) category.getAddress());
                bundle.putSerializable("jobImage", (ArrayList<String>)  category.getImage());
                bundle.putString("jobId", category.getId());
                bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                bundle.putString("status", String.valueOf(category.getStatus()));
                bundle.putString("assignedStatus", "notassign");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobDescription, jobPlaced, jobPrice, jobAddress;
        LinearLayout layoutItemData;

        public ViewHolder(View itemView) {
            super(itemView);

            jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
            jobDescription = (TextView) itemView.findViewById(R.id.jobDescription);
            jobPlaced = (TextView) itemView.findViewById(R.id.jobPlaced);
            jobPrice = (TextView) itemView.findViewById(R.id.jobPrice);
            jobAddress = (TextView) itemView.findViewById(R.id.jobAddress);
            layoutItemData = (LinearLayout) itemView.findViewById(R.id.layoutItemData);


        }
    }
}
