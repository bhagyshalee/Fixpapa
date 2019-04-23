package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.fixpapa.ffixpapa.UserPart.HomePart.PaymentCustomer;
import com.fixpapa.ffixpapa.UserPart.HomePart.ReviewRating;
import com.fixpapa.ffixpapa.UserPart.PartRequest;
import com.fixpapa.ffixpapa.VendorPart.HomePart.ViewJobsAccpted;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.CancelModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.CompleteJobModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class BookingOngoingAdapter extends RecyclerView.Adapter<BookingOngoingAdapter.ViewHolder> {
    private Context context;
    List<SampleData> services;
    List<String> stringList;
    int loginStatus;
    String color, FragStatus;

    String  USER_ID;

    public BookingOngoingAdapter(String FragStatus, String color, List<SampleData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.FragStatus = FragStatus;
        stringList = new ArrayList<>();
        this.color = color;
        getUserDetailId(context);
    }

    @Override
    public BookingOngoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        BookingOngoingAdapter.ViewHolder viewHolder = new BookingOngoingAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final BookingOngoingAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);

        SharedPreferences sharedPref = context.getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
          USER_ID = sharedPref.getString("USER_ID", "");

        final SampleData category = services.get(position);

        if (color.equals("blue")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.blue));
        }
        if (color.equals("green")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (color.equals("red")) {
            holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.bookingTitle.setText("" + category.getCategory().getName());

      /*  if (category.getCategory().getName().equals(category.getProduct().getName())) {
            holder.bookingTitle.setText("" + category.getCategory().getName());
            if (category.getBrand()!=null) {
                if (category.getProduct().getName().equals(category.getBrand().getName())) {
                    holder.bookingTitle.setText("" + category.getBrand().getName());
                } else {
                    holder.bookingTitle.setText("" + category.getProduct().getName());
                }
            }
        }
        else {
            holder.bookingTitle.setText("" + category.getCategory().getName());
        }*/



       /* holder.bookingTitle.setText("" + category.getCategory().getName());
      //  holder.bookingDate.setText("Placed On: " +  getDateFromUtc(category.getCreatedAt()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        Log.e("fgnbghfgb",""+category.getCategory().getName());
*/
        Log.e("szvdddddd", "" + category.getRatedetail());
        if (category.getStartDate() != null) {
            holder.bookingDate.setText("Placed On: " + getDateFromUtc(category.getCreatedAt()));
        }
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        //holder.bookingStatus.setText("" + category.getCategory().getName());

        for (int i = 0; i < category.getProblems().size(); i++) {
            if (category.getProblems().size() == 1) {
                holder.bookingissue.setText(category.getProblems().get(i).getProbContent());
            } else {
                holder.bookingissue.setText(category.getProblems().get(i).getProbContent() + " and other Issues");
            }
        }
        if (FragStatus.equals("open")) {
            if (!category.getVendorAssigned() && !category.getEngineerAssigned()) {
                holder.bookingStatus.setText("Requested");
            } else if (category.getVendorAssigned() && category.getEngineerAssigned()) {
                holder.bookingStatus.setText("Engineer Assigned");
            }else if (category.getVendorAssigned())
            {
                holder.bookingStatus.setText("Waiting for vendor confirmation");
            }
        }
        if (FragStatus.equals("close")) {
            if (category.getStatus().equals("canceled")) {
                holder.bookingStatus.setText("canceled");
            } else if (category.getStatus().equals("completed")) {
                holder.bookingStatus.setText("completed");
            }else if (category.getStatus().equals("indispute")) {
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
                int addtionalChar = 0;
                //Log.e("fvndfvdfsdv", "" + holder.bookingStatus.getText()+" "+category.getSiteType());
                if (holder.bookingStatus.getText().equals("In Process")) {

                    if (category.getSiteType().equals("Offsite")) {
                            if (category.getBill().getClientResponse().equals("requested")) {
                                if (category.getBill() != null) {

                                    Intent intent = new Intent(context, PartRequest.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("jobId", category.getId());
                                    bundle.putSerializable("addPart", (BillModel) category.getBill());
                                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                                    bundle.putSerializable("subCategory", category.getProduct().getName());
                                    if (category.getBrand() != null) {
                                        bundle.putSerializable("brand", category.getBrand().getName());
                                    }
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            }else if (category.getBill().getClientResponse().equals("decline")){
                                Intent intent = new Intent(context, ViewJobsAccpted.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                bundle.putSerializable("sendCategory", category.getCategory().getName());
                                bundle.putSerializable("subCategory", category.getProduct().getName());
                                if (category.getBrand() != null) {
                                    bundle.putSerializable("brand", category.getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) category.getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
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
                            } else if (category.getStatus().equals("billGenerated") || category.getStatus().equals("paymentDone")) {
                                Intent intent = new Intent(context, PaymentCustomer.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal", category.getBill().getTotalAmount());
                                for (int i = 0; i < category.getBill().getAddPart().size(); i++) {
                                    addtionalChar += category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putInt("additionalCharge", category.getBill().getAddServiceCost());
                                if (category.getBill().getDiscount()!=null) {
                                    bundle.putInt("discount", category.getBill().getDiscount());
                                }
                                bundle.putInt("totalPrice", category.getBill().getTotal());
                                bundle.putString("jobId", category.getId());
                                bundle.putString("cusId", USER_ID);
                                bundle.putString("enginId", category.getEngineerId());
                                bundle.putString("orderId", category.getOrderId());
                                if (category.getSchedule() != null) {
                                    bundle.putString("scheduledOn", category.getSchedule().getEStartDate());
                                }
                                bundle.putString("customerName", category.getCustomerName());
                                if (category.getStatus().equals("paymentDone")) {
                                    bundle.putString("modePayment", category.getTransactionModel().getModeOfPayment());
                                }
                                bundle.putString("status", category.getStatus());
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                            }
                            else if (holder.bookingStatus.getText().equals("canceled")) {
                                Intent intent = new Intent(context, EngineerViewJobs.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                bundle.putSerializable("sendCategory", category.getCategory().getName());
                                bundle.putSerializable("subCategory", category.getProduct().getName());
                                if (category.getBrand() != null) {
                                    bundle.putSerializable("brand", category.getBrand().getName());
                                }
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                if ( category.getBill()!=null) {
                                    bundle.putInt("payAmt", category.getBill().getTotal());
                                }
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("additionalCharge",category.getBill().getAddServiceCost());
                                bundle.putInt("discount",category.getBill().getDiscount());
                                bundle.putInt("totalPrice",category.getBill().getTotal());

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
                                bundle.putString("showButton", "no");
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
                            else if (holder.bookingStatus.getText().equals("completed"))
                            {
                                if (category.getRatedetail().size()!=0) {
                                    boolean getRateStatus=false;
                                    for (int i = 0; i < category.getRatedetail().size(); i++) {
                                        if (category.getRatedetail().get(i).getForUser().equals("engineer")) {
                                            getRateStatus=true;
                                        }
                                    }

                                    if (getRateStatus)
                                    {
                                        Intent intent = new Intent(context, EngineerCompleteJobs.class);
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
                                        bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                                        bundle.putSerializable("completedDate",(CompleteJobModel) category.getCompleteJob());
                                        bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                                        bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                                        bundle.putString("scheduleStatus", category.getStatus());
                                        bundle.putString("enginId", category.getEngineerId());
                                        //bundle.putString("status", String.valueOf(category.getStatus()));
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);

                                    }
                                    else
                                    {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("customerName", category.getEngineer().getFullName());
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
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("customerName", category.getEngineer().getFullName());
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
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }

                            }
                            else {
                                Intent intent = new Intent(context, ViewJobsAccpted.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                bundle.putSerializable("sendCategory", category.getCategory().getName());
                                bundle.putSerializable("subCategory", category.getProduct().getName());
                                if (category.getBrand() != null) {
                                    bundle.putSerializable("brand", category.getBrand().getName());
                                }
                                bundle.putSerializable("address", (AddressesModel) category.getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
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

                    } else
                        if (category.getStatus().equals("billGenerated") ||category.getStatus().equals("paymentDone")) {
                        Intent intent = new Intent(context, PaymentCustomer.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("serviceTotal", category.getBill().getTotalAmount());
                        for (int i = 0; i < category.getBill().getAddPart().size(); i++) {
                            addtionalChar += category.getBill().getAddPart().get(i).getPartCost();
                        }
                        bundle.putInt("AdditionalPart", addtionalChar);
                        bundle.putInt("additionalCharge", category.getBill().getAddServiceCost());
                        if (category.getBill().getDiscount()!=null) {
                            bundle.putInt("discount", category.getBill().getDiscount());
                        }
                        bundle.putInt("totalPrice", category.getBill().getTotal());
                        bundle.putString("jobId", category.getId());
                        bundle.putString("cusId", USER_ID);
                        bundle.putString("enginId", category.getEngineerId());
                        bundle.putString("orderId", category.getOrderId());
                        if (category.getSchedule() != null) {
                            bundle.putString("scheduledOn", category.getSchedule().getEStartDate());
                        }
                        bundle.putString("customerName", category.getCustomerName());
                            if (category.getStatus().equals("paymentDone")) {
                                bundle.putString("modePayment", category.getTransactionModel().getModeOfPayment());
                            }
                        bundle.putString("status", category.getStatus());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else if (holder.bookingStatus.getText().equals("canceled")) {
                            Intent intent = new Intent(context, EngineerViewJobs.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                            bundle.putSerializable("sendCategory", category.getCategory().getName());
                            bundle.putSerializable("subCategory", category.getProduct().getName());
                            if (category.getBrand() != null) {
                                bundle.putSerializable("brand", category.getBrand().getName());
                            }
                            for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                            }
                            bundle.putInt("AdditionalPart", addtionalChar);
                            bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                            bundle.putInt("additionalCharge",category.getBill().getAddServiceCost());
                            bundle.putInt("discount",category.getBill().getDiscount());
                            bundle.putInt("totalPrice",category.getBill().getTotal());

                            bundle.putSerializable("address", (AddressesModel) category.getAddress());
                            bundle.putSerializable("jobImage", (ArrayList<String>)category.getImage());
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
                            bundle.putString("showButton", "no");
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
                        else if (holder.bookingStatus.getText().equals("completed"))
                        {
                            if (category.getRatedetail().size()!=0) {
                                boolean getRateStatus=false;
                                for (int i = 0; i < category.getRatedetail().size(); i++) {
                                    if (category.getRatedetail().get(i).getForUser().equals("engineer")) {
                                        getRateStatus=true;
                                    }
                                }

                                if (getRateStatus)
                                {
                                    Intent intent = new Intent(context, EngineerCompleteJobs.class);
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
                                    bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                                    bundle.putSerializable("completedDate",(CompleteJobModel) category.getCompleteJob());
                                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                                    bundle.putString("scheduleStatus", category.getStatus());
                                    bundle.putString("enginId", category.getEngineerId());
                                    //bundle.putString("status", String.valueOf(category.getStatus()));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                }
                                else
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("customerName", category.getEngineer().getFullName());
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
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }
                            }

                        }
                        else {
                            Intent intent = new Intent(context, ViewJobsAccpted.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                            bundle.putSerializable("sendCategory", category.getCategory().getName());
                            bundle.putSerializable("subCategory", category.getProduct().getName());
                            if (category.getBrand() != null) {
                                bundle.putSerializable("brand", category.getBrand().getName());
                            }
                            bundle.putSerializable("address", (AddressesModel) category.getAddress());
                            bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
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

                } else if (holder.bookingStatus.getText().equals("completed"))
                {
                    if (category.getRatedetail().size()!=0) {
                        boolean getRateStatus=false;
                        for (int i = 0; i < category.getRatedetail().size(); i++) {
                            if (category.getRatedetail().get(i).getForUser().equals("engineer")) {
                                getRateStatus=true;
                            }
                        }

                        if (getRateStatus)
                        {
                            Intent intent = new Intent(context, EngineerCompleteJobs.class);
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
                            bundle.putString("dateVendorAssign", String.valueOf(category.getStartDate()));
                            bundle.putSerializable("completedDate",(CompleteJobModel) category.getCompleteJob());
                            bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                            bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                            bundle.putString("scheduleStatus", category.getStatus());
                            bundle.putString("enginId", category.getEngineerId());
                            //bundle.putString("status", String.valueOf(category.getStatus()));
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                        else
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("customerName", category.getEngineer().getFullName());
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
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                    }

                } else if (holder.bookingStatus.getText().equals("indispute"))
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
                }else if (holder.bookingStatus.getText().equals("canceled")) {
                    Intent intent = new Intent(context, EngineerViewJobs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand() != null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    for (int i=0;i<category.getBill().getAddPart().size();i++) {
                        addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                    }
                    bundle.putInt("AdditionalPart", addtionalChar);
                    bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                    bundle.putInt("additionalCharge",category.getBill().getAddServiceCost());
                    bundle.putInt("discount",category.getBill().getDiscount());
                    bundle.putInt("totalPrice",category.getBill().getTotal());

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
                    bundle.putString("showButton", "no");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, ViewJobsAccpted.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                    bundle.putSerializable("sendCategory", category.getCategory().getName());
                    bundle.putSerializable("subCategory", category.getProduct().getName());
                    if (category.getBrand() != null) {
                        bundle.putSerializable("brand", category.getBrand().getName());
                    }
                    bundle.putSerializable("address", (AddressesModel) category.getAddress());
                    bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
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

        TextView bookingTitle, bookingDate, bookingPrice, bookingStatus,bookingissue;
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