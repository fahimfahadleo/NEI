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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfflineChatList extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }

    static native void loaddata(String [] value);
    static native void updateloaddata(String field,String value);
    static native JSONObject getData();


    ImageView search;
    LinearLayout searchlayout;
    boolean isopen = false;
    static RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView newmessage;
    String number;
    ArrayList<String> chatlist;

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
                chatlist.add(number);
                loaddata(new String[]{number,"false"});

            }

            if (cursor != null) {
                cursor.close();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void updateChatlist(){

    }



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


            swipeRefreshLayout.setRefreshing(false);
        }, 2000));

    }
    class offlinechatlistadapter extends  RecyclerView.Adapter<offlinechatlistadapter.ViewHolder>{
        ArrayList<String> listdata;
        Context context;


        public offlinechatlistadapter(ArrayList<String> listdata, Context context) {
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
            final String data = listdata.get(position);


                holder.textView.setText(data);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(OfflineChatList.this,OfflineActivity.class);
                    i.putExtra("data",data);
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
                            i.putExtra("data",data);
                            startActivity(i);
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    ignore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

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
            public CircleImageView imageView;
            public TextView textView;
            public CardView cardView;
            public TextView phonenumber;
            public TextView information;

            public ViewHolder(View itemView) {
                super(itemView);

                this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
                this.textView = (TextView) itemView.findViewById(R.id.name);
                this.cardView = (CardView) itemView.findViewById(R.id.singlecardview);
                this.phonenumber = (TextView) itemView.findViewById(R.id.text);
                this.information = (TextView) itemView.findViewById(R.id.options);

            }
        }
    }
}