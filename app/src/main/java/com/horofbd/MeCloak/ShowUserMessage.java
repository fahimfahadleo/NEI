package com.horofbd.MeCloak;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.logging.LoggingEventListener;

import static com.horofbd.MeCloak.MainActivity.adapter;
import static com.horofbd.MeCloak.MainActivity.connection;

public class ShowUserMessage extends AppCompatActivity implements ServerResponse, IncomingChatMessageListener, OutgoingChatMessageListener {

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void publicKeyRequest(ServerResponse serverResponse, String name, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);

    TextInputEditText typemessage;
    ImageView send;
    List<Message> messages;
    ChatManager chatManager;
    RecyclerView messagerecyclerview;
    int me = 0;
    int him =0;
    DrawerLayout drawerLayout;
    ChatMessageAdapter adapter;

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    void InitConnection(){
        if (connection != null) {
            Log.e("connection", "notnull");
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this);
            chatManager.addOutgoingListener(this);

            ChatStateManager chatmanager = ChatStateManager.getInstance(connection);
            chatmanager.addChatStateListener(new ChatStateListener() {
                @Override
                public void stateChanged(Chat chat, ChatState state, Message message) {
                    Log.e("chatstate",String.valueOf(state));
                }
            });



            MessageEventNotificationListener listener = new MessageEventNotificationListener() {
                @Override
                public void deliveredNotification(Jid from, String packetID) {
                    Log.e("messaereceived","from: "+from.toString()+" id: "+packetID);
                }

                @Override
                public void displayedNotification(Jid from, String packetID) {
                    Log.e("notification","from: "+from.toString()+" id: "+packetID);
                }

                @Override
                public void composingNotification(Jid from, String packetID) {
                    Log.e("typing","from: "+from.toString()+" id: "+packetID);
                }

                @Override
                public void offlineNotification(Jid from, String packetID) {
                    Log.e("offline","from: "+from.toString()+" id: "+packetID);
                }

                @Override
                public void cancelledNotification(Jid from, String packetID) {
                    Log.e("cancelled","from: "+from.toString()+" id: "+packetID);
                }
            };

            MessageEventManager messageEventManager = MessageEventManager.getInstanceFor(connection);
            messageEventManager.addMessageEventNotificationListener(listener);
            DeliveryReceiptManager dm = DeliveryReceiptManager.getInstanceFor(connection);
            dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
            dm.autoAddDeliveryReceiptRequests();
            try {
                if(dm.isSupported(JidCreate.bareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost()))){
                    Log.e("Deliveryreceipt","supported");
                }else {
                    Log.e("Deliveryreceipt","notsupported");
                }
                dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                    @Override
                    public void onReceiptReceived(Jid fromJid, Jid toJid,
                                                  final String receiptId, Stanza receipt) {
                        Log.e("messagestatus","received");
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
                messages =  new LinkedList<Message>(mamQuery.getMessages());;
                Log.e("list", messages.toString());

                adapter = new ChatMessageAdapter(this,messages);
                messagerecyclerview.setAdapter(adapter);
                Log.e("message size",String.valueOf(messages.size()));


                messagerecyclerview.scrollToPosition(adapter.getItemCount()-1);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_message);
        messagerecyclerview = findViewById(R.id.messagerecyclerview);
        typemessage = findViewById(R.id.typemessage);
        send = findViewById(R.id.sendbutton);
        InitLinks();
        InitConnection();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(manager);
       drawerLayout=findViewById(R.id.drawer);








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
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });






        if (!Functions.fileExists(getIntent().getStringExtra("phone_no") + ".key", this)) {
            publicKeyRequest(this, getIntent().getStringExtra("phone_no"), "GET", Important.getPublickey() + "?user_id=" + getIntent().getStringExtra("id"), new JSONObject(), 21, this);
        }
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
                    chat = chatManager.chatWith(JidCreate.entityBareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost()));
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
                    sendMessage(message, getIntent().getStringExtra("phone_no"));
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
                messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 100);

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.e("messageincomming", message.getBody());
        MessageEventManager manager = MessageEventManager.getInstanceFor(connection);
        try {
            manager.sendDeliveredNotification(from,message.getStanzaId());
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
        addNewMessage(message);

    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
        Message message = messageBuilder.build();
        try {
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        Log.e("messageoutgoing", message.getTo().toString());
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
            model.setBody(body);
            model.setBoundage("0");
            model.setEncryptionpass("64742812");
            model.setTimestamp(getDate());
            model.setExpiry("none");

            Gson gson = new Gson();
            String finalbody = gson.toJson(model);
            Message message = new Message();
            message.setType(Message.Type.chat);
            message.setBody(finalbody);
            message.setStanzaId(sb.toString());
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            message.setTo(jid);


            Chat chat = chatManager.chatWith(jid);
            chat.send(message);
            MessageEventManager.addNotificationsRequests( message, false, true, false, false );
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


    void addNewMessage(Message message){
        messages.add(message);
        Log.e("size ",String.valueOf(messages.size()));
        adapter.notifyDataSetChanged();

        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 100);


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


            private TextView message,time;
            private CircleImageView profilepicture;

            MessageInViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him++;
                me=0;
                if(him>1){
                    profilepicture.setVisibility(View.INVISIBLE);
                }
                Message messageModel = list.get(position);
                Gson gson = new Gson();
                //final ChatModel chatModel = gson.fromJson(messageModel.getBody(), ChatModel.class);
                message.setText("Someuser Sent A amessage "+position);
                time.setText("10/12/21");
            }
        }

        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

           private TextView message,time;
            private CircleImageView profilepicture;

            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him=0;
                profilepicture.setVisibility(View.INVISIBLE);
                Message messageModel = list.get(position);
                Gson gson = new Gson();
                //final ChatModel chatModel = gson.fromJson(messageModel.getBody(), ChatModel.class);
                message.setText("I sent a message "+position);
                time.setText("10/12/21");
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.e("messagetypecreateviewholder",String.valueOf(viewType));
            if (viewType == MESSAGE_TYPE_IN) {
                return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.bubbleend, parent, false));
            }else {
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

            } else if(jid.toString().equals(message.getTo().toString().split("/")[0])) {
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
                 jid= JidCreate.bareFrom(JidCreate.bareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }

            Log.e("messagefrom",message.getFrom().toString());

            if(message.getFrom().toString().split("/")[0].equals(jid.toString())){
                Log.e("itemtype","fromme");
                return 0;
            }else if(message.getTo().toString().split("/")[0]==jid.toString()){
                Log.e("itemtype","fromhim");
                return 1;
            }else {
                return 1;
            }

        }
    }

    private String getDate()
    {
        String ourDate;
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getDefault());
            Date value = formatter.parse(String.valueOf(System.currentTimeMillis()));

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        }
        catch (Exception e)
        {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }

}