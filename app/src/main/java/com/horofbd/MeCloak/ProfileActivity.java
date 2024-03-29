package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements ServerResponse,ImageResponse {

    TextView premium, mainbalancetext, refbalance,accountcreationtime,accountupdatetime,friendlist;
    ImageView toggolmainstartopen, toggolmainopen, toggolrefstartopen, toggolrefopen;
    LinearLayout friendlistlayout;
    RelativeLayout rootview, rootview2;
    CircleImageView profile_image;
    TextView username;
    TextView userphonenumber,referral,wallet;
    TextView mystuff;



    public static final int ANIMATION_SPEED = 750;
    boolean isMainbalanceopened = false;
    boolean isRefbalanceopened = false;
    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }


    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity,String data);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode,Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        premium = findViewById(R.id.premiumstatus);
        toggolmainopen = findViewById(R.id.toggolmainopen);
        toggolmainstartopen = findViewById(R.id.toggolmainstartopen);
        toggolrefstartopen = findViewById(R.id.toggolrefstartopen);
        toggolrefopen = findViewById(R.id.toggolrefopen);
        toggolmainopen.setVisibility(View.INVISIBLE);
        mainbalancetext = findViewById(R.id.mainbalancetext);
        rootview = findViewById(R.id.rootview);
        refbalance = findViewById(R.id.refbalance);
        rootview2 = findViewById(R.id.rootview2);
        toggolrefopen.setVisibility(View.INVISIBLE);
        accountcreationtime = findViewById(R.id.accountcreatetime);
        accountupdatetime = findViewById(R.id.accountupdatetime);
        friendlist = findViewById(R.id.friendlist);
        friendlistlayout = findViewById(R.id.firendlistlayout);
        username = findViewById(R.id.username);
        userphonenumber = findViewById(R.id.userphonenumber);
        profile_image = findViewById(R.id.profile_image);
        mystuff = findViewById(R.id.mystuff);
        referral = findViewById(R.id.referral);
        wallet = findViewById(R.id.wallet);
        InitLinks();

        ImageRequest(this, profile_image, "GET", Important.getViewprofilepicture(), new JSONObject(), 1);



        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,WalletActivity.class));
            }
        });

        if(getLoginInfo("premium").equals("Null")){
            premium.setText("Go Premium");
        }else {
            //set Premium Data

            premium.setText("Set Premium Data");
        }

        referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(ProfileActivity.this,"Referral","");
            }
        });

        mystuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(ProfileActivity.this,"MyStuff","");
            }
        });
        userphonenumber.setText(getLoginInfo("phone_no"));
        username.setText(getLoginInfo("user_name"));

        friendlistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(ProfileActivity.this,"FriendList","");
            }
        });

        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(ProfileActivity.this,"FriendList","");
            }
        });


        accountcreationtime.setText(getLoginInfo("created_at"));
        accountupdatetime.setText(getLoginInfo("updated_at"));
        context = this;


        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMainbalanceopened) {
                    maincloseanimation();
                } else {
                    mainopenanimation();
                }

            }
        });


        toggolmainopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maincloseanimation();
            }
        });
        toggolmainstartopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainopenanimation();
            }
        });

        rootview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRefbalanceopened) {
                    refcloseanimation();
                } else {
                    refopenanimation();
                }
            }
        });

        toggolrefstartopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refopenanimation();
            }
        });

        toggolrefopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refcloseanimation();
            }
        });

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, GetPremiumActivity.class));
            }
        });
    }


    public static AnimatorSet getViewToViewScalingAnimator(final RelativeLayout parentView,
                                                           final View viewToAnimate,
                                                           final Rect fromViewRect,
                                                           final Rect toViewRect,
                                                           final long duration,
                                                           final long startDelay) {
        // get all coordinates at once
        final Rect parentViewRect = new Rect(), viewToAnimateRect = new Rect();
        parentView.getGlobalVisibleRect(parentViewRect);
        viewToAnimate.getGlobalVisibleRect(viewToAnimateRect);

        viewToAnimate.setScaleX(1f);
        viewToAnimate.setScaleY(1f);

        // rescaling of the object on X-axis
        final ValueAnimator valueAnimatorWidth = ValueAnimator.ofInt(fromViewRect.width(), toViewRect.width());
        valueAnimatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newWidth = (int) valueAnimatorWidth.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();

                lp.width = newWidth;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // rescaling of the object on Y-axis
        final ValueAnimator valueAnimatorHeight = ValueAnimator.ofInt(fromViewRect.height(), toViewRect.height());
        valueAnimatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newHeight = (int) valueAnimatorHeight.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
                lp.height = newHeight;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // moving of the object on X-axis
        ObjectAnimator translateAnimatorX = ObjectAnimator.ofFloat(viewToAnimate, "X", fromViewRect.left - parentViewRect.left, toViewRect.left - parentViewRect.left);

        // moving of the object on Y-axis
        ObjectAnimator translateAnimatorY = ObjectAnimator.ofFloat(viewToAnimate, "Y", fromViewRect.top - parentViewRect.top, toViewRect.top - parentViewRect.top);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1f));
        animatorSet.setDuration(duration); // can be decoupled for each animator separately
        animatorSet.setStartDelay(startDelay); // can be decoupled for each animator separately
        animatorSet.playTogether(valueAnimatorWidth, valueAnimatorHeight, translateAnimatorX, translateAnimatorY);

        return animatorSet;
    }

    void mainopenanimation() {
        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        toggolmainstartopen.getGlobalVisibleRect(fromRect);
        toggolmainopen.getGlobalVisibleRect(toRect);

        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootview, toggolmainopen, fromRect, toRect, ANIMATION_SPEED, 0);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toggolmainopen.setVisibility(View.VISIBLE);
                toggolmainstartopen.setVisibility(View.INVISIBLE);
                mainbalancetext.setVisibility(View.INVISIBLE);
                mainbalancetext.setGravity(GravityCompat.START);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toggolmainopen.setVisibility(View.VISIBLE);
                toggolmainstartopen.setVisibility(View.INVISIBLE);
                mainbalancetext.setVisibility(View.VISIBLE);
                mainbalancetext.setText(getLoginInfo("recharge_balance"));
                mainbalancetext.setGravity(GravityCompat.START);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        isMainbalanceopened = true;
        animatorSet.start();
    }

    void maincloseanimation() {
        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        toggolmainopen.getGlobalVisibleRect(fromRect);
        toggolmainstartopen.getGlobalVisibleRect(toRect);

        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootview, toggolmainstartopen, fromRect, toRect, ANIMATION_SPEED, 0);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toggolmainstartopen.setVisibility(View.VISIBLE);
                toggolmainopen.setVisibility(View.INVISIBLE);
                mainbalancetext.setVisibility(View.INVISIBLE);
                mainbalancetext.setGravity(GravityCompat.END);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toggolmainstartopen.setVisibility(View.VISIBLE);
                toggolmainopen.setVisibility(View.INVISIBLE);
                mainbalancetext.setText("Main Balance");
                mainbalancetext.setVisibility(View.VISIBLE);
                mainbalancetext.setGravity(GravityCompat.END);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        isMainbalanceopened = false;

    }

    void refopenanimation() {
        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        toggolrefstartopen.getGlobalVisibleRect(fromRect);
        toggolrefopen.getGlobalVisibleRect(toRect);

        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootview2, toggolrefopen, fromRect, toRect, ANIMATION_SPEED, 0);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toggolrefopen.setVisibility(View.VISIBLE);
                toggolrefstartopen.setVisibility(View.INVISIBLE);
                refbalance.setVisibility(View.INVISIBLE);
                refbalance.setGravity(GravityCompat.START);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toggolrefopen.setVisibility(View.VISIBLE);
                toggolrefstartopen.setVisibility(View.INVISIBLE);
                refbalance.setVisibility(View.VISIBLE);
                refbalance.setText(getLoginInfo("referral_balance"));
                refbalance.setGravity(GravityCompat.START);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        isRefbalanceopened = true;
    }

    void refcloseanimation() {
        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        toggolrefopen.getGlobalVisibleRect(fromRect);
        toggolrefstartopen.getGlobalVisibleRect(toRect);

        AnimatorSet animatorSet = getViewToViewScalingAnimator(rootview2, toggolrefstartopen, fromRect, toRect, ANIMATION_SPEED, 0);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toggolrefstartopen.setVisibility(View.VISIBLE);
                toggolrefopen.setVisibility(View.INVISIBLE);
                refbalance.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toggolrefstartopen.setVisibility(View.VISIBLE);
                toggolrefopen.setVisibility(View.INVISIBLE);
                refbalance.setText("Ref. Balance");
                refbalance.setVisibility(View.VISIBLE);
                refbalance.setGravity(GravityCompat.END);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        isRefbalanceopened = false;
    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProfileActivity.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }

    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    Log.e("bitmap", "notnull");
                    setImage(imageView, bitmap);
                } else {
                    Log.e("bitmap", "null");
                }
            }
        });
    }

    @Override
    public void onImageFailure(String failresponse) throws JSONException {

    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    Bitmap bitmap;
}