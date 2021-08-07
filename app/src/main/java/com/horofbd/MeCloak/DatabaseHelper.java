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
    private static final String OfflineChatList = "OFFLINECHATLIST";


    static SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+USERINFORMATION+" "+"(id integer primary key AUTOINCREMENT, username text, userphone text, userpass text, userid text, userref text, loginstatus text,ispremium text)"
        );

        db.execSQL(
                "create table "+FRIENDINFORMATION+" "+"(id INTEGER primary key AUTOINCREMENT, phonenumber text, textpass text, boundage text, timer text,pageno text)"
        );

        db.execSQL(
                "create table "+NOTIFICATION+" "+"(id INTEGER primary key AUTOINCREMENT, notificationcount text, notificationread text)"
        );

        db.execSQL(
                "create table "+OfflineChatList+" "+"(id INTEGER primary key AUTOINCREMENT, number text, isignored text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+USERINFORMATION);
        db.execSQL("DROP TABLE IF EXISTS "+FRIENDINFORMATION);
        db.execSQL("DROP TABLE IF EXISTS "+NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS "+OfflineChatList);
        onCreate(db);
    }
    public static void deleteAllData(){

         db.execSQL("delete from "+USERINFORMATION);
         db.execSQL("delete from "+FRIENDINFORMATION);
         db.execSQL("delete from "+NOTIFICATION);
         db.execSQL("delete from "+OfflineChatList);
         db.close();
    }
    public static boolean setUserInformation(String username, String userphone, String userpass, String userid, String userref, String loginstatus,String ispremium){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("userphone",userphone);
        contentValues.put("userpass",userpass);
        contentValues.put("userid",userid);
        contentValues.put("userref",userref);
        contentValues.put("loginstatus",loginstatus);
        contentValues.put("ispremium",ispremium);
        db.insert(USERINFORMATION,null,contentValues);
        return true;
    }
    public static Cursor getUserInformation(){
        return db.rawQuery( "select * from "+USERINFORMATION, null );
    }
    public static boolean updateUserInformation(String field,String value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(USERINFORMATION,contentValues,field+" = ?",null);
        return true;
    }
    public static boolean setFriendInformation(String phonenumber,String textpass, String boundage,String timer,String pageno){
        ContentValues contentValues = new ContentValues();
        contentValues.put("phonenumber",phonenumber);
        contentValues.put("textpass",textpass);
        contentValues.put("boundage",boundage);
        contentValues.put("timer",timer);
        contentValues.put("pageno",pageno);
        db.insert(FRIENDINFORMATION, null, contentValues);
        return true;
    }


    public static Cursor getData(String phonenu,String pageno) {
        return db.rawQuery( "SELECT * FROM "+FRIENDINFORMATION+" WHERE phonenumber=? AND pageno=?",new String[]{phonenu,pageno});
    }

    public static boolean updateFriendInformation(String phonenumber,String field,String value,String phoneno){
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(FRIENDINFORMATION,contentValues,"phonenumber=? AND pageno=?",new String[]{phonenumber,phoneno});
        return true;
    }

    public static boolean deleteFriend(String phonenumber,String pageno){
        return db.delete(FRIENDINFORMATION, "phonenumber=? AND pageno=?", new String[]{phonenumber,pageno}) > 0;
    }


    public static void setNotificationInformation(String notification,String notificationread){
        ContentValues contentValues = new ContentValues();
        contentValues.put("notificationcount",notification);
        contentValues.put("notificationread",notificationread);
        db.insert(NOTIFICATION, null, contentValues);
    }

    public static boolean updateNotification(String field,String value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(NOTIFICATION,contentValues,field+" = ?",null);
        return true;
    }

    public static Cursor getNotificationData(){
        return db.rawQuery( "SELECT * FROM "+NOTIFICATION+"",null);
    }

    public static void setOfflineChatListnInformation(String number,String isignored){
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("isignored",isignored);
        db.insert(OfflineChatList, null, contentValues);
    }

    public static boolean updateOfflineChatList(String position,String field,String value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(field,value);
        db.update(OfflineChatList,contentValues,"number=? AND "+field+"=?",new String[]{position,field});
        return true;
    }

    public static boolean deleteofflinefriend(String number){
        return db.delete(OfflineChatList, "number=?", new String[]{number}) > 0;
    }

    public static Cursor getOfflineChatList(){
        return db.rawQuery( "SELECT * FROM "+OfflineChatList+"",null);
    }


}
