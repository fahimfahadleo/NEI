package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.text.webvtt.WebvttCssStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ForgotProfilePassword extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);


    EditText phone, password, confirmpassword;
    static EditText code1, code2, code3, code4, code5, code6;
    TextView submitbutton, submitphoneverificationcode, submitnewpassword;
    CheckBox accesstophone;
    static TextView title;
    CountryCodePicker picker;
    static Context context;
    static LinearLayout layout1,phoneverify,setnewpasswordview;
    static int visivility = 1;


    public static void showPhoneVerificationlayout(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout1.setVisibility(View.INVISIBLE);
                phoneverify.setVisibility(View.VISIBLE);
                title.setText("Phone number verification");
                visivility = 2;
            }
        });

    }

    public static void showResetPasswordLayout(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                phoneverify.setVisibility(View.INVISIBLE);
                setnewpasswordview.setVisibility(View.VISIBLE);
                title.setText("Set Password");
                visivility = 3;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if(visivility == 1){
            super.onBackPressed();
        }else if(visivility == 2){
            visivility = 1;
            layout1.setVisibility(View.VISIBLE);
            phoneverify.setVisibility(View.INVISIBLE);
            setnewpasswordview.setVisibility(View.INVISIBLE);
            title.setText("Password Reset");
        }else if(visivility == 3){
            visivility = 2;
            layout1.setVisibility(View.INVISIBLE);
            phoneverify.setVisibility(View.VISIBLE);
            setnewpasswordview.setVisibility(View.INVISIBLE);
            title.setText("Phone number verification");
        }
    }

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    static String phonenumber;
    static String forgotid;
    LinearLayout invisiblecrtupload;

    public static void sendOtp(int forgot) {
        forgotid = String.valueOf(forgot);
        sendVerificationCode(phonenumber);
    }

    static ServerResponse serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_profile_password);
        context = this;
        phone = findViewById(R.id.phone);
        submitbutton = findViewById(R.id.proceedbutton);
        InitLinks();
        accesstophone = findViewById(R.id.accesstophone);
        picker = findViewById(R.id.countryNameHolder);
        submitphoneverificationcode = findViewById(R.id.submitphoneverificationcode);
        mAuth = FirebaseAuth.getInstance();
        serverResponse = this;
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        submitnewpassword = findViewById(R.id.submitnewpassword);
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        invisiblecrtupload = findViewById(R.id.invisiblecrtupload);
        layout1 = findViewById(R.id.layout1);
        phoneverify = findViewById(R.id.phoneverify);
        setnewpasswordview = findViewById(R.id.setnewpasswordview);
        title = findViewById(R.id.title);
        if(getIntent().hasExtra("title")){
            title.setText(getIntent().getStringExtra("title"));
        }else {
            title.setText("Password Reset");
        }







        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phone.setText(picker.getSelectedCountryCodeWithPlus());


        picker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    phone.setText(picker.getSelectedCountryCodeWithPlus());
                }else {
                    if(!phone.getText().toString().contains("+")){
                        phone.setText(picker.getSelectedCountryCodeWithPlus()+phone.getText().toString());
                    }else {
                        phone.setText(picker.getSelectedCountryCodeWithPlus());
                    }
                }

            }
        });

        accesstophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accesstophone.isChecked()){
                    invisiblecrtupload.setVisibility(View.VISIBLE);
                }else {
                    invisiblecrtupload.setVisibility(View.GONE);
                }
            }
        });


        submitnewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordstr = password.getText().toString();
                String conpasswordstr = confirmpassword.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("forgot_id", forgotid);
                    jsonObject.put("password", passwordstr);
                    jsonObject.put("password_confirmation", conpasswordstr);
                    globalRequest(serverResponse, "POST", Important.getChangeforgottenpassword(), jsonObject, 40, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        submitphoneverificationcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = code1.getText().toString() + code2.getText().toString() +
                        code3.getText().toString() + code4.getText().toString() +
                        code5.getText().toString() + code6.getText().toString();
                verifyCode(code);
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumber = phone.getText().toString();
                if(!TextUtils.isEmpty(phonenumber)){
                    Functions.athenticationRequired = false;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone_no", phonenumber);
                        jsonObject.put("flag1", (accesstophone.isChecked() ? "1" : "0"));
                        jsonObject.put("country_code", picker.getSelectedCountryNameCode());
                        jsonObject.put("type", "profile");
                        Log.e("link", Important.getForgot_profile_password());
                        globalRequest(ForgotProfilePassword.this, "POST", Important.getForgot_profile_password(), jsonObject, 25, ForgotProfilePassword.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    phone.setError("Field Can Not Be Empty!");
                    phone.requestFocus();
                }



            }
        });


    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }

    static FirebaseAuth mAuth;
    private static String verificationId;


    private static void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.

                            Functions.isActive = true;
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    Log.e("token", task.getResult().getToken());


                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("for", "passwordReset");
                                        jsonObject.put("idToken", task.getResult().getToken());
                                        jsonObject.put("forgot_id", forgotid);

                                        globalRequest(serverResponse, "POST", Important.getCheckfirebaseotp(), jsonObject, 39, context);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            //user.delete();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                });
    }


    private static void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                ((Activity) context), // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Verification Code sent!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // callback method is called on Phone auth provider.
    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials. n
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                char[] codes = code.toCharArray();

                code1.setText(String.valueOf(codes[0]));
                code2.setText(String.valueOf(codes[1]));
                code3.setText(String.valueOf(codes[2]));
                code4.setText(String.valueOf(codes[3]));
                code5.setText(String.valueOf(codes[4]));
                code6.setText(String.valueOf(codes[5]));

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    };

    // below method is use to verify code from Firebase.
    private static void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }


}