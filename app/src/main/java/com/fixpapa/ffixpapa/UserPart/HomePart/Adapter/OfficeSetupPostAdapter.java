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

public class OfficeSetupPostAdapter extends RecyclerView.Adapter<OfficeSetupPostAdapter.ViewHolder> {
    private Context context;
    List<AmcCategory> services;
    int pos = 0;
    List<String> listUnit;
    public static List<String> getSelectedSystem;
    public static List<String> getSelectedUnits;
    public static List<CheckBox> getSelectedCheck;

    public OfficeSetupPostAdapter(List<String> listUnit, List<AmcCategory> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        this.listUnit = listUnit;

        getSelectedSystem = new ArrayList<>();
        getSelectedUnits = new ArrayList<>();
        getSelectedCheck = new ArrayList<>();
    }

    @Override
    public OfficeSetupPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_amc, parent, false);
        OfficeSetupPostAdapter.ViewHolder viewHolder = new OfficeSetupPostAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final OfficeSetupPostAdapter.ViewHolder holder, final int position) {
        final AmcCategory category = services.get(position);
        Log.e("rgbrtrt",""+category.getName());
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

        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        holder.noOfUnits.setAdapter(spinnerArrayAdapter);
        getSelectedCheck.add(holder.checkboxBrands);
        getSelectedSystem.add(category.getName());

        holder.noOfUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positio, long id) {

                if (pos > position) {
                    getSelectedUnits.set(position, String.valueOf(holder.noOfUnits.getItemAtPosition(positio)));
               } else {
                    getSelectedUnits.add(String.valueOf(holder.noOfUnits.getItemAtPosition(positio)));
                   pos=pos+1;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


     /*   holder.checkboxBrands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSelectedSystem.add(category.getName());
                if (isChecked) {
                    holder.checkboxBrands.setSelected(true);
                   // getSelectedSystem.add(category.getName());
                } else {
                    holder.checkboxBrands.setSelected(false);
                   // getSelectedSystem.remove(category.getName());
                    //getSelectedUnits.remove(String.valueOf(holder.noOfUnits.getItemAtPosition(position)));
                }
            }
        });*/

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
