package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfflineActivity extends AppCompatActivity {
    static{
        System.loadLibrary("native-lib");
    }

    static native String EncrytpAndDecrypt(String message, String type, String password);


    //static native String DefaultED(String message, String type);

    ImageView backbutton, setEncryptionpasswordtop, setboundagetop, settimertop, menu, sendbutton;
    CardView setEncryptionpasswordmenuview,  ignoreview, removeview, toppasswordview, toptimerview, topboundageview;
    TextView setEncryptionpasswordmenu, ignore, remove, titletext,  passwordtip;
    EditText message;
    RecyclerView recyclerView;
    static Context context;
    DrawerLayout drawerLayout;
    public static Intent i;
    public static boolean isActivityVisible = false;
    public static List<JSONObject> messagelist;

    @Override
    protected void onStart() {
        super.onStart();
        isActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    int me = 0;
    int him = 0;



    String offlinepassstr = "";

    boolean isKeyboardShowing = false;
    AlertDialog timerdialog;
    public static ChatMessageAdapter adapter;

    @Override
    protected void onDestroy() {
        try {
            updateUserInfo(1, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isActivityVisible = false;
        super.onDestroy();
    }




    void setScrollPosition() {
        if (getIntent().hasExtra("recyclerview")) {
            message.setText(getIntent().getStringExtra("message"));
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(getIntent().getIntExtra("recyclerview", 0));
                }
            }, 10);

        } else {

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                }
            }, 10);

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

    void setUpData() {
        Log.e("setUpData", "called");
        recyclerView.setAdapter(null);
        adapter = new ChatMessageAdapter(this, messagelist);
        recyclerView.setAdapter(adapter);
        Log.e("message size", String.valueOf(messagelist.size()));


        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView.getAdapter() != null)
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        }, 10);

    }

    void setPassword() {
        Dialog dialog = new Dialog(OfflineActivity.this);
        View vi = getLayoutInflater().inflate(R.layout.setencryptionpassword, null, false);
        EditText setpassword = vi.findViewById(R.id.typepassword);
        TextView cancel = vi.findViewById(R.id.cancel);
        TextView save = vi.findViewById(R.id.save);
        setpassword.setText(offlinepassstr);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = setpassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password = "";
                    try {
                        updateUserInfo(1, password);
                        toppasswordview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                        setEncryptionpasswordmenuview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        updateUserInfo(1, password);
                        toppasswordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setEncryptionpasswordmenuview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
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
              //  DatabaseHelper.updateFriendInformation(userphonenumber, "textpass", value, getLoginInfo("page_no"));
                Functions.setSharedPreference("textpass",value);
                offlinepassstr = value;
                break;
            }
        }
    }

    void onKeyboardVisibilityChanged() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView.getAdapter() != null)
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        }, 10);

    }


    void loadSMS(String types){
        Uri mSmsinboxQueryUri = Uri.parse(types);
        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri,new String[] { "_id", "thread_id", "address", "person", "date","body", "type" }, null, null, null);
        startManagingCursor(cursor1);
