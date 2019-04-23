package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class EngineerTodayAdapter extends RecyclerView.Adapter<EngineerTodayAdapter.ViewHolder> {
    private Context context;
    List<SampleData> services;
    List<String> stringList;

    public EngineerTodayAdapter(List<SampleData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();
    }

    @Override
    public EngineerTodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        EngineerTodayAdapter.ViewHolder viewHolder = new EngineerTodayAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EngineerTodayAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final SampleData category = services.get(position);
        // holder.bookingTitle.setText(category.getCategory().getName());
        holder.bookingissue.setVisibility(View.GONE);
        holder.bookingDate.setText("Placed On: " + getDateFromUtc(category.getVendorAssignedDate()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        holder.bookingStatus.setText("" + category.getCategory().getName());
        for (int i = 0; i < category.getProblems().size(); i++) {
            if (category.getProblems().size() == 1) {

                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent());
            } else {
                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent() + " and other Issues");
            }

        }
        holder.jobsDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.jobsDetailLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JOB_ID = category.getId();
                        Intent intent = new Intent(context, EngineerViewPendingJobs.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                        bundle.putSerializable("sendCategory", category.getCategory().getName());
                        bundle.putSerializable("subCategory", category.getProduct().getName());
                        if (category.getBrand()!=null) {
                            bundle.putSerializable("brand", category.getBrand().getName());
                        }
                        bundle.putSerializable("address", (AddressesModel) category.getAddress());
                        bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                        bundle.putString("jobId", category.getId());
                        bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                        bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                        bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                        bundle.putString("dateVendorAssign", String.valueOf(category.getVendorAssignedDate()));
                        bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                        bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                        bundle.putString("scheduleStatus", category.getStatus());
                        bundle.putString("enginId", category.getEngineerId());
                        bundle.putString("customerName", category.getCustomer().getFullName());
                        bundle.putString("customerMobile", category.getCustomer().getMobile());
                        if (category.getStatus().equals("scheduled")) {
                            bundle.putSerializable("Schedule", (ScheduleModel) category.getSchedule());
                        }
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookingTitle, bookingDate, bookingPrice, bookingStatus, bookingissue;
        LinearLayout jobsDetailLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            bookingTitle = (TextView) itemView.findViewById(R.id.bookingTitle);
            bookingDate = (TextView) itemView.findViewById(R.id.bookingDate);
            bookingPrice = (TextView) itemView.findViewById(R.id.bookingPrice);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);
            bookingissue = (TextView) itemView.findViewById(R.id.bookingissue);
            jobsDetailLayout = (LinearLayout) itemView.findViewById(R.id.jobsDetailLayout);

        }
    }
}