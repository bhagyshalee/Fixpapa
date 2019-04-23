package com.fixpapa.ffixpapa.UserPart.HomePart.AllPostModule;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Problem;
import com.fixpapa.ffixpapa.Services.Rest.Utility;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssue;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuePrice;
import static com.fixpapa.ffixpapa.Services.Rest.Config.imagesArray;
import static com.fixpapa.ffixpapa.Services.Rest.Config.problemDescription;
import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuecategoryId;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssuecreatedAt;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueid;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueprobContent;
import static com.fixpapa.ffixpapa.UserPart.HomePart.Adapter.ShowIssueAdapter.listIssueupdatedAt;

public class ShowIssues extends AppCompatActivity implements View.OnClickListener {
    RecyclerView issuesRecycler;
    Button buttonNextIssues;
    ImageView backImage;
    List<Problem> problems;
    ShowIssueAdapter showIssueAdapter;
    String brandName;
    ImageView clickImageFirst, clickImageSecond;
    Uri imageUri, imageUriS;
    File compressedImage, compressedImageS;
    TextView brandNameShow;
    EditText editTextDescription;
    int REQUEST_CAMERA_PERMISSION = 1;
    TextView img_removeF, img_removeS;
    private static final int PICK_Camera_IMAGE = 100;
    String firstTag, secondTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_issues);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        problems = (List<Problem>) bundle.getSerializable("getIssues");
        brandName = bundle.getString("getMainCat");

        imagesArray = new JSONArray();
        secondTag = "imageTwo";
        firstTag = "imageOne";
        buttonNextIssues = (Button) findViewById(R.id.buttonNextIssues);
        issuesRecycler = (RecyclerView) findViewById(R.id.issuesRecycler);
        backImage = (ImageView) findViewById(R.id.backImage);
        clickImageFirst = (ImageView) findViewById(R.id.clickImageFirst);
        clickImageSecond = (ImageView) findViewById(R.id.clickImageSecond);
        brandNameShow = (TextView) findViewById(R.id.brandNameShow);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        clickImageFirst.setOnClickListener(this);
        clickImageSecond.setOnClickListener(this);
        backImage.setOnClickListener(this);
        buttonNextIssues.setOnClickListener(this);

        brandNameShow.setText(brandName);

        issuesRecycler.setHasFixedSize(false);
        issuesRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowIssues.this);
        issuesRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ShowIssues.this, layoutManager.getOrientation());
        issuesRecycler.addItemDecoration(dividerItemDecoration);
        showIssueAdapter = new ShowIssueAdapter(problems, ShowIssues.this);
        issuesRecycler.setAdapter(showIssueAdapter);


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

    private void selectCameraImageS() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUriS = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriS);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNextIssues) {
            problemDescription = editTextDescription.getText().toString().trim();
            if (listIssue.isEmpty()) {
                showToast(ShowIssues.this, "Please Select Issues");
            } else {

                Bundle bundle = new Bundle();
                Intent intent = new Intent(ShowIssues.this, TotalPrice.class);
                intent.putExtra("issueValue", listIssue);
                intent.putExtra("issueValuePrice", listIssuePrice);
               /* intent.putExtra("issueId", listIssueid);
                intent.putExtra("issueCategoryId", listIssuecategoryId);
                intent.putExtra("issueProbContent", listIssueprobContent);
                intent.putExtra("issueCreatedAt", listIssuecreatedAt);
                intent.putExtra("issueUpdatedAt", listIssueupdatedAt);
                Log.e("hgerbrereregh",""+listIssue+" "+listIssuePrice+" "+listIssueid+" "+listIssuecategoryId
                        +" "+listIssueprobContent+" "+listIssueupdatedAt+" "+listIssuecreatedAt);*/
                startActivity(intent);
            }

          /*  ArrayList<List<String>> myList = new ArrayList<>();
            myList.add(listIssue)*/


        }
        if (v == backImage) {
            finish();
        }
        if (v == clickImageSecond) {
            try {
                final Dialog dialog = new Dialog(ShowIssues.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popuup_camragallary_design);
                dialog.setCanceledOnTouchOutside(false);

                TextView img_gallery = (TextView) dialog.findViewById(R.id.img_gallery);
                TextView img_camera = (TextView) dialog.findViewById(R.id.img_camera);
                ImageView closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
                img_removeS = (TextView) dialog.findViewById(R.id.img_remove);

                if (secondTag.equals("imageTwo")) {
                    img_removeS.setVisibility(View.GONE);
                } else {
                    img_removeS.setVisibility(View.VISIBLE);
                }
                dialog.show();
                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                img_removeS.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        clickImageSecond.setImageResource(R.drawable.camera_add);
                        dialog.dismiss();
                        secondTag = "imageTwo";
                        imagesArray.remove(1);
                    }
                });
                img_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(ShowIssues.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ShowIssues.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                            } else {

                                selectCameraImageS();
                                dialog.dismiss();
                            }
                        } else {
                            selectCameraImageS();
                            dialog.dismiss();
                        }
                    }
                });
                img_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentAPIVersion = Build.VERSION.SDK_INT;
                        if (currentAPIVersion >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(ShowIssues.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ShowIssues.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 400);
                                dialog.dismiss();
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 400);
                            dialog.dismiss();
                        }
                    }
                });


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (v == clickImageFirst) {
            try {
                final Dialog dialog = new Dialog(ShowIssues.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.popuup_camragallary_design);
                dialog.setCanceledOnTouchOutside(false);

                TextView img_gallery = (TextView) dialog.findViewById(R.id.img_gallery);
                TextView img_camera = (TextView) dialog.findViewById(R.id.img_camera);
                ImageView closePopup = (ImageView) dialog.findViewById(R.id.closePopup);
                img_removeF = (TextView) dialog.findViewById(R.id.img_remove);

                img_removeF.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        clickImageFirst.setImageResource(R.drawable.camera_add);
                        dialog.dismiss();
                        firstTag = "imageOne";
                        imagesArray.remove(0);
                    }
                });
                if (firstTag.equals("imageOne")) {
                    img_removeF.setVisibility(View.GONE);
                } else {
                    img_removeF.setVisibility(View.VISIBLE);
                }
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
                            if (ActivityCompat.checkSelfPermission(ShowIssues.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ShowIssues.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                            if (ActivityCompat.checkSelfPermission(ShowIssues.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ShowIssues.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");

                    new ImageCompressionAsyncTask(ShowIssues.this).execute(data.getData().toString(),
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

            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(ShowIssues.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }

        }
        if (requestCode == 400) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");

                    new ImageCompressionAsyncTaskS(ShowIssues.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
            }
        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUriS;

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

            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/fixpapa");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {
                    Log.e("fgnjfgb", "" + selectedImageUri.toString());
                    Log.e("fgnjfgb", "" + fileImage.getPath());
                    new ImageCompressionAsyncTaskS(ShowIssues.this).execute(selectedImageUri.toString(),
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

    class ImageCompressionAsyncTaskS extends AsyncTask<String, Void, String> {
        Context mContext;
        int imageViewSattus;

        public ImageCompressionAsyncTaskS(Context context) {
            mContext = context;
            imageViewSattus = imageViewSattus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(ShowIssues.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(ShowIssues.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                compressedImageS = new File(s);
                Uri compressUri = Uri.fromFile(compressedImageS);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ShowIssues.this.getContentResolver(), compressUri);
                    clickImageSecond.setImageBitmap(bitmap);
                    secondTag = "imageTo";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("images", s);
                        imagesArray.put(1, compressedImageS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        int imageViewSattus;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
            imageViewSattus = imageViewSattus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(ShowIssues.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(ShowIssues.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            Utility.close();
            if (s != null) {
                // strCompressedFileImage = s;
                compressedImage = new File(s);

                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ShowIssues.this.getContentResolver(), compressUri);
                    clickImageFirst.setImageBitmap(bitmap);
                    firstTag = "imageOn";
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("images", s);
                        imagesArray.put(0, compressedImage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}
