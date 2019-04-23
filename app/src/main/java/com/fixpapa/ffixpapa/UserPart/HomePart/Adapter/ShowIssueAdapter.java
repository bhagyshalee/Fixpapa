package com.fixpapa.ffixpapa.UserPart.HomePart.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Problem;


import java.util.ArrayList;
import java.util.List;


public class ShowIssueAdapter extends RecyclerView.Adapter<ShowIssueAdapter.ViewHolder> {
    private Context context;
    List<Problem> services;
   public static ArrayList<String> listIssue;
   public static ArrayList<Integer> listIssuePrice;
   public static ArrayList<String> listIssueid;
   public static ArrayList<String> listIssuecategoryId;
   public static ArrayList<String> listIssueprobContent;
   public static ArrayList<String> listIssueupdatedAt;
   public static ArrayList<String> listIssuecreatedAt;
   int pos=0;


    public ShowIssueAdapter(List<Problem> services, Context context) {
        super();
        this.services = services;
        this.context = context;
        listIssue = new ArrayList<>();
        listIssuePrice = new ArrayList<>();
        listIssueid = new ArrayList<>();
        listIssuecategoryId = new ArrayList<>();
        listIssueprobContent = new ArrayList<>();
        listIssueupdatedAt = new ArrayList<>();
        listIssuecreatedAt = new ArrayList<>();

    }

    @Override
    public ShowIssueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_issuesshow, parent, false);
        ShowIssueAdapter.ViewHolder viewHolder = new ShowIssueAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ShowIssueAdapter.ViewHolder holder, final int position) {
        final Problem category = services.get(position);
        holder.checkboxIssues.setText(category.getProbContent());
        holder.priceIssues.setText(category.getPrice() + "/-");


        holder.checkboxIssues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listIssue.add(holder.checkboxIssues.getText().toString());
                    listIssuePrice.add(category.getPrice());
                    listIssueid.add(category.getId());
                    listIssuecategoryId.add(category.getCategoryId());
                    listIssueprobContent.add(category.getProbContent());
                    listIssueupdatedAt.add(category.getUpdatedAt());
                    listIssuecreatedAt.add(category.getCreatedAt());
                    Log.e("vgerbrereregh",""+listIssue+" "+listIssuePrice+" "+listIssueid+" "+listIssuecategoryId
                            +" "+listIssueprobContent+" "+listIssueupdatedAt+" "+listIssuecreatedAt);

                  /*  JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("id",category.getId());
                        jsonObject.put("categoryId",category.getCategoryId());
                        jsonObject.put("probContent",category.getProbContent());
                        jsonObject.put("price",category.getPrice());
                        jsonObject.put("createdAt",category.getCreatedAt());
                        jsonObject.put("updatedAt",category.getCreatedAt());
                        issuesListArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    if (pos > position) {
                        listIssue.remove(position);
                        listIssuePrice.remove(position);
                        listIssueid.remove(position);
                        listIssuecategoryId.remove(position);
                        listIssueprobContent.remove(position);
                        listIssueupdatedAt.remove(position);
                        listIssuecreatedAt.remove(position);
                        Log.e("vgerbrereregh",""+listIssue+" "+listIssuePrice+" "+listIssueid+" "+listIssuecategoryId
                                +" "+listIssueprobContent+" "+listIssueupdatedAt+" "+listIssuecreatedAt);

                        //issuesListArray.remove(position);
                    }
                    else
                    {
                        listIssue.remove(holder.checkboxIssues.getText().toString());
                        listIssuePrice.remove(category.getPrice());
                        listIssueid.remove(category.getId());
                        listIssuecategoryId.remove(category.getCategoryId());
                        listIssueprobContent.remove(category.getProbContent());
                        listIssueupdatedAt.remove(category.getUpdatedAt());
                        listIssuecreatedAt.remove(category.getCreatedAt());
                        Log.e("vgerbrereregh",""+listIssue+" "+listIssuePrice+" "+listIssueid+" "+listIssuecategoryId
                                +" "+listIssueprobContent+" "+listIssueupdatedAt+" "+listIssuecreatedAt);

                        //issuesListArray.remove(position);
                        pos=pos+1;

                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxIssues;
        TextView priceIssues;

        public ViewHolder(View itemView) {
            super(itemView);
            checkboxIssues = (CheckBox) itemView.findViewById(R.id.checkboxIssues);
            priceIssues = (TextView) itemView.findViewById(R.id.priceIssues);
        }
    }
}
