package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    protected ArrayList<JSONObject> listdata;
    Context context;
    HashMap<Integer, Boolean> isExpand;


    public NotificationAdapter(ArrayList<JSONObject> listdata, Context context) {
        Log.e("listview", listdata.toString());
        this.listdata = listdata;
        this.context = context;
        isExpand = new HashMap<>();

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NotNull NotificationAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);
        holder.deletenotification.setVisibility(View.INVISIBLE);


        try {
            String message = myListData.getString("message");
            TextPaint paint = holder.notificationtext.getPaint();
            holder.notificationtext.measure(0,0);
            int wordwidth=(int)paint.measureText("a",0,1);
            int screenwidth = holder.notificationtext.getMeasuredWidth();
            int num = screenwidth/wordwidth;



            if (message.length() > num) {
                holder.notificationtext.setText(myListData.getString("message").substring(0, num) + "...");
            } else {
                holder.notificationtext.setText(myListData.getString("message"));
            }


            switch (myListData.getString("type")) {
                case "alert":
                case "expiry": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    break;
                }
                case "new_friend_request": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    break;
                }
                case "friend_request_accepted": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    break;
                }
                case "offers": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    break;
                }
                case "recharge": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));

                    break;
                }
                case "message_recall": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    break;
                }
                case "success": {
                    holder.notificationicon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));

                    break;
                }
            }

            holder.downarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isExpand.containsKey(position)) {
                        if (isExpand.get(position)) {
                            isExpand.put(position, false);
                            holder.downarrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowdown));
                            if (message.length() > num) {
                                try {
                                    holder.notificationtext.setText(myListData.getString("message").substring(0, num) + "...");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                holder.notificationtext.setText(message);
                            }
                            Transition transition = new Fade();
                            transition.setDuration(200);
                            transition.addTarget(R.id.delete);
                            TransitionManager.beginDelayedTransition(holder.notificationlayout, transition);
                            holder.deletenotification.setVisibility(View.INVISIBLE);
                        } else {
                            isExpand.put(position, true);
                            holder.downarrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowupicon));
                            holder.notificationtext.setText(message);

                            Transition transition = new Fade();
                            transition.setDuration(200);
                            transition.addTarget(R.id.delete);
                            TransitionManager.beginDelayedTransition(holder.notificationlayout, transition);
                            holder.deletenotification.setVisibility(View.VISIBLE);


                        }

                        AutoTransition autoTransition = new AutoTransition();
                        autoTransition.setDuration(500);

                        TransitionManager.beginDelayedTransition(holder.notificationlayout, autoTransition);
                       // TransitionManager.beginDelayedTransition(holder.notificationlayout, new AutoTransition());

                    }else {
                        isExpand.put(position, true);
                        holder.downarrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowupicon));
                        holder.notificationtext.setText(message);

                        Transition transition = new Fade();
                        transition.setDuration(200);
                        transition.addTarget(R.id.delete);
                        TransitionManager.beginDelayedTransition(holder.notificationlayout, transition);

                        holder.deletenotification.setVisibility(View.VISIBLE);
                        AutoTransition autoTransition = new AutoTransition();
                        autoTransition.setDuration(500);

                        TransitionManager.beginDelayedTransition(holder.notificationlayout, autoTransition);



                    }
                }
            });

            holder.notificationdate.setText(Functions.getTimeWithZone(myListData.getString("created_at")));
            holder.deletenotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deletenotification(myListData.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.notificationlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.e("notification", "notificationcardviewselected " + myListData.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected abstract void deletenotification(int id);



    @Override
    public int getItemCount() {

        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView notificationtext, notificationdate;
        public CardView notificationcardview;
        public ImageView downarrow, notificationicon;
        public ConstraintLayout notificationlayout;
        public TextView deletenotification;


        public ViewHolder(View itemView) {
            super(itemView);

            this.notificationtext = itemView.findViewById(R.id.notificationtext);
            this.notificationdate = itemView.findViewById(R.id.notificationdate);
            this.notificationcardview = itemView.findViewById(R.id.notificationcardview);
            this.downarrow = itemView.findViewById(R.id.downarrow);
            this.notificationicon = itemView.findViewById(R.id.notificationicon);
            this.notificationlayout = itemView.findViewById(R.id.notificationlayout);
            this.deletenotification = itemView.findViewById(R.id.delete);
        }
    }
}
