package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowBrandAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Problem;

import java.util.ArrayList;
import java.util.List;
import static com.fixpapa.ffixpapa.Services.Rest.Config.brandName;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;


public class ShowBrands extends AppCompatActivity {
    RecyclerView brandsRecycler;
    Button buttonNextBrands;
    ImageView backImage;
    List<Brand> brandList;
    List<Problem> productIssues;
    ShowBrandAdapter showBrandAdapter;
    TextView catName;
    String productName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_brands);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        brandList = (List<Brand>) bundle.getSerializable("getBrands");
        productIssues = (List<Problem>) bundle.getSerializable("getIssues");
        productName = bundle.getString("getMainCatName");

        backImage = (ImageView) findViewById(R.id.backImage);
        brandsRecycler = (RecyclerView) findViewById(R.id.brandsRecycler);
        buttonNextBrands = (Button) findViewById(R.id.buttonNextBrands);
        catName = (TextView) findViewById(R.id.catName);
        catName.setText(productName);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        brandsRecycler.setHasFixedSize(false);
        brandsRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowBrands.this);
        brandsRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ShowBrands.this, layoutManager.getOrientation());
        brandsRecycler.addItemDecoration(dividerItemDecoration);
        showBrandAdapter = new ShowBrandAdapter(brandList, ShowBrands.this);
        brandsRecycler.setAdapter(showBrandAdapter);

        buttonNextBrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brandName.equals("")) {
                    showToast(ShowBrands.this, "Please select Brand");
                } else {
                    Intent intent = new Intent(ShowBrands.this, ShowIssues.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("getIssues", (ArrayList<Problem>) productIssues);
                    bundle.putString("getMainCat", brandName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }


}
