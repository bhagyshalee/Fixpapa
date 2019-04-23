package com.fixpapa.ffixpapa.VendorPart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;

public class EngineerProfileView extends AppCompatActivity implements View.OnClickListener{

    ImageView crossImage,engineerProfilePic,whatsappImage;
    RatingBar ratingEngineerProfile;
    TextView engineerName,experienceValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_engineer_profile);
        ratingEngineerProfile=(RatingBar)findViewById(R.id.ratingEngineerProfile);
        crossImage=(ImageView) findViewById(R.id.crossImage);
        engineerProfilePic=(ImageView) findViewById(R.id.engineerProfilePic);
        whatsappImage=(ImageView) findViewById(R.id.whatsappImage);
        engineerName=(TextView) findViewById(R.id.engineerName);
        experienceValue=(TextView) findViewById(R.id.experienceValue);
        crossImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==crossImage)
        {
            finish();
        }
    }
}
