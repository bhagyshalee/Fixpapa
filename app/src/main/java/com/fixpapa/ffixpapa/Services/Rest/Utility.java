package com.fixpapa.ffixpapa.Services.Rest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.EngineerHomeScreen;
import com.fixpapa.ffixpapa.EngineerPart.MapEngineerTrack;
import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.UserPart.UserRegistration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Advosoft2 on_toggle 4/19/2018.
 */

public class Utility {
    static ProgressDialog progressDialog;
    static  Dialog dialog;
    static ProgressDialog progressDialogHome;
    public static String USER_EMAIL, USER_ID, USER_NAME, USER_MOBILE_NO, USER_GST, ACCESS_TOKEN, USER_ADDRESS, USER_STREET,
            USER_LATITUDE, USER_LONGITUDE, USER_TYPE, USER_IMAGE, LOGIN_TYPE = "";
    public static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.ENGLISH);


    public static void getUserDetailId(Context context) {

        if (context!=null){
            DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
            Cursor cursor = dataBaseHandler.getUserDetails();
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    ACCESS_TOKEN = cursor.getString(0);
                    USER_ID = cursor.getString(1);
                    USER_EMAIL = cursor.getString(2);
                    USER_NAME = cursor.getString(3);
                    USER_MOBILE_NO = cursor.getString(4);
                    USER_GST = cursor.getString(5);
                    USER_ADDRESS = cursor.getString(6);
                    USER_STREET = cursor.getString(7);
                    USER_LATITUDE = cursor.getString(8);
                    USER_LONGITUDE = cursor.getString(9);
                    USER_TYPE = cursor.getString(10);
                    USER_IMAGE = cursor.getString(11);
                    LOGIN_TYPE = cursor.getString(12);
                    cursor.moveToNext();
                }
            }
                cursor.close();
        }



        Log.e("userName", "===> " + "USER_TYPE" + USER_TYPE + "ACCESS_TOKEN" + ACCESS_TOKEN);

    }


    public static String getDateDifference(Date thenDate) {

        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);

        Log.e("gregrgdgdfg", now + " bfg " + then);
        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes < 60) {
            if (diffMinutes == 1)
                return diffMinutes + " minute ago";
            else
                if (diffMinutes==0)
                {
                    return "just Now";
                }
                else {
                    return diffMinutes + " minutes ago";
                }
        } else if (diffHours < 24) {
            if (diffHours == 1)
                return diffHours + " hour ago";
            else
                return diffHours + " hours ago";
        } else if (diffDays < 30) {
            if (diffDays == 1)
                return diffDays + " day ago";
            else
                return diffDays + " days ago";
        } else {
            return "a long time ago..";
        }
    }


    public static int isLogin(Context context) {
        int userId = 0;
        /*DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
        userId = dataBaseHandler.getRowCount();
        Log.e("userId", "===> " + userId);*/
        SharedPreferences shared = context.getSharedPreferences("LOGINVALUES", MODE_PRIVATE);
        if (shared.getBoolean("LOGIN_STATUS",false)==true){
            userId=1;
        }else {
            userId=0 ;
        }

        return userId;
    }

    public static String getDateFromUtcToday(String utcDate) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MM yyyy",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
            inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String srDate = "";
            srDate = inputFormat.format(myDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(srDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;
    }

    public static String getDateFromUtc(String utcDate) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
            inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String srDate = "";
            srDate = inputFormat.format(myDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(srDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;
    }


    public static String getDateFromUtcPurchase(String utcDate) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = outputFormat.parse(utcDate);
            //inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DATE, +1);
            String srDate = inputFormat.format(calendar.getTime());
            Date datee = null;
            try {
                datee = inputFormat.parse(srDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = String.valueOf(datee);
           // System.out.println(formattedDatee);
            Log.e("thrtyhe5y", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;
    }


    public static String getDateFromDa(String utcDate) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
            inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String srDate = "";
            srDate = inputFormat.format(myDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(srDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;

    }

    public static String getTimeFromUtc(String utcDate) {

        SimpleDateFormat outputFormatt = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(utcDate);
           // inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = inputFormat.format(myDate);
            Log.e("fdvdvdffdfgreg", "" + strDate);
            Date datee = null;
            try {
                datee = inputFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = outputFormatt.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;
    }

    public static String getTimeToUtc(String utcDate) {
        SimpleDateFormat outputFormatt = new SimpleDateFormat("yyyy-M-d hh:mm a",Locale.ENGLISH);
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            //outputFormatt.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = outputFormatt.parse(utcDate);
            outputFormatt.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = outputFormatt.format(myDate);
            Log.e("fdvdvdffdfgreg", "" + strDate);
            Date datee = null;
            try {
                datee = outputFormatt.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDatee = inputFormat.format(datee);
            System.out.println(formattedDatee);
            Log.e("nghnghn", "" + formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
         return  formattedDatee;
    }

/*
    public static String convertInLocalTime(String serverDate) throws ParseException {
        String formattedDatee="";
        try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            inputFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = inputFormat.parse(serverDate);
            inputFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            String strDate = "";
            strDate = inputFormat.format(myDate);

            SimpleDateFormat outputFormatt = new SimpleDateFormat("hh:mm a");
            Date datee = null;
            try {
                datee = inputFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
             formattedDatee = outputFormatt.format(datee);
            System.out.println(formattedDatee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  formattedDatee;
    }
*/

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
    }

   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
 /*   public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.progress_show);
       // progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.luncher_icon, null));

        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException e) {

        }

    }

    public static void close() {
        progressDialog.dismiss();
    }*/



    public static void showProgress(Context context) {
        try {

            dialog = new Dialog(context);
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(R.color.transparentW);
            dialog.setContentView(R.layout.progress_design);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //ImageView img_gallery = (ImageView) dialog.findViewById(R.id.img_logo);
            dialog.show();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void close() {
        dialog.dismiss();
    }



    /*  public static void showProgressHome(Context context) {
        progressDialogHome = new ProgressDialog(context);

        progressDialogHome.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialogHome.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
       // params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.gravity=Gravity.CENTER;
        progressDialogHome.setContentView(new ProgressBar(context),params);
        //progressDialogHome.setTitle(R.string.progress_show);
        progressDialogHome.show();
    }

    public static void closeHome() {
        progressDialogHome.dismiss();
    }
*/
    public static boolean isOnline(boolean showWarning, final Context ctx) {
        boolean connected = false;
        if (ctx!=null) {

            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    int numActiveNetwork = cm.getAllNetworks().length;

                    for (int i = 0; i < numActiveNetwork; i++) {
                        if (cm.getNetworkCapabilities(cm.getAllNetworks()[i]).hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                            connected = true;
                            return connected;
                        }
                    }
                } else {
                    connected = true;
                }
            }

            if (!connected && showWarning) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
                builder.setTitle("No connection!");
                builder.setMessage("Sorry You Are Offline!");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_no_internet);

                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Utility.isOnline(true, ctx);

                    }
                });

                builder.show();
            }
        }
        return connected;
    }

    public static final void setDatePicker(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    public static final void setDatePickerIn(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
       // dialog.DatePicker.MinDate = Java.Lang.JavaSystem.CurrentTimeMillis();
    }
    public static final void setDatePickerHidePrevious(Context context, final TextView et) {
        int mYear;
        int mMonth;
        int mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        // dialog.DatePicker.MinDate = Java.Lang.JavaSystem.CurrentTimeMillis();
    }

    public static String getLatLngFromAddress(String strAddress, Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = ((double) (location.getLatitude() * 1E6) + "" +
                    (double) (location.getLongitude() * 1E6));
            Log.e("dfvnfdv", location.getLatitude() + "");
            Log.e("dfvnfdv", location.getLongitude() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);

            if (addresses != null) {

                Address returnedAddress = addresses.get(0);

                StringBuilder strReturnedAddress = new StringBuilder("");
                strReturnedAddress.append(returnedAddress.getAddressLine(0));

               /*   for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                   strReturnedAddress
                           .append(returnedAddress.getAddressLine(i)).append(
                           ",");
               }
               */

                strAdd = strReturnedAddress.toString();

                Log.e("My Current ",
                        "" + strReturnedAddress.toString());
            } else {
                Log.e("My Current ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current ", "Canont get Address!");
        }

        return strAdd;

    }

    public static void ShowAlertDialog(final Context context)
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }




}
