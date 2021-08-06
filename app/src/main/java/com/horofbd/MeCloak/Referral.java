package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Referral extends AppCompatActivity implements ServerResponse{

    static {
        System.loadLibrary("native-lib");
    }
    static RecyclerView recyclerView;

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);
    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);
   static ServerResponse serverResponse;
   static Context context;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral2);
        recyclerView = findViewById(R.id.myreferralrecyclerview);
        context = this;
        InitLinks();
        serverResponse = this;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",getLoginInfo("user_id"));
            globalRequest(this,"POST",Important.getReferrals(),jsonObject,32,this);
            ids.add(getLoginInfo("user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    static referralAdapter adapter;

    public static void setUpReferralData(ArrayList<JSONObject> listdata){
        Log.e("mainlist", listdata.toString());
        ((Activity) context).runOnUiThread(() -> {
            Log.e("listviewdata", listdata.toString());
            adapter = new ReferralAdapter(listdata, context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            Functions.dismissDialogue();

        });

    }



    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    static class ReferralAdapter extends referralAdapter{
        public ReferralAdapter(ArrayList<JSONObject> listdata, Context context) {
            super(listdata, context);
        }

        @Override
        public void performClick(String id) {
            requestReferral(id);
        }
    }


    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    static int position=0;
    static ArrayList<String> ids = new ArrayList<>();

    static void requestReferral(String id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            globalRequest(serverResponse,"POST",Important.getReferrals(),jsonObject,32, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(null);
        ids.add(id);
        position++;
    }

    @Override
    public void onBackPressed() {
        if(position!=0){
            requestReferral(ids.get(position-1));
            ids.remove(position);
            position = position-2;
        }else {
            super.onBackPressed();
        }

    }
}