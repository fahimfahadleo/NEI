package com.horofbd.MeCloak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoggedInDevices extends AppCompatActivity implements ServerResponse{
    static {
        System.loadLibrary("native-lib");
    }


    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static Context context;
    static RecyclerView recyclerView;
    static ServerResponse serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_devices);
        recyclerView = findViewById(R.id.loggedinrecyclerview);
        serverResponse = this;
        context = this;
        globalRequest(LoggedInDevices.this,"GET",Important.getLogininformation(),new JSONObject(),35,context);
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    ImageView backbutton;
    static LoggedInDeviceAdapter adapter;
    public static void viewLoggedinDevices(ArrayList<JSONObject> list){
        Log.e("mainlist", list.toString());
        ((Activity) context).runOnUiThread(() -> {
            Log.e("listviewdata", list.toString());
            adapter = new LoggedInDeviceAdapter(list, context);
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
                            globalRequest(serverResponse, "GET", nextpageurl, new JSONObject(), 35, context);
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

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }

    static class LoggedInDeviceAdapter extends RecyclerView.Adapter<LoggedInDeviceAdapter.ViewHolder>{
        ArrayList<JSONObject> listdata;
        Context context;

        public LoggedInDeviceAdapter(ArrayList<JSONObject> listdata, Context context) {
            Log.e("listview", listdata.toString());
            this.listdata = listdata;
            this.context = context;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.singleloggedindevice, parent, false);
            return new LoggedInDeviceAdapter.ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JSONObject jsonObject = listdata.get(position);
            try {
                holder.devicename.setText(jsonObject.getString("device_name"));
                holder.loggedintime.setText(jsonObject.getString("created_at"));
                //holder.devicename.setText(jsonObject.getString("device_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView devicename;
            public TextView loggedintime;
            public CircleImageView DeviceImage;
            public ViewHolder(View itemView) {
                super(itemView);
                this.DeviceImage = itemView.findViewById(R.id.deviceimage);
                this.devicename = itemView.findViewById(R.id.devicename);
                this.loggedintime = itemView.findViewById(R.id.logintime);
            }
        }
    }

}