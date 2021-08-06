package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class RegisterVerifiedUser extends AppCompatActivity implements ServerResponse {
    EditText password, confirmpassword,pagename;
    TextView proceed;

     static {
         System.loadLibrary("native-lib");
     }
    static native void StartActivity(Context context, String activity);
    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode,Context context);
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);
    static native void InitLinks();
    static native String getLoginInfo(String key);

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }
    static ServerResponse serverResponse;

    Intent i;

    static AlertDialog dialog;
    public static void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pagecreateworning,null,false);
        TextView cancel = view.findViewById(R.id.dismiss);
        TextView visit = view.findViewById(R.id.visit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(serverResponse,"POST",Important.getPagelogout(),new JSONObject(),34,context);
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verified_user);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        proceed = findViewById(R.id.proceedbutton);
        pagename =findViewById(R.id.pagename);

        new Functions(this);

        i = getIntent();
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
                        jsonObject.put("name",name);
                        jsonObject.put("password",pass);
                        jsonObject.put("page_password_confirmation",confirmpass);

                        Log.e("link",Important.getPage_register());

                        if(i.hasExtra("newpage")){
                            jsonObject.put("password_confirmation",i.getStringExtra("pass"));
                            globalRequest(RegisterVerifiedUser.this,"POST",Important.getPage_register(),jsonObject,33,context);
                        }else {
                            jsonObject.put("password_confirmation",getLoginInfo("password"));
                            globalRequest(RegisterVerifiedUser.this,"POST",Important.getPage_register(),jsonObject,3,context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



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