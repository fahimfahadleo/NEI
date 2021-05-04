package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.horofbd.nei.Models.FriendListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.function.Function;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.logging.LoggingEventListener;

public class MainActivity extends AppCompatActivity implements ServerResponse {

    CircleImageView profileimage;
    public static boolean active = false;
    DatabaseHelper helper;
    ImageView newmessage,options,notification;

    boolean isopen = false;
    RecyclerView recyclerView;
    public static final int PICK_IMAGE = 1;
    String number;
    DrawerLayout drawerLayout;
    TextView logout,Settings;
    CardView logoutview,SettingsView;
    ImageView search;
    LinearLayout searchlayout;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public static native void globalRequest(ServerResponse serverResponse,String requesttype,String link,JSONObject jsonObject,int requestcode);



    @Override
    public void onStart() {
        super.onStart();
        active = true;

        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));

        if(Functions.getSharedPreference("private_token","").equals("") || System.currentTimeMillis()>expires){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else if (!Functions.isActive) {
            Cursor cursor = helper.getUserPassword(1);

            if (cursor != null && cursor.getCount() > 0) {
                Log.e("check", "login");
                startActivity(new Intent(this, UserVerificationActivity.class));
            } else {
                Log.e("check", "register");
                startActivity(new Intent(this, RegisterVerifiedUser.class));
            }
            finish();
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


                Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                String md5 = Functions.calculateMD5(finalFile);
                Log.e("mainmd5",md5);

                //ServerRequest.UploadPhoto(this,getRealPathFromURI(tempUri),finalFile,"01757121999",md5);
                //ServerRequest.UploadPhoto(this,getRealPathFromURI(tempUri),finalFile,"01757121999",md5);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (requestCode == 2 && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = this.getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                 number = cursor.getString(numberIndex);
                // Do something with the phone number
                Log.e("phone number",number);
                ServerRequest.CheckPhoneNumber(this,number,2);
            }

            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileimage = findViewById(R.id.profile_image);
        newmessage = findViewById(R.id.newmessage);
        options = findViewById(R.id.options);
        drawerLayout = findViewById(R.id.drawer);
        logout = drawerLayout.findViewById(R.id.logout);
        search = findViewById(R.id.search);
        logoutview = drawerLayout.findViewById(R.id.logoutview);
        notification = findViewById(R.id.notification);
        searchlayout = findViewById(R.id.searchlayout);
        searchlayout.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);
        SettingsView = findViewById(R.id.settingsview);
        Settings = findViewById(R.id.settings);


        SettingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });


        namelist = new ArrayList<>();

        //startActivity(new Intent(this,ProfileActivity.class));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen) {
                    TransitionManager.beginDelayedTransition(searchlayout,new AutoTransition());
                    TransitionManager.beginDelayedTransition(recyclerView,new AutoTransition());
                    searchlayout.setVisibility(View.GONE);
                    isopen = false;
                } else {
                    TransitionManager.beginDelayedTransition(recyclerView,new AutoTransition());
                    TransitionManager.beginDelayedTransition(searchlayout,new AutoTransition());
                    searchlayout.setVisibility(View.VISIBLE);
                    isopen = true;
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
            }
        });

        logoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.LogOut(MainActivity.this,3);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.LogOut(MainActivity.this,3);
            }
        });


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });


        newmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 2);
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });

        String s = tryencode("this is the message","encode","password");
        Log.e("encoded",s);

        Log.e("encoded",Functions.Base64encode(s));
        String d = tryencode(s,"decode","password");

        Log.e("decoded",d);


        ServerRequest.ServerRequest(this);
        new Functions(this);
        helper = new DatabaseHelper(this);
        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));


        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_NO);

        if (!Functions.getSharedPreference("private_token", "").equals("") || System.currentTimeMillis() <= expires) {
            if(Functions.isActive){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("page","1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                globalRequest(this,"POST","http://10.0.2.2:8000/api/get-friends",jsonObject,1);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }

        }



    }

    ArrayList<FriendListModel> namelist;

    ListviewAdapter adapter;

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String tryencode(String message,String type,String password);



    @Override
    public void onResponse(String response,int code,int requestcode) throws JSONException {
       Log.e("Mainresponse",response);

       if(requestcode == 1){

           Log.e("dhukche","dhukche1");
           try{
               JSONObject jsonObject = new JSONObject(response);
               Log.e("dhukche","dhukche2");
               if(jsonObject.has("error")){
                   startActivity(new Intent(this,PhoneNumberVerificationActivity.class));
               }

           }catch (Exception e){
               Log.e("jsonexception",e.getMessage());
           }



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
               MainActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(MainActivity.this, "toast", Toast.LENGTH_SHORT).show();
                       Log.e("namelist2",namelist.toString());

                       adapter = new ListviewAdapter(namelist);
                       recyclerView.setHasFixedSize(true);
                       recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                       recyclerView.setAdapter(adapter);
                   }
               });

           }








       }else if(requestcode == 2){
           Log.e("Mainresponse2",response);

           JSONObject jsonObject = new JSONObject(response);
           Log.e("json",jsonObject.toString());

           if(jsonObject.has("error")){
               Log.e("failed","error");
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(MainActivity.this,"Phone Number Not Found. Refer 4 Friends to get Free Premium!",Toast.LENGTH_LONG).show();
                   }
               });
           }else {
               Intent i = new Intent(this,ShowUserMessage.class);
               i.putExtra("username",jsonObject.getString("name"));
               i.putExtra("phone",jsonObject.getString("phone_no"));
               startActivity(i);
           }
       }else if(requestcode == 3){
           Functions.ClearSharedPreference();
           Functions.setSharedPreference("private_token","");
            helper.deleteAllData();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
       }

    }

    @Override
    public void onFailure(String failresponse) {

    }

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
            View listItem= layoutInflater.inflate(R.layout.singlefriend, parent, false);
            return new ListviewAdapter.ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(ListviewAdapter.ViewHolder holder, int position) {
            final FriendListModel myListData = listdata.get(position);
            holder.textView.setText(myListData.getName());
//        holder.imageView.setImageResource(listdata[position].getImgId());
            holder.phonenumber.setText(myListData.getPhone_no());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,ShowUserMessage.class);
                    i.putExtra("phone",myListData.getPhone_no());
                    startActivity(i);
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
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
                this.textView = (TextView) itemView.findViewById(R.id.name);
                this.cardView = (CardView) itemView.findViewById(R.id.singlecardview);
                this.phonenumber = (TextView)itemView.findViewById(R.id.phonenumber);
            }
        }
    }



}