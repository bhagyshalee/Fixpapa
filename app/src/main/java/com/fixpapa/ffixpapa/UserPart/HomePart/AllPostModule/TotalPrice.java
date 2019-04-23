package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowTotalPriceAdapter;
import com.fixpapa.ffixpapa.UserPart.SelectAddress;

import java.util.ArrayList;

import static android.text.Layout.DIR_LEFT_TO_RIGHT;
import static com.fixpapa.ffixpapa.Services.Rest.Config.totalPrice;

public class TotalPrice extends AppCompatActivity implements View.OnClickListener {

    TextView totalAmountCount, termAndCondition, whatsAmount, guaranteeText,termAndConditionV,whatsAmountV;
    RecyclerView totalPriceRecycler;
    Button buttonNextTotal;
    ShowTotalPriceAdapter showIssueAdapter;
    ArrayList<String> issueList;

    ArrayList<String> issueId;
    ArrayList<String> issueCategoryId;
    ArrayList<String> issueProbContent;
    ArrayList<String> issueCreatedAt;
    ArrayList<String> issueUpdatedAt;

    ArrayList<Integer> issueListPrice;
    int addPrice = 0;
    ImageView backImage;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        //issueList = (ArrayList<String>)bundle.getSerializable("issueValue");
        issueList = (ArrayList<String>) getIntent().getSerializableExtra("issueValue");
        issueListPrice = (ArrayList<Integer>) getIntent().getSerializableExtra("issueValuePrice");

        issueId = (ArrayList<String>) getIntent().getSerializableExtra("issueId");
        issueCategoryId = (ArrayList<String>) getIntent().getSerializableExtra("issueCategoryId");
        issueProbContent = (ArrayList<String>) getIntent().getSerializableExtra("issueProbContent");
        issueCreatedAt = (ArrayList<String>) getIntent().getSerializableExtra("" +
                "issueCreatedAt");
        issueUpdatedAt = (ArrayList<String>) getIntent().getSerializableExtra("issueUpdatedAt");

        totalAmountCount = (TextView) findViewById(R.id.totalAmountCount);
        termAndConditionV = (TextView) findViewById(R.id.termAndConditionV);
        termAndCondition = (TextView) findViewById(R.id.termAndCondition);
        whatsAmountV = (TextView) findViewById(R.id.whatsAmountV);
        whatsAmount = (TextView) findViewById(R.id.whatsAmount);
        guaranteeText = (TextView) findViewById(R.id.guaranteeText);
        totalPriceRecycler = (RecyclerView) findViewById(R.id.totalPriceRecycler);
        buttonNextTotal = (Button) findViewById(R.id.buttonNextTotal);
        backImage = (ImageView) findViewById(R.id.backImage);


        buttonNextTotal.setOnClickListener(this);
        backImage.setOnClickListener(this);
        for (int i = 0; i < issueListPrice.size(); i++) {
            addPrice = addPrice + issueListPrice.get(i);
            Log.e("vvvfdvfdvd", "" + addPrice);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            termAndConditionV.setJustificationMode(DIR_LEFT_TO_RIGHT);
            whatsAmountV.setJustificationMode(DIR_LEFT_TO_RIGHT);
        }

        termAndConditionV.setText("Warranty/Guarantee on new product or spare parts would be as described on the material bill online. Please insist to generate the material bill online from our FIXPAPA IT Professional in case he is procuring the materials for you.\n" +"\n"+

                "We FIXPAPA are individual IT sales & service provider and have a well-qualified and police verified IT Professionals. We are not authorized service centre for any IT brand.\n" +"\n"+
                "Any device which is repairable would be insured only up to the repair amount or the amount mentioned above or the depreciated value which will be verified by FIXPAPA IT Professional.\n" +"\n"+
                "Physical damage, adaptor, charger and any sort of software will not be covered in warranty in any case.\n" +
                "We strongly discourage any direct contact with our FIXPAPA IT Professional regarding a job booked through us, as in such cases FIXPAPA will not be liable or responsible and has no control over the situation or condition. All warranty and service assurance offered by FIXPAPA, in such case, stands null & void.\n" +"\n"+
                "If you do have any complaints, suggestions or feedback with regard to the service or product quality, or our service provider, pricing etc. please report it to our Customer Support Desk at 0141-2980666, +919799044449 or mail us at support@fixpapa.com. It is advisable to do this within 48hrs of your service.\n"+"\n");

        whatsAmountV.setText("Procedure - Initially the faulty system will be inspected by FIXPAPA IT Professional, details and a quote will be provided and once approved by you it will be fixed. In case faulty product need to be picked up and inspected at our partner service station, an online quote will be provided on your panel (web & app) and once approved by you it will be fixed and delivered back.\n" +"\n"+
                "Prices – Prices which is shown in web or app for any service or product may very after the inspection of faulty product by FIXPAPA IT Professional.\n" +"\n"+
                "Cancellation & Charges - In case you book a service with FIXPAPA, our IT Professional visit your premises, same moment you can cancel the call, No charge will be taken for this situation, but if you ask our IT Professional to inspect the faulty product, then the mentioned or minimum service charges has to be given to our IT Professional.\n" +"\n"+
                "Warranty - Maximum  7 days warranty for Software related issues, Hardware related issues as per part used and repair performed\n" +"\n"+
                "Liability – No liability for pre-existing issues/potential risks reported by the service centre but not handled due to customer refusal\n" +"\n"+
                "Parts used (if applicable) - Genuine or compatible parts used depending on customer request & warranties will be applicable accordingly.\n" +"\n"+
                "Before you start - Since data can be wiped off in case of OS installation, inform IT Professional if you need a backup; charges will apply.\n" +"\n"+
                "Data Backup & Recovery - Depending on the status of the memory drive charges may vary based on diagnosis. We do not take or provide any kind of responsibility for data backup data lost or data recovery.\n" +"\n"+
                "Software Installation – FIXPAPA only recommends Genuine Software will be installed. If you wish to install procured software, provide the license key to the IT Professional.\n" +"\n"+
                "Tax – All prices are inclusive of Tax/GST as applicable by Govt’s Policies\n");
        String udata = String.valueOf(getText(R.string.term_condition_text));
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        termAndCondition.setText(content);

        termAndConditionV.setMovementMethod(new ScrollingMovementMethod());
        whatsAmountV.setMovementMethod(new ScrollingMovementMethod());

        String udatfa = String.valueOf("What's This Amount Includes");
        SpannableString contentt = new SpannableString(udatfa);
        contentt.setSpan(new UnderlineSpan(), 0, udatfa.length(), 0);
        whatsAmount.setText(contentt);

        String udatf = String.valueOf("FIXPAPA Assurance");
        SpannableString conten = new SpannableString(udatf);
        conten.setSpan(new UnderlineSpan(), 0, udatf.length(), 0);
        guaranteeText.setText(conten);

        totalAmountCount.setText("Rs." + addPrice + "/-");
        totalPrice= String.valueOf(addPrice);

        totalPriceRecycler.setHasFixedSize(false);
        totalPriceRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TotalPrice.this);
        totalPriceRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(TotalPrice.this, layoutManager.getOrientation());
        totalPriceRecycler.addItemDecoration(dividerItemDecoration);
        showIssueAdapter = new ShowTotalPriceAdapter(issueListPrice, issueList, TotalPrice.this);
        totalPriceRecycler.setAdapter(showIssueAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == backImage) {
            finish();
        }
        if (v == buttonNextTotal) {
            Intent intent = new Intent(TotalPrice.this, SelectAddress.class);
            intent.putExtra("issueValueList", issueList);
            startActivity(intent);
        }
    }
}
