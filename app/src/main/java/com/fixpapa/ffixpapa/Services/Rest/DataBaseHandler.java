package com.fixpapa.ffixpapa.Services.Rest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fixpapa.ffixpapa.BuildConfig;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DataBaseHandler.class.getSimpleName();
    public SQLiteDatabase sqliteDB;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Fixpapa";

    // Login table name
    public static final String TABLE_USER = "user_table";
    public static final String NEW_TABLE = "new_table";



    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG,"Create Database");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_USER+"(Id INTEGER PRIMARY KEY,user_access_token TEXT,user_id TEXT,user_email TEXT" +
                ",user_name TEXT,user_mobile TEXT,user_gst TEXT,user_address TEXT,user_street TEXT,user_latitude REAL,user_longitude REAL,user_type TEXT,user_image TEXT,login_type TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+NEW_TABLE+"(Id INTEGER PRIMARY KEY,userid TEXT,username TEXT,image TEXT)");

        Log.e(TAG, "Tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+NEW_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String user_access_token,String user_id, String user_email, String user_name, String user_mobile
            , String user_gst, String user_address, String user_street, double user_latitude, double user_longitude, String user_type,String user_image,String login_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_access_token", user_access_token);
        values.put("user_id", user_id);
        values.put("user_email", user_email);
        values.put("user_name", user_name);
        values.put("user_mobile", user_mobile);
        values.put("user_gst", user_gst);
        values.put("user_address", user_address);
        values.put("user_street", user_street);
        values.put("user_latitude", user_latitude);
        values.put("user_longitude", user_longitude);
        values.put("user_type", user_type);
        values.put("user_image", user_image);
        values.put("login_type", login_type);

        // Inserting Row
        Log.e("ListItemValues",values.toString());
        db.insert(TABLE_USER,null,values);
        db.close();
    }

    public void addSearchDataInsert(String userid, String username, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userid", userid);
        values.put("username", username);
        values.put("image", image);

        // Inserting Row
        Log.e("List Item insert Values",values.toString());
        db.insert(NEW_TABLE,null,values);
        db.close();
    }



    public void addSearchDataUpdate(String user_id, String user_name, String user_gst,String user_image,String user_mobile,String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        values.put("user_name", user_name);
        values.put("user_gst", user_gst);
        values.put("user_image", user_image);
        values.put("user_mobile", user_mobile);
        values.put("user_email", user_email);

        // Inserting Row
        Log.e("List Item UPDATE Values",values.toString());
        db.update(TABLE_USER, values, "user_id = '" + user_id + "'", null);
        db.close();
    }

    public void UpdateEmailMobile(String user_id,String user_email, String user_mobile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        values.put("user_email", user_email);
        values.put("user_mobile", user_mobile);

        // Inserting Row
        Log.e("List Item UPDATE Values",values.toString());
        db.update(TABLE_USER, values, "user_id = '" + user_id + "'", null);
        db.close();
    }



    public boolean CheckIsDataAlreadyInDBorNot(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from "+NEW_TABLE+" where userid = '"+userid+"'" ;
        Cursor cursor = db.rawQuery(Query, null); // add the String your
        // searching by here

        boolean hasObject = false;
        if (cursor.getCount() > 0) {
            hasObject = true;
            cursor.close(); // Don't forget to close your cursor
        }

        cursor.close();
        return hasObject;
    }

    public Cursor getUserDetails(){
        Cursor cursor = getWritableDatabase().query(TABLE_USER,new String[]{"user_access_token","user_id","user_email","user_name","user_mobile"
        ,"user_gst" ,"user_address" ,"user_street" ,"user_latitude" ,"user_longitude" ,"user_type","user_image","login_type"}, BuildConfig.FLAVOR,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor getDetails(){
        Cursor cursor = getWritableDatabase().query(NEW_TABLE,new String[]{"userid","username","image"}, BuildConfig.FLAVOR,null,null,null,null);
        Log.e("get Values","======>");
        cursor.moveToFirst();
        return cursor;
    }

    //---deletes a particular title---
    public boolean deleteTitle(String userid) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(NEW_TABLE, "userid = " + userid, null) > 0;
    }



    public void resetTables(String tablename) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(tablename, null, null);

    }

    public int getRowCount(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_USER,null);
        int rowCount = cursor.getCount();
        database.close();
        cursor.close();
        return rowCount;
    }



    public void open() {
        this.sqliteDB = getWritableDatabase();
        this.sqliteDB = getReadableDatabase();
    }

    public void close() {
        if (this.sqliteDB != null) {
            this.sqliteDB.close();
        }
    }

}
