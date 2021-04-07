package com.horofbd.nei;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Functions {
    Context context;
    static SharedPreferences preferences;
    public static boolean isActive = false;
    public Functions(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("MessagePreference", MODE_PRIVATE);
    }

    public static void setSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();// commit is important here.
    }

    public static String getSharedPreference(String key, String defaultvalue) {
        return preferences.getString(key, defaultvalue);
    }



}
