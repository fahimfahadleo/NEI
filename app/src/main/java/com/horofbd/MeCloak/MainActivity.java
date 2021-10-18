package com.horofbd.MeCloak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.MessageBuilder;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.ByteUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.minidns.record.A;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.callback.CallbackHandler;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;
import okhttp3.logging.LoggingEventListener;

public class MainActivity extends AppCompatActivity implements ServerResponse, ImageResponse {

    static {
        System.loadLibrary("native-lib");
    }

    static RecyclerView notificationrecyclerview;
    static ArrayList<JSONObject> myNotificationData;
    static NotificationAdapter notificationAdapter;
    static int firstvisibleitem = 1;
    static int lastVisibleitem = 4;
    static int notificaionpageposition;
    static String nextnotificationpageurl = "";
    static TextView notificationcounttext;

    CircleImageView profileimage;
    public static boolean active = false;
    static DatabaseHelper helper;
    ImageView options, notification;
    boolean isopen = false;
    static RecyclerView recyclerView;
    public static final int PICK_IMAGE = 1;
    String number;
    static DrawerLayout drawerLayout;
    TextView logout, Settings, gopremiumtext, helptext, contactustext, tnctext, policytext, aboutustext, pagelogout;
    CardView logoutview, SettingsView, gopremiumview, helpview, contactusview, tncview, policyview, aboutusview, pagelogoutview;


    static ServerResponse serverResponse;
    SwipeRefreshLayout swipeRefreshLayout;
    public static AbstractXMPPConnection connection;
    public static int notificationcount = 0;
    public static String notificationRead = "true";
    static TextView notificationcounter;
    static CardView notificationcounterveiw;
    static Context context;
    ServerRequest serverRequest;
    static int pageposition;
    static String nextpageurl = "";
    static ListviewAdapter adapter;
    EditText searchedt;


