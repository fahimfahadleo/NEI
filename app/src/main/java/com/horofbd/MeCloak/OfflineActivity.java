package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class OfflineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);


    }


    SubscriptionInfo simcardid = null;
    SubscriptionInfo simInfo1, simInfo2;



    private void SendSMS(final String phoneNumber, final String message) {
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
                manager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

            }
            return;
        } else {
            ActivityCompat.requestPermissions(OfflineActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
    }






}