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
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowProductsAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Brand;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Problem;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Product;

import java.util.ArrayList;
import java.util.List;
import static com.fixpapa.ffixpapa.Services.Rest.Config.productName;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowProductsAdapter.brandsAccording;


public class ShowProducts extends AppCompatActivity {

    RecyclerView productRecycler;
    Button buttonNextProduct;
    ShowProductsAdapter adapter;
    List<Brand> brandsList;
    List<Product> productList;
    List<Problem> problemList;
    String getMainCatName;
    ImageView backImage;
    TextView catNameShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        productList = (List<Product>) bundle.getSerializable("getProducts");
        brandsList = (List<Brand>) bundle.getSerializable("getBrands");
        problemList = (List<Problem>) bundle.getSerializable("getIssues");
        getMainCatName = bundle.getString("getMainCatName");
        productRecycler = (RecyclerView) findViewById(R.id.productRecycler);
        buttonNextProduct = (Button) findViewById(R.id.buttonNextProduct);
        backImage = (ImageView) findViewById(R.id.backImage);
        catNameShow = (TextView) findViewById(R.id.catNameShow);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        catNameShow.setText(getMainCatName);
        productRecycler.setHasFixedSize(false);
        productRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowProducts.this);
        productRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ShowProducts.this, layoutManager.getOrientation());
        productRecycler.addItemDecoration(dividerItemDecoration);

        adapter = new ShowProductsAdapter(productList, ShowProducts.this);
        productRecycler.setAdapter(adapter);

        buttonNextProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productName.equals(""))
                {
                    showToast(ShowProducts.this,"Please select Product");
                }
                else if (brandsAccording==null)
                {
                    Intent intent = new Intent(ShowProducts.this, ShowIssues.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("getIssues", (ArrayList<Problem>) problemList);
                    bundle.putString("getMainCatName",productName);
                    bundle.putString("getMainCat",productName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (brandsAccording.size()==1)
                {
                    if (brandsAccording.get(0).getName().equals(getMainCatName))
                    {
                        Intent intent = new Intent(ShowProducts.this, ShowIssues.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("getIssues", (ArrayList<Problem>) problemList);
                        bundle.putString("getMainCatName",productName);
                        bundle.putString("getMainCat",productName);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(ShowProducts.this, ShowBrands.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("getIssues", (ArrayList<Problem>) problemList);
                        bundle.putSerializable("getBrands", (ArrayList<Brand>) brandsAccording);
                        bundle.putString("getMainCatName",productName);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                else if (brandsAccording.size()>1)
                {
                    Intent intent = new Intent(ShowProducts.this, ShowBrands.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("getIssues", (ArrayList<Problem>) problemList);
                    bundle.putSerializable("getBrands", (ArrayList<Brand>) brandsAccording);
                    bundle.putString("getMainCatName",productName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }
}
