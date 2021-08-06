package com.horofbd.MeCloak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReferralActivity extends AppCompatActivity{
    static {
        System.loadLibrary("native-lib");
    }
    ImageView backbutton;

    static native String getLoginInfo(String key);

    int step = 0;

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    TextView myreferencecode,myreferral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        context = this;
        myreferencecode = findViewById(R.id.myreferencecode);
        myreferral = findViewById(R.id.myreferrals);

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        myreferencecode.setText("Your reference code is: "+getLoginInfo("phone_no"));
        myreferencecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("lebel", getLoginInfo("phone_no"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ReferralActivity.this,"Copied!",Toast.LENGTH_SHORT).show();
            }
        });
        myreferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReferralActivity.this,Referral.class));
            }
        });

    }



}