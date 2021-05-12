package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

public class Animation extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);


    static Context context;
    public static void closeActivtiy(){
        ((Activity)context).finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        findViewById(R.id.animation).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fadein));

        context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               StartActivity(Animation.this,"UserVerification");
            }
        },2250);



    }
}