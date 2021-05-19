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

    Context context;
    int vi;

    public ListViewAdapter(ArrayList<JSONObject> listdata, Context context,int view) {
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
    public void onBindViewHolder(ListViewAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);
        try {
            if(vi==R.layout.showsinglefriendrequest){
                holder.textView.setText(myListData.getString("name") + "(" + myListData.getString("phone_no") + ")");
            }else if(vi == R.layout.singlefriend){
                holder.textView.setText(myListData.getString("name"));
                holder.phonenumber.setText(myListData.getString("phone_no"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vi == R.layout.showsinglefriendrequest){
                    initDialogue(listdata.get(position));
                }else if(vi == R.layout.singlefriend){
                    Intent i = new Intent(context,ShowUserMessage.class);
                    i.putExtra("data",listdata.get(position).toString());
                    ((Activity)context).startActivity(i);
                }

            }
        });
    }

protected JSONObject mylistdata;
    private void initDialogue(JSONObject myList) {
        mylistdata = myList;
        Log.e("mylist",myList.toString());
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
            this.phonenumber = (TextView)itemView.findViewById(R.id.text);
        }
    }
}
