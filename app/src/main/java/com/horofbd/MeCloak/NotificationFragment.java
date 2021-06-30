package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationFragment extends Fragment {
    static Context context;
    static ServerResponse serverResponse;

    public NotificationFragment(Context context,ServerResponse serverResponse) {
        NotificationFragment.context = context;
        this.serverResponse = serverResponse;
    }

    static RecyclerView recyclerView;
    static ListviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_notification,container,false);



        recyclerView = vi.findViewById(R.id.recyclerview);
        NotificationActivity.globalRequest(serverResponse, "GET", Important.getGetNotification(), new JSONObject(), 22,context);

        return vi;
    }


    public static void setUpData(ArrayList<JSONObject> listdata) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("listviewdata", listdata.toString());
                adapter = new ListviewAdapter(listdata, context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }
        });

    }



    static class ListviewAdapter extends ListViewAdapter {
        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context) {
            super(listdata, context,R.layout.notificationlayout);

        }

        @Override
        protected void imageViewSetUp(String id, CircleImageView imageView) {
            switch (id){
                case "alert":{
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.appicon));
                    break;
                } case "new_friend_request":{
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.person));
                    break;
                } case "friend_request_accepted":
                case "success": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.done));
                    break;
                } case "expiry":
                case "message_recall": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.close));
                    break;
                } case "offers":{
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.offer));
                    break;
                } case "recharge":{
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.money));
                    break;
                }
            }
        }

        @Override
        protected void longPressOptions(JSONObject jsonObject) {}

        @Override
        public void showDialogue() {
        }
    }


}