package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.AmcCategory;

import java.util.ArrayList;
import java.util.List;

public class AmcPostAdapter extends RecyclerView.Adapter<AmcPostAdapter.ViewHolder> {
    public static List<String> getSelectedStatus;
    public static List<String> getSelectedPosi;
    List<AmcCategory> services;
    int pos = 0;
    List<String> listUnit;
    private Context context;
    public static List<CheckBox> getCehckBox;


    public AmcPostAdapter(List<String> listUnit, List<AmcCategory> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.listUnit = listUnit;

        getSelectedStatus = new ArrayList<>();
        getSelectedPosi = new ArrayList<>();
        getCehckBox = new ArrayList<>();

    }

    @Override
    public AmcPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_amc, parent, false);
        AmcPostAdapter.ViewHolder viewHolder = new AmcPostAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final AmcPostAdapter.ViewHolder holder, final int position) {
        final AmcCategory category = services.get(position);

        holder.checkboxBrands.setText(category.getId());
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.design_spinner, R.id.textSpinner, listUnit) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        getCehckBox.add(holder.checkboxBrands);
        getSelectedStatus.add(category.getName());
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        holder.noOfUnits.setAdapter(spinnerArrayAdapter);

        holder.noOfUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positio, long id) {
                    if (pos > position) {
                        getSelectedPosi.set(position, String.valueOf(holder.noOfUnits.getItemAtPosition(holder.noOfUnits.getSelectedItemPosition())));
                       // Log.e("posjdsd", position + "::" + getSelectedPosi);
                    } else {
                        getSelectedPosi.add(String.valueOf(holder.noOfUnits.getItemAtPosition(holder.noOfUnits.getSelectedItemPosition())));
                        pos = pos + 1;
                     //   Log.e("hmgm", position + "::" + getSelectedPosi);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*
        holder.checkboxBrands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    holder.checkboxBrands.setSelected(true);
                    getSelectedStatus.add(String.valueOf(holder.checkboxBrands.getText().toString()));
                   // getSelectedPosi.add(String.valueOf(holder.noOfUnits.getItemAtPosition(holder.noOfUnits.getSelectedItemPosition())));
                    //Log.e("heloop", "" + getSelectedPosi.size());

                } else {
                    holder.checkboxBrands.setSelected(false);
                    getSelectedStatus.remove(String.valueOf(holder.checkboxBrands.getText().toString()));
                    //getSelectedPosi.remove(String.valueOf(holder.noOfUnits.getItemAtPosition(holder.noOfUnits.getSelectedItemPosition())));
                }
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxBrands;
        Spinner noOfUnits;

        public ViewHolder(View itemView) {
            super(itemView);

            checkboxBrands = (CheckBox) itemView.findViewById(R.id.checkboxBrands);
            noOfUnits = (Spinner) itemView.findViewById(R.id.noOfUnits);


        }
    }
}
