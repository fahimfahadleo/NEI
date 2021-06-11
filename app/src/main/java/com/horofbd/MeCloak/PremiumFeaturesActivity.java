package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PremiumFeaturesActivity extends AppCompatActivity {
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }


    CardView tutorialview;
    TextView tutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_features);
        context = this;

        tutorialview =findViewById(R.id.tuorialview);
        tutorial =findViewById(R.id.tutorial);



        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PremiumFeaturesActivity.this,TutorialActivity.class));
            }
        });

        tutorialview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PremiumFeaturesActivity.this,TutorialActivity.class));
            }
        });


    }
}