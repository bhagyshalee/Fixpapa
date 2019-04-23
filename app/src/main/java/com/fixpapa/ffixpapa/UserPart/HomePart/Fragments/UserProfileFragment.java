package com.fixpapa.ffixpapa.UserPart.HomePart.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.fixpapa.ffixpapa.EngineerPart.EngineerInfo;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.AccountSetting;
import com.fixpapa.ffixpapa.UserPart.HomePart.ManageAddress;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.UserLogin;
import com.fixpapa.ffixpapa.UserPart.UserNotification;
import com.fixpapa.ffixpapa.UserPart.UserProfile;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isLogin;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UserProfileFragment extends Fragment implements View.OnClickListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    String ACCESS_TOKEN,LOGIN_TYPE,USER_ID,USER_NAME;
    Button loginButton;
    TextView accountHolderName;
    int loginStatus;
    LinearLayout myInfoLayout, accLayout, notification, termAndCondition, aboutUsLayout, shareAppLayout, manageAddressLayout, helpLayout;
    DataBaseHandler dataBaseHandler;
    String address;
    int experience;
    Set<String> getExpertiesId;
    List<String> getExpertiesIdA;
    private GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View v = inflater.inflate(R.layout.fragment_profile_user, container, false);
        dataBaseHandler=new DataBaseHandler(getActivity());
