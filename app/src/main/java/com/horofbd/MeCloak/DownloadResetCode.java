package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DownloadResetCode extends AppCompatActivity {
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }
    ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_reset_code);
        context = this;

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}