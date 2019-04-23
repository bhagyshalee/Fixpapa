package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.EngineerViewJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs;
import com.fixpapa.ffixpapa.EngineerPart.EngineerViewSite;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack;
import com.fixpapa.ffixpapa.EngineerPart.OffSiteView;
import com.fixpapa.ffixpapa.EngineerPart.PaymentEngineerOffsite;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;
import com.fixpapa.ffixpapa.VendorPart.Model.AddPart;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.ScheduleModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.BillModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.PickModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Problems;
import com.fixpapa.ffixpapa.VendorPart.Model.SampleData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Config.JOB_ID;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.ShowAlertDialog;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class EngineerTaskListOngoingAdapter extends RecyclerView.Adapter<EngineerTaskListOngoingAdapter.ViewHolder> {
    private Context context;
    List<SampleData> services;
    List<String> stringList;

    public EngineerTaskListOngoingAdapter(List<SampleData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();
    }

    @Override
    public EngineerTaskListOngoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        EngineerTaskListOngoingAdapter.ViewHolder viewHolder = new EngineerTaskListOngoingAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EngineerTaskListOngoingAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final SampleData category = services.get(position);
       // holder.bookingissue.setVisibility(View.GONE);
      //  holder.bookingTitle.setText(category.getCategory().getName());
        holder.bookingDate.setText("Placed On: " + getDateFromUtc(category.getVendorAssignedDate()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
       // holder.bookingStatus.setTextColor(context.getResources().getColor(R.color.gray));
      holder.bookingStatus.setText("OrderId: " + category.getOrderId());
        holder.bookingissue.setText("" + category.getCategory().getName());

        for (int i=0;i<category.getProblems().size();i++) {
            if (category.getProblems().size() == 1) {
                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent());
            }
            else
            {
                holder.bookingTitle.setText(category.getProblems().get(i).getProbContent()+" and other Issues");
            }

        }

       // Log.e("cvncncncnv",category.getBill().getAddPart()+"");


        holder.jobsDetailLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JOB_ID=category.getId();
                       int  addtionalChar=0;
                        if (category.getSiteType().equals("Onsite")) {
                            if (category.getStatus().equals("paymentDone"))
                            {
                                Intent intent = new Intent(context, PaymentEngineerOffsite.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal",category.getBill().getTotalAmount());
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("additionalCharge", category.getBill().getAddServiceCost());
                                bundle.putInt("discount", category.getBill().getDiscount());
                                bundle.putInt("totalPrice",category.getBill().getTotal());
                                bundle.putString("jobId", category.getId());
                                bundle.putString("cusId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                bundle.putString("orderId", category.getOrderId());
                                bundle.putString("scheduledStart", category.getStartDate());
                                bundle.putString("scheduledEnd", category.getEndDate());
                                bundle.putString("customerName", category.getCustomerName());
                                bundle.putString("customerId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                            if (category.getStatus().equals("billGenerated"))
                            {
                                Intent intent = new Intent(context, PaymentEngineerOffsite.class);
                                Bundle bundle=new Bundle();
                                bundle.putInt("serviceTotal",category.getBill().getTotalAmount());
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putInt("additionalCharge",category.getBill().getAddServiceCost());
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("discount",category.getBill().getDiscount());
                                bundle.putInt("totalPrice",category.getBill().getTotal());
                                bundle.putString("jobId",category.getId());
                                bundle.putString("cusId",category.getCustomerId());
                                bundle.putString("enginId",category.getEngineerId());
                                bundle.putString("orderId",category.getOrderId());
                                if(category.getSchedule()!=null) {
                                    bundle.putString("scheduledOn", category.getSchedule().getEStartDate());
                                }
                                bundle.putString("customerName",category.getCustomerName());
                                bundle.putString("customerId",category.getCustomerId());
                                bundle.putString("status",category.getStatus());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }

                           if(category.getStatus().equals("inprocess")) {
                               Intent intent = new Intent(context, EngineerViewSite.class);
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
                               bundle.putString("customerName", category.getCustomerName());
                               bundle.putString("customerId", category.getCustomerId());
                               bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                               bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                               bundle.putString("dateVendorAssign", String.valueOf(category.getVendorAssignedDate()));
                               bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                               bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                               bundle.putString("timeDuration", String.valueOf(category.getDuration()));
                               bundle.putString("enginId", category.getEngineerId());
                               bundle.putString("scheduleStatus", category.getStatus());
                               if (category.getStatus().equals("scheduled")) {
                                   bundle.putSerializable("Schedule", (ScheduleModel) category.getSchedule());
                               }
                               intent.putExtras(bundle);
                               context.startActivity(intent);
                           }

                           else if (category.getStatus().equals("on the way"))
                           {
                               final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                               if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                   ShowAlertDialog(context);
                               }else {

                                   Intent intent = new Intent(context, MapEngineerTrack.class);
                                   Bundle bundle = new Bundle();
                                   bundle.putString("latitude", String.valueOf(category.getAddress().getLocation().getLat()));
                                   bundle.putString("longitude", String.valueOf(category.getAddress().getLocation().getLng()));
                                   bundle.putString("street", String.valueOf(category.getAddress().getStreet()));
                                   bundle.putString("value", String.valueOf(category.getAddress().getValue()));
                                   bundle.putString("jobId", category.getId());
                                   /// bundle.putString("jobIdCC", category.getId());
                                   bundle.putString("otp", String.valueOf(category.getOtp()));

                                   bundle.putSerializable("pick", (PickModel) category.getPick());
                                   bundle.putSerializable("addpart", (ArrayList<AddPart>) category.getBill().getAddPart());
                                   bundle.putSerializable("address", (AddressesModel) category.getAddress());
                                   bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                                   bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                   bundle.putString("dateVendorAssign", String.valueOf(category.getVendorAssignedDate()));
                                   bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                                   bundle.putString("sendCategory", category.getCategory().getName());
                                   bundle.putString("subCategory", category.getProduct().getName());
                                   if (category.getBrand() != null) {
                                       bundle.putString("brand", category.getBrand().getName());
                                   }
                                   bundle.putString("customerName", category.getCustomerName());
                                   bundle.putString("customerId", category.getCustomerId());
                                   bundle.putString("customerMobile", category.getCustomer().getMobile());
                                   bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                                   bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                                   bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                                   bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                                   bundle.putString("timeDuration", String.valueOf(category.getDuration()));
                                   bundle.putString("scheduleStatus", category.getStatus());
                                   bundle.putString("enginId", category.getEngineerId());
                                   if (category.getStatus().equals("scheduled")) {
                                       bundle.putSerializable("Schedule", (ScheduleModel) category.getSchedule());
                                   }
                                   intent.putExtras(bundle);
                                   context.startActivity(intent);
                               }
                           }
                        } else if (category.getSiteType().equals("Offsite")) {
                            if (category.getStatus().equals("billGenerated"))
                            {
                                Intent intent = new Intent(context, PaymentEngineerOffsite.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal",category.getTotalPrice());
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("additionalCharge", category.getBill().getAddServiceCost());
                                if (category.getBill().getDiscount()!=null) {
                                    bundle.putInt("discount", category.getBill().getDiscount());
                                }
                                bundle.putInt("totalPrice",category.getBill().getTotal());
                                bundle.putString("jobId", category.getId());
                                bundle.putString("cusId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                bundle.putString("orderId", category.getOrderId());
                                bundle.putString("scheduledStart", category.getStartDate());
                                bundle.putString("scheduledEnd", category.getEndDate());
                                bundle.putString("customerName", category.getCustomerName());
                                bundle.putString("customerId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                            if (category.getStatus().equals("paymentDone"))
                            {
                                Intent intent = new Intent(context, PaymentEngineerOffsite.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("serviceTotal",category.getBill().getTotalAmount());
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("additionalCharge", category.getBill().getAddServiceCost());
                                bundle.putInt("discount", category.getBill().getDiscount());
                                bundle.putInt("totalPrice",category.getBill().getTotal());
                                bundle.putString("jobId", category.getId());
                                bundle.putString("cusId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                bundle.putString("orderId", category.getOrderId());
                                bundle.putString("scheduledStart", category.getStartDate());
                                bundle.putString("scheduledEnd", category.getEndDate());
                                bundle.putString("customerName", category.getCustomerName());
                                bundle.putString("customerId", category.getCustomerId());
                                bundle.putString("enginId", category.getEngineerId());
                                // bundle.putString("cusId",response.body().getSuccess().getData().getCustomerId());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                            if (category.getStatus().equals("outForDelivery"))
                            {
                                Intent intent = new Intent(context, EngineerViewJobs.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                bundle.putSerializable("sendCategory", category.getCategory().getName());
                                bundle.putSerializable("subCategory", category.getProduct().getName());
                                if (category.getBrand()!=null) {
                                    bundle.putSerializable("brand", category.getBrand().getName());
                                }
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    addtionalChar+=category.getBill().getAddPart().get(i).getPartCost();
                                }
                                bundle.putInt("AdditionalPart", addtionalChar);
                                bundle.putSerializable("AdditionalPartDetail",(ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putInt("additionalCharge",category.getBill().getAddServiceCost());
                                bundle.putInt("discount",category.getBill().getDiscount());
                                bundle.putInt("totalPrice",category.getTotalPrice());
                                bundle.putInt("serviceTotal",category.getTotalPrice());
                                Log.e("mgkmrtg", "" + addtionalChar+" "+category.getBill().getAddServiceCost());

                                bundle.putSerializable("address", (AddressesModel)  category.getAddress());
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
                                bundle.putString("showButton","yes");
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                            else if (category.getStatus().equals("inprocess"))
                            {
                                Intent intent = new Intent(context, OffSiteView.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pick", (PickModel) category.getPick());
                                bundle.putSerializable("billModel", (BillModel) category.getBill());
                                bundle.putSerializable("AdditionalPartDetail", (ArrayList<AddPart>) category.getBill().getAddPart());
                                bundle.putSerializable("address", (AddressesModel) category.getAddress());
                                bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                                bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                bundle.putString("dateVendorAssign", String.valueOf(category.getVendorAssignedDate()));
                                bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                                bundle.putString("sendCategory", category.getCategory().getName());
                                bundle.putString("subCategory", category.getProduct().getName());
                                if (category.getBrand()!=null) {
                                    bundle.putString("brand", category.getBrand().getName());
                                }
                                bundle.putString("jobId", category.getId());
                                bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                                bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                                bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                                bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                                bundle.putString("timeDuration", String.valueOf(category.getDuration()));
                                bundle.putString("scheduleStatus", category.getStatus());
                                bundle.putString("categoryName", category.getCategory().getName());
                                bundle.putInt("serviceTotal", category.getTotalPrice());
                                bundle.putString("enginId", category.getEngineerId());
                                for (int i=0;i<category.getBill().getAddPart().size();i++) {
                                    bundle.putInt("serviceAdpartCost", category.getBill().getAddPart().get(i).getPartCost());
                                }
                                bundle.putInt("addServiceCost", category.getBill().getAddServiceCost());

                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }else if (category.getStatus().equals("on the way"))
                            {
                                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    ShowAlertDialog(context);
                                }else {
                                    Log.e("fdfdfdfdfdfdfdfdfdv", category.getId() + "");
                                    Intent intent = new Intent(context, MapEngineerTrack.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("latitude", String.valueOf(category.getAddress().getLocation().getLat()));
                                    bundle.putString("longitude", String.valueOf(category.getAddress().getLocation().getLng()));
                                    bundle.putString("street", String.valueOf(category.getAddress().getStreet()));
                                    bundle.putString("value", String.valueOf(category.getAddress().getValue()));
                                    bundle.putString("jobId", category.getId());
                                    /// bundle.putString("jobIdCC", category.getId());
                                    bundle.putString("otp", String.valueOf(category.getOtp()));

                                    bundle.putSerializable("pick", (PickModel) category.getPick());
                                    bundle.putSerializable("addpart", (ArrayList<AddPart>) category.getBill().getAddPart());
                                    bundle.putSerializable("address", (AddressesModel) category.getAddress());
                                    bundle.putSerializable("jobImage", (ArrayList<String>) category.getImage());
                                    bundle.putSerializable("sendIssues", (ArrayList<Problems>) category.getProblems());
                                    bundle.putString("dateVendorAssign", String.valueOf(category.getVendorAssignedDate()));
                                    bundle.putString("jobDescription", String.valueOf(category.getProblemDes()));
                                    bundle.putString("sendCategory", category.getCategory().getName());
                                    bundle.putString("subCategory", category.getProduct().getName());
                                    if (category.getBrand() != null) {
                                        bundle.putString("brand", category.getBrand().getName());
                                    }
                                    bundle.putString("customerName", category.getCustomerName());
                                    bundle.putString("customerId", category.getCustomerId());
                                    bundle.putString("customerMobile", category.getCustomer().getMobile());
                                    bundle.putString("budget", String.valueOf(category.getTotalPrice()));
                                    bundle.putString("dateTimeStart", String.valueOf(category.getStartDate()));
                                    bundle.putString("dateTimeEnd", String.valueOf(category.getEndDate()));
                                    bundle.putString("jonOrderId", String.valueOf(category.getOrderId()));
                                    bundle.putString("timeDuration", String.valueOf(category.getDuration()));
                                    bundle.putString("scheduleStatus", category.getStatus());
                                    bundle.putString("enginId", category.getEngineerId());
                                    if (category.getStatus().equals("scheduled")) {
                                        bundle.putSerializable("Schedule", (ScheduleModel) category.getSchedule());
                                    }
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
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