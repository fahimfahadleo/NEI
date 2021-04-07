package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServerResponse {

    ImageView search;
    LinearLayout searchlayout;
    static ListView listView;
    public static boolean active = false;
    DatabaseHelper helper;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        active = true;
//
//
//        if (!Boolean.parseBoolean(Functions.getSharedPreference("loginstatus", "false"))) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        } else {
//            if(!Functions.isActive){
//                Cursor cursor = helper.getUserPassword(1);
//                if (cursor != null && cursor.getCount() > 0) {
//                    startActivity(new Intent(this, UserVerificationActivity.class));
//                } else {
//                    startActivity(new Intent(this, RegisterVerifiedUser.class));
//                }
//            }
//
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search);
        searchlayout = findViewById(R.id.searchlayout);
        searchlayout.setVisibility(View.GONE);
        listView = findViewById(R.id.listview);

        String s = stringFromJNI();


        Log.e("native",s);

        //ServerRequest.ServerRequest(this);
        new Functions(this);
        helper = new DatabaseHelper(this);
        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));


//        if (Functions.getSharedPreference("private_token", null) != null || System.currentTimeMillis() <= expires) {
//            ServerRequest.GetAllMessage(this);
//        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isActive) {
                    TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                    TransitionManager.beginDelayedTransition(listView, new AutoTransition());
                    searchlayout.setVisibility(View.GONE);
                    Functions.isActive = false;
                } else {
                    TransitionManager.beginDelayedTransition(listView, new AutoTransition());
                    TransitionManager.beginDelayedTransition(searchlayout, new AutoTransition());
                    searchlayout.setVisibility(View.VISIBLE);
                    Functions.isActive = true;
                }
            }
        });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onFailure(String failresponse) {

    }
}