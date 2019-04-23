package com.fixpapa.ffixpapa.VendorPart;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.DataBaseHandler;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.VendorPart.Adapters.ExpertInAdapterUpdate;
import com.fixpapa.ffixpapa.VendorPart.Model.OverViewModule;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.ApiClient.Image_BASE_URL;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.VendorPart.Adapters.ExpertInAdapterUpdate.expertiseIdUpdate;

public class UpdateEngineerProfile extends AppCompatActivity {

    RecyclerView expertInRecycler;
    ExpertInAdapterUpdate expertInAdapter;
    Spinner experienceSpinner;
    Button buttonVendorSignUp;
    ImageView backImage, engineerProfilePic;
    Uri imageUri;
    String strName, strEmail, strPassword, strContactNo, strAddress, strConfirmPassword;
    String strUserId, strUserName, strUserGstNo, strUserImage;

    File fileImage, compressedImage;
    int REQUEST_CAMERA_PERMISSION = 11;
    EditText engName, engContactNo, engEmail, engPassword, engConPassword, engAddress;
    private static final int PICK_Camera_IMAGE = 100;
    String engineerId = "", engineerName = "", engineerEmail = "", engineerMobile = "", engineerAddress = "", engineerImage, loginStatus;
    TextView titleText;
    int engineerExp;
    List<String> engineerIds;
    int spinnerPosition = 0;
    boolean setCheckedStatus = true;
    DataBaseHandler baseHandler;
    String LOGIN_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_registration);
        engineerIds = new ArrayList<>();
        baseHandler = new DataBaseHandler(UpdateEngineerProfile.this);
        expertInRecycler = (RecyclerView) findViewById(R.id.expertInRecycler);
        experienceSpinner = (Spinner) findViewById(R.id.experienceSpinner);
        backImage = (ImageView) findViewById(R.id.backImage);
        engineerProfilePic = (ImageView) findViewById(R.id.engineerProfilePic);
        buttonVendorSignUp = (Button) findViewById(R.id.buttonVendorSignUp);
        engName = (EditText) findViewById(R.id.engName);
        engContactNo = (EditText) findViewById(R.id.engContactNo);
        engEmail = (EditText) findViewById(R.id.engEmail);
        engPassword = (EditText) findViewById(R.id.engPassword);
        engConPassword = (EditText) findViewById(R.id.engConPassword);
        engAddress = (EditText) findViewById(R.id.engAddress);
        titleText = (TextView) findViewById(R.id.titleText);

        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        LOGIN_TYPE= sharedPref.getString("LOGIN_TYPE","");

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            engineerIds = (ArrayList<String>) bundle.getSerializable("engineerexpId");
            engineerId = bundle.getString("engineerId");
            engineerName = bundle.getString("engineerName");
            engineerEmail = bundle.getString("engineerEmail");
            engineerMobile = bundle.getString("engineerMobile");
            engineerExp = bundle.getInt("engineerExp");
            engineerImage = bundle.getString("engineerImage");
            engineerAddress = bundle.getString("engineerAddress");
            loginStatus = bundle.getString("loginStatus");
            engName.setText(engineerName);
            engEmail.setText(engineerEmail);
            engContactNo.setText(engineerMobile);
            engAddress.setText(engineerAddress);
            Picasso.with(UpdateEngineerProfile.this)
                    .load(Image_BASE_URL + engineerImage)
                    .placeholder(R.drawable.user_circle_img)
                    .error(R.drawable.user_circle_img)
                    .into(engineerProfilePic);

        }


        engPassword.setVisibility(View.GONE);
        engConPassword.setVisibility(View.GONE);
        buttonVendorSignUp.setText(getText(R.string.update_text));
        titleText.setText("Update Profile");

        expertInRecycler.setHasFixedSize(true);
        expertInRecycler.setNestedScrollingEnabled(false);
        expertInRecycler.setLayoutManager(new GridLayoutManager(UpdateEngineerProfile.this, 2));

        buttonVendorSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = engName.getText().toString().trim();
                strEmail = engEmail.getText().toString().trim();
                strPassword = engPassword.getText().toString().trim();
                strContactNo = engContactNo.getText().toString().trim();
                strAddress = engAddress.getText().toString().trim();
                strConfirmPassword = engConPassword.getText().toString().trim();

                if (strName.isEmpty()) {
                    engName.setError(getText(R.string.name_error));
                    engName.requestFocus();
                } else if (strEmail.isEmpty()) {
                    engEmail.setError(getText(R.string.email_empty_error));
                    engEmail.requestFocus();
                } else if (!isValidEmail(strEmail)) {
                    engEmail.setError(getText(R.string.email_pattern_error));
                    engEmail.requestFocus();
                } else if (strContactNo.length() <= 9 || strContactNo.isEmpty()) {
                    engContactNo.setError(getText(R.string.contact_valid_error));
                    engContactNo.requestFocus();
                } else if (strAddress.isEmpty()) {
                    engAddress.setError(getText(R.string.address_error));
                    engAddress.requestFocus();
                } else if (isOnline(true, UpdateEngineerProfile.this)) {
                    int spinnerExp = Integer.parseInt(experienceSpinner.getSelectedItem().toString().trim());
                    updateVendorProfile(strName, strEmail, strContactNo, strAddress, spinnerExp, engineerId);

                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (LOGIN_TYPE.equals("engineer")) {
                  /*  Intent intent = new Intent(UpdateEngineerProfile.this, UpdateEngineer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                } else if (LOGIN_TYPE.equals("vendor")) {
                    Intent intent = new Intent(UpdateEngineerProfile.this, UpdateEngineer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        engineerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(UpdateEngineerProfile.this);
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
                                if (ActivityCompat.checkSelfPermission(UpdateEngineerProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UpdateEngineerProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                if (ActivityCompat.checkSelfPermission(UpdateEngineerProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UpdateEngineerProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
        });

        Integer[] plants = new Integer[]{
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };
        final ArrayList<Integer> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(UpdateEngineerProfile.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                return true;

            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        experienceSpinner.setAdapter(spinnerArrayAdapter);
        experienceSpinner.setSelection(spinnerArrayAdapter.getPosition(engineerExp));

        if (LOGIN_TYPE.equals("engineer")) {

            engName.setFocusable(false);
            engEmail.setFocusable(false);
            engContactNo.setFocusable(false);
            engAddress.setFocusable(false);
            experienceSpinner.setEnabled(false);
            buttonVendorSignUp.setVisibility(View.GONE);
            setCheckedStatus = false;
        }
        if (isOnline(true, UpdateEngineerProfile.this)) {
            getExpertIn();
        }

    }

    private void updateVendorProfile(String strName, String strEmail, String strContactNo, String strAddress, int spinnerExp, String enginId) {

        JSONArray jsonElements = new JSONArray(expertiseIdUpdate);
        SharedPreferences sharedPref =getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);


        showProgress(UpdateEngineerProfile.this);
        HashMap hashMap = new HashMap();
        hashMap.put("fullName", strName);
        hashMap.put("email", strEmail);
        hashMap.put("mobile", strContactNo);
        hashMap.put("exp", spinnerExp);
        hashMap.put("expertiseIds", jsonElements);
        hashMap.put("address", strAddress);
        hashMap.put("engineerId", enginId);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.editEngineer(ACCESS_TOKEN, hashMap);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        showToast(UpdateEngineerProfile.this, "Image Updated Successfully");
                        finish();
                        Intent intent = new Intent(UpdateEngineerProfile.this, UpdateEngineer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UpdateEngineerProfile.this, "" + jsonObjError.getString("message"));
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

                    new ImageCompressionAsyncTask(UpdateEngineerProfile.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTask(UpdateEngineerProfile.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(UpdateEngineerProfile.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(UpdateEngineerProfile.this).compress(params[0], new File(params[1]));

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UpdateEngineerProfile.this.getContentResolver(), compressUri);
                    engineerProfilePic.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
            }
        }
    }

    private void getExpertIn() {
        showProgress(UpdateEngineerProfile.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<OverViewModule> expertModuleCall = apiInterface.getExpertQuality();
        expertModuleCall.enqueue(new Callback<OverViewModule>() {
            @Override
            public void onResponse(Call<OverViewModule> call, Response<OverViewModule> response) {
                close();
                if (response.isSuccessful()) {
                    expertInAdapter = new ExpertInAdapterUpdate(setCheckedStatus, response.body().getSuccess().getEngineerRegi(), UpdateEngineerProfile.this, engineerIds);
                    expertInRecycler.setAdapter(expertInAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(UpdateEngineerProfile.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<OverViewModule> call, Throwable t) {
                close();
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void updateEnginProfile() {
        MultipartBody.Part filePartmultipleImages = null;
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);

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
                    showToast(UpdateEngineerProfile.this, "" + "Image Updated Successfully");
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
                        showToast(UpdateEngineerProfile.this, "" + jsonObjError.getString("message"));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (LOGIN_TYPE.equals("engineer")) {
                  /*  Intent intent = new Intent(UpdateEngineerProfile.this, UpdateEngineer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
        } else if (LOGIN_TYPE.equals("vendor")) {
            Intent intent = new Intent(UpdateEngineerProfile.this, UpdateEngineer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void updateProfile(String engineerId) {
        MultipartBody.Part filePartmultipleImages = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engineerId", engineerId);
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        Log.e("dgndndfbfdg", "" + ACCESS_TOKEN);

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
                    showToast(UpdateEngineerProfile.this, "" + "Image Updated Successfully");
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
                        showToast(UpdateEngineerProfile.this, "" + jsonObjError.getString("message"));
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
