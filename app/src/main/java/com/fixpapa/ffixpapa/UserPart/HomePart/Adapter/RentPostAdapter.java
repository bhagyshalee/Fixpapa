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

import java.util.ArrayList;
import java.util.List;

public class RentPostAdapter extends RecyclerView.Adapter<RentPostAdapter.ViewHolder> {
    private Context context;
    List<String> name;
    List<String> CatId;
    String[] plants;
    public static List<String> getSelectedRentCat;
    public static List<String> getSelectedSystems;
    public static List<String> getSelectedTimePeriod;
    int posUnits=0,posTimePeriod=0;
    public static List<CheckBox> getSelecCheck;


    public RentPostAdapter(List<String> name,List<String> CatId, Context context) {
        super();
        this.name = name;
        this.CatId = CatId;
        this.context = context;
        plants = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"
        };
        getSelectedRentCat = new ArrayList<>();
        getSelectedSystems = new ArrayList<>();
        getSelectedTimePeriod = new ArrayList<>();
        getSelecCheck = new ArrayList<>();
    }

    @Override
    public RentPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_rent_post, parent, false);
        RentPostAdapter.ViewHolder viewHolder = new RentPostAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RentPostAdapter.ViewHolder holder, final int position) {

        holder.checkboxBrands.setText(name.get(position));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.design_spinner, R.id.textSpinner, plants) {
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
        holder.noOfSystem.setAdapter(spinnerArrayAdapter);
        getSelecCheck.add(holder.checkboxBrands);
        final ArrayAdapter<String> spinnerArrayAdapte = new ArrayAdapter<String>(context, R.layout.design_spinner, R.id.textSpinner, plants) {
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

        spinnerArrayAdapte.setDropDownViewResource(R.layout.design_spinner);
        holder.timesOfPeriod.setAdapter(spinnerArrayAdapte);
        getSelectedRentCat.add(CatId.get(position));

        holder.noOfSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positio, long id) {
                if (posUnits > position) {
                    getSelectedSystems.set(position, String.valueOf(holder.noOfSystem.getItemAtPosition(positio)));
                    Log.e("posjdsd", position + "::" + getSelectedSystems);
                } else {
                    getSelectedSystems.add(String.valueOf(holder.noOfSystem.getItemAtPosition(positio)));
                    posUnits=posUnits+1;
                    Log.e("hmgm", position + "::" + getSelectedSystems);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("dfb", "" + position);
            }
        });
        holder.timesOfPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positi, long id) {

                if (posTimePeriod > position) {
                    getSelectedTimePeriod.set(position, String.valueOf(holder.timesOfPeriod.getItemAtPosition(positi)));
                    //Log.e("posjdsd", position + "::" + getSelectedPosi);
                } else {
                    getSelectedTimePeriod.add(String.valueOf(holder.timesOfPeriod.getItemAtPosition(positi)));
                    posTimePeriod=posTimePeriod+1;
                    ///Log.e("hmgm", position + "::" + getSelectedPosi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("dfb", "" + position);
            }
        });
/*
        holder.checkboxBrands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkboxBrands.setSelected(true);
                    getSelectedRentCat.add(String.valueOf(holder.checkboxBrands.getText().toString()));

                } else {
                    holder.checkboxBrands.setSelected(false);
                    getSelectedRentCat.remove(String.valueOf(holder.checkboxBrands.getText().toString()));
                  //  getSelectedSystems.remove(String.valueOf(holder.noOfSystem.getItemAtPosition(position)));
                   // getSelectedTimePeriod.remove(String.valueOf(holder.timesOfPeriod.getItemAtPosition(position)));
                }
            }
        });
*/
    }

    @Override
    public int getItemCount() {

        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxBrands;
        Spinner noOfSystem, timesOfPeriod;

        public ViewHolder(View itemView) {
            super(itemView);

            checkboxBrands = (CheckBox) itemView.findViewById(R.id.checkboxBrands);
            noOfSystem = (Spinner) itemView.findViewById(R.id.noOfSystem);
            timesOfPeriod = (Spinner) itemView.findViewById(R.id.timesOfPeriod);


        }
    }
}
