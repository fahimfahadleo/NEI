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
import android.widget.ImageView;

public class Animation extends AppCompatActivity{
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
       ImageView iv =  findViewById(R.id.animation);

        android.view.animation.Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                StartActivity(Animation.this,"Login");
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        iv.setAnimation(animation);



        context = this;



    }


}