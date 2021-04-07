package com.horofbd.nei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ServerResponse{


    TextInputEditText phone,password;
    MaterialButton proceed;
    TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        proceed = findViewById(R.id.proceedbutton);
        register = findViewById(R.id.register);

        new Functions(this);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phonestr = phone.getText().toString();
                String passwordstr = password.getText().toString();
                ServerRequest.LoginRegisteredUser(LoginActivity.this,phonestr,passwordstr);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterNewUser.class));
            }
        });



    }

    @Override
    public void onResponse(String response) throws JSONException {

        Log.e("responsqqqqe",response);

        JSONObject jsonObject = new JSONObject(response);
        Log.e("status","checking");
            String accesstoken = jsonObject.getString("access_token");
            String expirytime = jsonObject.getString("expires_in");
        Log.e("status","checking");
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");
        Log.e("status","checking3");

        Log.e("jsonobe",jsonObject1.toString());
            Functions.setSharedPreference("private_token",accesstoken);
            Functions.setSharedPreference("expires_in",String.valueOf(System.currentTimeMillis()+(Long.parseLong(expirytime)*1000)));
            Functions.setSharedPreference("user_name",jsonObject1.getString("name"));
            Functions.setSharedPreference("ref",jsonObject1.getString("ref"));
           // Functions.setSharedPreference("premium",jsonObject1.getString("premium"));
            Functions.setSharedPreference("loginstatus","true");
            Functions.setSharedPreference("phone_no",jsonObject1.getString("phone_no"));
            Log.e("status","checking");
            startActivity(new Intent(this,MainActivity.class));





    }

    @Override
    public void onFailure(String failresponse) {

        Toast.makeText(this, "There was an error in login!", Toast.LENGTH_SHORT).show();
    }
}