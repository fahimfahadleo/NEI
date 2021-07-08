package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactRequest extends Fragment {
    static Context context;
    static ServerResponse serverResponse;

    public ContactRequest(Context context, ServerResponse serverResponse) {
        ContactRequest.context = context;
        ContactRequest.serverResponse = serverResponse;
    }


    static RecyclerView recyclerView;
    static ListviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fragment_contact_request, container, false);
        recyclerView = vi.findViewById(R.id.recyclerview);
        NotificationActivity.globalRequest(serverResponse, "GET", Important.getRetrive_request_list(), new JSONObject(), 9,context);
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
                                NotificationActivity.globalRequest(serverResponse, "GET", nextpageurl, new JSONObject(), 9, context);
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
            super(listdata, context,R.layout.showsinglefriendrequest);

        }


        @Override
        protected void imageViewSetUp(String id, CircleImageView imageView) {

        }

        @Override
        protected void longPressOptions(JSONObject jsonObject) {}

        @Override
        public void showDialogue() {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("page_id", NotificationActivity.getLoginInfo("page_id"));
                        jsonObject.put("friend_id", mylistdata.getString("table_id"));
                        Log.e("data", jsonObject.toString());
                        NotificationActivity.globalRequest(serverResponse, "POST", Important.getAccept_friend(), jsonObject, 10,context);
                        listdata.remove(mylistdata);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("page_id", NotificationActivity.getLoginInfo("page_id"));
                        jsonObject.put("friend_id", mylistdata.getString("table_id"));
                        NotificationActivity.globalRequest(serverResponse, "POST", Important.getCancel_friend_request(), jsonObject, 11,context);
                        listdata.remove(mylistdata);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}