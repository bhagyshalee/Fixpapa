package com.fixpapa.ffixpapa.UserPart.HomePart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fixpapa.ffixpapa.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class ShowPaytmScreen extends AppCompatActivity {

    String orderId, custmerId, emailId, mobileNo;
    int totalPrice;
    WebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_paytm_screen);

        Bundle bundle = getIntent().getExtras();
        getUserDetailId(ShowPaytmScreen.this);

        if (bundle != null) {
            orderId = bundle.getString("orderId");
            custmerId = bundle.getString("custmId");
            emailId = bundle.getString("email");
            mobileNo = bundle.getString("mobile");
            totalPrice = bundle.getInt("totalPrice");
        }

        Log.e("fgjiof", "" + orderId + " " + custmerId + " " + emailId + " " + mobileNo);
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

       // String url = "http://139.59.71.150:3008/api/Transactions/payment";
        String url = "https://api.fixpapa.com/api/Transactions/payment";
        String rpostData = null;
        try {
            rpostData = "MID=" + URLEncoder.encode("FIXPAP29847308876050", "UTF-8") + "&ORDER_ID=" + URLEncoder.encode(orderId, "UTF-8") + "&CUST_ID=" + URLEncoder.encode(custmerId, "UTF-8") + "&INDUSTRY_TYPE_ID=" + URLEncoder.encode("Retail109", "UTF-8") + "&CHANNEL_ID=" + URLEncoder.encode("WEB", "UTF-8") + "&TXN_AMOUNT=" + URLEncoder.encode(String.valueOf(totalPrice), "UTF-8") + "&WEBSITE=" + URLEncoder.encode("WEBPROD", "UTF-8") + "&EMAIL=" + URLEncoder.encode(emailId, "UTF-8") + "&MOBILE_NO=" + URLEncoder.encode(mobileNo, "UTF-8") + "&CALLBACK_URL=" + URLEncoder.encode("https://api.fixpapa.com/api/Transactions/paytmResponseUrl", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (rpostData != null) {
            webview.postUrl(url, rpostData.getBytes());
            webview.setWebViewClient(new WebViewClient());
        }


        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Toast.makeText(getApplicationContext(), "Loading page...", Toast.LENGTH_LONG).show();
                Log.e("sdcsdc", url);
                if (url.equals("https://api.fixpapa.com/api/Transactions/payment-success")) {
                    showToast(ShowPaytmScreen.this, "Payment successfully done");
                    /*Intent intent=new Intent(ShowPaytmScreen.this,UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();*/
                    finish();
                } else if (url.equals("https://api.fixpapa.com/api/Transactions/payment-failure")) {
                    showToast(ShowPaytmScreen.this, "Payment failure");
                    finish();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(getApplicationContext(), "Page is loaded.", Toast.LENGTH_LONG).show();
                Log.e("sdcsdc", url);
                if (url.equals("https://api.fixpapa.com/api/Transactions/payment-success")) {

                    showToast(ShowPaytmScreen.this, "Payment successfully done");
                    Intent intent=new Intent(ShowPaytmScreen.this,UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (url.equals("https://api.fixpapa.com/api/Transactions/payment-failure")) {
                    showToast(ShowPaytmScreen.this, "Payment failure");
                    finish();
                }
            }

            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                super.onReceivedError(webView, errorCode, description, failingUrl);
                showToast(ShowPaytmScreen.this, ""+description);
                Log.e("sdcsdc", failingUrl + "" + description);
            }
        });
    }

}
