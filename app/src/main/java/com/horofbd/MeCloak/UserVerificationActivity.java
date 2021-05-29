package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserVerificationActivity extends AppCompatActivity implements ServerResponse {

    EditText code;
    TextView proceedbutton;
    DatabaseHelper helper;
    TextView registernewpage;
    String userpass;
    EditText autotext;
    LinearLayout passwordfieldlayout;
    TextView initialize;
    CircleImageView profilepicture;
    ImageView nameedit;
    TextView username;
    CardView view;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);


    int point = 0;

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 40) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    AlertDialog dialog;

    private void initDialogue() {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.nameeditmenu, null, false);
        CardView viewprofileview = VI.findViewById(R.id.viewprofileview);
        TextView viewprofile = VI.findViewById(R.id.viewprofile);
        CardView changeprofileview = VI.findViewById(R.id.changeprofileview);
        TextView changeprofile = VI.findViewById(R.id.changeprofile);
        CardView changeavatarview = VI.findViewById(R.id.changeavatarview);
        TextView changeavatar = VI.findViewById(R.id.changeavatar);

        // create the popup window

        builder.setView(VI);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
        dialog.show();


    }


    private void ChangeName() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.changename, null, false);
        EditText namefield = VI.findViewById(R.id.profilename);
        TextView save = VI.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserVerificationActivity.this, "Name Changed", Toast.LENGTH_SHORT).show();
            }
        });
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(VI, width, height, focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAsDropDown(username);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        registernewpage = findViewById(R.id.registernewpage);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);
        autotext = findViewById(R.id.autotext);
        passwordfieldlayout = findViewById(R.id.passwordfieldlayout);
        initialize = findViewById(R.id.initialize);
        profilepicture = findViewById(R.id.profile_image);
        nameedit = findViewById(R.id.nameedit);
        username = findViewById(R.id.username);
        view = findViewById(R.id.view);






        nameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeName();
            }
        });


        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogue();
            }
        });


//        String s = "~+8801914616453@MeCloak> auth status\n" +
//                "~+8801914616453@MeCloak> connected to Fahim Fahad Leon\n" +
//                "~+8801914616453@MeCloak> requesting session\n" +
//                "~+8801914616453@MeCloak> accepted with (" + getSaltString() + ")\n" +
//                "~+8801914616453@MeCloak> generating request id\n" +
//                "~+8801914616453@MeCloak> successful!!\n" +
//                "~+8801914616453@MeCloak> loading horoftech crypto\n" +
//                "~+8801914616453@MeCloak> ... ok\n" +
//                "~+8801914616453@MeCloak> loading MeCloak premium icon\n" +
//                "~+8801914616453@MeCloak> failed/... ok\n" +
//                "~+8801914616453@MeCloak> loading boundage policy\n" +
//                "~+8801914616453@MeCloak> ... ok\n" +
//                "~+8801914616453@MeCloak> loading modules\n" +
//                "~+8801914616453@MeCloak> https ... ok\n" +
//                "~+8801914616453@MeCloak> wss ... ok\n" +
//                "~+8801914616453@MeCloak> LDAP ... ok\n" +
//                "~+8801914616453@MeCloak> SqLite ... ok\n" +
//                "~+8801914616453@MeCloak> XML ... ok\n" +
//                "~+8801914616453@MeCloak> bash ... ok\n" +
//                "~+8801914616453@MeCloak> loading certificates ... ok\n" +
//                "~+8801914616453@MeCloak> establishing cryptographic structure ... ok\n" +
//                "~+8801914616453@MeCloak> starting MeCloak\n";
//        String[] chararray = s.split("\n");
//
//
//        int length = chararray.length - 1;
//
//
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(
//                new TimerTask() {
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                autotext.setCursorVisible(true);
//                                autotext.setText(autotext.getText().toString() + "\n" + chararray[point]);
//                                autotext.setSelection(autotext.getText().length());
//
//                                if (point == length) {
//                                    t.cancel();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    autotext.setVisibility(View.GONE);
//                                                    passwordfieldlayout.setVisibility(View.VISIBLE);
//                                                    initialize.setText("Page Login");
//                                                }
//                                            });
//                                        }
//                                    }, 1000);
//                                }
//                                point++;
//                            }
//                        });
//                    }
//                },
//                0,      // run first occurrence immediatetly
//                150); // run every two seconds

        autotext.setVisibility(View.GONE);
        passwordfieldlayout.setVisibility(View.VISIBLE);

        InitLinks();
        new Functions(this);


        registernewpage = findViewById(R.id.registernewpage);

        registernewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(UserVerificationActivity.this, "RegisterVerifiedUser");
            }
        });

//        Cursor c = helper.getUserPassword(1);
//        if (c.moveToFirst()){
//             userpass = c.getString(1);
//
//        }
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = code.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password", pass);
                    globalRequest(UserVerificationActivity.this, "POST", Important.getPage_login(), jsonObject, 4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(pass.equals(userpass)){
//                    ServerRequest.PageLogin(UserVerificationActivity.this,pass,1);
//                }
            }
        });
        //  c.close();

        context = this;
    }

    String pass;
    static Context context;

    public static void closeActivtiy() {
        ((Activity) context).finish();
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("responsestr", response);
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}