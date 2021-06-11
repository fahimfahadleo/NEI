package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterVerifiedUser extends AppCompatActivity implements ServerResponse {
    EditText password, confirmpassword,pagename;
    TextView proceed;
    DatabaseHelper helper;

     static {
         System.loadLibrary("native-lib");
     }
    static native void StartActivity(Context context, String activity);
    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode,Context context);
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);
    static native void InitLinks();

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verified_user);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        proceed = findViewById(R.id.proceedbutton);
        pagename =findViewById(R.id.pagename);
        helper = new DatabaseHelper(this);
        new Functions(this);

        InitLinks();
        context = this;

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String confirmpass = confirmpassword.getText().toString();
                String name = pagename.getText().toString();



                if (pass.equals(confirmpass)) {

                   // ServerRequest.SetUserAppPassword(RegisterVerifiedUser.this, pass, confirmpass,1);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name",name);
                        jsonObject.put("password",pass);
                        jsonObject.put("password_confirmation",confirmpass);

                        Log.e("link",Important.getPage_register());
                        globalRequest(RegisterVerifiedUser.this,"POST",Important.getPage_register(),jsonObject,3,context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    helper.setUserPassword(pass);

                }

            }
        });
    }

    @Override
    public void onResponse(String response,int code,int requestcode) throws JSONException {
        Log.e("response", response);
       CheckResponse(this,this,response,3);

    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterVerifiedUser.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });

    }


}