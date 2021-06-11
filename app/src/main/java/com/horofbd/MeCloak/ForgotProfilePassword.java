package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForgotProfilePassword extends AppCompatActivity {
    LinearLayout toolbar;
    ImageView backbutton;
    ImageView menu;
    TextView titleview;
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_profile_password);
        toolbar = findViewById(R.id.toolbar);
        backbutton = toolbar.findViewById(R.id.backbutton);
        context = this;
        menu = toolbar.findViewById(R.id.options);
        titleview = toolbar.findViewById(R.id.title);
        backbutton.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        titleview.setText("Forgot Password");



    }
}