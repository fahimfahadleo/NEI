package com.horofbd.MeCloak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.radiobutton.MaterialRadioButton;

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

public class MainActivity extends AppCompatActivity implements ServerResponse, ImageResponse {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String tryencode(String message, String type, String password);

    static native void StartActivity(Context context, String activity, String data);

    public static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks(Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    public static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);


    CircleImageView profileimage, newmessage;
    public static boolean active = false;
    DatabaseHelper helper;
    ImageView options, notification;
    boolean isopen = false;
    static RecyclerView recyclerView;
    public static final int PICK_IMAGE = 1;
    String number;
    static DrawerLayout drawerLayout;
    TextView logout, Settings, gopremiumtext, helptext, contactustext, tnctext, policytext, aboutustext;
    CardView logoutview, SettingsView, gopremiumview, helpview, contactusview, tncview, policyview, aboutusview;
    ImageView search;
    LinearLayout searchlayout;
    static ServerResponse serverResponse;
    SwipeRefreshLayout swipeRefreshLayout;
    public static AbstractXMPPConnection connection;


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
    ServerRequest serverRequest;


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


        new Functions(this);
        InitLinks(this);


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
            Log.e("onResume", "Called");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (!Functions.fileExists("private.key", this)) {
//            globalRequest(this, "GET", Important.getPrivatekey(), new JSONObject(), 20, context);
//        }


        init();


        new Functions(this);
        helper = new DatabaseHelper(this);

        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_NO);


        new ConnnectXmpp(this, getLoginInfo("phone_no"), getLoginInfo("sec"));


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
        logoutview.setOnClickListener(view -> globalRequest(MainActivity.this, "POST", Important.getProfile_logout(), new JSONObject(), 8, context));
        logout.setOnClickListener(view -> globalRequest(MainActivity.this, "POST", Important.getProfile_logout(), new JSONObject(), 8, context));
        search.setOnClickListener(view -> {
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
        });
        notification.setOnClickListener(view -> StartActivity(MainActivity.this, "Notification", ""));
        options.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));
        newmessage.setOnClickListener(view -> {
//                Intent i = new Intent(Intent.ACTION_PICK);
//                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//                startActivityForResult(i, 2);

            StartActivity(MainActivity.this, "AddFriend", "");
        });
        profileimage.setOnClickListener(view -> StartActivity(MainActivity.this, "Profile", ""));
    }

    static ListviewAdapter adapter;

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("MainResponse", response);
        Functions.dismissDialogue();
        CheckResponse(this, this, response, requestcode);

    }

    @Override
    public void onFailure(String failresponse) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, failresponse, Toast.LENGTH_SHORT).show();
            Functions.dismissDialogue();
        });
    }

    public static void setUpData(ArrayList<JSONObject> listdata) {
        Log.e("mainlist", listdata.toString());
        ((Activity) context).runOnUiThread(() -> {
            Log.e("listviewdata", listdata.toString());
            adapter = new ListviewAdapter(listdata, context, R.layout.singlefriend);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
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


            mutenotificationview.setOnClickListener(new View.OnClickListener() {
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(24*60)));
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(30*24*60)));
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(60*60*60*60)));
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(24*60)));
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(30*24*60)));
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
                                    jsonObject.put("mute_expiry", getDate(String.valueOf(60*60*60*60)));
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

            markasunreadview.setOnClickListener(view -> {

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
            ignoremessageview.setOnClickListener(view -> {
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
            sealview.setOnClickListener(view -> {
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
                canclebutton.setOnClickListener(view24 -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });
                sealbutton.setOnClickListener(view23 -> {
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
                });

                dialog.setContentView(view1);
                dialog.show();
            });
            block.setOnClickListener(view -> {
                try {
                    blockRequest(jsonObject.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            blockview.setOnClickListener(view -> {
                try {
                    blockRequest(jsonObject.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            delete.setOnClickListener(view -> {

            });

            deleteview.setOnClickListener(view -> {

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
                    //   ((Activity)context).runOnUiThread(() -> Toast.makeText(context, "Logged in successfully...", Toast.LENGTH_LONG).show());


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
            Log.e("mainincomming", message.getBody());
        }

        @Override
        public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
            Message message = messageBuilder.build();
            Log.e("mainoutgoing", message.getBody());

        }
    }
}