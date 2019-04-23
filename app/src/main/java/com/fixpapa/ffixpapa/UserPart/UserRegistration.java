package com.fixpapa.ffixpapa.UserPart;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final int PICK_Camera_IMAGE = 100;
    int REQUEST_CAMERA_PERMISSION = 1;
    Uri imageUri;
    File fileImage, compressedImage;
    String strCompressedFileImage, jsonObjData;
    String strName, strEmail, strGst, strPassword, strAltAddress, strContactNo, strAddress, strConfirmPassword, strUsertType = "home", strCompanyName;
    ImageView profileImage, crossImage;
    EditText userGstNo, userAddress, userContactNo, userConPassword, userPassword, userName, userEmail, userAlternateAddress, userCompanyName;
    RadioGroup radioGrp;
    RadioButton radioHome;
    Button btnSignUp;
    double latitude, longitude;
    boolean getfbLogin = false, compOr;
    String emailFb, imageFb, nameFb, userId;
    GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("677218008910-31lbla4iqcnaap6fn8evg34qajnkt821.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(UserRegistration.this)
                .enableAutoManage(UserRegistration.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            emailFb = bundle.getString("email");
            imageFb = bundle.getString("image");
            nameFb = bundle.getString("name");
            getfbLogin = bundle.getBoolean("loginFB");
            userId = bundle.getString("userId");
            compOr = bundle.getBoolean("profileComplete");
        }

       /* gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("677218008910-31lbla4iqcnaap6fn8evg34qajnkt821.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(UserRegistration.this)
                .enableAutoManage(UserRegistration.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();*/


        Log.e("dfvfdvdfdbbbbb", "" + userId + " " + compOr);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        crossImage = (ImageView) findViewById(R.id.crossImage);

        userGstNo = (EditText) findViewById(R.id.userGstNo);
        userAlternateAddress = (EditText) findViewById(R.id.userAlternateAddress);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userContactNo = (EditText) findViewById(R.id.userContactNo);
        userConPassword = (EditText) findViewById(R.id.userConPassword);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userName = (EditText) findViewById(R.id.userName);
        userCompanyName = (EditText) findViewById(R.id.userCompanyName);
        userEmail = (EditText) findViewById(R.id.userEmail);
        radioGrp = (RadioGroup) findViewById(R.id.radioGrp);
        btnSignUp.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        crossImage.setOnClickListener(this);
        userAddress.setOnClickListener(this);

        if (getfbLogin) {
            //userEmail.setFocusable(false);
            Picasso.with(UserRegistration.this)
                    .load(imageFb)
                    .placeholder(R.drawable.icon_fixpapa)
                    .error(R.drawable.icon_fixpapa)
                    .into(profileImage);
            if (emailFb != null) {
                userEmail.setFocusable(false);
                userEmail.setText(emailFb);
            }
            userName.setText(nameFb);
        }

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp.getCheckedRadioButtonId();
                radioHome = (RadioButton) findViewById(selectedId);

                strUsertType = radioHome.getText().toString().trim();
                if (strUsertType.equals("home")) {
                    userGstNo.setVisibility(View.GONE);
                    userCompanyName.setVisibility(View.GONE);
                    strGst = "";
                }
                if (strUsertType.equals("corporate")) {
                    userGstNo.setVisibility(View.VISIBLE);
                    userCompanyName.setVisibility(View.VISIBLE);

                }

            }
        });


    }

    private void selectCameraImage() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, PICK_Camera_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                userAddress.setText(place.getAddress());
                //latitude=place.getl
            }
        }

        if (requestCode == 200) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");

                    new ImageCompressionAsyncTask(UserRegistration.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
            }
        } else if (requestCode == 100) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUri;

            if (selectedImageUri != null) {
                try {
                    String filemanagerstring = selectedImageUri.getPath();
                    String selectedImagePath = getPath(selectedImageUri);
                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;

                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown path",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Internal error",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }

            fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(UserRegistration.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onClick(View v) {
        if (v == userAddress) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, 19);
            } catch (Exception e) {
                Log.e("vfkldfnv", "" + e);
            }
        }
        if (v == crossImage) {
            if (getfbLogin) {
                SharedPreferences sharedD = getSharedPreferences("loginSoci", MODE_PRIVATE);
                String channel = sharedD.getString("providerI", "");

                if (channel.equals("facebook")) {

                    // LoginManager.getInstance().logOut();

                      /*  AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        if(accessToken != null){
                            LoginManager.getInstance().logOut();
                            sharedD.edit().clear().apply();
                            accessToken.setCurrentAccessToken(null);
                        }*/

                    AccessToken accessToken = AccessToken.getCurrentAccessToken();

                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();

                    sharedD.edit().clear().apply();
                    AccessToken.setCurrentAccessToken(null);
                } else if (channel.equals("google")) {
                    sharedD.edit().clear().apply();
                    signOut();
                }
            }
            finish();
        }
        if (v == profileImage) {
            try {
                final Dialog dialog = new Dialog(UserRegistration.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popuup_camragallary_design);
                dialog.setCanceledOnTouchOutside(false);

                TextView img_gallery = (TextView) dialog.findViewById(R.id.img_gallery);
                TextView img_camera = (TextView) dialog.findViewById(R.id.img_camera);
                ImageView closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
                dialog.show();
                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                img_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(UserRegistration.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UserRegistration.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                            } else {

                                selectCameraImage();
                                dialog.dismiss();
                            }
                        } else {
                            selectCameraImage();
                            dialog.dismiss();
                        }
                    }
                });
                img_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(UserRegistration.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UserRegistration.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 200);
                                dialog.dismiss();
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 200);
                            dialog.dismiss();
                        }
                    }
                });


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == btnSignUp) {
            strName = userName.getText().toString().trim();
            strEmail = userEmail.getText().toString().trim();
            strGst = userGstNo.getText().toString().trim();
            strCompanyName = userCompanyName.getText().toString().trim();
            strPassword = userPassword.getText().toString().trim();
            strContactNo = userContactNo.getText().toString().trim();
            strAddress = userAddress.getText().toString().trim();
            strAltAddress = userAlternateAddress.getText().toString().trim();
            strConfirmPassword = userConPassword.getText().toString().trim();
            if (strUsertType.isEmpty()) {
                Toast.makeText(UserRegistration.this, getText(R.string.select_usertype_show), Toast.LENGTH_SHORT).show();
            } else if (strName.isEmpty()) {
                userName.setError(getText(R.string.name_error));
                userName.requestFocus();
            } else if (strEmail.isEmpty()) {
                userEmail.setError(getText(R.string.email_empty_error));
                userEmail.requestFocus();
            } else if (!isValidEmail(strEmail)) {
                userEmail.setError(getText(R.string.email_pattern_error));
                userEmail.requestFocus();
            } else if (strPassword.isEmpty()) {
                userPassword.setError(getText(R.string.password_error));
                userPassword.requestFocus();
            } else if (strConfirmPassword.isEmpty() || !strConfirmPassword.equals(strPassword)) {
                userConPassword.setError(getText(R.string.confirm_password_error));
                userConPassword.requestFocus();
            } else if (strContactNo.length() <= 9 || strContactNo.isEmpty()) {
                userContactNo.setError(getText(R.string.contact_valid_error));
                userContactNo.requestFocus();
            } /*else if (strAddress.isEmpty()) {
                userAddress.setError(getText(R.string.selection_address_error));
                userAddress.requestFocus();
            } else if (strAltAddress.isEmpty()) {
                userAlternateAddress.setError(getText(R.string.altaddress_error));
                userAlternateAddress.requestFocus();
            }*/ else if (isOnline(true, UserRegistration.this)) {

                registerUser(userId, strName, strEmail, strContactNo, strAddress, strPassword, strUsertType, strAltAddress, strGst, strCompanyName);
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


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void registerUser(String userId, String name, String email, String mobile, String address, String password, String userType, String altAddress, String gstno, String strCompanyName) {
        MultipartBody.Part filePartmultipleImages = null;
        if (compressedImage != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            filePartmultipleImages = MultipartBody.Part.createFormData("image", compressedImage.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.ic_user));
            filePartmultipleImages = MultipartBody.Part.createFormData("image", String.valueOf(R.drawable.ic_user), requestBody);
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject subObject = new JSONObject();
        JSONObject latlong = new JSONObject();
        JSONArray innerArray = new JSONArray();

        try {
            jsonObject.put("realm", "customer");
            jsonObject.put("fullName", name);
            jsonObject.put("email", email);
            jsonObject.put("mobile", mobile);
            latlong.put("lat", latitude);
            latlong.put("lng", longitude);
            subObject.put("value", address);
            subObject.put("location", latlong);
            subObject.put("street", altAddress);
            innerArray.put(subObject);
            //jsonObject.put("addresses", innerArray);
            jsonObject.put("password", password);
            jsonObject.put("customerType", userType);
            jsonObject.put("gstNo", gstno);
            jsonObject.put("companyName", strCompanyName);
            if (!compOr) {
                jsonObject.put("peopleId", userId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjData = String.valueOf(jsonObject);
        showProgress(UserRegistration.this);
        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

        ApiInterface apinterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> modelCall =
                apinterface.userRegister(filePartmultipleImages, objdata);
        modelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, final Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    if (compressedImage != null) {
                        File file = new File(String.valueOf(compressedImage));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                         if (!response.body().getSuccess().getData().getEmailVerified()) {
                            try {
                                final Dialog dialog = new Dialog(UserRegistration.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.show_msg_email);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                                final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
                                dialog.show();
                                //titlePopup.setText(response.body().getSuccess().getMsg().getReplyMessage());
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        if (!response.body().getSuccess().getData().getMobileVerified()) {

                                            String userid = String.valueOf(response.body().getSuccess().getData().getId());
                                            String userotp = String.valueOf(response.body().getSuccess().getData().getMobOtp().getOtp());
                                            Intent intent = new Intent(UserRegistration.this, OtpVarification.class);
                                            intent.putExtra("userOtp", userotp);
                                            intent.putExtra("userId", userid);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            finish();
                                            showToast(UserRegistration.this,"SignUp Successfully");
                                        }

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                      else if (response.body().getSuccess().getData().getEmailVerified() && !response.body().getSuccess().getData().getMobileVerified()) {
                            String userid = String.valueOf(response.body().getSuccess().getData().getId());
                            String userotp = String.valueOf(response.body().getSuccess().getData().getMobOtp().getOtp());
                            Intent intent = new Intent(UserRegistration.this, OtpVarification.class);
                            intent.putExtra("userOtp", userotp);
                            intent.putExtra("userId", userid);
                            startActivity(intent);
                        }
                        else if (response.body().getSuccess().getData().getMobileVerified() && response.body().getSuccess().getData().getEmailVerified())
                         {
                             finish();
                             showToast(UserRegistration.this,"SignUp Successfully");
                         }


                }
                    else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UserRegistration.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                close();
                showToast(UserRegistration.this, "" + t);
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
    public void onBackPressed() {
        super.onBackPressed();
        if (getfbLogin) {
            SharedPreferences sharedD = getSharedPreferences("loginSoci", MODE_PRIVATE);
            String channel = sharedD.getString("providerI", "");

            if (channel.equals("facebook")) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                sharedD.edit().clear().apply();

            } else if (channel.equals("google")) {

                sharedD.edit().clear().apply();
                signOut();
            }
        }
        finish();
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(UserRegistration.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(UserRegistration.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                strCompressedFileImage = s;
                compressedImage = new File(s);
                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserRegistration.this.getContentResolver(), compressUri);
                    profileImage.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
            }
        }
    }
}
