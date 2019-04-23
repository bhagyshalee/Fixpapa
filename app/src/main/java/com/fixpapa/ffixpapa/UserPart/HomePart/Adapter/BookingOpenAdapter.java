package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.JobsModel.GetJobsData;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class BookingOpenAdapter extends RecyclerView.Adapter<BookingOpenAdapter.ViewHolder> {
    private Context context;
    List<GetJobsData> services;
    List<String> stringList;
    int loginStatus;
    String color, FragStatus;


    public BookingOpenAdapter(String FragStatus, String color, List<GetJobsData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.FragStatus = FragStatus;
        stringList = new ArrayList<>();
        this.color = color;
    }

    @Override
    public BookingOpenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_booking_fragment, parent, false);
        BookingOpenAdapter.ViewHolder viewHolder = new BookingOpenAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(BookingOpenAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        GetJobsData category = services.get(position);

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
        holder.bookingDate.setText("Placed On: " + getTimeFromUtc(category.getCreatedAt()));
        holder.bookingPrice.setText("" + category.getTotalPrice() + "/-");
        Log.e("fgnbghfgb",""+category.getCategory().getName());

        if (FragStatus.equals("open")) {
            if (category.getIsVendorAssigned() && !category.getIsEngineerAssigned() || !category.getIsVendorAssigned() && !category.getIsEngineerAssigned()) {
                holder.bookingStatus.setText("Requested");
            } else if (category.getIsVendorAssigned() && category.getIsEngineerAssigned()) {
                holder.bookingStatus.setText("Engineer Assigned");
            }
        }
        if (FragStatus.equals("close")) {
            if (category.getStatus().equals("canceled")) {
                holder.bookingStatus.setText("cancel");
            } else if (category.getStatus().equals("completed")) {
                holder.bookingStatus.setText("Complete");
            }
        }
        if (FragStatus.equals("ongoing")) {
            holder.bookingStatus.setText("In Process");
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookingTitle, bookingDate, bookingPrice, bookingStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            bookingTitle = (TextView) itemView.findViewById(R.id.bookingTitle);
            bookingDate = (TextView) itemView.findViewById(R.id.bookingDate);
            bookingPrice = (TextView) itemView.findViewById(R.id.bookingPrice);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);

        }
    }
}