package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public class LoginActivity extends AppCompatActivity implements ServerResponse {


    EditText phone, password;
    TextView proceed, forgotpassword;
    TextView register, donthaveaccount;

    static {
        System.loadLibrary("native-lib");
    }





    static native void LoginRequest(Context context, ServerResponse serverResponse, String phone, String passwrod, int requestcode);

    static native void SaveLogindata(String response, Context context);

    static native String GetLoginInfo(String key);

    static native void StartActivity(Context context, String classname);

    static native JSONObject getData(String method);

    static native void InitLinks();


    @Override
    protected void onPause() {
        super.onPause();
        Functions.dismissDialogue();
    }
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        proceed = findViewById(R.id.proceedbutton);
        register = findViewById(R.id.register);
        donthaveaccount = findViewById(R.id.donthaveaccount);
        forgotpassword = findViewById(R.id.forgotpassword);
        new Functions(this);
        context = this;

        InitLinks();

        jsonObject = getData("gui");
        Log.e("gui",jsonObject.toString());
        if(jsonObject.length()!=0){
            if(Functions.isInternetAvailable()) {
                try {
                    if (jsonObject.getString("loginstatus").equals("true")) {
                        LoginRequest(this,this,jsonObject.getString("userphone"),jsonObject.getString("userpass"),1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//        AppCompatDelegate
//                .setDefaultNightMode(
//                        AppCompatDelegate
//                                .MODE_NIGHT_NO);


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(LoginActivity.this, "ForgotProfilePassword");
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonestr = phone.getText().toString();
                String passwordstr = password.getText().toString();

                if (TextUtils.isEmpty(phonestr)) {
                    phone.setError("Phone can not be empty!");
                    phone.requestFocus();
                } else if (TextUtils.isEmpty(passwordstr)) {
                    password.setError("Phone can not be empty!");
                    password.requestFocus();
                } else {
                    if (!phonestr.contains("+88")) {
                        phonestr = "+88" + phonestr;
                    }

                    if(Functions.isInternetAvailable()){
                        Log.e("internet","available");
                        LoginRequest(LoginActivity.this, LoginActivity.this, phonestr, passwordstr, 1);
                    }else {
                        Log.e("internet","not available");
                        if(jsonObject.length()!=0){
                            try {
                                if(jsonObject.getString("userphone").equals(phonestr) && jsonObject.getString("userpass").equals(passwordstr)){
                                    startActivity(new Intent(LoginActivity.this,OfflineChatList.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(LoginActivity.this, "Register");
            }
        });


    }

    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }


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
                Toast.makeText(LoginActivity.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }


}