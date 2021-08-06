package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WalletActivity extends AppCompatActivity {
    TextView mainbalance,referralbalance,watchvideobalance;

    static {
        System.loadLibrary("native-lib");
    }
    ImageView backbutton;

    static native String getLoginInfo(String key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        mainbalance = findViewById(R.id.mainbalance);
        referralbalance = findViewById(R.id.referralbalanec);
        watchvideobalance = findViewById(R.id.adwathcbalance);
        mainbalance.setText("Main Balance: "+getLoginInfo("recharge_balance"));
        referralbalance.setText("Referral Balance: "+getLoginInfo("referral_balance"));
        watchvideobalance.setText("Watch Video Balance: "+getLoginInfo("view_ad_point_balance"));

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mainbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WalletActivity.this,WithdrawActivity.class);
                i.putExtra("data","mainbalance");
                startActivity(i);
            }
        });
        referralbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WalletActivity.this,WithdrawActivity.class);
                i.putExtra("data","referralbalance");
                startActivity(i);
            }
        });
        watchvideobalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WalletActivity.this,WithdrawActivity.class);
                i.putExtra("data","adbalance");
                startActivity(i);
            }
        });
    }
}