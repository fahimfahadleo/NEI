package com.horofbd.MeCloak;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import static com.horofbd.MeCloak.MainActivity.active;
import static com.horofbd.MeCloak.OfflineActivity.adapter;
import static com.horofbd.MeCloak.OfflineActivity.isActivityVisible;
import static com.horofbd.MeCloak.OfflineActivity.messagelist;
import static com.horofbd.MeCloak.OfflineChatList.chatlist;
import static com.horofbd.MeCloak.OfflineChatList.isChatListActive;


public class SMSRECEIVER extends BroadcastReceiver {
    Context context;


    void triggernotification(String address) throws JSONException {
        Intent i;
        if(isChatListActive){
            i = new Intent(context,OfflineActivity.class);
            i.putExtra("data",address);
            showNotification(address,i);
        }else if(isActivityVisible){
            for(JSONObject j:chatlist){
                if(j.getString("number").equals(address)){
                    if(!OfflineActivity.i.getStringExtra("data").equals(address)){
                        i = new Intent(context,OfflineActivity.class);
                        i.putExtra("data",address);
                        showNotification(address,i);
                    }
                }
            }

        }else if(Functions.isInternetAvailable()){
            if(active){
                i = new Intent(context,OfflineActivity.class);
                i.putExtra("data",address);
                showNotification(address,i);
            }
        }

    }

    void showNotification(String address,Intent i){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon))
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("MeCloak")
                        .setContentText("New Message Received from "+address);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);


        String name ="NotificationChannelName";
        String descriptionText = "SomeDescription";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel("NOTIFICATION_CHANNEL_ID", name, importance);
        mChannel.setDescription(descriptionText);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this




        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mBuilder.setChannelId("NOTIFICATION_CHANNEL_ID");
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Log.e("receiver","sms received");
        Toast.makeText(context,"sms received",Toast.LENGTH_SHORT).show();
        final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    Log.e("TAG", "Message recieved: " + messages[0].getMessageBody());
                    Log.e("TAG", "Message recieved: " + messages[0].getOriginatingAddress());
                    if(isActivityVisible){
                        if(messagelist.size()!=0){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                String name = messagelist.get(0).getString("name");
                                jsonObject.put("name",name);
                                jsonObject.put("body",messages[0].getMessageBody());
                                jsonObject.put("date",System.currentTimeMillis());
                                jsonObject.put("type","1");
                                messagelist.add(jsonObject);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("name",messages[0].getMessageBody());
                                jsonObject.put("body",messages[0].getMessageBody());
                                jsonObject.put("date",System.currentTimeMillis());
                                jsonObject.put("type","1");
                                messagelist.add(jsonObject);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        try {
                            triggernotification(messages[0].getOriginatingAddress());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

    }
}
