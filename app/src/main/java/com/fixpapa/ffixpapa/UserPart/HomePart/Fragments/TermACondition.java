package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;

import static android.text.Layout.DIR_LEFT_TO_RIGHT;

public class TermACondition extends AppCompatActivity {

    WebView termAConditionText;
    ImageView crossImage;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_acondition);
        termAConditionText = (WebView) findViewById(R.id.termAConditionText);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        termAConditionText.getSettings().setJavaScriptEnabled(true);
        termAConditionText.loadUrl("file:///android_asset/termcondition.html");

    }

}
