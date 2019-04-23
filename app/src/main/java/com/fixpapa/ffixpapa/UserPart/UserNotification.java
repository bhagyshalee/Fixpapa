package com.fixpapa.ffixpapa.UserPart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.NotificationAdapter;
import com.fixpapa.ffixpapa.UserPart.Model.NotificationModel.SuccessNoti;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UserNotification extends AppCompatActivity implements View.OnClickListener {

    RecyclerView notificationRecycler;
    ImageView notificationCross;
    NotificationAdapter notificationAdapter;
    LinearLayout emptyLayout;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        getUserDetailId(UserNotification.this);
        notificationCross = (ImageView) findViewById(R.id.notificationCross);
        emptyLayout = (LinearLayout) findViewById(R.id.emptyLayout);
        notificationRecycler = (RecyclerView) findViewById(R.id.notificationRecycler);
        notificationCross.setOnClickListener(this);

        notificationRecycler.addItemDecoration(new DividerItemDecoration(UserNotification.this, DividerItemDecoration.VERTICAL));
        notificationRecycler.setLayoutManager(new LinearLayoutManager(UserNotification.this));
        if (isOnline(true, UserNotification.this)) {
            getNotificationAll();
        }
    }

    private void getNotificationAll() {
        showProgress(UserNotification.this);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
      String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessNoti> call = apiInterface.getAllNotification(ACCESS_TOKEN);
        call.enqueue(new Callback<SuccessNoti>() {
            @Override
            public void onResponse(Call<SuccessNoti> call, Response<SuccessNoti> response) {
                close();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().getData().size() == 0) {
                        emptyLayout.setVisibility(View.VISIBLE);
                    } else {
                        if (i==0){
                            i++;
                            getNotificationAll();
                        }
                        notificationAdapter = new NotificationAdapter(response.body().getSuccess().getData(), UserNotification.this);
                        notificationRecycler.setAdapter(notificationAdapter);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UserNotification.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessNoti> call, Throwable t) {
                close();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == notificationCross) {
            finish();
        }
    }
}
