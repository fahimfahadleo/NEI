package com.horofbd.nei;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "NEIMESSENGER.db";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table APP_PASSWORD " + "(user_id integer primary key, user_password text)"
        );

        db.execSQL(
                "create table USER_INFORMATION " + "(phone BIGINT primary key, back text, textcolor text, chathead text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS APP_PASSWORD");
        db.execSQL("DROP TABLE IF EXISTS USER_INFORMATION");
        onCreate(db);
    }


    public boolean setUserPassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_password",password);
        db.insert("APP_PASSWORD",null,contentValues);
        return true;
    }
    public Cursor getUserPassword(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from APP_PASSWORD where user_id= "+id+"", null );
    }

    public boolean updateUserPassword(int id,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_password",password);
        db.update("APP_PASSWORD",contentValues,"user_id = "+ id,null);
        return true;
    }



    public boolean InsertUserInformation(String phone,String name, String referral,String theme){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("name",name);
        contentValues.put("referral",referral);
        contentValues.put("theme",theme);
        db.insert("USER_INFORMATION", null, contentValues);
        return true;
    }


    public boolean UpdateUserInformation(String phone,String bg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("back",bg);
        db.update("background",contentValues,"phone = ? ", new String[] {phone});
        return true;
    }




    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from password where id="+id+"", null );
    }



}
