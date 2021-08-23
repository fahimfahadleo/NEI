package com.horofbd.MeCloak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfflineChatList extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }

    static native void loaddata(String [] value);
    static native void updateloaddata(String position,String field,String value);
    static native JSONArray getData();
    static native void deleteFriend(String phonenumber);


    ImageView search;
    LinearLayout searchlayout;
    boolean isopen = false;
    static RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView newmessage;
    String number;
    public static ArrayList<JSONObject> chatlist;
    public static boolean isChatListActive = false;

    @Override
    protected void onStart() {
        super.onStart();
        isChatListActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChatListActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChatListActive = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = null;
            if (data != null) {
                contactUri = data.getData();
            }
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = this.getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                number = cursor.getString(numberIndex);
                // Do something with the phone number
                Log.e("phone number", number);
                if(number.contains(" ")){
                    number = number.replaceAll(" ","");
                }
                if(!number.contains("+88")){
                    number = "+88"+number;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("number",number);
                    jsonObject.put("isignored","false");
                    chatlist.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loaddata(new String[]{number,"false"});

                updateChatlist();

            }

            if (cursor != null) {
                cursor.close();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void updateChatlist(){
       chatlist = new ArrayList<>();
        JSONArray jsonArray = getData();
        Log.e("JSOnObject",jsonArray.toString());
        for(int i=0;i<jsonArray.length();i++){
            try {
                chatlist.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new offlinechatlistadapter(chatlist, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    offlinechatlistadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chat_list);
        search = findViewById(R.id.search);
        searchlayout = findViewById(R.id.searchlayout);
        searchlayout.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        newmessage = findViewById(R.id.newmessage);
        chatlist = new ArrayList<>();


        JSONArray jsonArray = getData();
        Log.e("JSOnObject",jsonArray.toString());
        for(int i=0;i<jsonArray.length();i++){
            try {
                chatlist.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


         adapter = new offlinechatlistadapter(chatlist,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);





        newmessage.setOnClickListener(view -> {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 2);


        });


        search.setOnClickListener(view -> {
            if (isopen) {
                TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                TransitionManager.beginDelayedTransition(recyclerView, new AutoTransition());
                searchlayout.setVisibility(View.GONE);
                isopen = false;
            } else {
                TransitionManager.beginDelayedTransition(recyclerView, new AutoTransition());
                TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                searchlayout.setVisibility(View.VISIBLE);
                isopen = true;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            recyclerView.setAdapter(null);
            updateChatlist();
            swipeRefreshLayout.setRefreshing(false);
        }, 2000));

    }
    class offlinechatlistadapter extends  RecyclerView.Adapter<offlinechatlistadapter.ViewHolder>{
        ArrayList<JSONObject> listdata;
        Context context;


        public offlinechatlistadapter(ArrayList<JSONObject> listdata, Context context) {
            Log.e("listview", listdata.toString());
            this.listdata = listdata;
            this.context = context;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.singlefriendlistfirend, parent, false);
            return new offlinechatlistadapter.ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final JSONObject data = listdata.get(position);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.phonenumber.setVisibility(View.GONE);


            try {
                holder.textView.setText(data.getString("number"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(OfflineChatList.this,OfflineActivity.class);
                    try {
                        i.putExtra("data",data.getString("number"));
                        i.putExtra("boundage","0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);
                }
            });


            holder.information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view1 = inflater.inflate(R.layout.offlinefriendoptions, null, false);
                    TextView sendmessage = view1.findViewById(R.id.sendmessage);
                    TextView remove = view1.findViewById(R.id.remove);
                    TextView ignore = view1.findViewById(R.id.ignore);
                    sendmessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i =new Intent(context,OfflineActivity.class);
                            try {
                                i.putExtra("data",data.getString("number"));
                                i.putExtra("boundage","0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(i);
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                deleteFriend(data.getString("number"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    ignore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                updateloaddata(data.getString("number"),"isignored","true");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });



                    builder.setView(view1);
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView imageView,activestatus;
            public TextView textView;
            public CardView cardView;
            public TextView phonenumber;
            public RelativeLayout relativeLayout;
            public TextView information;

            public ViewHolder(View itemView) {
                super(itemView);
//inv
                this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
                //inv
                this.activestatus = (CircleImageView) itemView.findViewById(R.id.activestatus);
                this.relativeLayout = itemView.findViewById(R.id.relaitvelayout);

                this.textView = (TextView) itemView.findViewById(R.id.name);
                this.cardView = (CardView) itemView.findViewById(R.id.singlecardview);
                //inv
                this.phonenumber = (TextView) itemView.findViewById(R.id.mutualfirend);
                this.information = (TextView) itemView.findViewById(R.id.options);

            }
        }
    }
}