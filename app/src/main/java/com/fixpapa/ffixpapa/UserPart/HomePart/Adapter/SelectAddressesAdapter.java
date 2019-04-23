package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AddressesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SelectAddressesAdapter extends RecyclerView.Adapter<SelectAddressesAdapter.ViewHolder> {
    private Context context;
    List<AddressesModel> services;
    TextView textView;
    int row_index=-1;

    public SelectAddressesAdapter(List<AddressesModel> services, Context context, TextView textView) {
        super();
        this.services = services;
        this.context = context;
        this.textView = textView;
    }

    @Override
    public SelectAddressesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_select_address, parent, false);
        SelectAddressesAdapter.ViewHolder viewHolder = new SelectAddressesAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final SelectAddressesAdapter.ViewHolder holder, final int position) {
        final AddressesModel category = services.get(position);
        holder.addressText.setText(category.getStreet().toString() + " " + category.getValue());



        holder.addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            JSONObject subObject = new JSONObject();
            JSONObject latlong = new JSONObject();
           JSONObject innerAddressObject= new JSONObject();
            try {
                latlong.put("lat", category.getLocation().getLat());
                latlong.put("lng", category.getLocation().getLng());
                innerAddressObject.put("value", category.getValue());
                innerAddressObject.put("location", latlong);
                innerAddressObject.put("street", category.getStreet());
                SharedPreferences.Editor editor = context.getSharedPreferences("SELECTED_ADDRESS", MODE_PRIVATE).edit();
                editor.putString("jsonAdd",innerAddressObject.toString());
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            textView.setText(holder.addressText.getText().toString());
            holder.addressText.setBackgroundColor(context.getResources().getColor(R.color.transparent));

        }
        else
        {
            holder.addressText.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }


    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressText;
        LinearLayout mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            addressText = (TextView) itemView.findViewById(R.id.addressText);
            mainLay = (LinearLayout) itemView.findViewById(R.id.mainLay);


        }
    }
}
