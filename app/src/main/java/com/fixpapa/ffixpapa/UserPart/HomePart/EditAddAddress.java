package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class EditAddAddress extends AppCompatActivity implements View.OnClickListener {

    TextView userLocation, userStreetName;
    ImageView closeDialog;
    Button saveAddressButton;
    double latitude, longitude;
    String strStreet,strLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_address);
        userLocation = (TextView) findViewById(R.id.userLocation);
        userStreetName = (TextView) findViewById(R.id.userStreetName);
        closeDialog = (ImageView) findViewById(R.id.closeDialog);
        saveAddressButton = (Button) findViewById(R.id.saveAddressButton);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null)
        {
           strStreet= bundle.getString("street");
           strLocation= bundle.getString("valueadd");
            userStreetName.setText(""+strStreet);
            userLocation.setText(""+strLocation);
        }


        closeDialog.setOnClickListener(this);
        userLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == closeDialog) {
            finish();
        }
        if (v == userLocation) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(EditAddAddress.this);
                startActivityForResult(intent, 12);
            } catch (Exception e) {
                Log.e("Address", e.getStackTrace().toString());
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("datastr", "" + data);
        if (requestCode == 12) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                userLocation.setText(place.getAddress());
                //latitude=place.getl
            }
        }

    }
}
