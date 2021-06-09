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
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    protected ArrayList<JSONObject> listdata;
    protected AlertDialog dialog;
    protected TextView unfriend;
    protected TextView blockfriend;
    protected CardView viewprofileview;
    protected TextView viewprofile;
    protected CardView sendmessageview;
    protected TextView sendmessage;
    protected CardView unfriendview;
    protected TextView unfriend1;
    protected CardView ignoreview;
    protected TextView ignore;
    protected CardView blockview;
    protected TextView block;
    protected CardView reportuserview;
    protected TextView reportuser;

    Context context;


    public FriendListAdapter(ArrayList<JSONObject> listdata, Context context) {
        Log.e("listview", listdata.toString());
        this.listdata = listdata;
        this.context = context;

    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.singlefriendlistfirend, parent, false);
        return new FriendListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FriendListAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);
        try {

            holder.textView.setText(myListData.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogue(listdata.get(position),position);
            }
        });

        holder.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.singlefriendoptions, null, false);

                viewprofileview = view1.findViewById(R.id.viewprofileview);
                viewprofile = view1.findViewById(R.id.viewprofile);
                sendmessageview = view1.findViewById(R.id.sendmessageview);
                sendmessage = view1.findViewById(R.id.sendmessage);
                unfriendview = view1.findViewById(R.id.unfriendview);
                unfriend1 = view1.findViewById(R.id.unfriend);
                ignoreview = view1.findViewById(R.id.ignoreview);
                ignore = view1.findViewById(R.id.ignore);
                blockview = view1.findViewById(R.id.blockview);
                block = view1.findViewById(R.id.block);
                reportuserview = view1.findViewById(R.id.reportuserview);
                reportuser = view1.findViewById(R.id.report);
                SetUpOnClick(position);
                builder.setView(view1);
                builder.setCancelable(true);
                dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                dialog.show();
            }
        });
//        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//                View view1 = inflater.inflate(R.layout.friendlongpressoption, null, false);
//                CardView mutenotificationview = view1.findViewById(R.id.mutenotificationview);
//                TextView mutenotification = view1.findViewById(R.id.mutenotification);
//                CardView markasunreadview = view1.findViewById(R.id.markasunreadview);
//                TextView markasunread = view1.findViewById(R.id.markasunread);
//                CardView ignoremessageview = view1.findViewById(R.id.ignoremessageview);
//                TextView ignoremessage = view1.findViewById(R.id.ignoremessage);
//                CardView sealview = view1.findViewById(R.id.sealview);
//                TextView seal = view1.findViewById(R.id.seal);
//                CardView blockview = view1.findViewById(R.id.blockview);
//                TextView block = view1.findViewById(R.id.block);
//                CardView deleteview = view1.findViewById(R.id.deleteview);
//                TextView delete = view1.findViewById(R.id.delete);
////                Animation animation = AnimationUtils.loadAnimation(context,R.anim.fadein);
////                view1.setAnimation(animation);
////                view1.startAnimation(animation);
//
//
//                builder.setView(view1);
//                builder.setCancelable(true);
//                dialog = builder.create();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
//                dialog.show();
//
//                return true;
//            }
//        });
    }


    protected JSONObject mylistdata;

    private void initDialogue(JSONObject myList,int position) {
        mylistdata = myList;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.singlefriendprofile, null, false);
        builder.setView(VI);
        TextView titletext;
        ImageView closeButton;
        TextView name = VI.findViewById(R.id.username);
        TextView phonenumber = VI.findViewById(R.id.userphone);
        CircleImageView profilepic = VI.findViewById(R.id.proflepic);
        unfriend = VI.findViewById(R.id.unfriend);
        blockfriend = VI.findViewById(R.id.blockfriend);
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
            name.setText(myList.getString("name"));
            phonenumber.setText(myList.getString("phone_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogue(position);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#80030A25")));
        dialog.show();
    }

    public abstract void SetUpOnClick(int position);

    public abstract void showDialogue(int position);


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
