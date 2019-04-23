package com.fixpapa.ffixpapa.VendorPart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.GetDataModle;

import java.util.ArrayList;
import java.util.List;

public class ExpertInAdapter extends RecyclerView.Adapter<ExpertInAdapter.ViewHolder> {
    private Context context;
    List<GetDataModle> services;
    public static List<String> expertiseId;

    public ExpertInAdapter(List<GetDataModle> services, Context context) {
        super();
        this.services = services;
        this.context = context;

    }

    @Override
    public ExpertInAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_expertin, parent, false);
        ExpertInAdapter.ViewHolder viewHolder = new ExpertInAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ExpertInAdapter.ViewHolder holder, final int position) {

        final GetDataModle category=services.get(position);
        holder.expertCheckBox.setText(category.getName());
        expertiseId=new ArrayList<>();
        holder.expertCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expertCheckBox.isChecked())
                {
                    expertiseId.add(category.getId());
                }
                else
                {
                    expertiseId.remove(category.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return services.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox expertCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            expertCheckBox = (CheckBox) itemView.findViewById(R.id.expertCheckBox);

        }
    }
}