    public static void setUpNotificationData(ArrayList<JSONObject> listdata) {
        for (int i = 0; i < listdata.size(); i++) {
            if (!myNotificationData.contains(listdata.get(i))) {
                myNotificationData.add(listdata.get(i));
            }
        }

        for (int i = 0; i < myNotificationData.size(); i++) {
            Log.e("data1" + i, myNotificationData.get(i).toString());
        }


        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                notificationAdapter = new NotificationAdapter(myNotificationData, context) {
                    @Override
                    protected void deletenotification(int id) {
                        //TODO delete single notification
                    }
                };
                notificationrecyclerview.setHasFixedSize(true);

                notificationrecyclerview.setLayoutManager(new LinearLayoutManager(context));
                notificationrecyclerview.setAdapter(notificationAdapter);
                if (!nextnotificationpageurl.equals("")) {
                    notificationcounttext.setText(firstvisibleitem + " to " + lastVisibleitem + " of " + notificationAdapter.getItemCount());
                }


                notificationrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        firstvisibleitem = linearLayoutManager.findFirstVisibleItemPosition();
                        lastVisibleitem = linearLayoutManager.findLastVisibleItemPosition();

                        if (!recyclerView.canScrollVertically(1) && dy > 0) {
                            if (!nextnotificationpageurl.equals("")) {
                                globalRequest(serverResponse, "GET", nextnotificationpageurl, new JSONObject(), 22, context);
                                nextnotificationpageurl = "";
                                notificaionpageposition = linearLayoutManager.findFirstVisibleItemPosition();
                            }
                        }

                        notificationcounttext.setText(firstvisibleitem + " to " + lastVisibleitem + " of " + notificationAdapter.getItemCount());

                    }
                });


                notificationAdapter.notifyDataSetChanged();
                setNotificationRecyclerviewposition();
                Functions.dismissDialogue();
            }
        });

    }

    static void setNotificationRecyclerviewposition() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationrecyclerview.getLayoutManager().scrollToPosition(notificaionpageposition);
            }
        }, 50);
    }

    public static void setNextNotificationpageurl(String next) {
        nextnotificationpageurl = next;
    }

    public static void showNotification() {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.notification_layout, null, false);
        notificationcounttext = view1.findViewById(R.id.notificationcounter);
        TextView deleteall = view1.findViewById(R.id.deleteall);
        notificationrecyclerview = view1.findViewById(R.id.notificationrecyclerview);
        globalRequest(serverResponse, "GET", Important.getGetNotification(), new JSONObject(), 22, context);


        builder.setView(view1);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);


        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            InsetDrawable inset = new InsetDrawable(back, 8, actionBarHeight, 8, 8);
            ;
            dialog.getWindow().setBackgroundDrawable(inset);

        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id


        dialog.show();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String tryencode(String message, String type, String password);

    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks(Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    public static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);


    @Override
    protected void onPause() {
        super.onPause();
        Functions.dismissDialogue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
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

    ArrayList<JSONObject> temparraylist;

    boolean isPermissionGranted() {
        boolean b = false;
        for (String s : permissions) {
            b = ActivityCompat.checkSelfPermission(this, s) == PackageManager.PERMISSION_GRANTED;
            if (!b) {
                return false;
            }
        }
        return true;
    }

    void askPermission() {
        ActivityCompat.requestPermissions(this, permissions, 15);
    }


    String[] permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        profileimage = findViewById(R.id.profile_image);
        options = findViewById(R.id.options);
        drawerLayout = findViewById(R.id.drawer);
        logout = drawerLayout.findViewById(R.id.logout);
        logoutview = drawerLayout.findViewById(R.id.logoutview);
        notification = findViewById(R.id.notification);

        recyclerView = findViewById(R.id.recyclerview);
        SettingsView = findViewById(R.id.settingsview);
        Settings = findViewById(R.id.settings);
        serverRequest = new ServerRequest();
        serverRequest.ServerRequest(this);
        serverResponse = this;
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        gopremiumtext = findViewById(R.id.gopremiumtxt);
        gopremiumview = findViewById(R.id.gopremiumview);
        helptext = findViewById(R.id.helptext);
        helpview = findViewById(R.id.helpview);
        contactusview = findViewById(R.id.contactusview);
        contactustext = findViewById(R.id.contactustext);
        tnctext = findViewById(R.id.termsandconditionstext);
        tncview = findViewById(R.id.termsandconditionsview);
        policytext = findViewById(R.id.policytext);
        policyview = findViewById(R.id.policyview);
        aboutustext = findViewById(R.id.abouttext);
        aboutusview = findViewById(R.id.aboutview);
        notificationcounter = findViewById(R.id.notificationcounter);
        notificationcounterveiw = findViewById(R.id.notificationcounterview);
        pagelogout = findViewById(R.id.pagelogout);
        pagelogoutview = findViewById(R.id.pagelogoutview);
        searchedt = findViewById(R.id.searchedt);
        helper = new DatabaseHelper(this);
        new Functions(this);
        InitLinks(this);
        myNotificationData = new ArrayList<>();

        if (!isPermissionGranted()) {
            askPermission();
        }


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#CBE1CF"));




        searchedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                temparraylist = new ArrayList<>();
                if (text.length() != 0 && data.size() != 0) {
                    for (JSONObject jsonObject : data) {
                        try {
                            if (jsonObject.getString("name").toLowerCase().contains(text.toLowerCase())) {
                                temparraylist.add(jsonObject);
                            }
                            if (jsonObject.getString("phone_no").toLowerCase().contains(text.toLowerCase())) {
                                temparraylist.add(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setUpData(temparraylist);
                }
                if (text.length() == 0) {
                    setUpData(data);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        if (connection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connection.disconnect();
                }
            });
        }


        ImageRequest(this, profileimage, "GET", Important.getViewprofilepicture(), new JSONObject(), 1);


        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {

            recyclerView.setAdapter(null);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("page_id", getLoginInfo("page_id"));
                globalRequest(MainActivity.this, "POST", Important.getFriend_list(), jsonObject, 12, context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            swipeRefreshLayout.setRefreshing(false);
        }, 2000));


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_id", getLoginInfo("page_id"));
            globalRequest(this, "POST", Important.getFriend_list(), jsonObject, 12, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        init();


        new Functions(this);


        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_NO);


        if (connection == null || !connection.isConnected()) {
            new ConnnectXmpp(this, getLoginInfo("phone_no"), getLoginInfo("sec"));
        }


        Log.e("phone", getLoginInfo("phone_no"));
        Log.e("pass", getLoginInfo("sec"));


    }


    static void setRecyclerviewposition() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.getLayoutManager().scrollToPosition(pageposition);
            }
        }, 50);
    }


    public static void setNextpageurl(String next) {
        nextpageurl = next;
    }


    void init() {
        aboutustext.setOnClickListener(view -> StartActivity(MainActivity.this, "About", ""));
        aboutusview.setOnClickListener(view -> StartActivity(MainActivity.this, "About", ""));
        policyview.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Policy!", Toast.LENGTH_SHORT).show());
        policytext.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Policy!", Toast.LENGTH_SHORT).show());
        tncview.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Terms and conditions!", Toast.LENGTH_SHORT).show());
        tnctext.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Terms and conditions!", Toast.LENGTH_SHORT).show());
        contactustext.setOnClickListener(view -> StartActivity(MainActivity.this, "ContactUs", ""));
        contactusview.setOnClickListener(view -> StartActivity(MainActivity.this, "ContactUs", ""));
        helptext.setOnClickListener(view -> StartActivity(MainActivity.this, "Help", ""));
        helpview.setOnClickListener(view -> StartActivity(MainActivity.this, "Help", ""));
        gopremiumview.setOnClickListener(view -> StartActivity(MainActivity.this, "GetPremium", ""));
        gopremiumtext.setOnClickListener(view -> StartActivity(MainActivity.this, "GetPremium", ""));
        SettingsView.setOnClickListener(view -> StartActivity(MainActivity.this, "Settings", ""));
        Settings.setOnClickListener(view -> StartActivity(MainActivity.this, "Settings", ""));
        logoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(MainActivity.this, "POST", Important.getProfile_logout(), new JSONObject(), 8, context);
                if(connection!=null)
                connection.disconnect();


            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(MainActivity.this, "POST", Important.getProfile_logout(), new JSONObject(), 8, context);
                if(connection!=null)
                connection.disconnect();
            }
        });

        pagelogoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(MainActivity.this, "POST", Important.getPagelogout(), new JSONObject(), 37, context);
            }
        });

        pagelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(MainActivity.this, "POST", Important.getPagelogout(), new JSONObject(), 37, context);

            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
            }
        });
        options.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));

        profileimage.setOnClickListener(view -> StartActivity(MainActivity.this, "Profile", ""));
    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("MainResponse", response);
        Functions.dismissDialogue();
        CheckResponse(this, this, response, requestcode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationRead != null && notificationcount == 0) {
            notificationcounterveiw.setVisibility(View.INVISIBLE);
            notificationcounter.setText("0");
        }
    }

    @Override
    public void onFailure(String failresponse) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, failresponse, Toast.LENGTH_SHORT).show();
            Functions.dismissDialogue();
        });
    }

    public static ArrayList<JSONObject> data;


    public static void setUpData(ArrayList<JSONObject> listdata) {
        if (data == null) {
            data = listdata;
        }


        ((Activity) context).runOnUiThread(() -> {
            // Log.e("listviewdata", listdata.toString());
            adapter = new ListviewAdapter(listdata, context, R.layout.singlefriend);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        if (!nextpageurl.equals("")) {
                            NotificationActivity.globalRequest(serverResponse, "GET", nextpageurl, new JSONObject(), 22, context);
                            nextpageurl = "";
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            pageposition = linearLayoutManager.findFirstVisibleItemPosition();

                        } else {
                            Toast.makeText(context, "Reached Bottom", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

            adapter.notifyDataSetChanged();
            setRecyclerviewposition();
            Functions.dismissDialogue();

        });

    }

    public static void blockRequest(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", id);
            globalRequest(serverResponse, "POST", Important.getBlock_friend(), jsonObject, 7, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


//    public void showid(){
//        String id = android.telephony.TelephonyManager.getDeviceId();
//        Log.e("Imei",id);
//    }

    public static void setData(JSONObject jsonObject) {
        Log.e("longpress", jsonObject.toString());
    }

    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) {
        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        runOnUiThread(() -> {

            if (bitmap != null) {
                Log.e("bitmap", "notnull");
                setImage(imageView, bitmap);

            } else {
                Log.e("bitmap", "null");
                setImage(imageView, BitmapFactory.decodeResource(getResources(),
                        R.drawable.person));

            }
        });
    }

    @Override
    public void onImageFailure(String failresponse) {

    }

    static class ListviewAdapter extends ListViewAdapter implements ImageResponse {

        public ListviewAdapter(ArrayList<JSONObject> listdata, Context context, int view) {
            super(listdata, context, view);

        }

        @Override
        protected void imageViewSetUp(String id, CircleImageView imageView) {
            if (!id.equals("id")) {
                Log.e("mainid", id);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("friend", id);
                    ImageRequest(this, imageView, "GET", Important.getViewprofilepicture(), jsonObject, 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        AlertDialog mutedialog = null;

        @SuppressLint("SetTextI18n")
        @Override
        protected void longPressOptions(JSONObject jsonObject) {

            Log.e("jsonobject", jsonObject.toString());


            mutenotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.muteexpiry, null, false);
                    TextView mute1hr, mute24hr, mute30days, muteun;
                    mute1hr = vi.findViewById(R.id.mutefor1text);
                    mute24hr = vi.findViewById(R.id.mutefor24text);
                    mute30days = vi.findViewById(R.id.mutefor30text);
                    muteun = vi.findViewById(R.id.muteforuntext);
                    mute1hr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));

                                if (jsonObject.getString("status").equals("9")) {
                                    jsonObject1.put("mute", "0");
                                } else {
                                    jsonObject1.put("mute", "1");
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(60)));
                                }

                                globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mutedialog.dismiss();
                        }

                    });

                    mute24hr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));

                                if (jsonObject.getString("status").equals("9")) {
                                    jsonObject1.put("mute", "0");
                                } else {
                                    jsonObject1.put("mute", "1");
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(24 * 60)));
                                }

                                globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mutedialog.dismiss();
                        }

                    });

                    mute30days.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));

                                if (jsonObject.getString("status").equals("9")) {
                                    jsonObject1.put("mute", "0");
                                } else {
                                    jsonObject1.put("mute", "1");
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(30 * 24 * 60)));
                                }

                                globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mutedialog.dismiss();
                        }

                    });

                    muteun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));

                                if (jsonObject.getString("status").equals("9")) {
                                    jsonObject1.put("mute", "0");
                                } else {
                                    jsonObject1.put("mute", "1");
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(60 * 60 * 60 * 60)));
                                }

                                globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mutedialog.dismiss();
                        }

                    });


                    builder.setView(vi);
                    mutedialog = builder.create();
                    mutedialog.show();
                }
            });

            markasunread.setOnClickListener(view -> {

            });


            ignoremessage.setOnClickListener(view -> {

                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));

                    if (jsonObject.getString("status").equals("6")) {
                        jsonObject1.put("ignore", "0");
                    } else {
                        jsonObject1.put("ignore", "1");
                    }

                    globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            seal.setOnClickListener(view -> {
                Dialog dialog = new Dialog(context);
                View view1 = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.requireprofilepassword, null, false);
                TextView canclebutton = view1.findViewById(R.id.cancelbutton);
                TextView sealbutton = view1.findViewById(R.id.sealbutton);
                EditText profilepassword = view1.findViewById(R.id.password);

                try {
                    String s = jsonObject.getString("status");
                    if (s.equals("8")) {
                        sealbutton.setText("Unseal");
                    } else {
                        Log.e("false", s);
                        sealbutton.setText("Seal");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                canclebutton.setOnClickListener(view2 -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });
                sealbutton.setOnClickListener(view22 -> {
                    String userpassword = profilepassword.getText().toString();
                    if (!TextUtils.isEmpty(userpassword)) {
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("page_friend_id", jsonObject.getString("page_friend_id"));
                            jsonObject1.put("password_confirmation", userpassword);
                            if (jsonObject.getString("status").equals("8")) {
                                jsonObject1.put("unseal", "1");
                            } else {
                                jsonObject1.put("seal", "1");
                            }

                            globalRequest(serverResponse, "POST", Important.getIgnore_friend(), jsonObject1, 19, context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        profilepassword.setError("Field can not be empty!");
                        profilepassword.requestFocus();
                    }

                    dialog.setContentView(view1);
                    dialog.show();
                });


            });

            block.setOnClickListener(view -> {
                try {
                    blockRequest(jsonObject.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


            delete.setOnClickListener(view -> {

            });

        }

        @Override
        public void showDialogue() {
        }


        @Override
        public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) {

            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ((Activity) context).runOnUiThread(() -> {


                if (bitmap != null) {
                    Log.e("bitmap", "notnull");
                    Log.e("bitmap", bitmap.toString());
                    imageView.setImageBitmap(bitmap);

                } else {
                    Log.e("bitmap", "null");
                }
            });


        }

        @Override
        public void onImageFailure(String failresponse) {

        }
    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private static String getDate(String expiry) {
        String ourDate;
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (Integer.parseInt(expiry) * 60000));

            ourDate = dateFormatter.format(afterAddingTenMins);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }

    static class ConnnectXmpp extends XmppConnection implements IncomingChatMessageListener, OutgoingChatMessageListener {
        public ConnnectXmpp(Context context, String userid, String pass) {
            super(context, userid, pass);

            Log.e("connection", "estublished");


            connectionListener = new ConnectionListener() {
                @Override
                public void connected(XMPPConnection xmppConnection) {


                    Log.e("xmpp", "connected");
                    MainActivity.connection = mConnection;
                    ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                    chatManager.addOutgoingListener(ConnnectXmpp.this);
                    chatManager.addIncomingListener(ConnnectXmpp.this);


                    try {
                        SASLAuthentication.registerSASLMechanism(new SASLMechanism() {
                            @Override
                            protected void authenticateInternal(CallbackHandler callbackHandler) {

                            }

                            @Override
                            protected byte[] getAuthenticationText() {
                                byte[] authcid = toBytes('\u0000' + this.authenticationId);
                                byte[] passw = toBytes('\u0000' + this.password);
                                return ByteUtils.concat(authcid, passw);
                            }

                            @Override
                            public String getName() {
                                return "PLAIN";
                            }

                            @Override
                            public int getPriority() {
                                return 410;
                            }

                            @Override
                            public void checkIfSuccessfulOrThrow() {

                            }

                            @Override
                            protected SASLMechanism newInstance() {
                                return this;
                            }
                        });
                        mConnection.login();
                    } catch (XMPPException e) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                    } catch (SmackException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void authenticated(XMPPConnection xmppConnection, boolean b) {
                    Log.e("xmpp", "authenticated");
                }

                @Override
                public void connectionClosed() {
                    Log.e("xmpp", "connection closed");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    if (e != null) {
                        Log.e("xmpp", "cononection closed on error= " + e.getMessage());
                    }
                }
            };
        }

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            try {
                EntityBareJid jid = JidCreate.entityBareFrom("mecloak@" + Important.getXmppHost());
                if (jid.toString().equals(from.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(message.getBody());
                        String type = jsonObject.getString("type");
                        String body = jsonObject.getString("message");
                        Log.e("body", body);
                        Log.e("type", type);

                        notificationcount = notificationcount + 1;
                        notificationRead = "false";
                        helper.updateNotification("notificationcount", String.valueOf(notificationcount));
                        helper.updateNotification("notificationread", "false");

                        Log.e("notification count", String.valueOf(notificationcount));
                        Log.e("notification status", String.valueOf(notificationRead));
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notificationcounter.setText(String.valueOf(notificationcount));
                                notificationcounterveiw.setVisibility(View.VISIBLE);

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
            Message message = messageBuilder.build();
            Log.e("mainoutgoing", message.getBody());

        }
    }

    static void showSnackbar(String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar snack = Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
                View view = snack.getView();
                view.setBackgroundColor(context.getResources().getColor(R.color.background));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                TextView tv = view.findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.parseColor("#000000"));
                view.setLayoutParams(params);
                snack.show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
        if(connection!=null){
            connection.disconnect();
        }



    }
}