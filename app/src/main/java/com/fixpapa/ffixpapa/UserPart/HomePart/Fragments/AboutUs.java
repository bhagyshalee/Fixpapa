package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;

import static android.text.Layout.DIR_LEFT_TO_RIGHT;

public class AboutUs extends AppCompatActivity {

    //TextView aboutUsText;
    ImageView crossImage;
    WebView aboutUsText;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        aboutUsText=(WebView)findViewById(R.id.aboutUsText);
        crossImage=(ImageView) findViewById(R.id.crossImage);

        aboutUsText.getSettings().setJavaScriptEnabled(true);
        aboutUsText.loadUrl("file:///android_asset/aboutus.html");
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
