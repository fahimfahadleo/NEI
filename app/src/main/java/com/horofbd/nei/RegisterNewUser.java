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


    EditText name, phone, referral, password, confirmpassword;
    TextView proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        referral = findViewById(R.id.reference);
        password = findViewById(R.id.passwordt);
        confirmpassword = findViewById(R.id.confirmpassword);
        proceed = findViewById(R.id.proceedbutton);

        new ServerRequest();
        new Functions(this);


        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String phonestr = phone.getText().toString();
                String namestr = name.getText().toString().trim();
                String passwordstr = password.getText().toString().trim();
                String referencestr = referral.getText().toString().trim();
                String confirmpasswrodstr = confirmpassword.getText().toString().trim();

                if(TextUtils.isEmpty(referencestr)){
                    referencestr = "";
                }


                boolean isOk = false;


                if (TextUtils.isEmpty(namestr)) {
                    phone.setError("Field Can Not Be Empty!");
                    phone.requestFocus();
                    isOk = true;
                }
                if (TextUtils.isEmpty(phonestr)) {
                    name.setError("Field Can Not Be Empty!");
                    name.requestFocus();
                    isOk = true;
                }
                if (TextUtils.isEmpty(passwordstr)) {
                    password.setError("Field Can Not Be Empty!");
                    password.requestFocus();
                    isOk = true;
                }
                if (passwordstr.length() < 8) {
                    password.setError("Password Should Be At Least Of 8 Characters!");
                    password.requestFocus();
                    isOk = true;
                }
                if (TextUtils.isEmpty(confirmpasswrodstr)) {
                    confirmpassword.setError("Field Can Not Be Empty!");
                    confirmpassword.requestFocus();
                    isOk = true;
                }
                if (!passwordstr.equals(confirmpasswrodstr)) {
                    confirmpassword.setError("Password Did Not Match!");
                    confirmpassword.requestFocus();
                    isOk = true;
                }

                if(!isOk){
                    Log.e("text", phonestr);
                    ServerRequest.RegisterNewUser(RegisterNewUser.this, phonestr, namestr, passwordstr, confirmpasswrodstr, referencestr,1);

                }



            }
        });


    }

    @Override
    public void onResponse(String response,int code,int requestcode) throws JSONException {
        Log.e("response", response);

        if(requestcode == 1){
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");

            if (jsonObject.has("user")) {
                startActivity(new Intent(this, LoginActivity.class));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterNewUser.this, "Registration Successful! \nLogin Please!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }



    }

    @Override
    public void onFailure(String failresponse) {
        Toast.makeText(this, "There was an error creating your account!", Toast.LENGTH_SHORT).show();
    }

}