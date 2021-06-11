package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriend extends AppCompatActivity implements ServerResponse {
    EditText phone;
    ImageView contactbutton;
    ImageButton proceedbutton;
    TextView  text;
    String number;

    static {
        System.loadLibrary("native-lib");
    }

    @SuppressLint("StaticFieldLeak")
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();

        ((Activity)context).finish();
    }



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    static native void InitLinks();
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);
    static native void StartActivity(Context context, String activity,String data);
    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static AlertDialog profiledialogue;
    public static void showProfileDialogue(ServerResponse serverResponse,String name,String phone,String id,String pageid){


       ((Activity)context).runOnUiThread(new Runnable() {
           @Override
           public void run() {
               AlertDialog.Builder profiledialoguebuilder = new AlertDialog.Builder(context);
               LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(LAYOUT_INFLATER_SERVICE);
               View vi = inflater.inflate(R.layout.showfriendprofile,null,false);
               CircleImageView profilepicture = vi.findViewById(R.id.profilepicture);
               TextView friendname = vi.findViewById(R.id.username);
               TextView friendphone = vi.findViewById(R.id.userphone);
               TextView sendfriendreq = vi.findViewById(R.id.sendfriendrequest);
               TextView blockfriend = vi.findViewById(R.id.blockfriend);
               ImageButton closedialogue = vi.findViewById(R.id.closedialogue);
               friendname.setText(name);
               friendphone.setText(phone);

               closedialogue.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       profiledialogue.dismiss();
                   }
               });
               sendfriendreq.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       JSONObject jsonObject = new JSONObject();
                       try {
                           jsonObject.put("id",id);
                           jsonObject.put("page_id",pageid);
                           globalRequest(serverResponse,"POST",Important.getAdd_friend(),jsonObject,6);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               });

               blockfriend.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       JSONObject jsonObject = new JSONObject();
                       try {
                           jsonObject.put("user_id",id);
                           globalRequest(serverResponse,"POST",Important.getBlock_friend(),jsonObject,7);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
                profiledialoguebuilder.setView(vi);
               profiledialogue = profiledialoguebuilder.create();
               profiledialogue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#80030A25")));
               profiledialogue.show();
           }
       });

    }
    ImageView backbutton;
    ImageView menubutton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                //ServerRequest.CheckPhoneNumber(this, number, 2);

                if(!number.contains("+88")){
                    number = "+88"+number;
                }

                JSONObject object = new JSONObject();

                try {
                    object.put("number",number);
                    globalRequest(this,"POST",Important.getSearch_friend(),object,5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            if (cursor != null) {
                cursor.close();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        phone = findViewById(R.id.phone);
        contactbutton = findViewById(R.id.contact);
        proceedbutton = findViewById(R.id.proceedbutton);
        text = findViewById(R.id.text);


        backbutton = findViewById(R.id.backbutton);
        menubutton = findViewById(R.id.menubutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriend.super.onBackPressed();
            }
        });





        context = this;
        InitLinks();
        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 2);
            }
        });
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonestr = phone.getText().toString();

                if(!phonestr.contains("+88")){
                    phonestr = "+88"+phonestr;
                }

                JSONObject object = new JSONObject();

                try {
                    object.put("number",phonestr);
                    globalRequest(AddFriend.this,"POST",Important.getSearch_friend(),object,5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("respnse",response);
        CheckResponse(this,this,response,requestcode);

    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddFriend.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }
}