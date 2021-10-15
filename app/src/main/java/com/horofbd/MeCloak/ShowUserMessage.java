package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.MessageBuilder;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.httpfileupload.UploadProgressListener;
import org.jivesoftware.smackx.mam.MamManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

import static com.horofbd.MeCloak.MainActivity.connection;

public class ShowUserMessage extends AppCompatActivity implements ServerResponse, IncomingChatMessageListener, OutgoingChatMessageListener, ImageResponse {

    static {
        System.loadLibrary("native-lib");
    }

    static ServerResponse serverResponse;
    static IncomingChatMessageListener incomingChatMessageListener;
    static OutgoingChatMessageListener outgoingChatMessageListener;

    CardView setpasswordview;
    TextView setPassword;
    static String boundageposition;

    static EditText typemessage;
    ImageView send, sendimage;
    static List<Message> messages;
    static ChatManager chatManager;
    static RecyclerView messagerecyclerview;
    static int me = 0;
    static int him = 0;
    DrawerLayout drawerLayout;
    static ChatMessageAdapter adapter;
    EditText typepassword;
    ImageView timer, file;
    CardView timerview, boundageview, fileview;
    TextView boundagetext, boundagetip, passwordtip, timertip;
    DatabaseHelper helper;
    ImageView menubutton;
    static boolean isboundageitemavailable = false;
    static Context context;
    AlertDialog dialog;
    static String passstr = "";
    static String timerstr = "";
    static String boundagestr = "";
    static String userphonenumber;
    boolean isDatabaseAvailable = false;
    Bundle save;
    TextView  timerside;
    CircleImageView profilepicture;
    static ImageView showbelow;
    static ScrollView belowview;
    static boolean isbelowviewvisible = false;
    ImageView boundagebelow;
    CardView boundagebelowview;
    CardView  passwordbelowview, timerbelowview, timersideview;
    ImageView  passwordbelowbutton, timerbelow;
    TextView title,titlephonenumber;
    static LinearLayout sendmessagelayout;
    static ScrollView invisiblelayout;
    static String isReplied = "";
    static String  stanzaid = "";
    static LinearLayout forwardlayout;
    static LinearLayout visiblelayout;
    static TextView invisiblebutton;


    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);

    static native String EncrytpAndDecrypt(String message, String type, String password);

    static native void saveUserData(String key, String value);

    static native void sendFiletoUser(ServerResponse serverResponse, AbstractXMPPConnection connection, File file, int requestcode);

    static native String DefaultED(String message, String type);

    static native void ImageViewImageRequest(ImageResponseImageView imageResponse, ImageView imageView, String Link, int requestcode);

    UploadProgressListener listener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri uri = data.getData();

            String path = FileUtils.getPath(this, uri);
            Log.e("path", path);
            // Alternatively, use FileUtils.getFile(Context, Uri)
            if (FileUtils.isLocal(path)) {
                File file = new File(path);
                sendFiletoUser(this, connection, file, 123);
            }


        } else if (requestCode == 123 && resultCode == RESULT_OK) {
            final Uri uri = data.getData();

            // Get the File path from the Uri
            String path = FileUtils.getPath(this, uri);
            Log.e("path", path);
            // Alternatively, use FileUtils.getFile(Context, Uri)
            if (FileUtils.isLocal(path)) {
                File file = new File(path);
                sendFiletoUser(this, connection, file, 123);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(forwardlayout.getVisibility() == View.VISIBLE){
            forwardlayout.setVisibility(View.GONE);
            visiblelayout.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }

    }

    static PlayerView playerView;
    static String userboundage, usertimer, friendid;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_CHECKSUM = "CHECKSUM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boundageposition = getIntent().getStringExtra("boundage");
        if (boundageposition.equals("1")) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        }
        save = savedInstanceState;
        setContentView(R.layout.activity_show_user_message);
        messagerecyclerview = findViewById(R.id.messagerecyclerview);
        typemessage = findViewById(R.id.typemessage);
        send = findViewById(R.id.sendbutton);
        InitLinks();
        timer = findViewById(R.id.timer);
        timerview = findViewById(R.id.timerview);
        helper = new DatabaseHelper(this);
        menubutton = findViewById(R.id.menubutton);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        profilepicture = findViewById(R.id.profile_image);
        showbelow = findViewById(R.id.showbelow);
        belowview = findViewById(R.id.belowbar);
        drawerLayout = findViewById(R.id.drawer);
        typepassword = findViewById(R.id.typepassword);
        setPassword = findViewById(R.id.setpasswordtext);
        setpasswordview = findViewById(R.id.setpasswordview);
        boundageview = findViewById(R.id.boundageview);
        boundagetext = findViewById(R.id.boundagetext);
        boundagetip = findViewById(R.id.boundagetips);
        boundagebelow = findViewById(R.id.enablebelowboundage);
        boundagebelowview = findViewById(R.id.enablebelowboundageview);
        sendimage = findViewById(R.id.image);
        passwordbelowbutton = findViewById(R.id.belowpassword);
        passwordbelowview = findViewById(R.id.belowpasswordview);
        timerbelow = findViewById(R.id.belowtimer);
        timerbelowview = findViewById(R.id.belowtimerview);
        timerside = findViewById(R.id.tiemrtext);
        timersideview = findViewById(R.id.tiemrview);
        passwordtip = findViewById(R.id.encryptionpasstips);
        timertip = findViewById(R.id.expirytips);
        file = findViewById(R.id.file);
        fileview = findViewById(R.id.fileview);
        serverResponse = this;
        incomingChatMessageListener = this;
        outgoingChatMessageListener = this;
        title = findViewById(R.id.titletext);
        titlephonenumber = findViewById(R.id.titlephone);
        sendmessagelayout = findViewById(R.id.sendmessagelayout);
        invisiblelayout = findViewById(R.id.belowbar);
        forwardlayout = findViewById(R.id.forwordlayout);
        forwardlayout.setVisibility(View.GONE);
