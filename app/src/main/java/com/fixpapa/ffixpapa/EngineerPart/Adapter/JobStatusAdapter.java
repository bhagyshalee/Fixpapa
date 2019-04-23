package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.Offsitestatus;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getDateFromUtc;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class JobStatusAdapter extends RecyclerView.Adapter<JobStatusAdapter.ViewHolder> {
    private Context context;
    List<Offsitestatus> services;
    List<String> stringList;

    public JobStatusAdapter(List<Offsitestatus> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        stringList = new ArrayList<>();
    }

    @Override
    public JobStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_jobstatus, parent, false);
        JobStatusAdapter.ViewHolder viewHolder = new JobStatusAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final JobStatusAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final Offsitestatus category = services.get(position);
        Log.e("bfdbdfbdf",category.getWorkDate()+""+category.getText());
        holder.titleJobStatus.setText(""+category.getText());
        holder.dateJobStatus.setText(""+getDateFromUtc(category.getWorkDate()));

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateJobStatus, titleJobStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            dateJobStatus = (TextView) itemView.findViewById(R.id.dateJobStatus);
            titleJobStatus = (TextView) itemView.findViewById(R.id.titleJobStatus);

        }
    }
}