//        getUserDetailId(getActivity());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("677218008910-31lbla4iqcnaap6fn8evg34qajnkt821.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();



        getExpertiesId=new HashSet<String>();
        getExpertiesIdA=new ArrayList<>();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("storeEngineerData",MODE_PRIVATE);
        address=sharedPreferences.getString("AddressString",null);
        experience=sharedPreferences.getInt("EngineerExperience",0);
        getExpertiesId=sharedPreferences.getStringSet("expertID",null);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");
        USER_ID = sharedPref.getString("USER_ID","");
        USER_NAME = sharedPref.getString("USER_NAME","");



        if (getExpertiesId!=null) {

            Iterator it = getExpertiesId.iterator();

            while (it.hasNext()) {
                getExpertiesIdA.add(""+it.next());
            }
        }

        loginButton = (Button) v.findViewById(R.id.loginButton);
        accountHolderName = (TextView) v.findViewById(R.id.accountHolderName);
        myInfoLayout = (LinearLayout) v.findViewById(R.id.myInfoLayout);
        accLayout = (LinearLayout) v.findViewById(R.id.accLayout);
        notification = (LinearLayout) v.findViewById(R.id.notification);
        termAndCondition = (LinearLayout) v.findViewById(R.id.termAndCondition);
        aboutUsLayout = (LinearLayout) v.findViewById(R.id.aboutUsLayout);
        shareAppLayout = (LinearLayout) v.findViewById(R.id.shareAppLayout);
        helpLayout = (LinearLayout) v.findViewById(R.id.helpLayout);
        manageAddressLayout = (LinearLayout) v.findViewById(R.id.manageAddressLayout);
        loginButton.setOnClickListener(this);
        myInfoLayout.setOnClickListener(this);
        accLayout.setOnClickListener(this);
        manageAddressLayout.setOnClickListener(this);
        notification.setOnClickListener(this);
        termAndCondition.setOnClickListener(this);
        aboutUsLayout.setOnClickListener(this);
        shareAppLayout.setOnClickListener(this);
        helpLayout.setOnClickListener(this);

        loginStatus = isLogin(getActivity());
        if (loginStatus != 0) {


            if (LOGIN_TYPE.equals("vendor"))
            {
                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.GONE);
                accLayout.setVisibility(View.GONE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.GONE);
            }
            else if (LOGIN_TYPE.equals("customer"))
            {
                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.VISIBLE);
                accLayout.setVisibility(View.VISIBLE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.VISIBLE);
            }
            else if (LOGIN_TYPE.equals("engineer")) {

                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.VISIBLE);
                accLayout.setVisibility(View.GONE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.GONE);
                termAndCondition.setVisibility(View.GONE);
                aboutUsLayout.setVisibility(View.GONE);
                shareAppLayout.setVisibility(View.GONE);
            }
        } else {
            loginButton.setText(getText(R.string.login_text));
            accountHolderName.setVisibility(View.GONE);
            myInfoLayout.setVisibility(View.GONE);
            accLayout.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            manageAddressLayout.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v==helpLayout)
        {
            Intent intent=new Intent(getActivity(),HelpContent.class);
            startActivity(intent);
        }

        if (v==shareAppLayout)
        {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.fixpapa.ffixpapa\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }

        if (v==aboutUsLayout)
        {
            Intent intent=new Intent(getActivity(), AboutUs.class);
            startActivity(intent);
        }
        if (v==termAndCondition)
        {
            Intent intent=new Intent(getActivity(), TermACondition.class);
            startActivity(intent);
        }

        if (v==notification)
        {
            Intent intent=new Intent(getActivity(), UserNotification.class);
            startActivity(intent);
        }
        if (v == manageAddressLayout) {
            Intent intent = new Intent(getActivity(), ManageAddress.class);
            startActivity(intent);
        }
        if (v == accLayout) {
            Intent intent = new Intent(getActivity(), AccountSetting.class);
            startActivity(intent);
        }
        if (v == loginButton) {

            //SharedPreferences shared = getActivity().getSharedPreferences("logintype", MODE_PRIVATE);
            //String channel = shared.getString("loginType", "");
            SharedPreferences sharedD = getActivity().getSharedPreferences("loginSoci", MODE_PRIVATE);
            String channel = sharedD.getString("providerI", "");

            //Log.e("fdvbfdb",""+channel);
            if (loginButton.getText().toString().equals(getText(R.string.login_text))) {
                Intent intent = new Intent(getActivity(), UserLogin.class);
                startActivity(intent);
            } else {
                Log.e("vsdffdvfd",""+channel);
                if (channel.equals("facebook"))
                {
                    dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    sharedD.edit().clear().apply();
                    logoutCall();

                }
                else if (channel.equals("google"))
                {

                    dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);

                    sharedD.edit().clear().apply();
                    signOut();

                    logoutCall();
                    Log.e("vsdffdvfdfg",""+channel);
                }
                else {
                    Log.e("vsdffdvfdbfg",""+channel);
                    dataBaseHandler.resetTables(DataBaseHandler.TABLE_USER);
                    logoutCall();
                }

            }
        }
        if (v == myInfoLayout) {
               if (LOGIN_TYPE.equals("engineer")) {
                   Intent intent1 = new Intent(getActivity(), EngineerInfo.class);
                   Bundle bundle1 = new Bundle();
                   bundle1.putString("engineerId", USER_ID);
                   bundle1.putBoolean("showUpdation", true);
                   intent1.putExtras(bundle1);
                   startActivity(intent1);
               }
               else if (LOGIN_TYPE.equals("customer")) {
                   Intent intent = new Intent(getActivity(), UserProfile.class);
                   startActivity(intent);
               }

        }
    }

    private void signOut() {
//        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            //  Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                          /*  Intent intent = new Intent(getActivity(), UserHomeScreen.class);
                            startActivity(intent);*/
                        }
                    });
        }
    }

    private void logoutCall() {
        showProgress(getActivity());
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModelVendor> call=apiInterface.logoutAllServices(ACCESS_TOKEN);
        call.enqueue(new Callback<SuccessModelVendor>() {
            @Override
            public void onResponse(Call<SuccessModelVendor> call, Response<SuccessModelVendor> response) {
                close();
                if (response.isSuccessful())
                {
                    SharedPreferences.Editor editorValues = getActivity().getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
                    editorValues.putBoolean("LOGIN_STATUS",false);
                    editorValues.putString("LOGIN_TYPE","");
                    editorValues.apply();
                    editorValues.commit();
                    Intent intent=new Intent(getActivity(), UserHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
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
            public void onFailure(Call<SuccessModelVendor> call, Throwable t) {
                close();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE","");
        USER_ID = sharedPref.getString("USER_ID","");
        USER_NAME = sharedPref.getString("USER_NAME","");

        loginStatus = isLogin(getActivity());
        if (loginStatus != 0) {


            if (LOGIN_TYPE.equals("vendor"))
            {
                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.GONE);
                accLayout.setVisibility(View.GONE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.GONE);
            }
            else if (LOGIN_TYPE.equals("customer"))
            {
                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.VISIBLE);
                accLayout.setVisibility(View.VISIBLE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.VISIBLE);
            }
            else if (LOGIN_TYPE.equals("engineer")) {

                loginButton.setText(getText(R.string.logout_text));
                accountHolderName.setVisibility(View.VISIBLE);
                accountHolderName.setText(USER_NAME+"");
                myInfoLayout.setVisibility(View.VISIBLE);
                accLayout.setVisibility(View.GONE);
                notification.setVisibility(View.VISIBLE);
                manageAddressLayout.setVisibility(View.GONE);
                termAndCondition.setVisibility(View.GONE);
                aboutUsLayout.setVisibility(View.GONE);
                shareAppLayout.setVisibility(View.GONE);
            }
        } else {
            loginButton.setText(getText(R.string.login_text));
            accountHolderName.setVisibility(View.GONE);
            myInfoLayout.setVisibility(View.GONE);
            accLayout.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            manageAddressLayout.setVisibility(View.GONE);
        }
    }
}