visiblelayout = findViewById(R.id.visiblelayout);
invisiblebutton = findViewById(R.id.invisiblebutton);
invisiblebutton.setVisibility(View.GONE);

invisiblebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        forwardlayout.setVisibility(View.GONE);
        visiblelayout.setVisibility(View.VISIBLE);
        invisiblebutton.setVisibility(View.GONE);
    }
});


        // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(profilepicture);


        String premiumstatus = getLoginInfo("premium");
        userphonenumber = getIntent().getStringExtra("phone_no");
        userboundage = getIntent().getStringExtra("boundage");
        usertimer = getIntent().getStringExtra("");
        friendid = getIntent().getStringExtra("id");

        String username = getIntent().getStringExtra("name");


        title.setText(username);
        titlephonenumber.setText(userphonenumber);


        Cursor c = helper.getData(userphonenumber, getLoginInfo("page_no"));
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String data2 = c.getString(c.getColumnIndex("phonenumber"));
            passstr = c.getString(c.getColumnIndex("textpass"));
            boundagestr = c.getString(c.getColumnIndex("boundage"));
            timerstr = c.getString(c.getColumnIndex("timer"));
            isDatabaseAvailable = true;
        }
        c.close();

        if (!isDatabaseAvailable) {
            helper.setFriendInformation(userphonenumber, "", "0", "0", getLoginInfo("page_no"));
            passstr = "";
            timerstr = "0";
            boundagestr = "0";
        }

        context = this;


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend", getIntent().getStringExtra("id"));
            ImageRequest(this, profilepicture, "GET", Important.getViewprofilepicture(), jsonObject, 1);


            //loadImage(profilepicture,"https://192.168.152.9:5443/upload/279cb5520ea1288b318e1bbd7c8e566337e4f7f1/dwkn1QA01sIMdRZ6XPWHNKV49dcTjGtjyVH2xQ5w/IMG_20210905_195838.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        belowview.setVisibility(View.GONE);

        mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (isFullscreene)
                    closeFullscreenDialog(playerView);
                super.onBackPressed();
            }
        };


        timerbelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        timerbelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        timerside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        timersideview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        boundagebelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundagebelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });
        boundagetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundagetip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getResources().getString(R.string.boundagetip));
            }
        });
        timertip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getString(R.string.timertip));
            }
        });

        passwordtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getString(R.string.passwordtip));
            }
        });




        showbelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isbelowviewvisible) {
                    isbelowviewvisible = false;
                    belowview.setVisibility(View.GONE);
                    belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_up));
                    TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                    showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowdown));
                } else {
                    isbelowviewvisible = true;
                    belowview.setVisibility(View.VISIBLE);
                    belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_down));
                    TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                    showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowupicon));
                }
            }
        });

        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // takeScreenshot();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceName = android.os.Build.MODEL;
                String deviceMan = android.os.Build.MANUFACTURER;
                Log.e("devicename", deviceName);
                Log.e("deviceman", deviceMan);
                Intent intent;
                if (deviceMan.equals("Samsung")) {
                    intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent.putExtra("CONTENT_TYPE", "*/*");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                }
                startActivityForResult(intent, 123);
            }
        });
        fileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceName = android.os.Build.MODEL;
                String deviceMan = android.os.Build.MANUFACTURER;
                Log.e("devicename", deviceName);
                Log.e("deviceman", deviceMan);
                Intent intent;
                if (deviceMan.equals("Samsung")) {
                    intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent.putExtra("CONTENT_TYPE", "*/*");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                }
                startActivityForResult(intent, 123);
            }
        });

        sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, 1);
            }
        });


        Log.e("pass", passstr);
        Log.e("pass", "passstr");

        if (!timerstr.equals("0")) {
            timersideview.setCardBackgroundColor(getResources().getColor(R.color.green));
            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        if (!boundagestr.equals("0")) {
            boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }




        if (savedInstanceState != null && savedInstanceState.containsKey("STATE_CHECKSUM")) {
            Log.e("state", "available");
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            isFullscreene = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            tempchecksum = savedInstanceState.getString(STATE_CHECKSUM);
        }


        try {
            InitConnection();
        } catch (Settings.SettingNotFoundException e) {
            Log.e("error1", e.toString());
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(manager);


        setpasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });


        passwordbelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        passwordbelowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });



