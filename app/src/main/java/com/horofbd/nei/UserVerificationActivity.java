package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class UserVerificationActivity extends AppCompatActivity implements ServerResponse{

    EditText code;
    TextView proceedbutton;
    DatabaseHelper helper;

    String userpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);

        new Functions(this);


        Cursor c = helper.getUserPassword(1);
        if (c.moveToFirst()){
             userpass = c.getString(1);

        }
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = code.getText().toString();

                if(pass.equals(userpass)){

                    ServerRequest.PageLogin(UserVerificationActivity.this,pass,1);
                }
            }
        });
        c.close();

    }
    String pass;

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        if(requestcode == 1){
            startActivity(new Intent(UserVerificationActivity.this,MainActivity.class));
            Functions.isActive = true;
            finish();
        }
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}