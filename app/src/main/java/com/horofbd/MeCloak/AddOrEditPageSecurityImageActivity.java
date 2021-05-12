package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class AddOrEditPageSecurityImageActivity extends AppCompatActivity {
    static Context context;
    public static void closeActivtiy(){
        ((Activity)context).finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_page_security_image);
        context = this;
    }
}