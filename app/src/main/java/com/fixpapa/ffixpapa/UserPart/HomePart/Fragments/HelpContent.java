package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.fixpapa.ffixpapa.R;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;

public class HelpContent extends AppCompatActivity {

    WebView helpText;
    ImageView crossImage;
    String LOGIN_TYPE;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_content);
        getUserDetailId(HelpContent.this);
        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        LOGIN_TYPE= sharedPref.getString("LOGIN_TYPE","");

        helpText = (WebView) findViewById(R.id.helpText);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        helpText.getSettings().setJavaScriptEnabled(true);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (LOGIN_TYPE.equals("vendor")) {
            helpText.loadUrl("file:///android_asset/vendorhelp.html");
        }
       else if (LOGIN_TYPE.equals("customer")) {
            helpText.loadUrl("file:///android_asset/customerhelp.html");
        }
        else if (LOGIN_TYPE.equals("engineer")) {
            helpText.loadUrl("file:///android_asset/engineerhelp.html");
        }
        else {
            helpText.loadUrl("file:///android_asset/customerhelp.html");
        }


    }
}
