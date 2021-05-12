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
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView premium, mainbalancetext, refbalance;
    ImageView toggolmainstartopen, toggolmainopen, toggolrefstartopen, toggolrefopen;
    RelativeLayout rootview, rootview2;


    public static final int ANIMATION_SPEED = 750;
    boolean isMainbalanceopened = false;
    boolean isRefbalanceopened = false;
    static Context context;
    public static void closeActivtiy(){
        ((Activity)context).finish();
    }

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
        context= this;


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
                if(isRefbalanceopened){
                    refcloseanimation();
                }else {
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
                mainbalancetext.setText("0.00 BDT");
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

    void refopenanimation(){
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
                refbalance.setText("0.00 BDT");
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

    void refcloseanimation(){
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


}