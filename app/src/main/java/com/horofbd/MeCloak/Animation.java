package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class Animation extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }

    static native JSONArray getData(String method);

    static native void StartActivity(Context context, String activity);

    static native void LoginRequest(Context context, ServerResponse serverResponse, String phone, String passwrod, int requestcode);

    static native void InitLinks();

    static native void SaveLogindata(String response, Context context);

    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();

    }

    JSONArray jsonArray;
    boolean isAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ImageView iv = findViewById(R.id.animation);
        new Functions(this);
        InitLinks();

        android.view.animation.Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                jsonArray = getData("gui");
                Log.e("gui", jsonArray.toString());
                if (jsonArray.length() != 0) {
                    if (Functions.isInternetAvailable()) {
                        try {
                            isAvailable = true;
                            if (jsonArray.getJSONObject(0).getString("loginstatus").equals("true")) {
                                LoginRequest(Animation.this, Animation.this, jsonArray.getJSONObject(0).getString("userphone"), jsonArray.getJSONObject(0).getString("userpass"), 1);
                                Functions.dismissDialogue();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isValid){
                            StartActivity(Animation.this, "Login");
                        }

                    }
                },10000);
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (!isAvailable) {
                    StartActivity(Animation.this, "Login");
                }
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        iv.setAnimation(animation);


        context = this;
    }

    @Override
    protected void onDestroy() {
        isValid = true;
        super.onDestroy();
    }

    boolean isValid = false;
    @Override
    public void onResponse(String response, int responsecode, int requestcode) throws JSONException {
        Log.e("responsqqqqe", response);
        if (requestcode == 1) {
            SaveLogindata(response, this);
            Functions.isActive = false;


        }
    }

    @Override
    public void onFailure(String failresponse) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Animation.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }

}