// ContentView is the root view of the layout of this activity/fragment
        drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        drawerLayout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = drawerLayout.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged();
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged();
                            }
                        }
                    }
                });


        typemessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mess = charSequence.toString();
                send.setEnabled(mess != null);
                Chat chat = null;
                try {
                    chat = chatManager.chatWith(JidCreate.entityBareFrom(userphonenumber + "@" + Important.getXmppHost()));
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }

                try {
                    ChatStateManager.getInstance(connection).setCurrentState(ChatState.composing, chat);
                } catch (SmackException.NotConnectedException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = typemessage.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    typemessage.setText(null);
                    sendMessage(message, userphonenumber, "txt");
                }

            }
        });


        setScrollPosition();

    }


    void setBoundageIcon() {
        try {
            if (!boundagestr.equals("0")) {
                updateUserInfo(3, "0");
                boundageview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.red_500));

            } else {
                updateUserInfo(3, "1");
                boundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
                boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    AlertDialog tipdialog;

    void showTips(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View vi = getLayoutInflater().inflate(R.layout.tipbanner, null, false);
        TextView tipmessage = vi.findViewById(R.id.tipmessage);
        tipmessage.setText(type);
        builder.setView(vi);
        tipdialog = builder.create();
        tipdialog.show();
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    void setScrollPosition() {
        if (messagerecyclerview.getAdapter() != null && messagerecyclerview.getAdapter().getItemCount() != 0) {
            if (getIntent().hasExtra("recyclerview")) {
                typemessage.setText(getIntent().getStringExtra("message"));
                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.smoothScrollToPosition(getIntent().getIntExtra("recyclerview", 0));
                    }
                }, 10);

            } else {

                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                    }
                }, 10);

            }
        }
    }


    public static void sendMediaTypeData(String string) {


        try {
            JSONObject jsonObject = new JSONObject(string);
            Log.e("type", jsonObject.toString());
            String body = jsonObject.getString("body");
            String type = jsonObject.getString("type");

            Log.e("body", body);
            Log.e("type", type);

            sendMessage(jsonObject.getString("body"), userphonenumber, jsonObject.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





    static void InitConnection() throws Settings.SettingNotFoundException {
        if (connection != null) {
            Log.e("connection", "notnull");
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(incomingChatMessageListener);
            chatManager.addOutgoingListener(outgoingChatMessageListener);


            ChatStateManager chatmanager = ChatStateManager.getInstance(connection);
            chatmanager.addChatStateListener(new ChatStateListener() {
                @Override
                public void stateChanged(Chat chat, ChatState state, Message message) {
                    Log.e("chatstate", String.valueOf(state));
                }
            });


            MessageEventNotificationListener listener = new MessageEventNotificationListener() {
                @Override
                public void deliveredNotification(Jid from, String packetID) {
                    Log.e("messaereceived", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void displayedNotification(Jid from, String packetID) {
                    Log.e("notification", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void composingNotification(Jid from, String packetID) {
                    Log.e("typing", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void offlineNotification(Jid from, String packetID) {
                    Log.e("offline", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void cancelledNotification(Jid from, String packetID) {
                    Log.e("cancelled", "from: " + from.toString() + " id: " + packetID);
                }
            };


            MessageEventManager messageEventManager = MessageEventManager.getInstanceFor(connection);
            messageEventManager.addMessageEventNotificationListener(listener);
            DeliveryReceiptManager dm = DeliveryReceiptManager.getInstanceFor(connection);
            dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
            dm.autoAddDeliveryReceiptRequests();
            try {
                if (dm.isSupported(JidCreate.bareFrom(userphonenumber + "@" + Important.getXmppHost()))) {
                    Log.e("Deliveryreceipt", "supported");
                } else {
                    Log.e("Deliveryreceipt", "notsupported");
                }
                dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                    @Override
                    public void onReceiptReceived(Jid fromJid, Jid toJid,
                                                  final String receiptId, Stanza receipt) {
                        Log.e("messagestatus", "received");
                    }
                });
            } catch (SmackException | XmppStringprepException | InterruptedException | XMPPException e) {
                e.printStackTrace();
            }


            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            FormField formField = null;
                            try {
                                formField = FormField.builder("with").setValue(JidCreate.bareFrom(userphonenumber + "@" + Important.getXmppHost())).build();
                            } catch (XmppStringprepException e) {
                                e.printStackTrace();
                            }

                            MamManager manager = MamManager.getInstanceFor(connection);
                            MamManager.MamQueryArgs mamQueryArgs = MamManager.MamQueryArgs.builder()
                                    .limitResultsSince(yesterday())
                                    .queryLastPage().withAdditionalFormField(formField)
                                    .build();


                            
                            try {
                                MamManager.MamQuery mamQuery = manager.queryArchive(mamQueryArgs);
                                messages = new LinkedList<Message>(mamQuery.getMessages());

                                adapter = new ChatMessageAdapter(context, messages);
                                messagerecyclerview.setAdapter(adapter);
                                Log.e("message size", String.valueOf(messages.size()));

                                messagerecyclerview.scrollToPosition(adapter.getItemCount() - 1);
                                messagerecyclerview.setItemViewCacheSize(30);
                                messagerecyclerview.setDrawingCacheEnabled(true);
                                messagerecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                                messagerecyclerview.setItemAnimator(new DefaultItemAnimator());

                                messagerecyclerview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.e("clicked","recyclerviewclicked");
                                        if(invisiblelayout.getVisibility() == View.VISIBLE){
                                            invisiblelayout.setVisibility(View.GONE);
                                            visiblelayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                                messagerecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);




                                        if(recyclerView.getAdapter()!=null && recyclerView.getAdapter().getItemCount()!=0) {


                                            if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_DRAGGING){
                                                if (isbelowviewvisible) {
                                                    isbelowviewvisible = false;
                                                    belowview.setVisibility(View.GONE);
                                                    belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_up));
                                                    TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                                                    showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowdown));
                                                } else {
                                                    isbelowviewvisible = true;
                                                    belowview.setVisibility(View.VISIBLE);
                                                    belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_down));
                                                    TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                                                    showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowupicon));
                                                }
                                            }

                                            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                                                // fragProductLl.setVisibility(View.VISIBLE);
                                                Log.e("scrollstate", "idel");

                                                LinearLayoutManager layoutManager = ((LinearLayoutManager) messagerecyclerview.getLayoutManager());
                                                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                                                int lastvisiblePosition = layoutManager.findLastVisibleItemPosition();

                                                Log.e("first", String.valueOf(firstVisiblePosition));
                                                Log.e("last", String.valueOf(lastvisiblePosition));

                                                boundageposition = userboundage;


                                                ChatMessageAdapter adapter = (ChatMessageAdapter) messagerecyclerview.getAdapter();
                                                for (int i = firstVisiblePosition; i <= lastvisiblePosition; i++) {
                                                    String s = DefaultED(adapter.getItem(i).getBody(), "decode");


                                                    try {
                                                        JSONObject jsonObject = new JSONObject(s);

                                                        if (jsonObject.getString("boundage").equals("1")) {
                                                            if (!boundageposition.equals("1")) {
                                                                RestartActivity("type1");
                                                            }
                                                            isboundageitemavailable = true;
                                                            break;
                                                        } else if (i == lastvisiblePosition) {
                                                            if (!boundageposition.equals("0")) {
                                                                RestartActivity("type0");
                                                                isboundageitemavailable = false;
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }


                                        }else {
                                            if (isbelowviewvisible) {
                                                isbelowviewvisible = false;
                                                belowview.setVisibility(View.GONE);
                                                belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_up));
                                                TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                                                showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowdown));
                                            } else {
                                                isbelowviewvisible = true;
                                                belowview.setVisibility(View.VISIBLE);
                                                belowview.setAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_slide_down));
                                                TransitionManager.beginDelayedTransition(sendmessagelayout,new AutoTransition());
                                                showbelow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrowupicon));
                                            }
                                        }

                                    }
                                });


                            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NotLoggedInException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }
    }


    void setUpData() {
        Log.e("setUpData", "called");
        messagerecyclerview.setAdapter(null);
        adapter = new ChatMessageAdapter(this, messages);
        messagerecyclerview.setAdapter(adapter);
        Log.e("message size", String.valueOf(messages.size()));


        messagerecyclerview.scrollToPosition(adapter.getItemCount() - 1);
        messagerecyclerview.setItemViewCacheSize(30);
        messagerecyclerview.setDrawingCacheEnabled(true);
        messagerecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        messagerecyclerview.setItemAnimator(new DefaultItemAnimator());
        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagerecyclerview.getAdapter() != null)
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 10);

    }

    void setPassword() {
        Dialog dialog = new Dialog(ShowUserMessage.this);
        View vi = getLayoutInflater().inflate(R.layout.setencryptionpassword, null, false);
        EditText setpassword = vi.findViewById(R.id.typepassword);
        TextView cancel = vi.findViewById(R.id.cancel);
        TextView save = vi.findViewById(R.id.save);
        setpassword.setText(passstr);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = setpassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password = "";
                    try {
                        updateUserInfo(1, password);
                        setpasswordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        updateUserInfo(1, password);
                        setpasswordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(vi);
        dialog.show();
    }

    void updateUserInfo(int field, String value) throws JSONException {
        switch (field) {
            case 1: {
                //update password
                helper.updateFriendInformation(userphonenumber, "textpass", value, getLoginInfo("page_no"));
                passstr = value;
                break;
            }
            case 2: {
                //update timer
                helper.updateFriendInformation(userphonenumber, "timer", value, getLoginInfo("page_no"));
                timerstr = value;
                break;
            }
            case 3: {
                //update boundage
                helper.updateFriendInformation(userphonenumber, "boundage", value, getLoginInfo("page_no"));
                boundagestr = value;
                break;
            }
        }
    }

    void showTimerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.numberpicker, null, false);
        EditText setTimer = view1.findViewById(R.id.timer);
        TextView save = view1.findViewById(R.id.save);
        setTimer.setText(timerstr);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = setTimer.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    setTimer.setError("Field can not be emtpty!");
                    setTimer.requestFocus();
                } else {
                    try {
                        updateUserInfo(2, time);
                        if (time.equals("0")) {
                            timerview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                            timersideview.setCardBackgroundColor(getResources().getColor(R.color.red_500));

                        } else {
                            timerview.setCardBackgroundColor(getResources().getColor(R.color.green));
                            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                            timersideview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setView(view1);
        dialog = builder.create();
        dialog.show();
    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) {

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ((Activity) context).runOnUiThread(() -> {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);

            } else {
                Log.e("bitmap", "null");
            }
        });


    }

    @Override
    public void onImageFailure(String failresponse) {

    }


    boolean isKeyboardShowing = false;

    void onKeyboardVisibilityChanged() {
        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagerecyclerview.getAdapter() != null)
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 10);

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("response", response);
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.e("messageincomming", message.getBody());
        MessageEventManager manager = MessageEventManager.getInstanceFor(connection);
        try {
            manager.sendDeliveredNotification(from, message.getStanzaId());
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }


        try {
            String s = DefaultED(message.getBody(), "decode");
            JSONObject jsonObject = null;

            jsonObject = new JSONObject(s);

            String encodec;

            encodec = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);

            Log.e("encodeasdlfkdsaflkdsa", encodec);

            Log.e("odvudmessage", Base64.encodeToString(encodec.getBytes(), Base64.DEFAULT));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        addNewMessage(message);

    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
        Message message = messageBuilder.build();
        Log.e("messageoutgoing", message.getBody());
        try {
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        addNewMessage(message);

    }
