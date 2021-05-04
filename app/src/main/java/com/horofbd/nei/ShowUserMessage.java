package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

public class ShowUserMessage extends AppCompatActivity implements ServerResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_message);

        //ServerRequest.AddorSendNewMessage(this,"message",getIntent().getStringExtra("phone"),"1",1);


    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("showmessage",response);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}