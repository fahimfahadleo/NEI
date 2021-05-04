package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public class RegisterVerifiedUser extends AppCompatActivity implements ServerResponse {
    EditText password, confirmpassword;
    TextView proceed;
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

//        AppCompatDelegate
//                .setDefaultNightMode(
//                        AppCompatDelegate
//                                .MODE_NIGHT_YES);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String confirmpass = confirmpassword.getText().toString();



                if (pass.equals(confirmpass)) {

                    ServerRequest.SetUserAppPassword(RegisterVerifiedUser.this, pass, confirmpass,1);
                    helper.setUserPassword(pass);

                }

            }
        });
    }

    @Override
    public void onResponse(String response,int code,int requestcode) throws JSONException {
        Log.e("response", response);

        if(requestcode == 1){
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("error").equals("Unauthorized: Phone number verification failed")){
                Log.e("testing","testing");
                startActivity(new Intent(this,PhoneNumberVerificationActivity.class));
                finish();
            }else {
                startActivity(new Intent(this, MainActivity.class));
                Functions.isActive = true;
            }

            finish();
        }
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        Log.e("response", failresponse);

    }


}