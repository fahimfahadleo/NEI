package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UserVerificationActivity extends AppCompatActivity implements ServerResponse{

    EditText code;
    TextView proceedbutton;
    DatabaseHelper helper;
    TextView registernewpage;
    String userpass;
    EditText autotext;
    LinearLayout passwordfieldlayout;
    TextView initialize;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);
    static native void InitLinks();
    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);


    int point = 0;

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 40) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        registernewpage = findViewById(R.id.registernewpage);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);
        autotext = findViewById(R.id.autotext);
        passwordfieldlayout = findViewById(R.id.passwordfieldlayout);
        initialize = findViewById(R.id.initialize);

        passwordfieldlayout.setVisibility(View.VISIBLE);


        String s = "~+8801914616453@MeCloak> auth status\n" +
                "~+8801914616453@MeCloak> connected to Fahim Fahad Leon\n" +
                "~+8801914616453@MeCloak> requesting session\n" +
                "~+8801914616453@MeCloak> accepted with ("+getSaltString()+")\n" +
                "~+8801914616453@MeCloak> generating request id\n" +
                "~+8801914616453@MeCloak> successful!!\n" +
                "~+8801914616453@MeCloak> loading horoftech crypto\n" +
                "~+8801914616453@MeCloak> ... ok\n" +
                "~+8801914616453@MeCloak> loading MeCloak premium icon\n" +
                "~+8801914616453@MeCloak> failed/... ok\n" +
                "~+8801914616453@MeCloak> loading boundage policy\n" +
                "~+8801914616453@MeCloak> ... ok\n" +
                "~+8801914616453@MeCloak> loading modules\n" +
                "~+8801914616453@MeCloak> https ... ok\n" +
                "~+8801914616453@MeCloak> wss ... ok\n" +
                "~+8801914616453@MeCloak> LDAP ... ok\n" +
                "~+8801914616453@MeCloak> SqLite ... ok\n" +
                "~+8801914616453@MeCloak> XML ... ok\n" +
                "~+8801914616453@MeCloak> bash ... ok\n" +
                "~+8801914616453@MeCloak> loading certificates ... ok\n" +
                "~+8801914616453@MeCloak> establishing cryptographic structure ... ok\n" +
                "~+8801914616453@MeCloak> starting MeCloak\n";
        String[] chararray = s.split("\n");


        int length = chararray.length-1;


        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                autotext.setCursorVisible(true);
                                autotext.setText(autotext.getText().toString()+"\n"+chararray[point]);
                                autotext.setSelection(autotext.getText().length());

                                if(point == length){
                                    t.cancel();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    autotext.setVisibility(View.GONE);
                                                    passwordfieldlayout.setVisibility(View.VISIBLE);
                                                    initialize.setText("Page Login");
                                                }
                                            });
                                        }
                                    },1000);
                                }
                                point++;
                            }
                        });
                    }
                },
                0,      // run first occurrence immediatetly
                200); // run every two seconds
        
        InitLinks();
        new Functions(this);


        registernewpage = findViewById(R.id.registernewpage);

        registernewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(UserVerificationActivity.this,"RegisterVerifiedUser");
            }
        });

//        Cursor c = helper.getUserPassword(1);
//        if (c.moveToFirst()){
//             userpass = c.getString(1);
//
//        }
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = code.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password",pass);
                    globalRequest(UserVerificationActivity.this,"POST",Important.getPage_login(),jsonObject,4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(pass.equals(userpass)){
//                    ServerRequest.PageLogin(UserVerificationActivity.this,pass,1);
//                }
            }
        });
      //  c.close();

        context = this;
    }
    String pass;
    static Context context;

    public static void closeActivtiy() {
        ((Activity) context).finish();
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("responsestr",response);
        CheckResponse(this,this,response,4);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}