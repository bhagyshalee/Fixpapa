package com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.BlockEngineer;
import com.fixpapa.ffixpapa.VendorPart.EngineerRegistration;
import com.fixpapa.ffixpapa.VendorPart.UpdateEngineer;

public class SettingFragment extends Fragment {
LinearLayout addEngineerLayout,deleteEngineerLayout,updateEngineerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);
        deleteEngineerLayout=(LinearLayout)v.findViewById(R.id.deleteEngineerLayout);
        addEngineerLayout=(LinearLayout)v.findViewById(R.id.addEngineerLayout);
        updateEngineerLayout=(LinearLayout)v.findViewById(R.id.updateEngineerLayout);

        addEngineerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EngineerRegistration.class);
                startActivity(intent);
            }
        });

        deleteEngineerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BlockEngineer.class);
                startActivity(intent);
            }
        });

        updateEngineerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UpdateEngineer.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
