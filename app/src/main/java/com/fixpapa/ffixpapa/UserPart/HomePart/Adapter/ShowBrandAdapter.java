package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import static com.fixpapa.ffixpapa.Services.Rest.Config.brandName;
import java.util.List;
import static com.fixpapa.ffixpapa.Services.Rest.Config.brandId;

public class ShowBrandAdapter extends RecyclerView.Adapter<ShowBrandAdapter.ViewHolder> {
    private Context context;
    List<Brand> services;
    int categorySize;
    private int selectedPosition = -1;

    public ShowBrandAdapter(List<Brand> services, Context context) {
        super();
        this.services = services;
        this.context = context;

    }

    @Override
    public ShowBrandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_productshow, parent, false);
        ShowBrandAdapter.ViewHolder viewHolder = new ShowBrandAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ShowBrandAdapter.ViewHolder holder, final int position) {
        final Brand category = services.get(position);
        holder.checkboxService.setText(category.getName());

        holder.checkboxService.setTag(position); // This line is important.

        if (position == selectedPosition) {
            holder.checkboxService.setChecked(true);
        } else holder.checkboxService.setChecked(false);
        holder.checkboxService.setOnClickListener(onStateChangedListener(holder.checkboxService, position));

    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    brandName=checkBox.getText().toString().trim();
                    brandId=services.get(position).getId();
                } else {
                    selectedPosition = -1;
                    brandName="";
                    brandId="";
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxService;

        public ViewHolder(View itemView) {
            super(itemView);
            checkboxService = (CheckBox) itemView.findViewById(R.id.checkboxService);
        }
    }
}
