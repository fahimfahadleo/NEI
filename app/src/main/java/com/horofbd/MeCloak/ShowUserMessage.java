package com.horofbd.MeCloak;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.codec.DecoderException;
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
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.mam.MamManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.omemo.OmemoManager;
import org.jivesoftware.smackx.omemo.OmemoMessage;
import org.jivesoftware.smackx.omemo.OmemoService;
import org.jivesoftware.smackx.omemo.exceptions.CorruptedOmemoKeyException;
import org.jivesoftware.smackx.omemo.exceptions.CryptoFailedException;
import org.jivesoftware.smackx.omemo.exceptions.UndecidedOmemoIdentityException;
import org.jivesoftware.smackx.omemo.internal.OmemoDevice;
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener;
import org.jivesoftware.smackx.omemo.listener.OmemoMucMessageListener;
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore;
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore;
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService;
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint;
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback;
import org.jivesoftware.smackx.omemo.trust.TrustState;
import org.jivesoftware.smackx.pubsub.PubSubException;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.whispersystems.libsignal.IdentityKey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.logging.LoggingEventListener;

import static com.horofbd.MeCloak.MainActivity.adapter;
import static com.horofbd.MeCloak.MainActivity.connection;

public class ShowUserMessage extends AppCompatActivity implements ServerResponse, IncomingChatMessageListener, OutgoingChatMessageListener {

    static {
        System.loadLibrary("native-lib");
    }

    CardView setpasswordview;
    TextView setPassword;


    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void publicKeyRequest(ServerResponse serverResponse, String name, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);

    static native String EncrytpAndDecrypt(String message, String type, String password);

    static native void saveUserData(String key,String value);


    static native String DefaultED(String message, String type);