//,String isreplied,String stanzaid

    public static void sendMessage(String body, String toJid, String type) {

        try {
            EntityBareJid jid = JidCreate.entityBareFrom(toJid + "@" + Important.getXmppHost());
            char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            StringBuilder sb = new StringBuilder(20);
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }

            JSONObject jsonObject = new JSONObject();


            String encodec;
            Log.e("emoji",body);
            String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(body);
            Log.e("emojiunicode",toServerUnicodeEncoded);

            encodec = EncrytpAndDecrypt(toServerUnicodeEncoded, "encode", passstr);

            String userpassword = passstr;
            String userboundage = boundagestr;
            String userexpiry = timerstr;


            jsonObject.put("body", encodec);
            jsonObject.put("boundage", userboundage);
            jsonObject.put("hash1", Functions.md5(userpassword));
            jsonObject.put("hash2", Functions.Sha1(userpassword));
            jsonObject.put("timestamp", getDate("0"));
            jsonObject.put("expiry", getDate(userexpiry));
            jsonObject.put("type", type);

//            jsonObject.put("isreplied", isreplied);
//            jsonObject.put("stanzaid", stanzaid);



            Log.e("message", body);

            String finalbody = jsonObject.toString();

            Message message = new Message();
            message.setBody(DefaultED(finalbody, "encode"));

            message.setType(Message.Type.chat);
            message.setStanzaId(sb.toString());
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            message.setTo(jid);


            Chat chat = chatManager.chatWith(jid);
            chat.send(message);
            MessageEventManager.addNotificationsRequests(message, false, true, false, false);
            DeliveryReceiptManager.setDefaultAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
            ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
            ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
            DeliveryReceiptRequest.addTo(message);
            //mConnection.sendStanza();
        } catch (SmackException.NotConnectedException e) {
            Log.e("exception1", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("exception2", e.getMessage());
        } catch (XmppStringprepException e) {
            Log.e("exception3", e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void addNewMessage(Message message) {
        messages.add(message);
        Log.e("size ", String.valueOf(messages.size()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();

                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_END;
                    }
                };

                smoothScroller.setTargetPosition(messagerecyclerview.getAdapter().getItemCount());
                messagerecyclerview.getLayoutManager().startSmoothScroll(smoothScroller);

              //  messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());


            }
        });


    }
    static AlertDialog messagelongclickdialog;

    static Bitmap tempbitmap;





    public static class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageResponse, ImageResponseImageView {
        Context context;
        List<Message> list;

        public static final int MESSAGE_TYPE_IN = 1;


        public ChatMessageAdapter(Context context, List<Message> mlist) { // you can pass other parameters in constructor
            this.context = context;
            list = mlist;
        }

        @Override
        public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ((Activity) context).runOnUiThread(() -> {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);

                } else {
                    Log.e("bitmap", "null");
                }
            });
        }

        @Override
        public void onImageFailure(String failresponse) throws JSONException {

        }

        @Override
        public void onImageViewImageResponse(Response response, int code, int requestcode, ImageView imageView) throws JSONException {

            //InputStream inputStream = response.body().byteStream();

            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            if (tempbitmap == null) {
                tempbitmap = bitmap;
            }


            ((Activity) context).runOnUiThread(() -> {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);

                } else {
                    Log.e("bitmap", "null");
                }
            });
        }

        @Override
        public void onImageViewImageFailure(String failresponse) throws JSONException {

        }

        private class MessageInViewHolder extends RecyclerView.ViewHolder {


            private final TextView message, time;
            private final ImageView imageview;
            private final PlayerView videoplayer;
            private final RelativeLayout playerlayout;
            private final ImageView fullscreen;
            private final ImageView file;


            private final TextView expirytimertext;

            private final ImageView timericon;
            private final ImageView boundageicon;
            private final ImageView deliveryreport;
            private final ImageView replaybutton;

            MessageInViewHolder(final View itemView) {
                super(itemView);

                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                imageview = itemView.findViewById(R.id.senderimage);
                videoplayer = itemView.findViewById(R.id.playerview);
                playerlayout = itemView.findViewById(R.id.sendervideo);
                fullscreen = itemView.findViewById(R.id.fullscreene);
                file = itemView.findViewById(R.id.senderfile);

                expirytimertext = itemView.findViewById(R.id.timertext);
                timericon = itemView.findViewById(R.id.timericon);
                boundageicon = itemView.findViewById(R.id.boundageicon);
                deliveryreport = itemView.findViewById(R.id.sendstatus);
                replaybutton = itemView.findViewById(R.id.replaybutton);
            }

            void bind(int position) {

                Message messageModel = list.get(position);
                String mode = DefaultED(messageModel.getBody(), "deocode");


                Log.e("body",mode);



                try {
                    JSONObject jsonObject = new JSONObject(mode);
                    String test = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);
                    String encodec = StringEscapeUtils.unescapeJava(test);

                    Log.e("message", encodec);

                    Log.e("type", jsonObject.getString("type"));


                    if (jsonObject.getString("type").equals("txt")) {
                        //initialize for text message
                        message.setVisibility(View.VISIBLE);
                        imageview.setVisibility(View.GONE);
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                        Log.e("encodec",encodec);
                        message.setText(encodec);

                        message.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//                                View forwordview =inflater.inflate(R.layout.messageforword,null,false);
//                                RecyclerView recyclerView = forwordview.findViewById(R.id.selecefriend);
//                                simpleAdapter adapter = new simpleAdapter(MainActivity.data,context,mode);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                                recyclerView.setAdapter(adapter);
//                                builder.setView(forwordview);
//                                builder.setCancelable(true);
//                                messagelongclickdialog = builder.create();
//                                messagelongclickdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
//                                messagelongclickdialog.show();

                                forwardlayout.setVisibility(View.VISIBLE);
                                invisiblelayout.setVisibility(View.GONE);
                                visiblelayout.setVisibility(View.GONE);
                                TransitionManager.beginDelayedTransition(forwardlayout,new AutoTransition());
                                invisiblebutton.setVisibility(View.VISIBLE);
                                return true;
                            }
                        });
                    } else if (jsonObject.getString("type").equals("image")) {
                        //initialize for imagemessage
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            imageview.setVisibility(View.VISIBLE);
                            loadImage(imageview, encodec);
                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                    } else if (jsonObject.getString("type").equals("file")) {
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            file.setVisibility(View.VISIBLE);

                            file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downloadFile(encodec);
                                }
                            });
                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        imageview.setVisibility(View.GONE);
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                    } else if (jsonObject.getString("type").equals("video")) {
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            playerlayout.setVisibility(View.VISIBLE);

                            playerlayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playerView = videoplayer;
                                    initVideoPlayer(encodec);
                                    mResumePosition = 0L;
                                }
                            });

                            fullscreen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isFullscreene) {
                                        closeFullscreenDialog(playerView);
                                    } else {
                                        enterfullScreene(playerView);
                                    }

                                }
                            });

                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        imageview.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                    }

                    //check for boundage or expiry time
                    String timestamp = jsonObject.getString("timestamp");
                    String expirytime = jsonObject.getString("expiry");
                    String boundagetime = jsonObject.getString("boundage");
                    time.setText(Functions.getMessageTime(jsonObject.getString("timestamp")));
                    if (!boundagetime.equals("0")) {
                        if (list.size() - 1 == position) {
                            if (!boundageposition.equals("1")) {
                                Toast.makeText(context, "Boundage content found Refreshing layout!", Toast.LENGTH_SHORT).show();
                                boundageicon.setVisibility(View.VISIBLE);
                                RestartActivity("type1");
                                isboundageitemavailable = true;
                            } else {
                                if (!isboundageitemavailable) {
                                    RestartActivity("type0");
                                }
                            }
                        }
                    }
                    if (!expirytime.equals(timestamp)) {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                        try {
                            Date expiredate = format.parse(expirytime);
                            Date currentime = format.parse(getDate("0"));
                            Date timestampdate = format.parse(timestamp);
                            if (currentime.compareTo(expiredate) > 0) {
                                message.setText("Message Expired!");
                            } else {
                                long difference = dateDifference(timestampdate, expiredate);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                message.setText("Message Expired!");
                                                messagerecyclerview.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                    }
                                                }, 10);
                                            }
                                        });

                                    }
                                }, difference);
                                //todo timertext show korte hobe
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }




        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

            private final TextView message;
            private final TextView time;
            private final ImageView imageview;
            private final PlayerView videoplayer;
            private final RelativeLayout playerlayout;
            private final ImageView fullscreen;
            private final ImageView file;
            private final TextView expirytimertext;
            private final ImageView timericon;
            private final ImageView boundageicon;
            private final ImageView deliveryreport;
            private final ImageView replaybutton;


            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);

                imageview = itemView.findViewById(R.id.senderimage);
                videoplayer = itemView.findViewById(R.id.playerview);
                playerlayout = itemView.findViewById(R.id.sendervideo);
                fullscreen = itemView.findViewById(R.id.fullscreene);
                file = itemView.findViewById(R.id.senderfile);

                expirytimertext = itemView.findViewById(R.id.timertext);
                timericon = itemView.findViewById(R.id.timericon);
                boundageicon = itemView.findViewById(R.id.boundageicon);
                deliveryreport = itemView.findViewById(R.id.sendstatus);
                replaybutton = itemView.findViewById(R.id.replaybutton);


            }

            void bind(int position) {
                Message messageModel = list.get(position);


                String mode = DefaultED(messageModel.getBody(), "deocode");

                Log.e("body",mode);


                try {
                    JSONObject jsonObject = new JSONObject(mode);

                    String test = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);
                    String encodec = StringEscapeUtils.unescapeJava(test);

                    Log.e("message", encodec);


                    Log.e("type", jsonObject.getString("type"));


                    if (jsonObject.getString("type").equals("txt")) {
                        //initialize for text message
                        message.setVisibility(View.VISIBLE);
                        imageview.setVisibility(View.GONE);
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                        Log.e("encodec",encodec);
                        message.setText(encodec);


                        message.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//                                View forwordview =inflater.inflate(R.layout.messageforword,null,false);
//                                RecyclerView recyclerView = forwordview.findViewById(R.id.selecefriend);
//                                simpleAdapter adapter = new simpleAdapter(MainActivity.data,context,mode);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                                recyclerView.setAdapter(adapter);
//                                builder.setView(forwordview);
//                                builder.setCancelable(true);
//                                messagelongclickdialog = builder.create();
//                                messagelongclickdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
//                                messagelongclickdialog.show();
                                forwardlayout.setVisibility(View.VISIBLE);
                                invisiblelayout.setVisibility(View.GONE);
                                visiblelayout.setVisibility(View.GONE);
                                TransitionManager.beginDelayedTransition(forwardlayout,new AutoTransition());
                                invisiblebutton.setVisibility(View.VISIBLE);


                                return true;
                            }
                        });

                    } else if (jsonObject.getString("type").equals("image")) {
                        //initialize for imagemessage
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            imageview.setVisibility(View.VISIBLE);
                            loadImage(imageview, encodec);

                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                    } else if (jsonObject.getString("type").equals("file")) {
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            file.setVisibility(View.VISIBLE);

                            file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downloadFile(encodec);
                                }
                            });
                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        imageview.setVisibility(View.GONE);
                        playerlayout.setVisibility(View.GONE);
                        fullscreen.setVisibility(View.GONE);
                    } else if (jsonObject.getString("type").equals("video")) {
                        if (URLUtil.isValidUrl(encodec)) {
                            message.setVisibility(View.GONE);
                            playerlayout.setVisibility(View.VISIBLE);


                            playerlayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playerView = videoplayer;
                                    initVideoPlayer(encodec);
                                    mResumePosition = 0L;
                                }
                            });

                            fullscreen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isFullscreene) {
                                        closeFullscreenDialog(playerView);
                                    } else {
                                        enterfullScreene(playerView);
                                    }

                                }
                            });


                        } else {
                            message.setVisibility(View.VISIBLE);
                            message.setText(encodec);
                        }
                        imageview.setVisibility(View.GONE);
                        file.setVisibility(View.GONE);
                    }
                    time.setText(Functions.getMessageTime(jsonObject.getString("timestamp")));

                    //check for boundage or expiry time
                    String timestamp = jsonObject.getString("timestamp");
                    String expirytime = jsonObject.getString("expiry");
                    String boundagetime = jsonObject.getString("boundage");

                    if (!boundagetime.equals("0")) {
                        if (list.size() - 1 == position) {
                            if (!boundageposition.equals("1")) {
                                Toast.makeText(context, "Boundage content found Refreshing layout!", Toast.LENGTH_SHORT).show();
                                RestartActivity("type1");
                                isboundageitemavailable = true;
                            } else {
                                if (!isboundageitemavailable) {
                                    RestartActivity("type0");
                                }
                            }
                        }
                    }
                    if (!expirytime.equals(timestamp)) {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                        try {
                            Date expiredate = format.parse(expirytime);
                            Date currentime = format.parse(getDate("0"));
                            Date timestampdate = format.parse(timestamp);
                            if (currentime.compareTo(expiredate) > 0) {
                                message.setText("Message Expired!");
                            } else {
                                long difference = dateDifference(timestampdate, expiredate);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                message.setText("Message Expired!");
                                                messagerecyclerview.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                    }
                                                }, 10);
                                            }
                                        });

                                    }
                                }, difference);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == MESSAGE_TYPE_IN) {
                return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.bubbleend, parent, false));
            } else {
                return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.bubblestart, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message message = list.get(position);
            Jid jid = null;
            try {
                jid = JidCreate.bareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost());
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }

            if (jid.toString().equals(message.getFrom().toString().split("/")[0])) {
                ((MessageOutViewHolder) holder).bind(position);

            } else if (jid.toString().equals(message.getTo().toString().split("/")[0])) {
                ((MessageInViewHolder) holder).bind(position);

            }
        }

        public Message getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            Message message = list.get(position);
            Jid jid = null;
            try {
                jid = JidCreate.bareFrom(JidCreate.bareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }


            if (message.getFrom().toString().split("/")[0].equals(jid.toString())) {
                return 0;
            } else if (message.getTo().toString().split("/")[0] == jid.toString()) {
                return 1;
            } else {
                return 1;
            }

        }
    }


    static void RestartActivity(String type) {
        Intent i = ((Activity) context).getIntent();
        if (type.equals("type1")) {
            i.putExtra("boundage", "1");
        } else {
            i.putExtra("boundage", "0");
        }

        LinearLayoutManager layoutManager = ((LinearLayoutManager) messagerecyclerview.getLayoutManager());
        ((Activity) context).overridePendingTransition(0, 0);
        i.putExtra("recyclerview", layoutManager.findFirstVisibleItemPosition());

        if (typemessage.getText().toString() != null) {
            i.putExtra("message", typemessage.getText().toString());
        }
        context.startActivity(i);

        ((Activity) context).overridePendingTransition(0, 0);
        ((Activity) context).finish();

    }


    //1 day = 3600 x 24 = 86400
    public static long dateDifference(Date startDate, Date endDate) {
        //milliseconds
        return endDate.getTime() - startDate.getTime();


    }

    private static String getDate(String expiry) {
        String ourDate;
        try {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //this format changeable


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


    @Override
    protected void onDestroy() {
        try {
            updateUserInfo(1, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (playerView != null && player != null) {
            mResumeWindow = player.getCurrentWindowIndex();
            mResumePosition = Math.max(0, player.getContentPosition());
            player.release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (playerView != null)
            initVideoPlayer(tempchecksum);

        if (isFullscreene) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //  mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreene);
        outState.putString(STATE_CHECKSUM, tempchecksum);


        super.onSaveInstanceState(outState);
    }


    static void loadImage(ImageView ImageView, String url) {

        Picasso picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(Functions.getUnsafeOkHttpClient())).build();
        picasso.setLoggingEnabled(true);
        picasso.load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .centerInside()
                .error(R.drawable.applogo)
                .into(ImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("loaderror", e.getMessage());
                    }
                });

    }

    static void downloadFile(String url) {

    }

    static SimpleExoPlayer player;
    private static int mResumeWindow;
    static String tempchecksum;
    private static long mResumePosition;


    public static void initVideoPlayer(String checksum) {
        try {
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new OkHttpDataSourceFactory(Functions.getUnsafeOkHttpClient(), Util.getUserAgent(context, context.getString(R.string.app_name)), defaultBandwidthMeter);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            Uri videoURI = Uri.parse(checksum);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            player.prepare(mediaSource);


        } catch (Exception e) {
            Log.e("errorasdfa", e.toString());
        }

        player.setPlayWhenReady(true);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            if (tempchecksum != null && tempchecksum.equals(checksum)) {
                Log.e("DEBUG", " haveResumePosition ");
                Log.e("position", String.valueOf(mResumePosition));
                player.seekTo(mResumeWindow, mResumePosition);
            }

            tempchecksum = checksum;


        }
//        String contentUrl = getString(url);

        playerView.setPlayer(player);


    }

    private static Dialog mFullScreenDialog;

    static void enterfullScreene(PlayerView playerView) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        isFullscreene = true;
        mFullScreenDialog.show();
    }

    static boolean isFullscreene = false;


    private static void closeFullscreenDialog(PlayerView playerView) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) playerView.getParent()).removeView(playerView);

        if (playerView != null && player != null) {
            player.stop(true);
        }

        isFullscreene = false;
        mFullScreenDialog.dismiss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InitConnection();
                } catch (Settings.SettingNotFoundException e) {
                    Log.e("error2", e.toString());
                }
            }
        }, 1000);


        // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));

    }


    public static class simpleAdapter extends RecyclerView.Adapter<simpleAdapter.ViewHolder> {
        protected ArrayList<JSONObject> listdata;
        protected AlertDialog dialog;

        Context context;
        String message;


        public simpleAdapter(ArrayList<JSONObject> listdata, Context context,String message) {
            Log.e("listviewcxvbcvb", listdata.toString());
            this.listdata = listdata;
            this.context = context;
            this.message = message;
        }

        @Override
        public simpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.forwordtosinglefriend, parent, false);
            return new simpleAdapter.ViewHolder(listItem);
        }


        @Override
        public void onBindViewHolder(@NotNull simpleAdapter.ViewHolder holder, int position) {
            final JSONObject myListData = listdata.get(position);
            try {

                holder.friendname.setText(myListData.getString("name"));

                holder.sendbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (MainActivity.connection != null && MainActivity.connection.isConnected() && MainActivity.connection.isAuthenticated()) {
//                            try {
//                                //sendMessage(message,myListData.getString("phone"),"txt");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }

                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        protected JSONObject mylistdata;


        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView sendbutton;
            public TextView friendname;


            public ViewHolder(View itemView) {
                super(itemView);

                this.sendbutton = itemView.findViewById(R.id.sendtofriendbutton);
                this.friendname = (TextView) itemView.findViewById(R.id.friendname);

            }
        }
    }


}