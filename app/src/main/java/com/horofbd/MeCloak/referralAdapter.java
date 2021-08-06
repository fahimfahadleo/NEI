package com.horofbd.MeCloak;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public abstract class referralAdapter extends RecyclerView.Adapter<referralAdapter.ViewHolder> {

    ArrayList<JSONObject> listdata;
    Context context;


    public referralAdapter(ArrayList<JSONObject> listdata, Context context) {
        Log.e("listview", listdata.toString());
        this.listdata = listdata;
        this.context = context;

    }

    @Override
    public referralAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.referraluser, parent, false);
        return new referralAdapter.ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NotNull referralAdapter.ViewHolder holder, int position) {
        final JSONObject myListData = listdata.get(position);
        try {
            holder.username.setText(myListData.getString("name"));
            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        performClick(String.valueOf(myListData.getInt("id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public abstract void performClick(String id);



    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ViewHolder(View itemView) {
            super(itemView);
            this.username = (TextView) itemView.findViewById(R.id.username);
        }
    }
}
