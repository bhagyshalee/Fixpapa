package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.CategoryAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.CategoryFixProblem;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Amc;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Bid;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Category;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Newpurchase;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Rent;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Success;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;


public class CategoryFragment extends Fragment {
    CategoryAdapter categoryAdapter;
    CategoryFixProblem categorySubAdapter;

    //private List<String> mainCat;
    //private List<String> subCat;
    RecyclerView  subCategory;
    public static List<Category> fixproblem_text;
    public static List<Newpurchase> purchase_text;
    public static List<Bid> office_setup_text;
    public static List<Amc> maintenance_text;
    public static List<Rent> rent_text;
    TextView fixProblemText,purchaseText,officeText,maintanceText,rentText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        subCategory = (RecyclerView) v.findViewById(R.id.subCategory);
        fixProblemText = (TextView) v.findViewById(R.id.fixProblemText);
        purchaseText = (TextView) v.findViewById(R.id.purchaseText);
        officeText = (TextView) v.findViewById(R.id.officeText);
        maintanceText = (TextView) v.findViewById(R.id.maintanceText);
        rentText = (TextView) v.findViewById(R.id.rentText);
        subCategory.setHasFixedSize(true);
        subCategory.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManagerr
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        subCategory.setLayoutManager(layoutManagerr);
        fixproblem_text = new ArrayList<>();
        purchase_text = new ArrayList<>();
        office_setup_text = new ArrayList<>();
        maintenance_text = new ArrayList<>();
        rent_text = new ArrayList<>();

        callHomePage();
        return v;
    }

    private void callHomePage() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Success> modelCall = apiInterface.userService();
        modelCall.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getSuccess().getData().getCategories().size(); i++) {
                        fixproblem_text = response.body().getSuccess().getData().getCategories();
                    }
                    for (int i = 0; i < response.body().getSuccess().getData().getNewpurchases().size(); i++) {
                        purchase_text = response.body().getSuccess().getData().getNewpurchases();
                    }
                    for (int i = 0; i < response.body().getSuccess().getData().getBid().size(); i++) {
                        office_setup_text = response.body().getSuccess().getData().getBid();
                    }
                    for (int i = 0; i < response.body().getSuccess().getData().getAmc().size(); i++) {
                        maintenance_text = response.body().getSuccess().getData().getAmc();
                    }
                    for (int i = 0; i < response.body().getSuccess().getData().getRent().size(); i++) {
                        rent_text = response.body().getSuccess().getData().getRent();
                    }
                    for (int i = 0; i < response.body().getSuccess().getData().getCategories().size(); i++) {
                        categoryAdapter = new CategoryAdapter(response.body().getSuccess().getData().getCategories(), subCategory, getActivity(),
                                fixProblemText,purchaseText,officeText,maintanceText,rentText);
                        subCategory.setAdapter(categoryAdapter);

                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(getActivity(), "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                showToast(getActivity(), "Process Failed");
            }
        });
    }

/*
    private List<String> allMainCat() {
        List<String> data = new ArrayList<>();

        data.add(String.valueOf(getText(R.string.fixproblem_text)));
        data.add(String.valueOf(getText(R.string.purchase_text)));
        data.add(String.valueOf(getText(R.string.office_setup_text)));
        data.add(String.valueOf(getText(R.string.maintenance_text)));
        data.add(String.valueOf(getText(R.string.rent_text)));

        return data;
    }
*/


}
