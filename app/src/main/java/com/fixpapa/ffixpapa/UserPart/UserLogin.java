package com.fixpapa.ffixpapa.UserPart;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.Login;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.UserPart.HomePart.UserHomeScreen;
import com.fixpapa.ffixpapa.UserPart.Model.FbloginModel.SuccessFb;
import com.fixpapa.ffixpapa.UserPart.Model.UserLoginData.SucessLogin;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Config.GCM_TOKEN;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UserLogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button RegisterButton, loginButton, tabCustomer, tabPartner, tabEngineer;
    ImageView fbButton, googleButton;
    LoginButton login_button;
    //CheckBox checkBoxPartner;
    EditText emailMobileText, passwordText;
    TextView forgotPassword;
    String serviceType = "customer";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    String userGoogleName, userGoogleEmail, userGoogleImage;
    GoogleSignInOptions gso;
    DataBaseHandler baseHandler;
    TextView skipText;
    String userId, userName, userEmail="", userType, userMobile="", userGstNo, userAddress, userStreet,
            userAccessToken, userImage, getLoginType, fbName, fbGmail, fbGender, fbId, fbBirthday,
            googleAccessToken, fbAccessToken;
    double userLatitude, userLongitude;
    boolean mobileVerify, emailVerify;
    String emailMobileStatus = "";
    AccessToken accessToken;
    private CallbackManager callbackManager;
    public static final String SCOPES = "https://www.googleapis.com/auth/plus.login "
            + "https://www.googleapis.com/auth/drive.file";
    Task<GoogleSignInAccount> task;
    Intent signInIntent;
    FirebaseAuth mAuth;
    String channel;
  //  private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences.Editor editor;
    private Boolean mAllowNavigation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(UserLogin.this);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        //AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_user_login);
        editor = getSharedPreferences("logintype", MODE_PRIVATE).edit();

        SharedPreferences shared = getSharedPreferences("REG_TOKEN", MODE_PRIVATE);
        channel = shared.getString(getResources().getString(R.string.firebase_token), "");
        Log.e("djdnkl", "" + channel);
        RegisterButton = (Button) findViewById(R.id.RegisterButton);

        tabEngineer = (Button) findViewById(R.id.tabEngineer);
        tabCustomer = (Button) findViewById(R.id.tabCustomer);
        tabPartner = (Button) findViewById(R.id.tabPartner);

        loginButton = (Button) findViewById(R.id.loginButton);
        login_button = (LoginButton) findViewById(R.id.login_button);

        fbButton = (ImageView) findViewById(R.id.fbButton);
        googleButton = (ImageView) findViewById(R.id.googleButton);
        emailMobileText = (EditText) findViewById(R.id.emailMobileText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        skipText = (TextView) findViewById(R.id.skipText);
        RegisterButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        login_button.setReadPermissions("email", "public_profile", "user_friends");

        tabEngineer.setOnClickListener(this);
        tabPartner.setOnClickListener(this);
        tabCustomer.setOnClickListener(this);
        //login_button.setReadPermissions("email", "public_profile");


//        if (AccessToken.getCurrentAccessToken() != null) {
//            LoginManager.getInstance().logOut();
//        }

//        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                // currentUser.
//                if (user!=null)
//                {
// mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users")
//                            .child(mCurrentUser.getUid());
//
//                }else
//                {
//                    Log.e("fgbifgbtrdvs", ""+channel);
//                }
//            }
//        });

        baseHandler = new DataBaseHandler(UserLogin.this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("677218008910-31lbla4iqcnaap6fn8evg34qajnkt821.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        googleButton.setOnClickListener(this);
        fbButton.setOnClickListener(this);
        skipText.setOnClickListener(this);
        tabCustomer.setBackgroundColor(getResources().getColor(R.color.skyBlueColor));

        login_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("onSuccess");

                        //login_button.setLoginBehavior(LoginBehavior.WEB_ONLY);
                        Log.e("njkfnjkndfvf", "" + loginResult.getRecentlyGrantedPermissions()
                        +" "+loginResult.getRecentlyDeniedPermissions());
                        fbAccessToken = loginResult.getAccessToken().getToken();

                       /* if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                            mAuth.signOut();
                        }*/
                        if (channel != null) {
                            if (fbAccessToken == null) {
                                Log.e("njkfnjknvf", "" + channel);

                            } else {
                                fbLoginSendInfo(channel, fbAccessToken);
                            }
                        }

                    }

                    @Override
                    public void onCancel() {

                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        System.out.println("onError");
                        Log.e("fgbifgbtr", "ernf" + emailMobileText);
                       // FirebaseAuth.getInstance().signOut();
                      //  LoginManager.getInstance().logOut();
                    }
                });

    }


    public void logOut() {
        AccessToken.setCurrentAccessToken((AccessToken)null);
        Profile.setCurrentProfile((Profile)null);
    }

    private void fbLoginSendInfo(String firebasetoken, String fbtoken) {
        showProgress(UserLogin.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessFb> call = apiInterface.facebookLogin(firebasetoken, "customer", fbtoken);
        call.enqueue(new Callback<SuccessFb>() {
            @Override
            public void onResponse(Call<SuccessFb> call, Response<SuccessFb> response) {
                close();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = getSharedPreferences("loginSoci", MODE_PRIVATE).edit();
                    editor.putString("providerI", response.body().getSuccess().getData().getUser().getProvider());
                    editor.apply();
                    editor.commit();
                    if (!response.body().getSuccess().getData().getUser().getIsProfileComplete()) {

                        Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                        intent.putExtra("email", response.body().getSuccess().getData().getUser().getEmail());
                        intent.putExtra("image", response.body().getSuccess().getData().getUser().getImage());
                        intent.putExtra("name", response.body().getSuccess().getData().getUser().getFullName());
                        intent.putExtra("userId", response.body().getSuccess().getData().getUser().getId());
                        intent.putExtra("profileComplete", response.body().getSuccess().getData().getUser().getIsProfileComplete());
                        intent.putExtra("loginFB", true);
                        startActivity(intent);
                    } else {

                        userId = response.body().getSuccess().getData().getUserId();
                        userAccessToken = response.body().getSuccess().getData().getAccessToken();
                        userEmail = response.body().getSuccess().getData().getUser().getEmail();
                        userName = response.body().getSuccess().getData().getUser().getFullName();
                        userMobile = response.body().getSuccess().getData().getUser().getMobile();
                        userType = "customer";
                        userGstNo = response.body().getSuccess().getData().getUser().getGstNumber();
                        mobileVerify = response.body().getSuccess().getData().getUser().getMobileVerified();
                        emailVerify = response.body().getSuccess().getData().getUser().getEmailVerified();
                        if (response.body().getSuccess().getData().getUser().getAddresses().size() != 0) {
                            userAddress = response.body().getSuccess().getData().getUser().getAddresses().get(0).getValue();
                            userStreet = response.body().getSuccess().getData().getUser().getAddresses().get(0).getStreet();
                            userLatitude = response.body().getSuccess().getData().getUser().getAddresses().get(0).getLocation().getLat();
                            userLongitude = response.body().getSuccess().getData().getUser().getAddresses().get(0).getLocation().getLng();
                        }
                        userImage = response.body().getSuccess().getData().getUser().getImage();
                        getLoginType = response.body().getSuccess().getData().getUser().getRealm();

                        if (mobileVerify && emailVerify)
                        {

                            baseHandler.resetTables(DataBaseHandler.TABLE_USER);
                            baseHandler.addUser(userAccessToken, userId, userEmail, userName, userMobile, userGstNo, userAddress, userStreet, userLatitude, userLongitude, userType, userImage, getLoginType);


                            SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
                            editorValues.putString("ACCESS_TOKEN", userAccessToken);
                            editorValues.putString("USER_ID", userId);
                            editorValues.putString("USER_EMAIL", userEmail);
                            editorValues.putString("USER_NAME", userName);
                            editorValues.putString("USER_MOBILE_NO", userMobile);
                            editorValues.putString("USER_GST", userGstNo);
                            editorValues.putString("USER_ADDRESS", userAddress);
                            editorValues.putString("USER_STREET", userStreet);
                            editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
                            editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
                            editorValues.putString("USER_TYPE", userType);
                            editorValues.putString("USER_IMAGE", userImage);
                            editorValues.putString("LOGIN_TYPE", getLoginType);
                            editorValues.putBoolean("LOGIN_STATUS",true);
                            editorValues.apply();
                            editorValues.commit();

                            Intent intent = new Intent(UserLogin.this, UserHomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {

                            LoginManager.getInstance().logOut();
                            AccessToken.setCurrentAccessToken(null);
                            mAuth.signOut();

                            if (!mobileVerify)
                            {
                                showToast(UserLogin.this,"Please verify your mobile number");
                                Intent intent = new Intent(UserLogin.this, OtpVarification.class);
                                intent.putExtra("userOtp", "");
                                intent.putExtra("userId", response.body().getSuccess().getData().getUser().getId());
                                startActivity(intent);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            }else if (!emailVerify){
                               /* Intent intent = new Intent(UserLogin.this, UserLogin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);*/
                                //finish();

                                try {
                                    final Dialog dialog = new Dialog(UserLogin.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                                    dialog.setContentView(R.layout.show_msg_email);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(false);
                                    final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                                    //final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                                    dialog.show();
                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                //showToast(UserLogin.this,"Please check your mail for verification");

                            }

                        }
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UserLogin.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessFb> call, Throwable t) {
                close();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == tabPartner) {
            tabCustomer.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            tabEngineer.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            tabPartner.setBackgroundColor(getResources().getColor(R.color.skyBlueColor));
            serviceType = "vendor";
            fbButton.setVisibility(View.GONE);
            googleButton.setVisibility(View.GONE);
            RegisterButton.setVisibility(View.GONE);

        }
        if (v == tabEngineer) {
            tabCustomer.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            tabEngineer.setBackgroundColor(getResources().getColor(R.color.skyBlueColor));
            tabPartner.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            serviceType = "engineer";
            fbButton.setVisibility(View.GONE);
            googleButton.setVisibility(View.GONE);
            RegisterButton.setVisibility(View.GONE);
        }
        if (v == tabCustomer) {
            tabCustomer.setBackgroundColor(getResources().getColor(R.color.skyBlueColor));
            tabEngineer.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            tabPartner.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
            serviceType = "customer";
            fbButton.setVisibility(View.VISIBLE);
            googleButton.setVisibility(View.VISIBLE);
            RegisterButton.setVisibility(View.VISIBLE);
        }
        if (v == skipText) {
            Intent intent = new Intent(UserLogin.this, UserHomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        if (v == RegisterButton) {
            Intent intent = new Intent(UserLogin.this, UserRegistration.class);
            startActivity(intent);
        }

        if (v == loginButton) {
            String s = emailMobileText.getText().toString().trim();
            if (emailMobileText.getText().toString().trim().isEmpty()) {
                emailMobileText.setError(getText(R.string.email_mobile_error));
                emailMobileText.requestFocus();
            } else if (passwordText.getText().toString().trim().isEmpty()) {
                passwordText.setError(getText(R.string.password_error));
                passwordText.requestFocus();
            } else if (isOnline(true, UserLogin.this)) {

                if (!isValidEmail(s)) {
                    emailMobileStatus = "mobile";
                    userLogin();
                } else {
                    emailMobileStatus = "email";
                    userLogin();

                }
            }
        }
        if (v == forgotPassword) {
            Intent intent = new Intent(UserLogin.this, ForgotPassword.class);
            startActivity(intent);
            //finish();
        }
        if (v == fbButton) {
            //login_button.setLoginBehavior(LoginBehavior.WEB_ONLY);
            login_button.performClick();
            /*if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
                Log.e("fdhryhreyeryer", "dfbfds");

            }
*/
        }
        if (v == googleButton) {

            signIn();

        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void signIn() {

        signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            Log.e("restart",user.getDisplayName()+" "+user.getEmail()+" "+user.getToken(true));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("fdjkvbjkfd", data + "");
        if (requestCode == RC_SIGN_IN) {
            Log.e("fdjeesdkvbjkfd", data + "");
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    String idToken = account.getIdToken();
                    String name = account.getDisplayName();
                    Log.d("name google", name);
                    Log.d("tokendsvsd", idToken);
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithGoogle(credential, idToken);
                }
                else
                {
                    Log.e("fdjeesdkvbjkfdscasac", data + "");
                    GoogleSignInAccount account = result.getSignInAccount();
                    String idToken = account.getIdToken();
                    String name = account.getDisplayName();
                    Log.d("name google", name);
                    Log.d("tokendsvsd", idToken);
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithGoogle(credential, idToken);
                }
            }

        }
    }

    private void firebaseAuthWithGoogle(AuthCredential acct, String accesstoken) {
        Log.e("ngngnfg", "onAuth");


      /*  SharedPreferences shared = getSharedPreferences("REG_TOKEN", MODE_PRIVATE);
        String channel = shared.getString(getResources().getString(R.string.firebase_token), "");*/
        if (channel != null) {
            sendOtherData(channel, accesstoken);
        }


        mAuth.signInWithCredential(acct)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("gnfg", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(UserLogin.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("fnfgn", "signInWithCredential:failure", task.getException());
                            Toast.makeText(UserLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    private void sendOtherData(String firebaseToken, String googleAccessToken) {
        showProgress(UserLogin.this);
      /*  HashMap hashMap = new HashMap();
        hashMap.put("firebaseToken", firebaseToken);
        hashMap.put("realm", "customer");
        hashMap.put("access_token", googleAccessToken);*/

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessFb> call = apiInterface.googleLogin(firebaseToken, "customer", googleAccessToken);
        call.enqueue(new Callback<SuccessFb>() {
            @Override
            public void onResponse(Call<SuccessFb> call, Response<SuccessFb> response) {
                close();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = getSharedPreferences("loginSoci", MODE_PRIVATE).edit();
                    editor.putString("providerI", response.body().getSuccess().getData().getUser().getProvider());
                    editor.apply();
                    editor.commit();

                    if (!response.body().getSuccess().getData().getUser().getIsProfileComplete()) {

                        Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                        intent.putExtra("email", response.body().getSuccess().getData().getUser().getEmail());
                        intent.putExtra("image", response.body().getSuccess().getData().getUser().getImage());
                        intent.putExtra("name", response.body().getSuccess().getData().getUser().getFullName());
                        intent.putExtra("userId", response.body().getSuccess().getData().getUser().getId());
                        intent.putExtra("profileComplete", response.body().getSuccess().getData().getUser().getIsProfileComplete());
                        intent.putExtra("loginFB", true);
                        startActivity(intent);
                    } else {

                        userId = response.body().getSuccess().getData().getUserId();
                        userAccessToken = response.body().getSuccess().getData().getAccessToken();
                        userEmail = response.body().getSuccess().getData().getUser().getEmail();
                        userName = response.body().getSuccess().getData().getUser().getFullName();
                        userMobile = response.body().getSuccess().getData().getUser().getMobile();
                        userType = "customer";
                        userGstNo = response.body().getSuccess().getData().getUser().getGstNumber();
                        mobileVerify = response.body().getSuccess().getData().getUser().getMobileVerified();
                        emailVerify = response.body().getSuccess().getData().getUser().getEmailVerified();
                        if (response.body().getSuccess().getData().getUser().getAddresses().size() != 0) {
                            userAddress = response.body().getSuccess().getData().getUser().getAddresses().get(0).getValue();
                            userStreet = response.body().getSuccess().getData().getUser().getAddresses().get(0).getStreet();
                            userLatitude = response.body().getSuccess().getData().getUser().getAddresses().get(0).getLocation().getLat();
                            userLongitude = response.body().getSuccess().getData().getUser().getAddresses().get(0).getLocation().getLng();
                        }
                        userImage = response.body().getSuccess().getData().getUser().getImage();
                        getLoginType = response.body().getSuccess().getData().getUser().getRealm();

                        if (mobileVerify && emailVerify)
                        {

                            baseHandler.resetTables(DataBaseHandler.TABLE_USER);
                            baseHandler.addUser(userAccessToken, userId, userEmail, userName, userMobile, userGstNo, userAddress, userStreet, userLatitude, userLongitude, userType, userImage, getLoginType);

                            SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
                            editorValues.putString("ACCESS_TOKEN", userAccessToken);
                            editorValues.putString("USER_ID", userId);
                            editorValues.putString("USER_EMAIL", userEmail);
                            editorValues.putString("USER_NAME", userName);
                            editorValues.putString("USER_MOBILE_NO", userMobile);
                            editorValues.putString("USER_GST", userGstNo);
                            editorValues.putString("USER_ADDRESS", userAddress);
                            editorValues.putString("USER_STREET", userStreet);
                            editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
                            editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
                            editorValues.putString("USER_TYPE", userType);
                            editorValues.putString("USER_IMAGE", userImage);
                            editorValues.putString("LOGIN_TYPE", getLoginType);
                            editorValues.putBoolean("LOGIN_STATUS",true);
                            editorValues.apply();
                            editorValues.commit();

                            Intent intent = new Intent(UserLogin.this, UserHomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            if (!mobileVerify)
                            {
                                showToast(UserLogin.this,"Please verify your mobile number");
                                Intent intent = new Intent(UserLogin.this, OtpVarification.class);
                                intent.putExtra("userOtp", "");
                                intent.putExtra("userId", response.body().getSuccess().getData().getUser().getId());
                                startActivity(intent);
                            }else if (!emailVerify){

                                try {
                                    final Dialog dialog = new Dialog(UserLogin.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                                    dialog.setContentView(R.layout.show_msg_email);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(false);
                                    final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                                    //final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                                    dialog.show();
                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }






                               // showToast(UserLogin.this,"Please check your mail for verification");
                            }

                        }
                    }


                } else {
                    try {

                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.optJSONObject("error");
                        showToast(UserLogin.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessFb> call, Throwable t) {
                close();

            }
        });
    }


    private void userLogin() {
        SharedPreferences shared = getSharedPreferences("REG_TOKEN", MODE_PRIVATE);
        String channel = shared.getString(getResources().getString(R.string.firebase_token), "");

      /*  if (channel != null) {
            if (fbAccessToken==null)
            {
                Log.e("njkfnjknvf",""+channel);
            }else
            {
                fbLoginSendInfo(channel, fbAccessToken);
            }
        }*/


        showProgress(UserLogin.this);
        HashMap hashMap = new HashMap();

        hashMap.put("realm", serviceType);
        hashMap.put(emailMobileStatus, emailMobileText.getText().toString().trim());
        hashMap.put("password", passwordText.getText().toString().trim());
        hashMap.put("firebaseToken", channel);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<SucessLogin> dataCall = apiInterface.userLogin(hashMap);
        dataCall.enqueue(new Callback<SucessLogin>() {
            @Override
            public void onResponse(Call<SucessLogin> call, Response<SucessLogin> response) {
                close();
                if (response.isSuccessful()) {
                    userId = response.body().getSuccess().getUserId();
                    userAccessToken = response.body().getSuccess().getId();
                    userEmail = response.body().getSuccess().getUser().getEmail();
                    userName = response.body().getSuccess().getUser().getFullName();
                    userMobile = response.body().getSuccess().getUser().getMobile();
                    userType = response.body().getSuccess().getUser().getCustomerType();
                    userGstNo = response.body().getSuccess().getUser().getGstNumber();
                    mobileVerify = response.body().getSuccess().getUser().getMobileVerified();
                    emailVerify = response.body().getSuccess().getUser().getEmailVerified();
                    if (response.body().getSuccess().getUser().getAddresses().size() != 0) {
                        userAddress = response.body().getSuccess().getUser().getAddresses().get(0).getValue();
                        userStreet = response.body().getSuccess().getUser().getAddresses().get(0).getStreet();
                        userLatitude = response.body().getSuccess().getUser().getAddresses().get(0).getLocation().getLat();
                        userLongitude = response.body().getSuccess().getUser().getAddresses().get(0).getLocation().getLng();
                    }
                    userImage = response.body().getSuccess().getUser().getImage();
                    getLoginType = response.body().getSuccess().getUser().getRealm();

                    baseHandler.resetTables(DataBaseHandler.TABLE_USER);
                    baseHandler.addUser(userAccessToken, userId, userEmail, userName, userMobile, userGstNo, userAddress, userStreet, userLatitude, userLongitude, userType, userImage, getLoginType);

                    SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
                    editorValues.putString("ACCESS_TOKEN", userAccessToken);
                    editorValues.putString("USER_ID", userId);
                    editorValues.putString("USER_EMAIL", userEmail);
                    editorValues.putString("USER_NAME", userName);
                    editorValues.putString("USER_MOBILE_NO", userMobile);
                    editorValues.putString("USER_GST", userGstNo);
                    editorValues.putString("USER_ADDRESS", userAddress);
                    editorValues.putString("USER_STREET", userStreet);
                    editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
                    editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
                    editorValues.putString("USER_TYPE", userType);
                    editorValues.putString("USER_IMAGE", userImage);
                    editorValues.putString("LOGIN_TYPE", getLoginType);
                    editorValues.putBoolean("LOGIN_STATUS",true);
                    editorValues.apply();
                    editorValues.commit();

                    if (serviceType.equals("vendor")) {
                        // UserHomeScreen.fa.finish();
                        Intent intent = new Intent(UserLogin.this, VendorHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    if (serviceType.equals("customer")) {
                        //UserHomeScreen.fa.finish();
                        Intent intent = new Intent(UserLogin.this, UserHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    if (serviceType.equals("engineer")) {
                        // UserHomeScreen.fa.finish();
                        int EngineerExperience = response.body().getSuccess().getUser().getExp();
                        String AddressString = response.body().getSuccess().getUser().getAddress();
                        Set<String> set = new HashSet<String>();
                        set.addAll(response.body().getSuccess().getUser().getExpertiseIds());
                        SharedPreferences.Editor editor = getSharedPreferences("storeEngineerData", MODE_PRIVATE).edit();
                        editor.putStringSet("expertID", (Set<String>) set);
                        editor.putInt("EngineerExperience", EngineerExperience);
                        editor.putString("AddressString", AddressString);
                        editor.apply();
                        editor.commit();
                        Intent intent = new Intent(UserLogin.this, EngineerHomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");

                        if (jsonObjError.getString("code").equals("LOGIN_FAILED_EMAIL_MOBILE_NOT_VERIFIED") ||
                                jsonObjError.getString("code").equals("LOGIN_FAILED_MOBILE_NOT_VERIFIED")) {
                            JSONObject jsonObDetail = jsonObjError.getJSONObject("details");
                            Intent intent = new Intent(UserLogin.this, OtpVarification.class);
                            intent.putExtra("userOtp", "");
                            intent.putExtra("userId", jsonObDetail.getString("userId"));
                            startActivity(intent);
                        }else
                        {
                            showToast(UserLogin.this,""+jsonObjError.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SucessLogin> call, Throwable t) {
                close();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserLogin.this, UserHomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(UserLogin.this, "Connection fail", Toast.LENGTH_SHORT).show();

    }
}
