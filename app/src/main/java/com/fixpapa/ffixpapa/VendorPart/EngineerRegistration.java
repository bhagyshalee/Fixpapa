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

import com.fixpapa.ffixpapa.EngineerPart.EngineerViewPendingJobs;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.Services.Rest.ApiClient;
import com.fixpapa.ffixpapa.Services.Rest.ApiInterface;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.UserPart.OtpVarification;
import com.fixpapa.ffixpapa.VendorPart.Adapters.ExpertInAdapter;
import com.fixpapa.ffixpapa.VendorPart.HomePart.VendorHomeScreen;
import com.fixpapa.ffixpapa.VendorPart.Model.OverViewModule;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.close;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.getUserDetailId;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.isOnline;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showProgress;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.VendorPart.Adapters.ExpertInAdapter.expertiseId;

public class EngineerRegistration extends AppCompatActivity {

    RecyclerView expertInRecycler;
    ExpertInAdapter expertInAdapter;
    Spinner experienceSpinner;
    Button buttonVendorSignUp;
    ImageView backImage, engineerProfilePic;
    Uri imageUri;
    String strName, strEmail, strPassword, strContactNo, strAddress, strConfirmPassword;

    File fileImage, compressedImage;
    int REQUEST_CAMERA_PERMISSION = 11;
    EditText engName, engContactNo, engEmail, engPassword, engConPassword, engAddress;
    private static final int PICK_Camera_IMAGE = 100;
    int getStatus = 0;
    String engineerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_registration);
        getUserDetailId(EngineerRegistration.this);

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

        expertInRecycler.setHasFixedSize(true);
        expertInRecycler.setNestedScrollingEnabled(false);
        expertInRecycler.setLayoutManager(new GridLayoutManager(EngineerRegistration.this, 2));

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
                } else if (strPassword.isEmpty()) {
                    engPassword.setError(getText(R.string.password_error));
                    engPassword.requestFocus();
                } else if (strConfirmPassword.isEmpty() || !strConfirmPassword.equals(strPassword)) {
                    engConPassword.setError(getText(R.string.confirm_password_error));
                    engConPassword.requestFocus();
                } else if (strContactNo.length() <= 9 || strContactNo.isEmpty()) {
                    engContactNo.setError(getText(R.string.contact_valid_error));
                    engContactNo.requestFocus();
                } else if (strAddress.isEmpty()) {
                    engAddress.setError(getText(R.string.address_error));
                    engAddress.requestFocus();
                } else if (isOnline(true, EngineerRegistration.this)) {
                    int spinnerExp = Integer.valueOf(experienceSpinner.getSelectedItem().toString().trim());
                    registerVendor(strName, strEmail, strContactNo, strAddress, strPassword, spinnerExp);

                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        engineerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(EngineerRegistration.this);
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
                                if (ActivityCompat.checkSelfPermission(EngineerRegistration.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(EngineerRegistration.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                if (ActivityCompat.checkSelfPermission(EngineerRegistration.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(EngineerRegistration.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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


        if (isOnline(true, EngineerRegistration.this)) {
            getExpertIn();
        }
        Integer[] plants = new Integer[]{
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };
        final ArrayList<Integer> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(EngineerRegistration.this, R.layout.design_spinner, R.id.textSpinner, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.textSpinner);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.design_spinner);
        experienceSpinner.setAdapter(spinnerArrayAdapter);

    }

    private void registerVendor(String strName, String strEmail, String strContactNo, String strAddress, String strPassword, int nameExp) {
        SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
                Context.MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN","");
        String USER_ID = sharedPref.getString("USER_ID","");

        MultipartBody.Part filePartmultipleImages = null;
        if (compressedImage != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            filePartmultipleImages = MultipartBody.Part.createFormData("image", compressedImage.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.ic_user));
            filePartmultipleImages = MultipartBody.Part.createFormData("image", String.valueOf(R.drawable.ic_user), requestBody);
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray(expertiseId);

        try {
            jsonObject.put("realm", "engineer");
            jsonObject.put("vendorId", "" + USER_ID);
            jsonObject.put("fullName", strName);
            jsonObject.put("email", strEmail);
            jsonObject.put("mobile", strContactNo);
            jsonObject.put("password", strPassword);
            jsonObject.put("address", strAddress);
            jsonObject.put("expertiseIds", jsonArray);
            jsonObject.put("exp", nameExp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("allregisterData", String.valueOf(jsonObject));
        String jsonObjData = String.valueOf(jsonObject);
        showProgress(EngineerRegistration.this);
        RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

        ApiInterface apinterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> modelCall = apinterface.vendorRegister(ACCESS_TOKEN, filePartmultipleImages, objdata);
        modelCall.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                close();
                if (response.isSuccessful()) {
                    if (compressedImage != null) {
                        File file = new File(String.valueOf(compressedImage));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    showToast(EngineerRegistration.this,response.body().getSuccess().getMsg().getReplyMessage());
                    Intent intent = new Intent(EngineerRegistration.this, VendorHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerRegistration.this, "" + jsonObjError.getString("message"));
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

                    new ImageCompressionAsyncTask(EngineerRegistration.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTask(EngineerRegistration.this).execute(selectedImageUri.toString(),
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
            Utility.showProgress(EngineerRegistration.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(EngineerRegistration.this).compress(params[0], new File(params[1]));

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EngineerRegistration.this.getContentResolver(), compressUri);
                    engineerProfilePic.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
            }
        }
    }

    private void getExpertIn() {
        showProgress(EngineerRegistration.this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<OverViewModule> expertModuleCall = apiInterface.getExpertQuality();
        expertModuleCall.enqueue(new Callback<OverViewModule>() {
            @Override
            public void onResponse(Call<OverViewModule> call, Response<OverViewModule> response) {
                close();
                if (response.isSuccessful()) {

                    expertInAdapter = new ExpertInAdapter(response.body().getSuccess().getEngineerRegi(), EngineerRegistration.this);
                    expertInRecycler.setAdapter(expertInAdapter);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(EngineerRegistration.this, "" + jsonObjError.getString("message"));
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
}
