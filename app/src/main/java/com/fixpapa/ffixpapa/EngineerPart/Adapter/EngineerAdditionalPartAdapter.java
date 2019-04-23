package com.fixpapa.ffixpapa.EngineerPart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.AditionPart;
import com.fixpapa.ffixpapa.R;

import java.util.ArrayList;
import java.util.List;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class EngineerAdditionalPartAdapter extends RecyclerView.Adapter<EngineerAdditionalPartAdapter.ViewHolder> {
    private Context context;
   public static List<AditionPart> additionalServices=null;
    List<String> stringList;

    public EngineerAdditionalPartAdapter(List<AditionPart> services, Context context) {
        super();
        this.additionalServices = services;
        this.context = context;
        stringList = new ArrayList<>();
    }

    @Override
    public EngineerAdditionalPartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_additional_part, parent, false);
        EngineerAdditionalPartAdapter.ViewHolder viewHolder = new EngineerAdditionalPartAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EngineerAdditionalPartAdapter.ViewHolder holder, final int position) {
        getUserDetailId(context);
        final AditionPart category = additionalServices.get(position);
        holder.designPartName.setText(category.getPartName());
        holder.designPartNumber.setText(category.getPartNumber());
        holder.designCost.setText(category.getCost());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalServices.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, additionalServices.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView designPartName, designPartNumber, designCost;
        ImageView deleteItem;

        public ViewHolder(View itemView) {
            super(itemView);

            designPartName = (TextView) itemView.findViewById(R.id.designPartName);
            designPartNumber = (TextView) itemView.findViewById(R.id.designPartNumber);
            designCost = (TextView) itemView.findViewById(R.id.designCost);
            deleteItem = (ImageView) itemView.findViewById(R.id.deleteItem);


        }
    }
}