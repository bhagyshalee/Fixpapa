package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Category;

import java.util.List;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.ViewHolder> {
    private Context context;
    RecyclerView mainRecycler;
    List<String> mainList = null;
    List<Category> services;
    int i = 0;

    public CategoryMainAdapter(List<Category> services,List<String> mainList, Context context) {
        super();
        this.context = context;
        this.mainList = mainList;

        Log.e("sfvfdvddsv", "" + mainList);

    }

    @Override
    public CategoryMainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_category, parent, false);
        CategoryMainAdapter.ViewHolder viewHolder = new CategoryMainAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("sfvfdvd", "" + mainList.get(position));

        holder.categoryTitle.setText(mainList.get(position));
    }

    @Override
    public int getItemCount() {

        return mainList.size();
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