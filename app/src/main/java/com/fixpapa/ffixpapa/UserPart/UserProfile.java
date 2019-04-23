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

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;


public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    String ACCESS_TOKEN,USER_EMAIL,USER_GST,USER_IMAGE,USER_MOBILE_NO,USER_NAME,USER_TYPE,USER_ID;
    RadioGroup radioGrp;
    Button btnSignUp;
    ImageView profileImage, backImage;
    EditText userGstNo, userContactNo, userName, userEmail;
    RadioButton radioHome;
    String strUsertType = "";
    String strUserId, strUserName, strUserGstNo, strUserImage;


    int REQUEST_CAMERA_PERMISSION = 1;
    Uri imageUri;
    File fileImage, compressedImage;
    String strCompressedFileImage;
    private static final int PICK_Camera_IMAGE = 100;
    DataBaseHandler baseHandler;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getUserDetailId(UserProfile.this);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        backImage = (ImageView) findViewById(R.id.crossImage);
        userGstNo = (EditText) findViewById(R.id.userGstNo);
        userContactNo = (EditText) findViewById(R.id.userContactNo);
        userName = (EditText) findViewById(R.id.userName);
        userEmail = (EditText) findViewById(R.id.userEmail);
        radioGrp = (RadioGroup) findViewById(R.id.radioGrp);
        baseHandler = new DataBaseHandler(UserProfile.this);
        profileImage.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        backImage.setOnClickListener(this);

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
         ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
         USER_ID = sharedPref.getString("USER_ID","");
         USER_EMAIL = sharedPref.getString("USER_EMAIL","");
        USER_GST = sharedPref.getString("USER_GST","");
        USER_NAME = sharedPref.getString("USER_NAME","");
        USER_TYPE = sharedPref.getString("USER_TYPE","");
        USER_IMAGE = sharedPref.getString("USER_IMAGE","");
        USER_MOBILE_NO = sharedPref.getString("USER_MOBILE_NO","");

        userEmail.setText(USER_EMAIL);
        userContactNo.setText(USER_MOBILE_NO);
        userGstNo.setText(USER_GST);
        userName.setText(USER_NAME);
        Log.e("fdbfdbdfr",""+USER_TYPE);
        if (USER_TYPE.equals("home"))
        {
            userGstNo.setVisibility(View.GONE);
        }
        else
        {
            userGstNo.setVisibility(View.VISIBLE);
        }
        Picasso.with(UserProfile.this)
                .load(Image_BASE_URL + USER_IMAGE)
                .placeholder(R.drawable.about_us)
                .error(R.drawable.ic_user)
                .into(profileImage);

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp.getCheckedRadioButtonId();
                radioHome = (RadioButton) findViewById(selectedId);
                strUsertType = radioHome.getText().toString().trim();

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (data != null) {


                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");

                    new ImageCompressionAsyncTask(UserProfile.this).execute(data.getData().toString(),
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
                    new ImageCompressionAsyncTask(UserProfile.this).execute(selectedImageUri.toString(),
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
        if (v==backImage)
        {
            finish();
        }
        if (v == btnSignUp) {
            updateUserData(strUsertType, userName.getText().toString(), userGstNo.getText().toString());
        }
        if (v == profileImage) {
            try {
                final Dialog dialog = new Dialog(UserProfile.this);
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
                            if (ActivityCompat.checkSelfPermission(UserProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                            if (ActivityCompat.checkSelfPermission(UserProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(UserProfile.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(UserProfile.this).compress(params[0], new File(params[1]));

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

                    updateProfile();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserProfile.this.getContentResolver(), compressUri);
                    profileImage.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
            }
        }
    }

    private void updateProfile() {
        MultipartBody.Part filePartmultipleImages = null;

        if (compressedImage != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            filePartmultipleImages = MultipartBody.Part.createFormData("image", compressedImage.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.ic_user));
            filePartmultipleImages = MultipartBody.Part.createFormData("image", String.valueOf(R.drawable.ic_user), requestBody);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateUserProfile(ACCESS_TOKEN, filePartmultipleImages);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(UserProfile.this, "" + "Image Updated Successfully");
                    //strUserId = response.body().getSuccess().getData().getI;
                    if (compressedImage != null) {
                        File file = new File(String.valueOf(compressedImage));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    strUserName = response.body().getSuccess().getData().getFullName();
                    strUserGstNo = response.body().getSuccess().getData().getGstNumber();
                    strUserImage = response.body().getSuccess().getData().getImage();
                    strUserId = response.body().getSuccess().getData().getId();
                    baseHandler.addSearchDataUpdate(strUserId, strUserName, strUserGstNo, strUserImage,response.body().getSuccess().getData().getMobile(),response.body().getSuccess().getData().getEmail());

                    SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
//                    editorValues.putString("ACCESS_TOKEN", userAccessToken);
                    editorValues.putString("USER_ID", strUserId);
                    editorValues.putString("USER_EMAIL", response.body().getSuccess().getData().getEmail());
                    editorValues.putString("USER_NAME", strUserName);
                    editorValues.putString("USER_MOBILE_NO", response.body().getSuccess().getData().getMobile());
                    editorValues.putString("USER_GST", strUserGstNo);
//                    editorValues.putString("USER_ADDRESS", userAddress);
//                    editorValues.putString("USER_STREET", userStreet);
//                    editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
//                    editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
//                    editorValues.putString("USER_TYPE", userType);
                    editorValues.putString("USER_IMAGE", strUserImage);
//                    editorValues.putString("LOGIN_TYPE", getLoginType);
                    editorValues.apply();
                    editorValues.commit();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(UserProfile.this,""+jsonObjError.getString("message"));
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
            }
        });

    }

    private void updateUserData(String customerType, final String fullName, final String gstNo) {
        Utility.showProgress(UserProfile.this);
        HashMap hashMap = new HashMap();
        // hashMap.put("customerType", customerType);
        hashMap.put("fullName", fullName);
        hashMap.put("gstNumber", gstNo);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateUserDetail(ACCESS_TOKEN, hashMap);

        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    userGstNo.setText(gstNo);
                    userName.setText(fullName);
                    strUserName = response.body().getSuccess().getData().getFullName();
                    strUserGstNo = response.body().getSuccess().getData().getGstNumber();
                    strUserId = response.body().getSuccess().getData().getId();
                    strUserImage = response.body().getSuccess().getData().getImage();
                    baseHandler.addSearchDataUpdate(strUserId, strUserName, strUserGstNo, strUserImage,response.body().getSuccess().getData().getMobile(),response.body().getSuccess().getData().getEmail());

                    SharedPreferences.Editor editorValues = getSharedPreferences("LOGINVALUES", MODE_PRIVATE).edit();
//                    editorValues.putString("ACCESS_TOKEN", userAccessToken);
                    editorValues.putString("USER_ID", strUserId);
                    editorValues.putString("USER_EMAIL", response.body().getSuccess().getData().getEmail());
                    editorValues.putString("USER_NAME", strUserName);
                    editorValues.putString("USER_MOBILE_NO", response.body().getSuccess().getData().getMobile());
                    editorValues.putString("USER_GST", strUserGstNo);
//                    editorValues.putString("USER_ADDRESS", userAddress);
//                    editorValues.putString("USER_STREET", userStreet);
//                    editorValues.putString("USER_LATITUDE", String.valueOf(userLatitude));
//                    editorValues.putString("USER_LONGITUDE", String.valueOf(userLongitude));
//                    editorValues.putString("USER_TYPE", userType);
                    editorValues.putString("USER_IMAGE", strUserImage);
//                    editorValues.putString("LOGIN_TYPE", getLoginType);
                    editorValues.apply();
                    editorValues.commit();
                    showToast(UserProfile.this,""+response.body().getSuccess().getMsg().getReplyCode());
                    finish();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError=jsonObject.getJSONObject("error");
                        showToast(UserProfile.this,""+jsonObjError.getString("message"));
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
            }
        });
    }


}