//        CursorLoader loader = new CursorLoader(this);
//        loader.deliverResult(cursor1);

        String[] columns = new String[] { "address", "person", "date", "body","type" };
        if (cursor1.getCount() > 0) {
            String count = Integer.toString(cursor1.getCount());
            while (cursor1.moveToNext()){

                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
                Log.e("address",address);
                if(!address.contains("+88")){
                    address = "+88"+address;
                }
                if(address.equalsIgnoreCase(i.getStringExtra("data"))){ //put your number here
                    String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
                    String date = cursor1.getString(cursor1.getColumnIndex(columns[2]));
                    String body = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                    String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));
                    Log.e("smstag", "body="+body);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if(name==null){
                            jsonObject.put("name","defaultname");
                        }else {
                            jsonObject.put("name",name);
                        }

                        jsonObject.put("date",date);
                        jsonObject.put("body",body);
                        jsonObject.put("type",type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    messagelist.add(jsonObject);
                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        backbutton = findViewById(R.id.backbutton);
        setEncryptionpasswordmenuview = findViewById(R.id.setpasswordview);
        setEncryptionpasswordmenu = findViewById(R.id.setpasswordtext);
        setEncryptionpasswordtop = findViewById(R.id.security).findViewById(R.id.password);
        setboundagetop = findViewById(R.id.security).findViewById(R.id.enableboundage);
        settimertop = findViewById(R.id.security).findViewById(R.id.timer);
        menu = findViewById(R.id.menubutton);
        sendbutton = findViewById(R.id.sendbutton);
        passwordtip = findViewById(R.id.encryptionpasstips);


        ignoreview = findViewById(R.id.ignoreview);


        removeview = findViewById(R.id.removeview);

        toppasswordview = findViewById(R.id.security).findViewById(R.id.passwordview);
        toptimerview = findViewById(R.id.security).findViewById(R.id.timerview);
        topboundageview = findViewById(R.id.security).findViewById(R.id.enableboundageview);



        ignore = findViewById(R.id.ignore);
        remove = findViewById(R.id.remove);
        titletext = findViewById(R.id.titletext);
        message = findViewById(R.id.typemessage);
        recyclerView = findViewById(R.id.messagerecyclerview);
        drawerLayout = findViewById(R.id.drawer);
        new Functions(this);
        i = getIntent();
        Log.e("data", i.getStringExtra("data"));
        messagelist = new ArrayList<>();
        
        loadSMS("content://sms");

        Log.e("smslist",messagelist.toString());

        Collections.reverse(messagelist);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        setUpData();
        topboundageview.setVisibility(View.GONE);
        toptimerview.setVisibility(View.GONE);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Log.e("passwordstr",offlinepassstr);








        passwordtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getString(R.string.passwordtip));
            }
        });




        context = this;
        titletext.setText(i.getStringExtra("data"));


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // takeScreenshot();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });







        setEncryptionpasswordtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        toppasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });

        setEncryptionpasswordmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });

        setEncryptionpasswordmenuview.setOnClickListener(new View.OnClickListener() {
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


        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mess = charSequence.toString();
                sendbutton.setEnabled(mess != null);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagestr = message.getText().toString();
                if (!TextUtils.isEmpty(messagestr)) {
                    message.setText(null);
                    //send message
                    SendSMS(i.getStringExtra("data"),messagestr);
                }

            }
        });

        setScrollPosition();

    }


    SubscriptionInfo simcardid = null;
    SubscriptionInfo simInfo1, simInfo2;


    private void SendSMS(final String phoneNumber, final String message) {

        String finalmessage = EncrytpAndDecrypt(message,"encode",offlinepassstr);


        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        Log.e("message", message);

        final PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        final PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        try {
                            String name = messagelist.get(0).getString("name");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("date",getDate());
                            jsonObject.put("body",finalmessage);
                            jsonObject.put("type","2");
                            jsonObject.put("name",name);

                            messagelist.add(jsonObject);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure, Probably your Sms is too long for this app.",
                                Toast.LENGTH_SHORT).show();


                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));


        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(OfflineActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                simInfo1 = (SubscriptionInfo) localList.get(0);
                simInfo2 = (SubscriptionInfo) localList.get(1);

                if (simcardid != null) {
                    SmsManager.getSmsManagerForSubscriptionId(simcardid.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(OfflineActivity.this);
                    builder.setTitle("Choose Sim Card");
                    builder.setMessage("Choose Your Default Sim Card.");
                    builder.setPositiveButton("SIM 1", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            simcardid = simInfo1;
                            SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
                        }
                    });
                    builder.setNegativeButton("SIM 2", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            simcardid = simInfo2;
                            SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
                        }
                    });

                    builder.create();
                    builder.show();
                }

            } else {
                SmsManager manager = SmsManager.getDefault();


                Log.e("messagelength", phoneNumber);
                manager.sendTextMessage(phoneNumber, null, finalmessage, sentPI, deliveredPI);

            }
            return;
        } else {
            ActivityCompat.requestPermissions(OfflineActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
    }

    private String getDate() {
        String ourDate;
        try {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //this format changeable


            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (Integer.parseInt("0") * 60000));

            ourDate = dateFormatter.format(afterAddingTenMins);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }

    public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        List<JSONObject> list;

        public static final int MESSAGE_TYPE_IN = 1;


        public ChatMessageAdapter(Context context, List<JSONObject> mlist) { // you can pass other parameters in constructor
            this.context = context;
            list = mlist;
        }


        private class MessageInViewHolder extends RecyclerView.ViewHolder {


            private final TextView message, time;
            private final CircleImageView profilepicture;

            MessageInViewHolder(final View itemView) {
                super(itemView);

                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) throws JSONException {
                him++;
                me = 0;
                if (him > 1) {
                    profilepicture.setVisibility(View.INVISIBLE);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("friend", getIntent().getStringExtra("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject messageModel = list.get(position);

                String encodec;
                encodec = EncrytpAndDecrypt(messageModel.getString("body"), "decode", offlinepassstr);

                message.setText(encodec);
                time.setText(messageModel.getString("date"));

            }
        }

        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

            private final TextView message;
            private final TextView time;
            private final CircleImageView profilepicture;


            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) throws JSONException {
                him = 0;
                profilepicture.setVisibility(View.INVISIBLE);
                JSONObject messageModel = list.get(position);
                String encodec;
                encodec = EncrytpAndDecrypt(messageModel.getString("body"), "decode", offlinepassstr);

                message.setText(encodec);
                time.setText(messageModel.getString("date"));

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == MESSAGE_TYPE_IN) {
                return new ChatMessageAdapter.MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.bubbleend, parent, false));
            } else {
                return new ChatMessageAdapter.MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.bubblestart, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            JSONObject message = list.get(position);


            try {
                if (message.getString("type").equals("2")) {
                    ((MessageOutViewHolder) holder).bind(position);

                } else if (message.getString("type").equals("1")) {
                    ((MessageInViewHolder) holder).bind(position);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public JSONObject getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            JSONObject message = list.get(position);


            try {
                if (message.getString("type").equals("2")) {
                    return 0;
                } else if (message.getString("type").equals("1")) {
                    return 1;
                } else {
                    return 1;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
    }


}