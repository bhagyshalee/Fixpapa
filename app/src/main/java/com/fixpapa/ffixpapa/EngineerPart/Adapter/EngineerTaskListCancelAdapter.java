package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class EngineerTaskListCancelAdapter extends RecyclerView.Adapter<EngineerTaskListCancelAdapter.ViewHolder> {
    List<SampleData> services;
    List<String> stringList;
    List<Integer> listRating;
    ArrayList<Boolean> data_check = new ArrayList<>();
    List<Float> getUserRating;
    List<Integer> getRatePosition;
    private Context context;


    public EngineerTaskListCancelAdapter(List<SampleData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.listRating = listRating;
        stringList = new ArrayList<>();
        getUserRating = new ArrayList<>();
        getRatePosition = new ArrayList<>();

        for (int i = 0; i < services.size(); i++) {
            data_check.add(false);
        }
        for (int i = 0; i < services.size(); i++) {

            for (int j = 0; j < services.get(i).getRatedetail().size(); j++) {
                if (services.get(i).getRatedetail().get(j).getForUser().equals("engineer")) {
                    data_check.set(i, true);
                    getUserRating.add(services.get(i).getRatedetail().get(j).getUserRating());
                    break;
                }
            }

        }

    }

    @Override
    public EngineerTaskListCancelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        EngineerTaskListCancelAdapter.ViewHolder viewHolder = new EngineerTaskListCancelAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EngineerTaskListCancelAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final SampleData category = services.get(position);
        // holder.bookingissue.setVisibility(View.GONE);
        //holder.bookingTitle.setText(category.getCategory().getName());
        holder.bookingDate.setText("Placed On: " + getDateFromUtc(category.getVendorAssignedDate()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        //  holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.red));
        holder.bookingStatus.setText("OrderId: " + category.getOrderId());
        holder.bookingissue.setText("" + category.getCategory().getName());
        // holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.gray));
        for (int i = 0; i < category.getProblems().size(); i++) {
            if (category.getProblems().size() == 1) {
                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent());
            } else {
                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent() + " and other Issues");
            }
        }

        if (data_check.get(position)) {
            for (int r = 0; r < getUserRating.size(); r++) {
                holder.ratingJob.setRating(getUserRating.get(r));
                holder.ratingJob.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.ratingJob.setVisibility(View.GONE);
        }


       /* if (data_check.get(position)) {
            holder.ratingJob.setRating(getUserRating.get(position));
            holder.ratingJob.setVisibility(View.VISIBLE);
        }*/


       /* for (int j=0;j<category.getRatedetail().size();j++)
        {
            if (category.getRatedetail().get(j).getForUser().equals("engineer"))
            {
                getRateStatus=true;
            }
        }
            if (getRateStatus) {
                for (int j = 0; j < category.getRatedetail().size(); j++) {
                    if (category.getRatedetail().get(j).getForUser().equals("engineer")) {
                        holder.ratingJob.setRating(category.getRatedetail().get(j).getUserRating());
                        holder.ratingJob.setVisibility(View.VISIBLE);
                    }
                }
        }
*/
        holder.jobsDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addtionalChar = 0;
                JOB_ID = category.getId();
                if (category.getStatus().equals("canceled")) {

                    Intent intent = new Intent(context, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    if (category.getBrand() != null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                    bundle.putString("jobId", category.getId());
                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                    if (category.getBill() != null) {
                        bundle.putInt("payAmt", category.getBill().getTotal());
                    }
                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                    bundle.putSerializable("cancelJobDetail", (CancelModel) category.getCancelJob());
                    bundle.putString("scheduleStatus", category.getStatus());
                    bundle.putString("enginId", category.getEngineerId());
                    bundle.putString("showButton", "no");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (category.getStatus().equals("indispute")) {
                    Intent intent = new Intent(context, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    if (category.getBrand() != null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                    bundle.putString("jobId", category.getId());
                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                    bundle.putSerializable("completedDate", (CompleteJobModel) category.getCompleteJob());
                    bundle.putString("scheduleStatus", category.getStatus());
                    bundle.putString("enginId", category.getEngineerId());
                    bundle.putString("showButton", "no");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (category.getStatus().equals("completed")) {
                    if (category.getRatedetail().size() != 0) {
                        boolean getRateStatus = false;
                        for (int i = 0; i < category.getRatedetail().size(); i++) {
                            if (category.getRatedetail().get(i).getForUser().equals("customer")) {
                                getRateStatus = true;
                            }
                        }
                        if (getRateStatus) {
                            Intent intent = new Intent(context, EngineerViewJobs.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                            bundle.putSerializable("sendCategory", category.getCategory().getName());
                            if (category.getBrand() != null) {
                                bundle.putSerializable("brand", category.getBrand().getName());
                            }
                            if (category.getBill() != null) {
                                bundle.putInt("payAmt", category.getBill().getTotal());
                            }

                            bundle.putSerializable("address", (AddressesModel) category.getAddress());
                            bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                            bundle.putString("jobId", category.getId());
                            bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                            bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                            bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                            bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                            bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                            bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                            bundle.putSerializable("completedDate", (CompleteJobModel) category.getCompleteJob());
                            bundle.putString("scheduleStatus", category.getStatus());
                            bundle.putString("enginId", category.getEngineerId());
                            bundle.putString("showButton", "no");
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("customerName", category.getCustomer().getFullName());
                            bundle.putInt("totalAmount", category.getTotalPrice());
                            bundle.putString("orderId", category.getOrderId());
                            bundle.putString("scheduledStart", category.getStartDate());
                            bundle.putString("scheduledEnd", category.getStartDate());
                            bundle.putString("completedOn", category.getCompleteJob().getCompletedAt());
                            bundle.putString("cusId", category.getCustomer().getId());
                            bundle.putString("enginId", category.getEngineer().getId());
                            bundle.putString("jobId", category.getId());
                            Intent intent = new Intent(context, ReviewRating.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("customerName", category.getCustomer().getFullName());
                        bundle.putInt("totalAmount", category.getTotalPrice());
                        bundle.putString("orderId", category.getOrderId());
                        bundle.putString("scheduledStart", category.getStartDate());
                        bundle.putString("scheduledEnd", category.getStartDate());
                        bundle.putString("completedOn", category.getCompleteJob().getCompletedAt());
                        bundle.putString("cusId", category.getCustomer().getId());
                        bundle.putString("enginId", category.getEngineer().getId());
                        bundle.putString("jobId", category.getId());
                        Intent intent = new Intent(context, ReviewRating.class);
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

        TextView bookingTitle, bookingDate, bookingPrice, bookingStatus, bookingissue;
        LinearLayout jobsDetailLayout;
        RatingBar ratingJob;

        public ViewHolder(View itemView) {
            super(itemView);

            bookingTitle = (TextView) itemView.findViewById(R.id.bookingTitle);
            bookingDate = (TextView) itemView.findViewById(R.id.bookingDate);
            bookingPrice = (TextView) itemView.findViewById(R.id.bookingPrice);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);
            bookingissue = (TextView) itemView.findViewById(R.id.bookingissue);
            jobsDetailLayout = (LinearLayout) itemView.findViewById(R.id.jobsDetailLayout);
            ratingJob = (RatingBar) itemView.findViewById(R.id.ratingJob);

        }
    }
}