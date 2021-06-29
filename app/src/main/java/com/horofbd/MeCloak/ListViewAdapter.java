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
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    protected ArrayList<JSONObject> listdata;
    protected AlertDialog dialog;
    protected TextView accept;
    protected TextView decline;
    protected CardView mutenotificationview, markasunreadview, ignoremessageview, sealview, blockview, deleteview;
    protected TextView mutenotification, markasunread, ignoremessage, seal, block, delete;

    Context context;
    int vi;

    public ListViewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
        Log.e("listview", listdata.toString());
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

            } else if (vi == R.layout.singlefriend) {
                if (myListData.getString("status").equals("2")) {
                    holder.textView.setText(myListData.getString("name"));
                    holder.phonenumber.setText("Last Contact at");

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(context, ShowUserMessage.class);

                            try {
                                i.putExtra("id",myListData.getString("id"));
                                i.putExtra("name",myListData.getString("name"));
                                i.putExtra("phone_no",myListData.getString("phone_no"));
                                i.putExtra("table_id",myListData.getString("table_id"));
                                i.putExtra("page_friend_id",myListData.getString("page_friend_id"));
                                i.putExtra("status",myListData.getString("status"));
                                i.putExtra("boundage", "0");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ((Activity) context).startActivity(i);


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
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view1 = inflater.inflate(R.layout.friendlongpressoption, null, false);
                        mutenotificationview = view1.findViewById(R.id.mutenotificationview);
                        mutenotification = view1.findViewById(R.id.mutenotification);
                        markasunreadview = view1.findViewById(R.id.markasunreadview);
                        markasunread = view1.findViewById(R.id.markasunread);
                        ignoremessageview = view1.findViewById(R.id.ignoremessageview);
                        ignoremessage = view1.findViewById(R.id.ignoremessage);
                        sealview = view1.findViewById(R.id.sealview);
                        seal = view1.findViewById(R.id.seal);
                        blockview = view1.findViewById(R.id.blockview);
                        block = view1.findViewById(R.id.block);
                        deleteview = view1.findViewById(R.id.deleteview);
                        delete = view1.findViewById(R.id.delete);

                        try {
                            String status = myListData.getString("status");
                            if(status.equals("8")){
                                seal.setText("Unseal");
                                mutenotification.setEnabled(false);
                                mutenotificationview.setEnabled(false);
                                markasunread.setEnabled(false);
                                markasunreadview.setEnabled(false);
                                ignoremessage.setEnabled(false);
                                ignoremessageview.setEnabled(false);
                                block.setEnabled(false);
                                blockview.setEnabled(false);
                                delete.setEnabled(false);
                                deleteview.setEnabled(false);
                            }else if(status.equals("6")){
                                ignoremessage.setText("Release");
                                mutenotification.setEnabled(false);
                                mutenotificationview.setEnabled(false);
                                markasunread.setEnabled(false);
                                markasunreadview.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        longPressOptions(myListData);
                        builder.setView(view1);
                        builder.setCancelable(true);
                        dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                        dialog.show();

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
        public CardView cardView;
        public TextView phonenumber;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.cardView = (CardView) itemView.findViewById(R.id.singlecardview);
            this.phonenumber = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
