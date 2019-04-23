package com.fixpapa.ffixpapa.VendorPart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.Model.GetDataModle;

import java.util.ArrayList;
import java.util.List;

public class ExpertInAdapterUpdate extends RecyclerView.Adapter<ExpertInAdapterUpdate.ViewHolder> {
    private Context context;
    List<GetDataModle> services;
    public static List<String> expertiseIdUpdate;
    public static List<String> expertiseIdPre;
    ArrayList<Boolean> data_check = new ArrayList<>();
    boolean setCheckedStatus;

    public ExpertInAdapterUpdate(boolean setCheckedStatus, List<GetDataModle> services, Context context, List<String> expertiseIdPre) {
        super();
        this.services = services;
        this.context = context;
        this.expertiseIdPre = expertiseIdPre;
        this.setCheckedStatus = setCheckedStatus;
        expertiseIdUpdate = new ArrayList<>();
        Boolean check = false;
        for (int i = 0; i < services.size(); i++) {
            data_check.add(false);
        }
        for (int i = 0; i < services.size(); i++) {

            for (int j = 0; j < expertiseIdPre.size(); j++) {
                if (services.get(i).getId().compareTo(expertiseIdPre.get(j)) == 0) {
                    data_check.set(i, true);
                    expertiseIdUpdate.add(expertiseIdPre.get(j));
                    check = true;
                    break;
                }
            }

        }

    }

    @Override
    public ExpertInAdapterUpdate.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_expertin, parent, false);
        ExpertInAdapterUpdate.ViewHolder viewHolder = new ExpertInAdapterUpdate.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ExpertInAdapterUpdate.ViewHolder holder, final int position) {

        final GetDataModle category = services.get(position);
        holder.expertCheckBox.setText(category.getName());

        if (setCheckedStatus) {
            holder.expertCheckBox.setClickable(setCheckedStatus);
            holder.expertCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.expertCheckBox.isChecked()) {
                        expertiseIdUpdate.add(category.getId());
                        Log.e("cbbfbfgbfg", "" + expertiseIdUpdate);
                    } else {
                        expertiseIdUpdate.remove(category.getId());
                        Log.e("cbbfbfgbfg", "" + expertiseIdUpdate);
                    }
                }
            });
            holder.expertCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        data_check.set(position, true);
                    } else {
                        data_check.set(position, false);
                    }

                }
            });
        }
        else
        {
            holder.expertCheckBox.setClickable(setCheckedStatus);
        }

        Log.e("bcvbcvbcvb", "" + setCheckedStatus);

        holder.expertCheckBox.setChecked(data_check.get(position));


      /*  for(int k=0;k<data_check.size();k++)
        {
            if (data_check.contains(Boolean.TRUE))
            {
                if (data_check.get(k).toString().equals("true")) {
                    expertiseIdUpdate.add(category.getId());
                    Log.e("treee", expertiseIdUpdate + " " + category.getId());
                }else
                {
                   // expertiseIdUpdate.remove(position);
                    Log.e("vndnfvfdv", data_check.get(k).toString() + " " + category.getId());
                }
            }

        }*/



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
