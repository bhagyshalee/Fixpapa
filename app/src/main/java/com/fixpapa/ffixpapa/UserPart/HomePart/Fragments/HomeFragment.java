package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.FixProblemHomeAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.MaintenanceHomeAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.OfficeSetupHomeAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.PurchaseHomeAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.RentHomeAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Success;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.Model.CategoryNew;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public static int row_index;
    RecyclerView fixProblemRecycler, purchaseRecycler, officeSetupRecycler, maintenanceRecycler, rentRecycler;
    TextView fixProblemSolveText, purchaseText, officeSetupText, maintenanceText, rentText, showAllFixProblem, showAllPurchase,
            showAllOfficeSetUp, showAllMaintenance, showAllRent;
    FixProblemHomeAdapter problemHomeAdapter;
    PurchaseHomeAdapter purchaseHomeAdapter;
    MaintenanceHomeAdapter maintenanceHomeAdapter;
    OfficeSetupHomeAdapter officeSetupHomeAdapter;
    RentHomeAdapter rentHomeAdapter;
    CategoryFragment categoryFragment;
    String customerType;
    ArrayList<CategoryNew> data_arr = new ArrayList<>();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        context=getActivity();
        getUserDetailId(getActivity());
        fixProblemRecycler = (RecyclerView) v.findViewById(R.id.fixProblemRecycler);
        purchaseRecycler = (RecyclerView) v.findViewById(R.id.purchaseRecycler);
        officeSetupRecycler = (RecyclerView) v.findViewById(R.id.officeSetupRecycler);
        maintenanceRecycler = (RecyclerView) v.findViewById(R.id.maintenanceRecycler);
        rentRecycler = (RecyclerView) v.findViewById(R.id.rentRecycler);

        fixProblemSolveText = (TextView) v.findViewById(R.id.fixProblemSolveText);
        purchaseText = (TextView) v.findViewById(R.id.purchaseText);
        officeSetupText = (TextView) v.findViewById(R.id.officeSetupText);
        maintenanceText = (TextView) v.findViewById(R.id.maintenanceText);
        rentText = (TextView) v.findViewById(R.id.rentText);
        showAllFixProblem = (TextView) v.findViewById(R.id.showAllFixProblem);
        showAllPurchase = (TextView) v.findViewById(R.id.showAllPurchase);
        showAllOfficeSetUp = (TextView) v.findViewById(R.id.showAllOfficeSetUp);
        showAllMaintenance = (TextView) v.findViewById(R.id.showAllMaintenance);
        showAllRent = (TextView) v.findViewById(R.id.showAllRent);
        categoryFragment = new CategoryFragment();
        fixProblemSolveText.setText(getText(R.string.fixproblem_text_edit));
        purchaseText.setText(getText(R.string.purchase_text_edit));
        officeSetupText.setText(getText(R.string.office_setup_text_edit));
        maintenanceText.setText(getText(R.string.maintenance_text_edit));
        rentText.setText(getText(R.string.rent_text_edit));
        showAllFixProblem.setOnClickListener(this);
        showAllMaintenance.setOnClickListener(this);
        showAllOfficeSetUp.setOnClickListener(this);
        showAllRent.setOnClickListener(this);
        showAllPurchase.setOnClickListener(this);
        SharedPreferences shared = getActivity().getSharedPreferences("REG_TOKEN", MODE_PRIVATE);
        String channel = shared.getString(getResources().getString(R.string.firebase_token), "");
        Log.e("dfvgfdsdvsdv", "" + channel);

        fixProblemRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fixProblemRecycler.setLayoutManager(layoutManager);

        purchaseRecycler.setHasFixedSize(true);
        LinearLayoutManager purchase
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        purchaseRecycler.setLayoutManager(purchase);

        officeSetupRecycler.setHasFixedSize(true);
        LinearLayoutManager office
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        officeSetupRecycler.setLayoutManager(office);

        maintenanceRecycler.setHasFixedSize(true);
        LinearLayoutManager maintenance
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        maintenanceRecycler.setLayoutManager(maintenance);

        rentRecycler.setHasFixedSize(true);
        LinearLayoutManager rent
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rentRecycler.setLayoutManager(rent);
        if (isOnline(true, getActivity())) {
            callHomePage();
        }

        return v;
    }

    private void callHomePage() {
        showProgress(getActivity());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Success> modelCall = apiInterface.userService();
        modelCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                //close();
                if (response.isSuccessful()) {
                  /*  for (int i=0;i<response.body().getSuccess().getData().getCategories().size();i++) {
                        fixproblem_text.add(String.valueOf(response.body().getSuccess().getData().getCategories().get(i).getName()));
                        Log.e("userHomthththe", "Failed"+response.body().getSuccess().getData().getCategories().size());
                    }
                    for (int i=0;i<response.body().getSuccess().getData().getNewpurchases().size();i++) {
                        purchase_text.add(String.valueOf(response.body().getSuccess().getData().getNewpurchases().get(i).getName()));
                    }
                    for (int i=0;i<response.body().getSuccess().getData().getBid().size();i++) {
                        office_setup_text.add(String.valueOf(response.body().getSuccess().getData().getBid().get(i).getName()));
                    }
                    for (int i=0;i<response.body().getSuccess().getData().getAmc().size();i++) {
                        maintenance_text.add(String.valueOf(response.body().getSuccess().getData().getAmc().get(i).getName()));
                    }
                    for (int i=0;i<response.body().getSuccess().getData().getRent().size();i++) {
                        rent_text.add(String.valueOf(response.body().getSuccess().getData().getRent().get(i).getName()));
                    }*/
                /*  for(int i=0;i<response.body().getSuccess().getData().getCategories().size();i++) {
                      JSONArray jsonArray = new JSONArray();
                      jsonArray.put(response.body().getSuccess().getData().getCategories().get(i).getProducts());
                      products.add(response.body().getSuccess().getData().getCategories().get(i).getProducts());
                  }

                  for (int j=0;j<response.body().getSuccess().getData().getCategories().size();j++) {
                      for (int i = 0; i < response.body().getSuccess().getData().getCategories().get(j).getProducts().size(); i++) {
                          JSONArray jsonArrayBrand = new JSONArray();
                          jsonArrayBrand.put(response.body().getSuccess().getData().getCategories().get(j).getProducts().get(i).getBrand());
                          brands.add(response.body().getSuccess().getData().getCategories().get(j).getProducts().get(i).getBrand());
                      }
                  }*/


                  /*  JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsoData = jsSuccess.getJSONObject("data");
                        JSONArray jsonCategory = jsoData.getJSONArray("categories");
                       // JSONArray getJsonProduct = jsonCategory.getJSONArray("products");
                        //JSONArray getJsonProduct = ge.getJSONArray("brandIds");
                        JSONArray getJsonBrands;
                        JSONArray getJsonProduct;
                        JSONObject jOBJ=null;
                        data_arr.clear();
                        for (int i = 0; i < jsonCategory.length(); i++)
                        {
                            jOBJ = jsonCategory.getJSONObject(i);
                            CategoryNew contnt=new CategoryNew();
                            contnt.setName(jOBJ.optString("name"));



                            // getJsonProduct = jOBJ.getJSONArray("products");
                            JSONObject products_obj=null;
                            ArrayList<String> p_arr=new ArrayList();
                            ArrayList<ArrayList<String>> brandarr=new ArrayList<>();
                            p_arr.clear();
                            brandarr.clear();
                            for (int j = 0; j < jOBJ.optJSONArray("products").length(); j++)
                            {
                                products_obj= jOBJ.optJSONArray("products").getJSONObject(j);
                                p_arr.add(products_obj.optString("name"));
                                ArrayList<String> brand=new ArrayList<>();
                                JSONObject b_obj=null;
                                brand.clear();
                                for (int k=0;k<products_obj.optJSONArray("brand").length();k++){
                                    b_obj=products_obj.optJSONArray("brand").getJSONObject(k);
                                    brand.add(b_obj.optString("name"));
                                }
                                brandarr.add(brand);

                            }
                            contnt.setProducts(p_arr);
                            contnt.setBrands(brandarr);
                            data_arr.add(contnt);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                    if (response.body().getSuccess().getData() != null) {
                        problemHomeAdapter = new FixProblemHomeAdapter(response.body().getSuccess().getData().getCategories(), getActivity());
                        fixProblemRecycler.setAdapter(problemHomeAdapter);

                        purchaseHomeAdapter = new PurchaseHomeAdapter(response.body().getSuccess().getData().getNewpurchases(), getActivity());
                        purchaseRecycler.setAdapter(purchaseHomeAdapter);
                        officeSetupHomeAdapter = new OfficeSetupHomeAdapter(response.body().getSuccess().getData().getBid(), getActivity());
                        officeSetupRecycler.setAdapter(officeSetupHomeAdapter);
                        maintenanceHomeAdapter = new MaintenanceHomeAdapter(response.body().getSuccess().getData().getAmc(), getActivity());
                        maintenanceRecycler.setAdapter(maintenanceHomeAdapter);

                        rentHomeAdapter = new RentHomeAdapter(response.body().getSuccess().getData().getRent(), getActivity());
                        rentRecycler.setAdapter(rentHomeAdapter);
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");

                        showToast(context, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                close();
                showToast(getActivity(), "Process Failed");
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == showAllFixProblem) {
            row_index = 0;
            UserHomeScreen.viewPager.arrowScroll(View.FOCUS_RIGHT);
        }
        if (v == showAllPurchase) {
            row_index = 1;
            UserHomeScreen.viewPager.arrowScroll(View.FOCUS_RIGHT);

        }
        if (v == showAllOfficeSetUp) {
            row_index = 2;
            UserHomeScreen.viewPager.arrowScroll(View.FOCUS_RIGHT);

        }
        if (v == showAllMaintenance) {
            row_index = 3;
            UserHomeScreen.viewPager.arrowScroll(View.FOCUS_RIGHT);

        }
        if (v == showAllRent) {
            row_index = 4;
            UserHomeScreen.viewPager.arrowScroll(View.FOCUS_RIGHT);

        }

            /*        if (position == 0) {
                categoryFixProblem = new CategoryFixProblem(fixproblem_text, context);
                recyclerView.setAdapter(categoryFixProblem);
            }
            if (position == 1) {
                categoryNewPurchase = new CategoryNewPurchase(purchase_text, context);
                recyclerView.setAdapter(categoryNewPurchase);
            }
            if (position == 2) {
                categoryOfficeSetup = new CategoryOfficeSetup(office_setup_text, context);
                recyclerView.setAdapter(categoryOfficeSetup);

            }
            if (position == 3) {
                categoryMaintainance = new CategoryMaintainance(maintenance_text, context);
                recyclerView.setAdapter(categoryMaintainance);

            }
            if (position == 4) {
                categoryRent = new CategoryRent(rent_text, context);
                recyclerView.setAdapter(categoryRent);
            }*/


    }
}
