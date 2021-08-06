package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotProfilePassword extends AppCompatActivity implements ServerResponse{
    static {
        System.loadLibrary("native-lib");
    }

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);
    static native void InitLinks();
    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    ImageView backbutton;
    EditText phone;
    TextView submitbutton;

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_profile_password);
        context = this;
        phone = findViewById(R.id.phone);
        submitbutton = findViewById(R.id.proceedbutton);
        InitLinks();
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone_no",phone.getText().toString());

                    jsonObject.put("flag1","0");
                    Log.e("link",Important.getForgot_profile_password());
                    globalRequest(ForgotProfilePassword.this,"POST",Important.getForgot_profile_password(),jsonObject,25,ForgotProfilePassword.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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