package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
        NotificationActivity.globalRequest(serverResponse, "GET", Important.getRetrive_request_list(), new JSONObject(), 9);
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
            super(listdata, context,R.layout.showsinglefriendrequest);

        }

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
                        NotificationActivity.globalRequest(serverResponse, "POST", Important.getAccept_friend(), jsonObject, 10);
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
                        NotificationActivity.globalRequest(serverResponse, "POST", Important.getCancel_friend_request(), jsonObject, 11);
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