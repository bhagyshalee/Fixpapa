package com.fixpapa.ffixpapa.VendorPart.HomePart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerCompleteJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class VendorOpenAdapter extends RecyclerView.Adapter<VendorOpenAdapter.ViewHolder> {
    private Context context;
    List<SampleData> services;
    List<String> stringList;
    int loginStatus;
    String color, FragStatus;


    public VendorOpenAdapter(String FragStatus, String color, List<SampleData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();

        this.FragStatus = FragStatus;
        this.color = color;


    }

    @Override
    public VendorOpenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        VendorOpenAdapter.ViewHolder viewHolder = new VendorOpenAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final VendorOpenAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final SampleData category = services.get(position);
        holder.bookingissue.setVisibility(View.GONE);

        if (color.equals("blue")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.blue));
        }
        if (color.equals("green")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (color.equals("red")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.red));
        }
        Log.e("ffddddddbdfg",""+getDateFromUtc(category.getCreatedAt())+" "+category.getCreatedAt());
        holder.bookingTitle.setText("" + category.getCategory().getName());
        holder.bookingDate.setText("Placed On: " + getDateFromUtc(category.getCreatedAt()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        if (FragStatus.equals("open")) {
            if (category.getVendorAssigned() && !category.getEngineerAssigned() || !category.getVendorAssigned() && !category.getEngineerAssigned()) {
                holder.bookingStatus.setText("Engineer not assigned");
            } else if (category.getVendorAssigned() && category.getEngineerAssigned()) {
                holder.bookingStatus.setText("Job assigned to "+category.getEngineer().getFullName());
            }
        }
        if (FragStatus.equals("close")) {
            if (category.getStatus().equals("canceled")) {
                holder.bookingStatus.setText("canceled");
            } else if (category.getStatus().equals("completed")) {
                holder.bookingStatus.setText("completed");
            }
            else if (category.getStatus().equals("indispute")) {
                holder.bookingStatus.setText("indispute");
            }
        }
        if (FragStatus.equals("ongoing")) {
            holder.bookingStatus.setText("In Process");
        }


        holder.jobsDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JOB_ID=category.getId();
                if (holder.bookingStatus.getText().equals("In Process"))
                {
                    Intent intent = new Intent(context, ViewJobsAccpted.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
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
                    bundle.putString("assignedStatus", "assigned");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else  if (holder.bookingStatus.getText().equals("canceled"))
                {
                    Intent intent = new Intent(context, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) category.getImage());
                    bundle.putString("jobId", category.getId());
                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                    bundle.putSerializable("cancelJobDetail", (CancelModel)  category.getCancelJob());

                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                    bundle.putString("scheduleStatus", category.getStatus());
                    bundle.putString("enginId", category.getEngineerId());
                    //bundle.putString("status", String.valueOf(category.getStatus()));

                    bundle.putString("showButton","no");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else if (holder.bookingStatus.getText().equals("indispute"))
                {
                    Intent intent = new Intent(context, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) (ArrayList<String>) category.getImage());
                    bundle.putString("jobId", category.getId());
                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                    bundle.putSerializable("completedDate",(CompleteJobModel) category.getCompleteJob());
                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                    bundle.putString("scheduleStatus", category.getStatus());
                    bundle.putString("enginId", category.getEngineerId());
                    //bundle.putString("status", String.valueOf(category.getStatus()));

                    bundle.putString("showButton","no");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
              else  if (holder.bookingStatus.getText().equals("completed"))
                {
                    Intent intentt = new Intent(context, EngineerCompleteJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>)  category.getImage());
                    bundle.putString("jobId", category.getId());
                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                    bundle.putSerializable("completedDate",(CompleteJobModel) category.getCompleteJob());
                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                    bundle.putString("scheduleStatus", category.getStatus());
                    bundle.putString("enginId", category.getEngineerId());
                    //bundle.putString("status", String.valueOf(category.getStatus()));
                    intentt.putExtras(bundle);
                    context.startActivity(intentt);
                }
                else if (holder.bookingStatus.getText().equals("Engineer not assigned"))
                {
                    Intent intent = new Intent(context, ViewJobsAccpted.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
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
                else if (holder.bookingStatus.getText().equals("Engineer Assigned"))
                {
                    Intent intent = new Intent(context, ViewJobsAccpted.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
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
                    bundle.putString("assignedStatus", "assigned");
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }else if (category.getVendorAssigned() && category.getEngineerAssigned())
                {
                    Intent intent = new Intent(context, ViewJobsAccpted.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand()!=null) {
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
                    bundle.putString("assignedStatus", "assigned");
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

        TextView bookingTitle,bookingDate,bookingStatus,bookingPrice,bookingissue;
        LinearLayout jobsDetailLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            bookingTitle = (TextView) itemView.findViewById(R.id.bookingTitle);
            bookingDate = (TextView) itemView.findViewById(R.id.bookingDate);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);
            bookingPrice = (TextView) itemView.findViewById(R.id.bookingPrice);
            bookingissue = (TextView) itemView.findViewById(R.id.bookingissue);
            jobsDetailLayout = (LinearLayout) itemView.findViewById(R.id.jobsDetailLayout);
        }
    }
}
