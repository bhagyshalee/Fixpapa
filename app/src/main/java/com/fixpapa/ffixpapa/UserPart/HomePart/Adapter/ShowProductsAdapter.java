package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Product;
import java.util.ArrayList;
import java.util.List;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productId;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productName;


public class ShowProductsAdapter extends RecyclerView.Adapter<ShowProductsAdapter.ViewHolder> {
    private Context context;
    List<Product> services;
    int categorySize;
    private int selectedPosition = -1;
   public static List<Brand> brandsAccording=null;

    public ShowProductsAdapter(List<Product> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.categorySize = categorySize;
    }

    @Override
    public ShowProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_productshow, parent, false);
        ShowProductsAdapter.ViewHolder viewHolder = new ShowProductsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShowProductsAdapter.ViewHolder holder, final int position) {
        final Product category = services.get(position);

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
                    productName = checkBox.getText().toString().trim();
                    productId = services.get(position).getId();
                    brandsAccording=new ArrayList<>();
                    brandsAccording=services.get(position).getBrand();
                } else {
                    selectedPosition = -1;
                    productName = "";
                    productId = "";
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
