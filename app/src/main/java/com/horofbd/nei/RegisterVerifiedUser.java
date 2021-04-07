package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterVerifiedUser extends AppCompatActivity implements ServerResponse{
    TextInputEditText password,confirmpassword;
    MaterialButton proceed;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verified_user);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        proceed = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);
        new ServerRequest();
        new Functions(this);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String confirmpass = confirmpassword.getText().toString();

                ServerRequest.SetUserAppPassword(RegisterVerifiedUser.this,pass,confirmpass);

                if(pass.equals(confirmpass)){
                    helper.setUserPassword(pass);
                }

            }
        });
    }

    @Override
    public void onResponse(String response) throws JSONException {
        Log.e("response",response);
        JSONObject object = new JSONObject(response);
        if(object.has("error")&& object.getString("error").equals("Unauthorized: Phone number verification failed")){
            startActivity(new Intent(this,PhoneNumberVerificationActivity.class));
        }else {
            startActivity(new Intent(this,MainActivity.class));
            Functions.isActive = true;
        }
        finish();
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        Log.e("response",failresponse);

    }
}