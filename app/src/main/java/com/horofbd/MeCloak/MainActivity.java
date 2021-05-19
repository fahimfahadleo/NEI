package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ServerResponse {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String tryencode(String message, String type, String password);

    static native void StartActivity(Context context, String activity);

    public static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static native void InitLinks();
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);

    public static native String getLoginInfo(String key);


    CircleImageView profileimage, newmessage;
    public static boolean active = false;
    DatabaseHelper helper;
    ImageView options, notification;

    boolean isopen = false;
    static RecyclerView recyclerView;
    public static final int PICK_IMAGE = 1;
    String number;
    DrawerLayout drawerLayout;
    TextView logout, Settings;
    CardView logoutview, SettingsView;
    ImageView search;
    LinearLayout searchlayout;


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
                Log.e("mainmd5", md5);

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
                Log.e("phone number", number);
                ServerRequest.CheckPhoneNumber(this, number, 2);
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

    static Context context;

    public static void closeActivtiy() {
        ((Activity) context).finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
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

        InitLinks();


        SettingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.this, "Settings");
            }
        });
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.this, "Settings");
            }
        });


        //  namelist = new ArrayList<>();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen) {
                    TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                    TransitionManager.beginDelayedTransition(recyclerView, new AutoTransition());
                    searchlayout.setVisibility(View.GONE);
                    isopen = false;
                } else {
                    TransitionManager.beginDelayedTransition(recyclerView, new AutoTransition());
                    TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                    searchlayout.setVisibility(View.VISIBLE);
                    isopen = true;
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.this, "Notification");
            }
        });

        logoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.LogOut(MainActivity.this, 8);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest.LogOut(MainActivity.this, 8);
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
//                Intent i = new Intent(Intent.ACTION_PICK);
//                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//                startActivityForResult(i, 2);

                StartActivity(MainActivity.this, "AddFriend");
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.this, "Profile");

            }
        });

        String s = tryencode("this is the message", "encode", "password");
        Log.e("encoded", s);

        Log.e("encoded", Functions.Base64encode(s));
        String d = tryencode(s, "decode", "password");

        Log.e("decoded", d);


        new Functions(this);
        helper = new DatabaseHelper(this);



        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_NO);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_id",getLoginInfo("page_id"));
            globalRequest(this, "POST", Important.getFriend_list(), jsonObject, 12);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }

    static ListviewAdapter adapter;

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("MainResponse",response);
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) {

    }
    public static void setUpData(ArrayList<JSONObject> listdata) {
        Log.e("mainlist",listdata.toString());
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("listviewdata", listdata.toString());
                adapter = new ListviewAdapter(listdata, context,R.layout.singlefriend);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }
        });

    }
    static class ListviewAdapter extends ListViewAdapter {
        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
            super(listdata, context, view);
        }

        @Override
        public void showDialogue() {
        }
    }


}