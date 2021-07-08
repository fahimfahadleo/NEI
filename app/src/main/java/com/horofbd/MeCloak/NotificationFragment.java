package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationFragment extends Fragment {
    static Context context;
    static ServerResponse serverResponse;
    static ArrayList<JSONObject> mylistdata;

    public NotificationFragment(Context context, ServerResponse serverResponse) {
        NotificationFragment.context = context;
        this.serverResponse = serverResponse;
    }

    static RecyclerView recyclerView;
    static ListviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_notification, container, false);

        mylistdata = new ArrayList<>();


        recyclerView = vi.findViewById(R.id.recyclerview);
        NotificationActivity.globalRequest(serverResponse, "GET", Important.getGetNotification(), new JSONObject(), 22, context);

        return vi;
    }


    public static void setUpData(ArrayList<JSONObject> listdata) {
        for (int i = 0; i < listdata.size(); i++) {
            if (!mylistdata.contains(listdata.get(i))) {
                mylistdata.add(listdata.get(i));
            }
        }


        for (int i = 0; i < mylistdata.size(); i++) {
            Log.e("data1" + i, mylistdata.get(i).toString());
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter = new ListviewAdapter(mylistdata, context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);


                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!recyclerView.canScrollVertically(1) && dy > 0) {
                            if(!nextpageurl.equals("")){
                                NotificationActivity.globalRequest(serverResponse, "GET", nextpageurl, new JSONObject(), 22, context);
                                nextpageurl = "";
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                                 pageposition = linearLayoutManager.findFirstVisibleItemPosition();

                            }else {
                                Toast.makeText(context, "Reached Bottom", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                adapter.notifyDataSetChanged();
                setRecyclerviewposition();
                Functions.dismissDialogue();
            }
        });

    }
   static int pageposition;
    static void setRecyclerviewposition(){
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.getLayoutManager().scrollToPosition(pageposition);
            }
        },50);
    }

    static String nextpageurl = "";
    public static void setNextpageurl(String next){
        nextpageurl =next;
    }

    static class ListviewAdapter extends ListViewAdapter {
        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context) {
            super(listdata, context, R.layout.notificationlayout);

        }

        @Override
        protected void imageViewSetUp(String id, CircleImageView imageView) {
            switch (id) {
                case "alert": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.appicon));
                    break;
                }
                case "new_friend_request": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.person));
                    break;
                }
                case "friend_request_accepted":
                case "success": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.done));
                    break;
                }
                case "expiry":
                case "message_recall": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.close));
                    break;
                }
                case "offers": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.offer));
                    break;
                }
                case "recharge": {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.money));
                    break;
                }
            }
        }

        @Override
        protected void longPressOptions(JSONObject jsonObject) {
        }

        @Override
        public void showDialogue() {
        }
    }


}