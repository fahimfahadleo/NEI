package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPageSecurityQuestionActivity extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }


    static ServerResponse serverResponse;


    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static Context context;
    Intent i;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page_security_question);
        context = this;
        InitLinks();
        i = getIntent();
        serverResponse = this;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password_confirmation",i.getStringExtra("pass"));
            globalRequest(this,"POST",Important.getViewansweredsecurityquestions(),jsonObject,28,this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("questionresponse",response);
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}