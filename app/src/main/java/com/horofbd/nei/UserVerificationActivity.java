package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UserVerificationActivity extends AppCompatActivity {

    TextInputEditText code;
    MaterialButton proceedbutton;
    DatabaseHelper helper;

    String userpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);

        Cursor c = helper.getUserPassword(1);
        if (c.moveToFirst()){
             userpass = c.getString(1);
            Log.e("tag",userpass);
        }


        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = code.getText().toString();

                if(pass.equals(userpass)){
                    startActivity(new Intent(UserVerificationActivity.this,MainActivity.class));
                    Functions.isActive = true;
                    finish();
                }



            }
        });
        c.close();

    }
}