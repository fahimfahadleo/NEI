package com.horofbd.MeCloak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();

    }

    TextView titletext;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = this;
        titletext = findViewById(R.id.titletext);
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





    }
}