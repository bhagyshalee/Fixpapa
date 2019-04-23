package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

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

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.Model.NotificationModel.NotiData;
import com.fixpapa.ffixpapa.UserPart.ShowJobDetailJobId;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateDifference;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getTimeFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.inputFormat;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    List<NotiData> services;


    public NotificationAdapter(List<NotiData> services, Context context) {
        super();
        this.services = services;
        this.context = context;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_design, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final NotiData category = services.get(position);

        Date d = null;
        try {
            d = inputFormat.parse("" + category.getDate());
        } catch (ParseException ex) {
            Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        Log.e("njkvfnjklvfkl", d + "");
        holder.notiTitle.setText("" + category.getTitle());
        holder.notiBody.setText("" + category.getBody());
        holder.notiHoursAgo.setText("" + getDateDifference(d));
        holder.notiDateTime.setText("" + getDateFromUtc(category.getDate()) + "   " + getTimeFromUtc(category.getDate()));


        holder.Layoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowJobDetailJobId.class);
                Bundle bundle = new Bundle();
                bundle.putString("jobId", category.getJobId());
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

        TextView notiTitle, notiHoursAgo, notiBody, notiDateTime;
        LinearLayout Layoutt;

        public ViewHolder(View itemView) {
            super(itemView);

            notiHoursAgo = (TextView) itemView.findViewById(R.id.notiHoursAgo);
            notiBody = (TextView) itemView.findViewById(R.id.notiBody);
            notiTitle = (TextView) itemView.findViewById(R.id.notiTitle);
            notiDateTime = (TextView) itemView.findViewById(R.id.notiDateTime);
            Layoutt = (LinearLayout) itemView.findViewById(R.id.Layoutt);
        }
    }
}
