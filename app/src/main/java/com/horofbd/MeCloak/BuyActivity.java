package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyActivity extends AppCompatActivity implements ServerResponse {

    static {
        System.loadLibrary("native-lib");
    }

    static native String getLoginInfo(String key);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);
    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);
    static native void InitLinks();



    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();

    }
    TextView recharge,buynow,buyusingreferralbalance,buyusingvideobalance,texttutorial,videotutorial;
    int i = 1;
    int cost = 30;

    void showBuyDialog(String balance){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.buypremiumlayout,null,false);
        TextView buy = vi.findViewById(R.id.buy);
        TextView availablebalance = vi.findViewById(R.id.availablebalance);
        TextView price = vi.findViewById(R.id.premiumprice);
        TextView period = vi.findViewById(R.id.period);
        ImageView forword = vi.findViewById(R.id.more);
        ImageView backword = vi.findViewById(R.id.less);

        availablebalance.setText("Your current account balance is: "+balance);

        period.setText(i+" month");
        price.setText("Price: "+cost*i+" taka");
        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if(i>1){
                    period.setText(i+" months");
                }else {
                    period.setText(i+ " month");
                }
                price.setText(cost*i+" taka");
            }
        });
        backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                if(i<0){
                    i=0;
                }

                if(i>1){
                    period.setText(i+" months");
                }else {
                    period.setText(i+ " month");
                }
                price.setText(cost*i+" taka");
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("method","recharge");
                    jsonObject.put("credit_amount",price.getText().toString().split(" ")[0]);
                    jsonObject.put("method_id","1");
                    globalRequest(BuyActivity.this,"POST",Important.getBuy_premium(),jsonObject,38,BuyActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setView(vi);
        dialog = builder.create();
        dialog.show();

    }
    ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        context = this;
        recharge = findViewById(R.id.recharge);
        buynow = findViewById(R.id.buy);
        buyusingreferralbalance = findViewById(R.id.usereferralbalance);
        buyusingvideobalance = findViewById(R.id.usemainbalance);
        videotutorial = findViewById(R.id.videotutorial);
        texttutorial = findViewById(R.id.texttutorial);

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBuyDialog(getLoginInfo("recharge_balance"));
            }
        });

        buyusingreferralbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBuyDialog(getLoginInfo("referral_balance"));
            }
        });

        buyusingvideobalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBuyDialog(getLoginInfo("view_ad_point_balance"));
            }
        });

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}