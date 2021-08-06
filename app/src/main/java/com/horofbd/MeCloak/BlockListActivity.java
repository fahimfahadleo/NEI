package com.horofbd.MeCloak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class BlockListActivity extends AppCompatActivity implements ServerResponse{
    static {
        System.loadLibrary("native-lib");
    }

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);



    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();

    }
    static RecyclerView friendlist;
    static ListviewAdapter adapter;
    static TextView totalfriendcount;
    static ServerResponse serverResponse;

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


                friendlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!recyclerView.canScrollVertically(1) && dy > 0) {
                            if(!nextpageurl.equals("")){
                                globalRequest(serverResponse, "GET", nextpageurl, new JSONObject(), 22, context);
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

                totalfriendcount.setText("Total " + listdata.size() + " Friends");
            }
        });

    }

    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);
        context = this;

        friendlist = findViewById(R.id.friendlistrecyclerview);
        totalfriendcount = findViewById(R.id.totalfriends);
        InitLinks();
        serverResponse = this;

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        globalRequest(BlockListActivity.this,"GET",Important.getBlock_friend_list(),new JSONObject(),31,BlockListActivity.this);


    }



    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    static int pageposition;
    static void setRecyclerviewposition(){
        friendlist.postDelayed(new Runnable() {
            @Override
            public void run() {
                friendlist.getLayoutManager().scrollToPosition(pageposition);
            }
        },50);
    }

    static String nextpageurl = "";
    public static void setNextpageurl(String next){
        nextpageurl =next;
    }

    static class ListviewAdapter extends FriendListAdapter implements ImageResponse {
        ArrayList<JSONObject> mylistdata;

        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
            super(listdata, context);
            mylistdata = listdata;

        }

        @Override
        protected void ImageSetUp(CircleImageView imageView, String id) {
            Log.e("blockedid",id);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("friend", id);
                ImageRequest(this, imageView, "GET", Important.getViewprofilepicture(), jsonObject, 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        @Override
        public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
            Log.e("called","called");
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ((Activity) context).runOnUiThread(() -> {
                if (bitmap != null) {
                    Log.e("bitmap", "notnull");
                    Log.e("bitmap", bitmap.toString());
                    imageView.setImageBitmap(bitmap);

                } else {
                    Log.e("bitmap", "null");
                }
            });
        }

        @Override
        public void onImageFailure(String failresponse) throws JSONException {

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
            globalRequest(serverResponse, "POST", Important.getUnfriend_friend(), jsonObject, 14,context);
            friendlist.setAdapter(null);
            globalRequest(serverResponse,"GET",Important.getBlock_friend_list(),new JSONObject(),31,context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static void blockRequest(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", id);
            globalRequest(serverResponse, "POST", Important.getBlock_friend(), jsonObject, 7,context);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static void ignoreRequest(String pagefriendid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_friend_id", pagefriendid);

            Log.e("pagefriendid", pagefriendid);
            globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject, 15,context);
            friendlist.setAdapter(null);
            globalRequest(serverResponse,"GET",Important.getBlock_friend_list(),new JSONObject(),31,context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static void reportRequest(String id) {

    }
}