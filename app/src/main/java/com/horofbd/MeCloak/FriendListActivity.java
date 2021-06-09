package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListActivity extends AppCompatActivity implements ServerResponse {
    static Context context;

    public static void closeActivtiy() {
        ((Activity) context).finish();
    }

    static RecyclerView friendlist;
    static ListviewAdapter adapter;
    static TextView totalfriendcount;
    static ServerResponse serverResponse;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity, String data);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static void RequestFriendList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_id", getLoginInfo("page_id"));
            globalRequest(serverResponse, "POST", Important.getFriend_list(), jsonObject, 13);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        context = this;
        friendlist = findViewById(R.id.friendlistrecyclerview);
        totalfriendcount = findViewById(R.id.totalfriends);
        InitLinks();
        serverResponse = this;

        RequestFriendList();


    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }

    public static void setUpData(ArrayList<JSONObject> listdata) {
        Log.e("mainlist", listdata.toString());
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("listviewdata", listdata.toString());
                adapter = new ListviewAdapter(listdata, context, R.layout.singlefriend);
                friendlist.setHasFixedSize(true);
                friendlist.setLayoutManager(new LinearLayoutManager(context));
                friendlist.setAdapter(adapter);
                totalfriendcount.setText("Total " + listdata.size() + " Friends");
            }
        });

    }

    static class ListviewAdapter extends FriendListAdapter {
        ArrayList<JSONObject> mylistdata;

        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
            super(listdata, context);
            mylistdata = listdata;
        }

        @Override
        public void SetUpOnClick(int position) {
            viewprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProfileDialogue(mylistdata.get(position));
                }
            });
            viewprofileview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProfileDialogue(mylistdata.get(position));
                }
            });
            sendmessageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        StartActivity(context, "Inbox", mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        StartActivity(context, "Inbox", mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            unfriendview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        unfriendRequest(mylistdata.get(position).getString("table_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            unfriend1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        unfriendRequest(mylistdata.get(position).getString("table_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            ignoreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ignoreRequest(mylistdata.get(position).getString("page_friend_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ignoreRequest(mylistdata.get(position).getString("page_friend_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            blockview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        blockRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        blockRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            reportuserview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        reportRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            reportuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        reportRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void showDialogue(int position) {
            unfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        unfriendRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            blockfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        blockRequest(mylistdata.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    static AlertDialog dialog;

    static void showProfileDialogue(JSONObject mylistdata) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.singlefriendprofile, null, false);
        builder.setView(VI);
        TextView titletext;
        ImageView closeButton;
        TextView name = VI.findViewById(R.id.username);
        TextView phonenumber = VI.findViewById(R.id.userphone);
        CircleImageView profilepic = VI.findViewById(R.id.proflepic);
        TextView unfriend = VI.findViewById(R.id.unfriend);
        TextView blockfriend = VI.findViewById(R.id.blockfriend);
        titletext = VI.findViewById(R.id.titletext);
        closeButton = VI.findViewById(R.id.closedialogue);
        titletext.setText("Friend Profile");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        try {
            name.setText(mylistdata.getString("name"));
            phonenumber.setText(mylistdata.getString("phone_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#80030A25")));
        dialog.show();
    }

    static void unfriendRequest(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend_id", id);
            globalRequest(serverResponse, "POST", Important.getUnfriend_friend(), jsonObject, 14);
            friendlist.setAdapter(null);
            RequestFriendList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static void blockRequest(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", id);
            globalRequest(serverResponse, "POST", Important.getBlock_friend(), jsonObject, 7);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static void ignoreRequest(String pagefriendid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_friend_id", pagefriendid);

            Log.e("pagefriendid", pagefriendid);
            globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject, 15);
            friendlist.setAdapter(null);
            RequestFriendList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static void reportRequest(String id) {

    }
}