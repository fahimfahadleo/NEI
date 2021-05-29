package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity implements ServerResponse {
    static Context context;
    public static void closeActivtiy(){
        ((Activity)context).finish();
    }

    static RecyclerView friendlist;
    static ListviewAdapter adapter;
    static TextView totalfriendcount;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        context = this;
        friendlist = findViewById(R.id.friendlistrecyclerview);
        totalfriendcount = findViewById(R.id.totalfriends);
        InitLinks();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_id",getLoginInfo("page_id"));
            globalRequest(this,"POST",Important.getFriend_list(),jsonObject,13);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }

    public static void setUpData(ArrayList<JSONObject> listdata) {
        Log.e("mainlist",listdata.toString());
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("listviewdata", listdata.toString());
                adapter = new ListviewAdapter(listdata, context,R.layout.singlefriend);
                friendlist.setHasFixedSize(true);
                friendlist.setLayoutManager(new LinearLayoutManager(context));
                friendlist.setAdapter(adapter);
                totalfriendcount.setText("Total "+listdata.size()+" Friends");
            }
        });

    }

    static class ListviewAdapter extends FriendListAdapter{
        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
            super(listdata, context);
        }
        @Override
        public void showDialogue() {
        }
    }
}