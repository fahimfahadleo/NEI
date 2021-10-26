package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.media.Image;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public abstract class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    protected ArrayList<JSONObject> listdata;
    Context context;
    HashMap<Integer, Boolean> isExpand;
    AlertDialog dialog;


    public FriendRequestAdapter(ArrayList<JSONObject> listdata, Context context) {
        Log.e("listview", listdata.toString());
        this.listdata = listdata;
        this.context = context;
        isExpand = new HashMap<>();

    }

    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_friend_request_item, parent, false);
        return new FriendRequestAdapter.ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NotNull FriendRequestAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);

        Log.e("friendrequestobject", myListData.toString());
        try {
            setUpProfilePicture(Integer.parseInt(myListData.getString("id")), holder.profileimage);
            holder.friendrequestphone.setText(myListData.getString("phone_no"));
            holder.friendrequestname.setText(myListData.getString("name"));
            holder.friendrequestlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.acceptrequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        acceptfriendrequest(Integer.parseInt(myListData.getString("table_id")), myListData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.declinerequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        declinefriendrequest(Integer.parseInt(myListData.getString("table_id")), myListData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.friendrequestlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MainActivity.dismissFriendrequestdialog();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View vi = inflater.inflate(R.layout.friendprofile, null, false);
                                    CircleImageView profilepicture = vi.findViewById(R.id.profilepicture);
                                    TextView username = vi.findViewById(R.id.username);
                                    TextView phonenmber = vi.findViewById(R.id.userphone);
                                    TextView accept = vi.findViewById(R.id.accept);
                                    TextView decline = vi.findViewById(R.id.decline);

                                    try {
                                        phonenmber.setText(myListData.getString("phone_no"));
                                        username.setText(myListData.getString("name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    accept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                acceptfriendrequest(Integer.parseInt(myListData.getString("table_id")), myListData);
                                                dialog.dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    decline.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                declinefriendrequest(Integer.parseInt(myListData.getString("table_id")), myListData);
                                                dialog.dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    try {
                                        setUpProfilePicture(Integer.parseInt(myListData.getString("id")), profilepicture);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    builder.setView(vi);
                                    dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                                    dialog.show();
                                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.fadein);
                                    animation.setDuration(400);
                                    vi.setAnimation(animation);


                                }
                            });
                        }
                    },350);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected abstract void deletenotification(int id, JSONObject jsonObject);

    protected abstract void acceptfriendrequest(int id, JSONObject jsonObject);

    protected abstract void declinefriendrequest(int id, JSONObject jsonObject);

    protected abstract void setUpProfilePicture(int id, CircleImageView profileimage);


    @Override
    public int getItemCount() {

        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView friendrequestname, friendrequestphone;
        public CircleImageView profileimage;
        public CircleImageView acceptrequest, declinerequest;
        public ConstraintLayout friendrequestlayout;


        public ViewHolder(View itemView) {
            super(itemView);
            friendrequestname = itemView.findViewById(R.id.friendname);
            friendrequestphone = itemView.findViewById(R.id.friendphone);
            declinerequest = itemView.findViewById(R.id.decline);
            acceptrequest = itemView.findViewById(R.id.accept);
            profileimage = itemView.findViewById(R.id.profile_image);
            friendrequestlayout = itemView.findViewById(R.id.friendrequestlayout);


        }
    }
}
