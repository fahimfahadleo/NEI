package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ServerResponse{


    EditText phone,password;
    TextView proceed,worningtext,forgotpassword;
    TextView register,donthaveaccount;


    static {
        System.loadLibrary("native-lib");
    }

    static native void LoginRequest(ServerResponse serverResponse,String phone,String passwrod,int requestcode);
    static native void SaveLogindata(JSONObject jsonObject,JSONObject jsonObject1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        proceed = findViewById(R.id.proceedbutton);
        register = findViewById(R.id.register);
        donthaveaccount = findViewById(R.id.donthaveaccount);
        forgotpassword = findViewById(R.id.forgotpassword);


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotProfilePassword.class));
            }
        });

        worningtext = findViewById(R.id.worningtext);

        new Functions(this);


//        AppCompatDelegate
//                .setDefaultNightMode(
//                        AppCompatDelegate
//                                .MODE_NIGHT_NO);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phonestr = phone.getText().toString();
                String passwordstr = password.getText().toString();
                //ServerRequest.LoginRegisteredUser(LoginActivity.this,phonestr,passwordstr,1);

                LoginRequest(LoginActivity.this,phonestr,passwordstr,1);

            }
        });

        if(!Functions.getSharedPreference("private_token", "").equals("")){
            register.setVisibility(View.GONE);
            donthaveaccount.setVisibility(View.GONE);
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterNewUser.class));
            }
        });



    }

    @Override
    public void onResponse(String response,int responsecode,int requestcode) throws JSONException {

        Log.e("responsqqqqe",response);

        if(requestcode == 1 ){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String accesstoken = jsonObject.getString("access_token");
                if(!TextUtils.isEmpty(accesstoken)){
                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                    if(jsonObject1!=null && !TextUtils.isEmpty(jsonObject1.getString("name"))){
                        SaveLogindata(jsonObject,jsonObject1);
                        startActivity(new Intent(this,MainActivity.class));
                        Functions.isActive = false;
                    }

            }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"Unauthorised Access!",Toast.LENGTH_LONG).show();
                            worningtext.setText("Unauthorised Access!");
                        }
                    });
                }
            }catch (Exception e){
                Toast.makeText(this, "error occurred!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(String failresponse) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "There was an error in login!", Toast.LENGTH_SHORT).show();
            }
        });
    }



}