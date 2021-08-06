package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RechargeActivity extends AppCompatActivity implements ServerResponse {
    EditText amount;
    TextView submit;
    CircleImageView month1, month2, month6;
    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    String amountstr;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        amount = findViewById(R.id.amount);
        submit = findViewById(R.id.submitbutton);
        month1 = findViewById(R.id.month1);
        month2 = findViewById(R.id.month2);
        month6 = findViewById(R.id.month6);
        context = this;

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.BuyPremiumfromRecharge(RechargeActivity.this, "30", "recharge", 1);
            }
        });

        month2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.BuyPremiumfromRecharge(RechargeActivity.this, "60", "recharge", 2);

            }
        });

        month6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.BuyPremiumfromRecharge(RechargeActivity.this, "180", "recharge", 3);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountstr = amount.getText().toString();
                ServerRequest.Recharge(RechargeActivity.this, amountstr, 4);
            }
        });
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("recharge", response);
        if (requestcode == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RechargeActivity.this, "Premium Subscription Successful for 1 month.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (requestcode == 2) {
            if (requestcode == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "Premium Subscription Successful for 2 months.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (requestcode == 3) {
            if (requestcode == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "Premium Subscription Successful for 6 month.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        if (requestcode == 4) {
            if (requestcode == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "Recharged Taka " + amountstr + " successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RechargeActivity.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }
}