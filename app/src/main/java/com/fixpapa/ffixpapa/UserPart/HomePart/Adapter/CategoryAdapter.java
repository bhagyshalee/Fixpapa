package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Category;

import java.util.List;

import static com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment.fixproblem_text;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment.maintenance_text;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment.office_setup_text;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment.purchase_text;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Fragments.CategoryFragment.rent_text;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    List<Category> services;
    CategoryFixProblem categoryFixProblem;
    CategoryNewPurchase categoryNewPurchase;
    CategoryOfficeSetup categoryOfficeSetup;
    CategoryMaintainance categoryMaintainance;
    CategoryRent categoryRent;
    RecyclerView recyclerView;
    List<String> mainList = null;
    int i = 0;
    private static int SPLASH_TIME_OUT = 1000;
    TextView fixProblemText,purchaseText,officeText,maintanceText,rentText;


    public CategoryAdapter(List<Category> services, RecyclerView recyclerView,Context context,TextView fixProblemText,TextView purchaseText,
                           TextView officeText,TextView maintanceText,TextView rentText) {
        super();
        this.services = services;
        this.context = context;
        this.recyclerView = recyclerView;
        this.fixProblemText = fixProblemText;
        this.purchaseText = purchaseText;
        this.officeText = officeText;
        this.maintanceText = maintanceText;
        this.rentText = rentText;

    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_category, parent, false);
        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                categoryFixProblem = new CategoryFixProblem(fixproblem_text, context);
                recyclerView.setAdapter(categoryFixProblem);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.white));
            }

    }, SPLASH_TIME_OUT);


        fixProblemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryFixProblem = new CategoryFixProblem(fixproblem_text, context);
                recyclerView.setAdapter(categoryFixProblem);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.white));
                purchaseText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                purchaseText.setTextColor(context.getResources().getColor(R.color.gray));
                officeText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                officeText.setTextColor(context.getResources().getColor(R.color.gray));
                maintanceText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                maintanceText.setTextColor(context.getResources().getColor(R.color.gray));
                rentText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                rentText.setTextColor(context.getResources().getColor(R.color.gray));

            }
        });

        purchaseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryNewPurchase = new CategoryNewPurchase(purchase_text, context);
                recyclerView.setAdapter(categoryNewPurchase);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.gray));
                purchaseText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                purchaseText.setTextColor(context.getResources().getColor(R.color.white));
                officeText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                officeText.setTextColor(context.getResources().getColor(R.color.gray));
                maintanceText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                maintanceText.setTextColor(context.getResources().getColor(R.color.gray));
                rentText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                rentText.setTextColor(context.getResources().getColor(R.color.gray));

            }
        });
        officeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryOfficeSetup = new CategoryOfficeSetup(office_setup_text, context);
                recyclerView.setAdapter(categoryOfficeSetup);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.gray));
                purchaseText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                purchaseText.setTextColor(context.getResources().getColor(R.color.gray));
                officeText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                officeText.setTextColor(context.getResources().getColor(R.color.white));
                maintanceText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                maintanceText.setTextColor(context.getResources().getColor(R.color.gray));
                rentText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                rentText.setTextColor(context.getResources().getColor(R.color.gray));

            }
        });

        maintanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryMaintainance = new CategoryMaintainance(maintenance_text, context);
                recyclerView.setAdapter(categoryMaintainance);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.gray));
                purchaseText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                purchaseText.setTextColor(context.getResources().getColor(R.color.gray));
                officeText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                officeText.setTextColor(context.getResources().getColor(R.color.gray));
                maintanceText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                maintanceText.setTextColor(context.getResources().getColor(R.color.white));
                rentText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                rentText.setTextColor(context.getResources().getColor(R.color.gray));

            }
        });
        rentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryRent = new CategoryRent(rent_text, context);
                recyclerView.setAdapter(categoryRent);
                fixProblemText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                fixProblemText.setTextColor(context.getResources().getColor(R.color.gray));
                purchaseText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                purchaseText.setTextColor(context.getResources().getColor(R.color.gray));
                officeText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                officeText.setTextColor(context.getResources().getColor(R.color.gray));
                maintanceText.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                maintanceText.setTextColor(context.getResources().getColor(R.color.gray));
                rentText.setBackgroundColor(context.getResources().getColor(R.color.gray));
                rentText.setTextColor(context.getResources().getColor(R.color.white));

            }
        });






      /*  if (position < 5) {
            i++;
            holder.categoryTitle.setText(mainList.get(position));
        }


        if (row_index == position) {
            Log.e("mnvcvnnvm", position + "");
            holder.categoryTitle.setBackgroundColor(context.getResources().getColor(R.color.mediumGray));
            holder.categoryTitle.setTextColor(Color.parseColor("#ffffff"));
            if (position == 0) {
                categoryFixProblem = new CategoryFixProblem(fixproblem_text, context);
                recyclerView.setAdapter(categoryFixProblem);
            }

        } else {
            holder.categoryTitle.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
            holder.categoryTitle.setTextColor(context.getResources().getColor(R.color.gray));

        }


        holder.categoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
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
                }
                row_index = position;
                notifyDataSetChanged();


            }
        });
*/

    }

    @Override
    public int getItemCount() {
       /* if (i==5) {
            return mainList.size();
        } else {*/
            return services.size();
        //}
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        LinearLayout layoutRec;


        public ViewHolder(View itemView) {
            super(itemView);

            categoryTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            layoutRec = (LinearLayout) itemView.findViewById(R.id.layoutRec);
        }
    }
}