 package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class ForgotPagePassword extends AppCompatActivity {
    static Context context;
    public static void closeActivtiy(){
        ((Activity)context).finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_page_password);
        context = this;
    }
}