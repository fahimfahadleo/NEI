package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetPremiumActivity extends AppCompatActivity {
//    ExpandableTextView expandableTextView,expandableTextView2,expandableTextView3;
//    CircleImageView wv,ref;
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }
//
//    CardView cardView,premiumcongrats;
//    LinearLayout hiddenView,hiddenview2;
//    ImageButton arrow,arrow2;

    CardView premiumfeaturesview;
    TextView premiumfeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_premium);
//        expandableTextView = findViewById(R.id.expandabletextview);
//        expandableTextView2 = findViewById(R.id.expandabletextview2);
//        expandableTextView3 = findViewById(R.id.expandabletextview3);
//        recharge = findViewById(R.id.recharge);
//        wv = findViewById(R.id.watchvideo);
//        ref = findViewById(R.id.referfriend);

        premiumfeatures = findViewById(R.id.premiumfeatures);
        premiumfeaturesview = findViewById(R.id.premiumfeaturesview);

        premiumfeaturesview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetPremiumActivity.this,PremiumFeaturesActivity.class));
            }
        });

        premiumfeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetPremiumActivity.this,PremiumFeaturesActivity.class));
            }
        });



        context = this;

//        recharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(GetPremiumActivity.this,RechargeActivity.class));
//            }
//        });

        String rechargetext = "বিকাশ,রকেট বা নগদের মাধ্যমে আপনার মি-সিকিওর এপে মাত্র ২০ রিচার্জ করে উপভোগ করুন ১ মাস প্রিমিয়াম ফিচার। আমাদের বিকাশ মার্চেন্ট একাওন্টে ২০ টাকা পেমেন্ট করে ট্রাঞ্জেক্সান আইডি সাবমিট করে জিতে নিন প্রিমিয়াম সুবিধা। ";

        String watchvideo = "মাত্র ২০ টি ভিডিও দেখে আমাদের প্রিমিয়াম সেবা উপভোগ করুন ১ মাসের জন্য। উল্লেখ্য যে প্রতিদিন আপনি সর্বচ্চ একটি ভিডিও দেখতে পারবেন।";

        String referfriend = "মাত্র ৪ জন বন্ধুকে এপ টি ব্যবহারের জন্য রেফার করে ১ মাসের জন্য ফ্রি প্রিমিয়াম মেম্বারশিপ জিতে নিন। নিয়ম কানুন বিস্তারিত :- আপনার রেফার কোডটি জেনে নিন আপনার প্রফাইল থেকে। আপনার ফোন নাম্বারটি আপনার রেফার কোড। আপনার বন্ধুকে বলুন এপ টি প্লে স্টোর থেকে ডাওনলোড করতে। রেজিস্টেসান করার সময় রেফার কোডে আপনার ফোন ব্যবহার করলে আপনি পাচ্ছেন ৫ ক্রেডিট। এভাবে ২০ ক্রেডিট জমিয়ে আপনি কিনে নিতে পারেন নিজের প্রিমিয়ার। বা ১০০ ক্রেডিট জমিয়ে তা ক্যাশ আউট করে নিতে পারেন বিকাশ রকেট বা নগদ থেকে।";


//        expandableTextView.setText(rechargetext);
//        expandableTextView2.setText(watchvideo);
//        expandableTextView3.setText(referfriend);






//
//        cardView = findViewById(R.id.gopremiumview);
//        arrow = findViewById(R.id.arrow_button);
//        hiddenView = findViewById(R.id.hiddenview);
//
//        arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // If the CardView is already expanded, set its visibility
//                //  to gone and change the expand less icon to expand more.
//                if (hiddenView.getVisibility() == View.VISIBLE) {
//
//                    // The transition of the hiddenView is carried out
//                    //  by the TransitionManager class.
//                    // Here we use an object of the AutoTransition
//                    // Class to create a default transition.
//                    TransitionManager.beginDelayedTransition(cardView,
//                            new AutoTransition());
//                    hiddenView.setVisibility(View.GONE);
//                    arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
//                }
//
//                // If the CardView is not expanded, set its visibility
//                // to visible and change the expand more icon to expand less.
//                else {
//
//                    TransitionManager.beginDelayedTransition(cardView,
//                            new AutoTransition());
//                    hiddenView.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
//                }
//            }
//        });

//        premiumcongrats = findViewById(R.id.premiumcongratsview);
//        arrow2 = findViewById(R.id.arrow_button2);
//        hiddenview2 = findViewById(R.id.hiddenview2);
//
//        arrow2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // If the CardView is already expanded, set its visibility
//                //  to gone and change the expand less icon to expand more.
//                if (hiddenview2.getVisibility() == View.VISIBLE) {
//
//                    // The transition of the hiddenView is carried out
//                    //  by the TransitionManager class.
//                    // Here we use an object of the AutoTransition
//                    // Class to create a default transition.
//                    TransitionManager.beginDelayedTransition(premiumcongrats,
//                            new AutoTransition());
//                    hiddenview2.setVisibility(View.GONE);
//                    arrow2.setImageResource(R.drawable.ic_baseline_expand_more_24);
//                }
//
//                // If the CardView is not expanded, set its visibility
//                // to visible and change the expand more icon to expand less.
//                else {
//
//                    TransitionManager.beginDelayedTransition(premiumcongrats,
//                            new AutoTransition());
//                    hiddenview2.setVisibility(View.VISIBLE);
//                    arrow2.setImageResource(R.drawable.ic_baseline_expand_less_24);
//                }
//            }
//        });

    }
}