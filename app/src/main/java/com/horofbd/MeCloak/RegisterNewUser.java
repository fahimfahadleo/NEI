package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;

public class RegisterNewUser extends AppCompatActivity implements ServerResponse {


    EditText name, phone, referral, password, confirmpassword;
    TextView proceed;
    RadioButton termsandconditions, policy;
    boolean istandcchecked = false;
    boolean ispolicyischecked = false;
    ImageView passunhide, conpassunhide;
    boolean ispasshidden = false;
    boolean isconpasshidden = false;

    static {
        System.loadLibrary("native-lib");
    }


    static native void RegisterRequest(ServerResponse serverResponse, String phone, String name, String password, String confirmpassword, String reference, String termsandconditions, String policy, int requestcode);

    static native void CheckRegisterData(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void StartActivity(Context context, String activity);

    static Context context;

    public static void closeActivtiy() {
        ((Activity) context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        referral = findViewById(R.id.reference);
        password = findViewById(R.id.passwordt);
        confirmpassword = findViewById(R.id.confirmpassword);
        passunhide = findViewById(R.id.showpass);
        conpassunhide = findViewById(R.id.showconfirmpass);
        proceed = findViewById(R.id.proceedbutton);
        termsandconditions = findViewById(R.id.termsandconditions);
        policy = findViewById(R.id.policy);
        context = this;

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passunhide.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                conpassunhide.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passunhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ispasshidden) {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye_blocked));
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    ispasshidden = false;
                } else {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    password.setTransformationMethod(null);
                    ispasshidden = true;
                }
            }
        });
        conpassunhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isconpasshidden) {
                    conpassunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye_blocked));
                    confirmpassword.setTransformationMethod(new PasswordTransformationMethod());
                    isconpasshidden = false;
                } else {
                    conpassunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    confirmpassword.setTransformationMethod(null);
                    isconpasshidden = true;
                }
            }
        });

        proceed.setEnabled(false);
        proceed.setBackground(ContextCompat.getDrawable(this, R.drawable.buttonbackgroundgrey));

        termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (istandcchecked) {
                    termsandconditions.setChecked(false);
                    istandcchecked = false;
                    proceed.setEnabled(false);
                    proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundgrey));

                } else {
                    termsandconditions.setChecked(true);
                    istandcchecked = true;

                    if (ispolicyischecked) {
                        proceed.setEnabled(true);
                        proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundoceanblue));
                    } else {
                        proceed.setEnabled(false);
                        proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundgrey));
                    }
                }
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ispolicyischecked) {
                    policy.setChecked(false);
                    ispolicyischecked = false;

                    proceed.setEnabled(false);
                    proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundgrey));

                } else {
                    policy.setChecked(true);
                    ispolicyischecked = true;
                    if (istandcchecked) {
                        proceed.setEnabled(true);
                        proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundoceanblue));
                    } else {
                        proceed.setEnabled(false);
                        proceed.setBackground(ContextCompat.getDrawable(RegisterNewUser.this, R.drawable.buttonbackgroundgrey));
                    }

                }
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String phonestr = phone.getText().toString();
                String namestr = name.getText().toString().trim();
                String passwordstr = password.getText().toString().trim();
                String referencestr = referral.getText().toString().trim();
                String confirmpasswrodstr = confirmpassword.getText().toString().trim();

                if (TextUtils.isEmpty(referencestr)) {
                    referencestr = "";
                }
                boolean isOk = false;
                if (TextUtils.isEmpty(namestr)) {
                    name.setError("Field Can Not Be Empty!");
                    isOk = true;
                }
                if (TextUtils.isEmpty(phonestr)) {
                    phone.setError("Field Can Not Be Empty!");
                    isOk = true;
                }
                if (TextUtils.isEmpty(passwordstr)) {
                    password.setError("Field Can Not Be Empty!");
                    passunhide.setVisibility(View.INVISIBLE);
                    isOk = true;
                }
                if (passwordstr.length() < 6) {
                    password.setError("Password Should Be At Least Of 6 Characters!");
                    passunhide.setVisibility(View.INVISIBLE);
                    isOk = true;
                }
                if (TextUtils.isEmpty(confirmpasswrodstr)) {
                    confirmpassword.setError("Field Can Not Be Empty!");
                    conpassunhide.setVisibility(View.INVISIBLE);
                    isOk = true;
                }
                if (!passwordstr.equals(confirmpasswrodstr)) {
                    confirmpassword.setError("Password Did Not Match!");
                    conpassunhide.setVisibility(View.INVISIBLE);
                    isOk = true;
                }
                if (!termsandconditions.isChecked()) {
                    isOk = true;
                }

                if (!policy.isChecked()) {
                    isOk = true;
                }

                if (!phonestr.contains("+88")) {
                    phonestr = "+88" + phonestr;
                }
                if (!isOk) {

                    RegisterRequest(RegisterNewUser.this, phonestr, namestr, passwordstr, confirmpasswrodstr, referencestr, "1", "1", 0);
                }
            }
        });
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("response", response);
        CheckRegisterData(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("errpr", failresponse);
                Toast.makeText(RegisterNewUser.this, "There was an error creating your account!", Toast.LENGTH_SHORT).show();

            }
        });
    }

}