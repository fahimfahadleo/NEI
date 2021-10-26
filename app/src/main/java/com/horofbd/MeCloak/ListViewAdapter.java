package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    protected ArrayList<JSONObject> listdata;
    protected AlertDialog dialog;
    protected TextView accept;
    protected TextView decline;
    protected TextView mutenotification, markasunread, ignoremessage, seal, block, delete;
    protected AlertDialog longpressdialog;

    Context context;
    int vi;

    public ListViewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
//        Log.e("listview", listdata.toString());
        this.listdata = listdata;
        this.context = context;
        this.vi = view;
    }

    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(vi, parent, false);
        return new ListViewAdapter.ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NotNull ListViewAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);

        try {

            if (vi == R.layout.showsinglefriendrequest) {
                holder.textView.setText(myListData.getString("name") + "(" + myListData.getString("phone_no") + ")");
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initDialogue(listdata.get(position));
                    }
                });

                imageViewSetUp(myListData.getString("id"), holder.imageView);
            } else if (vi == R.layout.singlefriend) {

                String status = myListData.getString("status");
                switch (status) {
                    case "2": {
                        holder.status.setVisibility(View.GONE);
                        break;
                    }
                    case "8": {
                        holder.status.setVisibility(View.VISIBLE);
                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.sealed));
                        break;
                    }
                    case "6": {
                        holder.status.setVisibility(View.VISIBLE);
                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.igonre));
                        break;
                    }
                    case "9": {
                        holder.status.setVisibility(View.VISIBLE);
                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.mutenotification));
                        break;
                    }
                }

                if (myListData.has("isNew")) {
                    if (myListData.getString("isNew").equals("false")) {
                        holder.textView.setTextColor(context.getResources().getColor(R.color.forgotpasswordcolor));
                        holder.phonenumber.setTextColor(context.getResources().getColor(R.color.forgotpasswordcolor));
                    } else if (myListData.getString("isNew").equals("true")) {
                        holder.textView.setTextColor(context.getResources().getColor(R.color.textcolor));
                        holder.phonenumber.setTextColor(context.getResources().getColor(R.color.textcolor));
                    } else {
                        holder.textView.setTextColor(context.getResources().getColor(R.color.forgotpasswordcolor));
                        holder.phonenumber.setTextColor(context.getResources().getColor(R.color.forgotpasswordcolor));
                    }
                }


                if (status.equals("2") || status.equals("6") || status.equals("9")) {
                    holder.textView.setText(myListData.getString("name"));
                    if (myListData.has("timestamp")) {
                        long time = Long.parseLong(myListData.getString("timestamp"));
                        holder.phonenumber.setText(MainActivity.getTag(time));

                        Log.e("consolelog","timerfunctioncall");


                        Timer t = new Timer();
                        t.scheduleAtFixedRate(new TimerTask() {
                                       @Override
                                       public void run() {

                                           Log.e("consolelog","timerfunctioncall111111111111111111");


                                           ((Activity) context).runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   try {


                                                       String time = myListData.getString("timestamp");
                                                       Log.e("time", time);

                                                       long timeinmins = Long.parseLong(time);

                                                       holder.phonenumber.setText(MainActivity.getTag(timeinmins));
                                                       Log.e("timeintag", MainActivity.getTag(timeinmins));

                                                       timeinmins++;
                                                       myListData.put("timestamp", timeinmins);

                                                   } catch (JSONException e) {
                                                       e.printStackTrace();
                                                   }
                                               }
                                           });
                                       }
                                   },
                                0, 1000 * 60);


                    }


                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (myListData.has("isNew")) {
                                try {
                                    if (myListData.getString("isNew").equals("true")) {
                                        myListData.put("isNew", "false");
                                    } else {
                                        myListData.put("isNew", "true");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            notifyDataSetChanged();


                            if (MainActivity.connection != null && MainActivity.connection.isConnected() && MainActivity.connection.isAuthenticated()) {
                                Intent i = new Intent(context, ShowUserMessage.class);
                                try {
                                    i.putExtra("id", myListData.getString("id"));
                                    i.putExtra("name", myListData.getString("name"));
                                    i.putExtra("phone_no", myListData.getString("phone_no"));
                                    i.putExtra("table_id", myListData.getString("table_id"));
                                    i.putExtra("page_friend_id", myListData.getString("page_friend_id"));
                                    i.putExtra("status", myListData.getString("status"));
                                    i.putExtra("boundage", "0");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ((Activity) context).startActivity(i);
                            }
                        }
                    });
                    imageViewSetUp(myListData.getString("id"), holder.imageView);

                } else if (myListData.getString("status").equals("8")) {
                    holder.textView.setText("MeCloak User");
                    holder.phonenumber.setText("Sealed");
                    imageViewSetUp("id", holder.imageView);
                }
                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        Log.e("totaldata", myListData.toString());

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view1 = inflater.inflate(R.layout.friendlongpressoption, null, false);
                        mutenotification = view1.findViewById(R.id.mutenotification);

                        markasunread = view1.findViewById(R.id.markasunread);
                        ignoremessage = view1.findViewById(R.id.ignoremessage);
                        seal = view1.findViewById(R.id.seal);
                        block = view1.findViewById(R.id.block);
                        delete = view1.findViewById(R.id.delete);

                        try {
                            String status = myListData.getString("status");
                            if (status.equals("8")) {
                                seal.setText("Unseal");
                                mutenotification.setEnabled(false);
                                markasunread.setEnabled(false);
                                ignoremessage.setEnabled(false);
                                block.setEnabled(false);
                            } else if (status.equals("6")) {
                                ignoremessage.setText("Release");
                                mutenotification.setEnabled(false);
                                markasunread.setEnabled(false);
                            } else if (status.equals("9")) {
                                mutenotification.setText("Unmute");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        longPressOptions(myListData);
                        builder.setView(view1);
                        builder.setCancelable(true);
                        longpressdialog = builder.create();
                        longpressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                        longpressdialog.show();

                        return true;
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected abstract void imageViewSetUp(String id, CircleImageView imageView);

    protected abstract void longPressOptions(JSONObject jsonObject);


    protected JSONObject mylistdata;

    private void initDialogue(JSONObject myList) {
        mylistdata = myList;
        Log.e("mylist", myList.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View VI = inflater.inflate(R.layout.acceptdeclinedialogue, null, false);
        builder.setView(VI);
        TextView name = VI.findViewById(R.id.username);
        TextView phonenumber = VI.findViewById(R.id.userphone);
        CircleImageView profilepic = VI.findViewById(R.id.proflepic);
        accept = VI.findViewById(R.id.accpetbutton);
        decline = VI.findViewById(R.id.declinebutton);
        try {
            name.setText(myList.getString("name"));
            phonenumber.setText(myList.getString("phone_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            imageViewSetUp(myList.getString("id"), profilepic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogue();
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#80030A25")));

        dialog.show();
    }


    public abstract void showDialogue();


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView textView;
        public LinearLayout cardView;
        public TextView phonenumber;
        public ImageView audiocall;
        public ImageView videocall;
        public ImageView status;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.cardView = itemView.findViewById(R.id.singlecardview);
            this.phonenumber = (TextView) itemView.findViewById(R.id.text);
            this.audiocall = itemView.findViewById(R.id.audiocall);
            this.videocall = itemView.findViewById(R.id.videocall);
            this.status = itemView.findViewById(R.id.status);
        }
    }
}
