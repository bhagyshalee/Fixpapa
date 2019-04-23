package com.fixpapa.ffixpapa.EngineerPart;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.SuccessEn;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.VendorPart.EngineerExpertInAdapter;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class EngineerInfo extends AppCompatActivity {

    RatingBar ratingEngineerProfile;
    ImageView engineerProfilePic,whatsappImage;
    TextView engineerName, experienceValue,engineerEmail,engineerMobile;
    RecyclerView engineerBlockRecycler;
    ImageView crossImage;
    EngineerExpertInAdapter expertInAdapter;
    String engineerId,engMobile,jobId;
    List<String> expName;
    boolean shoTrue=false;
    LinearLayout layCon;
    int REQUEST_CAMERA_PERMISSION = 11;
    Uri imageUri;
    private static final int PICK_Camera_IMAGE = 100;
    File fileImage, compressedImage;
    String strUserId, strUserName, strUserGstNo, strUserImage;
    DataBaseHandler baseHandler;
    String LOGIN_TYPE, ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_engineer_profile);
        getUserDetailId(EngineerInfo.this);
        baseHandler = new DataBaseHandler(EngineerInfo.this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        engineerId = bundle.getString("engineerId");
        jobId = bundle.getString("jobId");
        shoTrue = bundle.getBoolean("showUpdation");
        expName=new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");
        LOGIN_TYPE = sharedPref.getString("LOGIN_TYPE", "");

        ratingEngineerProfile = (RatingBar) findViewById(R.id.ratingEngineerProfile);
        engineerProfilePic = (ImageView) findViewById(R.id.engineerProfilePic);
        engineerName = (TextView) findViewById(R.id.engineerName);
        experienceValue = (TextView) findViewById(R.id.experienceValue);
        engineerEmail = (TextView) findViewById(R.id.engineerEmail);
        engineerMobile = (TextView) findViewById(R.id.engineerMobile);
        crossImage = (ImageView) findViewById(R.id.crossImage);
        whatsappImage = (ImageView) findViewById(R.id.whatsappImage);
        layCon = (LinearLayout) findViewById(R.id.layCon);
        Log.e("kbjhvbhk",""+shoTrue);
        engineerBlockRecycler = (RecyclerView) findViewById(R.id.engineerBlockRecycler);
        engineerBlockRecycler.setHasFixedSize(true);
        engineerBlockRecycler.setNestedScrollingEnabled(false);
        engineerBlockRecycler.setLayoutManager(new LinearLayoutManager(EngineerInfo.this));
        getEngineerId();

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        whatsappImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (isPermissionGranted()) {
                    call_action();
//                }

            }
        });
        if (shoTrue) {
            whatsappImage.setVisibility(View.GONE);
        }
        else
        {
            getJobDetail(jobId);
            whatsappImage.setVisibility(View.VISIBLE);
        }
        engineerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoTrue)
                {
                    try {
                        final Dialog dialog = new Dialog(EngineerInfo.this);
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
                                    if (ActivityCompat.checkSelfPermission(EngineerInfo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(EngineerInfo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                    if (ActivityCompat.checkSelfPermission(EngineerInfo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(EngineerInfo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

        if (requestCode == 200) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");

                    new ImageCompressionAsyncTask(EngineerInfo.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTask(EngineerInfo.this).execute(selectedImageUri.toString(),
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

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(EngineerInfo.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(EngineerInfo.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                //strCompressedFileImage = s;
                compressedImage = new File(s);
                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    Log.e("dgklrger",""+engineerId);
                    if (LOGIN_TYPE.equals("engineer")) {
                        updateEnginProfile();
                    } else {
                        updateProfile(engineerId);
                    }
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EngineerInfo.this.getContentResolver(), compressUri);
                    engineerProfilePic.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
            }
        }
    }
    private void updateProfile(String engineerId) {
        MultipartBody.Part filePartmultipleImages = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engineerId", engineerId);

        if (compressedImage != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            filePartmultipleImages = MultipartBody.Part.createFormData("image", compressedImage.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.ic_user));
            filePartmultipleImages = MultipartBody.Part.createFormData("image", String.valueOf(R.drawable.ic_user), requestBody);
        }
        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(engineerId));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateEngineerProfile(ACCESS_TOKEN, objdata, filePartmultipleImages);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    showToast(EngineerInfo.this, "" + "Image Updated Successfully");
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
                    baseHandler.addSearchDataUpdate(strUserId, strUserName, strUserGstNo, strUserImage, response.body().getSuccess().getData().getMobile(), response.body().getSuccess().getData().getEmail());

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
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerInfo.this, "" + jsonObjError.getString("message"));
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



    private void updateEnginProfile() {
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
                    showToast(EngineerInfo.this, "" + "Image Updated Successfully");
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
                    baseHandler.addSearchDataUpdate(strUserId, strUserName, strUserGstNo, strUserImage, response.body().getSuccess().getData().getMobile(), response.body().getSuccess().getData().getEmail());


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
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerInfo.this, "" + jsonObjError.getString("message"));
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

    public void call_action() {
       /* Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+engMobile));
        if (ActivityCompat.checkSelfPermission(EngineerInfo.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);*/
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+"+engMobile, null));
        startActivity(intent);
    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    private void getJobDetail(String JOB_ID) {
        //showProgress(EngineerInfo.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SmallSucess> call = apiInterface.getJobDetail(ACCESS_TOKEN, JOB_ID);
        call.enqueue(new Callback<SmallSucess>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onResponse(Call<SmallSucess> call, Response<SmallSucess> response) {
               // close();
                if (response.isSuccessful()) {
                  if (response.body().getSuccess().getData().getStatus().equals("completed") || response.body().getSuccess().getData().getStatus().equals("canceled"))
                  {
                      engineerEmail.setVisibility(View.GONE);
                      engineerMobile.setVisibility(View.GONE);
                      whatsappImage.setVisibility(View.GONE);
                      layCon.setVisibility(View.GONE);
                  }
                  else
                  {
                      engineerEmail.setVisibility(View.VISIBLE);
                      engineerMobile.setVisibility(View.VISIBLE);
                      whatsappImage.setVisibility(View.VISIBLE);
                      layCon.setVisibility(View.VISIBLE);
                  }
                    //showToast(OrderDetail.this, "" + response.body().getSuccess().getMsg().getReplyCode());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerInfo.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SmallSucess> call, Throwable t) {
               // close();
            }
        });

    }


    private void getEngineerId() {
        showProgress(EngineerInfo.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessEn> call = apiInterface.getEgineerInfo(ACCESS_TOKEN, engineerId, "engineer");
        call.enqueue(new Callback<SuccessEn>() {
            @Override
            public void onResponse(Call<SuccessEn> call, Response<SuccessEn> response) {
                close();
               // Log.e("fdvdfvbdf", "" + response.body().getSuccess().getData().getFullName());
                if (response.isSuccessful()) {
                    Log.e("fdvdfvbdf", "" + response.body().getSuccess().getData());
                    Picasso.with(EngineerInfo.this)
                            .load(Image_BASE_URL + response.body().getSuccess().getData().getImage())
                            .placeholder(R.drawable.icon_fixpapa)
                            .error(R.drawable.icon_fixpapa)
                            .into(engineerProfilePic);
                    engineerName.setText(response.body().getSuccess().getData().getFullName());
                    experienceValue.setText(""+response.body().getSuccess().getData().getExp());
                    engineerEmail.setText(response.body().getSuccess().getData().getEmail());
                    engineerMobile.setText(response.body().getSuccess().getData().getMobile());
                    ratingEngineerProfile.setRating(Float.valueOf(String.valueOf(response.body().getSuccess().getData().getRating().getAvgRating())));
                   if (response.body().getSuccess().getData().getExpertise()!=null) {
                       for (int i = 0; i < response.body().getSuccess().getData().getExpertise().size(); i++) {
                           expName.add(response.body().getSuccess().getData().getExpertise().get(i).getName());
                       }
                   }
                    expertInAdapter = new EngineerExpertInAdapter(expName, EngineerInfo.this);
                    engineerBlockRecycler.setAdapter(expertInAdapter);
                    engMobile=response.body().getSuccess().getData().getMobile();
                  //  if (response.body().getSuccess().getData().get))
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerInfo.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessEn> call, Throwable t) {
                close();
                Log.e("fdvdfvbdf", "" + t);
            }
        });

    }
}