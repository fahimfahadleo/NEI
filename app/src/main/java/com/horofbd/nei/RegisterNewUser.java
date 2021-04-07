package com.horofbd.nei;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterNewUser extends AppCompatActivity implements ServerResponse {


    TextInputLayout name, phone, referral, password, confirmpassword;
    TextView myreference;
    MaterialButton proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        referral = findViewById(R.id.reference);
        password = findViewById(R.id.passwordt);
        confirmpassword = findViewById(R.id.confirmpassword);
        myreference = findViewById(R.id.myreference);
        proceed = findViewById(R.id.proceedbutton);

        new ServerRequest();
        new Functions(this);





        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String phonestr = phone.getEditText().getText().toString();
                String namestr = name.getEditText().getText().toString().trim();
                String passwordstr = password.getEditText().getText().toString().trim();
                String referencestr = referral.getEditText().getText().toString().trim();
                String confirmpasswrodstr = confirmpassword.getEditText().getText().toString().trim();


                if(TextUtils.isEmpty(phonestr)){
                    Log.e("text","empty");
                }else {
                    Log.e("text",phonestr);
                    ServerRequest.RegisterNewUser(RegisterNewUser.this, phonestr, namestr,passwordstr, confirmpasswrodstr, referencestr);

                }


            }
        });


    }

    @Override
    public void onResponse(String response) throws JSONException {
        Log.e("response", response);

        JSONObject jsonObject = new JSONObject(response);
        String message = jsonObject.getString("message");
        JSONObject jsonObject1 = jsonObject.getJSONObject("user");

        if(jsonObject.has("user")){
            startActivity(new Intent(this,LoginActivity.class));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterNewUser.this, "Registration Successful! \nLogin Please!", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    @Override
    public void onFailure(String failresponse) {
        Toast.makeText(this, "There was an error creating your account!", Toast.LENGTH_SHORT).show();
    }
}