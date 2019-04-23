package com.fixpapa.ffixpapa.VendorPart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;

import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class EngineerExpertInAdapter extends RecyclerView.Adapter<EngineerExpertInAdapter.ViewHolder> {
    private Context context;
    List<String> services;

    public EngineerExpertInAdapter(List<String> services, Context context) {
        super();
        this.services = services;
        this.context = context;
    }

    @Override
    public EngineerExpertInAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_expertin_info, parent, false);
        EngineerExpertInAdapter.ViewHolder viewHolder = new EngineerExpertInAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(EngineerExpertInAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final String category = services.get(position);

        holder.textExpertIn.setText("" + category);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textExpertIn;

        public ViewHolder(View itemView) {
            super(itemView);

            textExpertIn = (TextView) itemView.findViewById(R.id.textExpertIn);
        }
    }
}