    TextInputEditText typemessage;
    ImageView send;
    List<Message> messages;
    ChatManager chatManager;
    RecyclerView messagerecyclerview;
    int me = 0;
    int him = 0;
    DrawerLayout drawerLayout;
    ChatMessageAdapter adapter;
    EditText typepassword;
    OmemoManager omemoManager;
    ImageView timer, enableboundage;
    CardView timerview, enableboundageview;
    DatabaseHelper helper;



    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    void InitConnection() throws Settings.SettingNotFoundException {
        if (connection != null) {
            Log.e("connection", "notnull");
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this);
            chatManager.addOutgoingListener(this);


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
                if (dm.isSupported(JidCreate.bareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost()))) {
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


            FormField formField = null;
            try {
                formField = FormField.builder("with").setValue(JidCreate.bareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost())).build();
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
                ;
                Log.e("list", messages.toString());

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
                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                    }
                }, 100);


            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NotLoggedInException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void setPassword() {
        Dialog dialog = new Dialog(ShowUserMessage.this);
        View vi = getLayoutInflater().inflate(R.layout.setencryptionpassword, null, false);
        EditText setpassword = vi.findViewById(R.id.typepassword);
        TextView cancel = vi.findViewById(R.id.cancel);
        TextView save = vi.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = setpassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    setpassword.setError("Field Can Not be empty!");
                    setpassword.requestFocus();
                } else {
                    try {
                        updateUserInfo(1,password);
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
                helper.UpdateFriendInformation(userphonenumber,"textpass",value,getLoginInfo("page_no"));
                passstr = value;
                break;
            }
            case 2: {
                //update timer
                helper.UpdateFriendInformation(userphonenumber,"timer",value,getLoginInfo("page_no"));
                timerstr = value;
                break;
            }
            case 3: {
                //update boundage
                helper.UpdateFriendInformation(userphonenumber,"boundage",value,getLoginInfo("page_no"));
                boundagestr = value;
                break;
            }
        }
    }
    AlertDialog dialog;
    void showTimerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.numberpicker, null, false);
        EditText setTimer = view1.findViewById(R.id.timer);
        TextView save = view1.findViewById(R.id.save);
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
                        } else {
                            timerview.setCardBackgroundColor(getResources().getColor(R.color.green));
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


    String passstr = "";
    String timerstr="";
    String boundagestr="";
    String userphonenumber;
    boolean isDatabaseAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_message);
        messagerecyclerview = findViewById(R.id.messagerecyclerview);
        typemessage = findViewById(R.id.typemessage);
        send = findViewById(R.id.sendbutton);
        InitLinks();
        timer = findViewById(R.id.timer);
        timerview = findViewById(R.id.timerview);
        enableboundage = findViewById(R.id.enableboundage);
        enableboundageview = findViewById(R.id.enableboundageview);
        helper = new DatabaseHelper(this);
        userphonenumber = getIntent().getStringExtra("phone_no");

        Cursor c = helper.getData(userphonenumber,getLoginInfo("page_no"));
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String data2 = c.getString(c.getColumnIndex("phonenumber"));
            passstr = c.getString(c.getColumnIndex("textpass"));
            boundagestr = c.getString(c.getColumnIndex("boundage"));
            timerstr = c.getString(c.getColumnIndex("timer"));
            isDatabaseAvailable =true;
        }
            c.close();

            if(!isDatabaseAvailable){
                helper.setFriendInformation(userphonenumber,"","0","0",getLoginInfo("page_no"));
                passstr = "";
                timerstr = "0";
                boundagestr = "0";
            }



        if (!timerstr.equals("0")) {
            timerview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        if (!boundagestr.equals("0")) {
            enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        timerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        enableboundageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!boundagestr.equals("0")) {
                        updateUserInfo(3, "0");
                        enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                    } else {
                        updateUserInfo(3, "1");
                        enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        enableboundage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (!boundagestr.equals("0")) {
                        updateUserInfo(3, "0");
                        enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                    } else {
                        updateUserInfo(3, "1");
                        enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            InitConnection();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(manager);
        drawerLayout = findViewById(R.id.drawer);
        typepassword = findViewById(R.id.security).findViewById(R.id.typepassword);
        setPassword = findViewById(R.id.setpasswordtext);
        setpasswordview = findViewById(R.id.setpasswordview);


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
                                onKeyboardVisibilityChanged(true);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false);
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
                    chat = chatManager.chatWith(JidCreate.entityBareFrom(userphonenumber+ "@" + Important.getXmppHost()));
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
                    sendMessage(message, userphonenumber);
                }

            }
        });

        InitLinks();


    }


    boolean isKeyboardShowing = false;

    void onKeyboardVisibilityChanged(boolean opened) {
        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagerecyclerview.getAdapter() != null)
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 100);

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
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

        addNewMessage(message);

    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
        Message message = messageBuilder.build();
        Log.e("messageincomming", message.getBody());
        try {
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        addNewMessage(message);

    }


    public void sendMessage(String body, String toJid) {

        try {
            EntityBareJid jid = JidCreate.entityBareFrom(toJid + "@" + Important.getXmppHost());
            //org.jivesoftware.smack.chat2.Chat chat = org.jivesoftware.smack.chat2.ChatManager.getInstanceFor(mConnection).chatWith(jid);
            char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            StringBuilder sb = new StringBuilder(20);
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }


            ChatModel model = new ChatModel();


            String encodec;


            encodec = EncrytpAndDecrypt(body, "encode", passstr);

            String userpassword = passstr;
            String userboundage = boundagestr;
            String userexpiry = timerstr;




            model.setBody(encodec);
            model.setBoundage(userboundage);
            model.setHash1(Functions.md5(userpassword));
            model.setHash2(Functions.Sha1(userpassword));
            model.setTimestamp(getDate("0"));
            model.setExpiry(getDate(userexpiry));

            Gson gson = new Gson();
            String finalbody = gson.toJson(model);


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
        }
    }


    void addNewMessage(Message message) {
        messages.add(message);
        Log.e("size ", String.valueOf(messages.size()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                    }
                }, 100);
            }
        });


    }

    void setUpBoundage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowUserMessage.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
            }
        });

    }

    public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        List<Message> list;

        public static final int MESSAGE_TYPE_IN = 1;


        public ChatMessageAdapter(Context context, List<Message> mlist) { // you can pass other parameters in constructor
            this.context = context;
            list = mlist;
        }

        private class MessageInViewHolder extends RecyclerView.ViewHolder {


            private TextView message, time;
            private CircleImageView profilepicture;

            MessageInViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him++;
                me = 0;
                if (him > 1) {
                    profilepicture.setVisibility(View.INVISIBLE);
                }
                Message messageModel = list.get(position);
                Gson gson = new Gson();
                String s = DefaultED(messageModel.getBody(), "decode");

                final ChatModel chatModel = gson.fromJson(s, ChatModel.class);

                String encodec;

                encodec = EncrytpAndDecrypt(chatModel.getBody(), "decode", passstr);

                String timestamp = chatModel.getTimestamp();
                String expirytime = chatModel.getExpiry();
                String boundagetime = chatModel.getBoundage();
                if(!boundagetime.equals("0")){
                    setUpBoundage();
                }

                Log.e("timestamp",timestamp);
                Log.e("expirytime",expirytime);


                message.setText(encodec);
                time.setText(chatModel.getTimestamp());

                if(!expirytime.equals(timestamp)){
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                    try {
                        Date expiredate = format.parse(expirytime);
                        Date currentime = format.parse(getDate("0"));
                        Date timestampdate = format.parse(timestamp);


                        if(currentime.compareTo(expiredate)>0){
                            message.setText("Message Expired!");
                        }else {
                            long difference = dateDifference(timestampdate,expiredate);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            message.setText("Message Expired!");
                                            messagerecyclerview.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                }
                                            }, 100);
                                        }
                                    });

                                }
                            },difference);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }



            }
        }

        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

            private TextView message, time;
            private CircleImageView profilepicture;

            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him = 0;
                profilepicture.setVisibility(View.INVISIBLE);
                Message messageModel = list.get(position);
                Gson gson = new Gson();
                String mode = DefaultED(messageModel.getBody(), "deocode");
                final ChatModel chatModel = gson.fromJson(mode, ChatModel.class);
                String encodec;
                encodec = EncrytpAndDecrypt(chatModel.getBody(), "decode", passstr);
                String timestamp = chatModel.getTimestamp();
                String expirytime = chatModel.getExpiry();
                String boundagetime = chatModel.getBoundage();
                message.setText(encodec);
                time.setText(chatModel.getTimestamp());

                if(!boundagetime.equals("0")){
                    setUpBoundage();
                }


                if(!expirytime.equals(timestamp)){
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                    try {
                        Date expiredate = format.parse(expirytime);
                        Date currentime = format.parse(getDate("0"));
                        Date timestampdate = format.parse(timestamp);
                        if(currentime.compareTo(expiredate)>0){
                            message.setText("Message Expired!");
                        }else {
                            long difference = dateDifference(timestampdate,expiredate);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            message.setText("Message Expired!");
                                            messagerecyclerview.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                }
                                            }, 100);
                                        }
                                    });

                                }
                            },difference);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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


    //1 day = 3600 x 24 = 86400
    public long dateDifference(Date startDate, Date endDate) {
        //milliseconds
       return endDate.getTime() - startDate.getTime();



    }

    private String getDate(String expiry) {
        String ourDate;
        try {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //this format changeable


            Calendar date = Calendar.getInstance();
            long t= date.getTimeInMillis();
            Date afterAddingTenMins=new Date(t + (Integer.parseInt(expiry) * 60000));

            ourDate = dateFormatter.format(afterAddingTenMins);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }

}