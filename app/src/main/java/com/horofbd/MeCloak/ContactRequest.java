package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.horofbd.MeCloak.Models.FriendListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class ContactRequest extends Fragment implements ServerResponse {
    static Context context;
    static ArrayList<FriendListModel> namelist;

    public ContactRequest(Context context) {
        ContactRequest.context = context;
    }


     RecyclerView recyclerView;
    ListviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fragment_contact_request, container, false);
        recyclerView = vi.findViewById(R.id.recyclerview);
        namelist = new ArrayList<>();
        ServerRequest.RetriveFriendRequest(this, 1);


        return vi;
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        if (requestcode == 1) {

            Log.e("Notification",response);

            JSONArray jsonArray = new JSONArray();
            namelist.clear();

            try {
                jsonArray  = new JSONArray(response);
                Log.e("jsonarray", jsonArray.toString());
            }catch (Exception e){
                Log.e("error","error: "+e.getMessage());
            }

            if (jsonArray.length() > 0) {
                Log.e("dhukche", "dhukche");
                Log.e("jsonlen",String.valueOf(jsonArray.length()));
                for (int i = 0; i < jsonArray.length() ;i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FriendListModel model = new FriendListModel();

                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String ref = jsonObject.getString("ref");
                    String phone = jsonObject.getString("phone_no");
                    String theme = jsonObject.getString("theme")==null?"":jsonObject.getString("theme");
                    String created_at = jsonObject.getString("created_at");
                    String updated_at = jsonObject.getString("updated_at");
                    JSONObject temp = jsonObject.getJSONObject("pivot");
                    model.setId(id);
                    model.setName(name);
                    model.setRef(ref);
                    model.setPhone_no(phone);
                    model.setTheme(theme);
                    model.setCreated_at(created_at);
                    model.setUpdated_at(updated_at);
                    model.setPiovot(temp);
                    namelist.add(model);

                    Log.e("namelist",namelist.toString());
                }

                Log.e("namelist1",namelist.toString());
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "toast", Toast.LENGTH_SHORT).show();
                        Log.e("namelist2",namelist.toString());

                        adapter = new ListviewAdapter(namelist);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);
                    }
                });

            }

        }
        if(requestcode ==2){
            Log.e("response",response);
            JSONObject object = new JSONObject(response);
            if(object.getString("response").equals("Request Accepted")){
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Accepted!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }
        if(requestcode == 3){
            Log.e("response",response);
            JSONObject object = new JSONObject(response);
            if(object.getString("response").equals("Request Deleted")){
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
            Log.e("notificationerror",failresponse);
    }




    AlertDialog dialog;
    public class ListviewAdapter extends RecyclerView.Adapter<ListviewAdapter.ViewHolder>{
        private ArrayList<FriendListModel> listdata;

        // RecyclerView recyclerView;
        public ListviewAdapter(ArrayList<FriendListModel> listdata) {
            Log.e("listview",listdata.toString());
            this.listdata = listdata;
        }
        @Override
        public ListviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.showsinglefriendrequest, parent, false);
            return new ListviewAdapter.ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(ListviewAdapter.ViewHolder holder, int position) {
            final FriendListModel myListData = listdata.get(position);
            holder.textView.setText(myListData.getName()+"("+myListData.getPhone_no()+")");
//        holder.imageView.setImageResource(listdata[position].getImgId());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogue(myListData);
                }
            });
        }

        private void showDialogue(FriendListModel myListData) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View VI = inflater.inflate(R.layout.acceptdeclinedialogue, null, false);
            builder.setView(VI);
            TextView name = VI.findViewById(R.id.name);
            TextView phonenumber = VI.findViewById(R.id.phonenumber);
            TextView accept = VI.findViewById(R.id.accpetbutton);
            TextView decline = VI.findViewById(R.id.declinebutton);


            name.setText(myListData.getName());
            phonenumber.setText(myListData.getPhone_no());

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ServerRequest.ResponseToFriendRequest(ContactRequest.this,myListData.getPivot_id(),"2","1",2);
                    listdata.remove(myListData);
                    notifyDataSetChanged();
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ServerRequest.ResponseToFriendRequest(ContactRequest.this,myListData.getPivot_id(),"3","1",3);
                    listdata.remove(myListData);
                    notifyDataSetChanged();

                }
            });

            builder.setCancelable(true);
            dialog = builder.create();
            dialog.show();
        }


        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView imageView;
            public TextView textView;
            public CardView cardView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
                this.textView = (TextView) itemView.findViewById(R.id.name);
                this.cardView = (CardView) itemView.findViewById(R.id.singlecardview);
            }
        }
    }











}