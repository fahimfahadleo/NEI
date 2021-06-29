package com.horofbd.MeCloak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "MeCloak.db";
    private static final String USERINFORMATION = "USERINFORMATION";
    private static final String FRIENDINFORMATION = "FRIENDINFORMATION";
    private static final String NOTIFICATION = "NOTIFICATION";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+USERINFORMATION+" "+"(id integer primary key AUTOINCREMENT, username text, userphone text, userpassword text, userid text, userref text, loginstatus text)"
        );

        db.execSQL(
                "create table "+FRIENDINFORMATION+" "+"(id INTEGER primary key AUTOINCREMENT, phonenumber text, textpass text, boundage text, timer text,pageno text)"
        );

        db.execSQL(
                "create table "+NOTIFICATION+" "+"(id INTEGER primary key AUTOINCREMENT, notificationcount text, notificationread text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+USERINFORMATION);
        db.execSQL("DROP TABLE IF EXISTS "+FRIENDINFORMATION);
        db.execSQL("DROP TABLE IF EXISTS "+NOTIFICATION);
        onCreate(db);
    }
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("delete from "+USERINFORMATION);
         db.execSQL("delete from "+FRIENDINFORMATION);
         db.execSQL("delete from "+NOTIFICATION);
         db.close();
    }
    public boolean setUserInformation(String username,String userphone,String userpass,String userid,String userref,String loginstatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("userphone",userphone);
        contentValues.put("userpass",userpass);
        contentValues.put("userid",userid);
        contentValues.put("userref",userref);
        contentValues.put("loginstatus",loginstatus);
        db.insert(USERINFORMATION,null,contentValues);
        return true;
    }
    public Cursor getUserInformation(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+USERINFORMATION, null );
    }
    public boolean UpdateUserInformation(String field,String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(USERINFORMATION,contentValues,field+" = ?",null);
        return true;
    }
    public boolean setFriendInformation(String phonenumber,String textpass, String boundage,String timer,String pageno){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phonenumber",phonenumber);
        contentValues.put("textpass",textpass);
        contentValues.put("boundage",boundage);
        contentValues.put("timer",timer);
        contentValues.put("pageno",pageno);
        db.insert(FRIENDINFORMATION, null, contentValues);
        return true;
    }


    public Cursor getData(String phonenu,String pageno) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( "SELECT * FROM "+FRIENDINFORMATION+" WHERE phonenumber=? AND pageno=?",new String[]{phonenu,pageno});
    }

    public boolean UpdateFriendInformation(String phonenumber,String field,String value,String phoneno){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(FRIENDINFORMATION,contentValues,"phonenumber=? AND pageno=?",new String[]{phonenumber,phoneno});
        return true;
    }

    public boolean deleteFriend(String phonenumber,String pageno){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FRIENDINFORMATION, "phonenumber=? AND pageno=?", new String[]{phonenumber,pageno}) > 0;
    }


    public void setNotificationInformation(String notification,String notificationread){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notificationcount",notification);
        contentValues.put("notificationread",notificationread);
        db.insert(NOTIFICATION, null, contentValues);
    }

    public boolean UpdateNotification(String field,String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(NOTIFICATION,contentValues,field+" = ?",null);
        return true;
    }

    public Cursor getNotificationData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( "SELECT * FROM "+NOTIFICATION+"",null);
    }


}
