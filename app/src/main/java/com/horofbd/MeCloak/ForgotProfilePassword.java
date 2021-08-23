package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ForgotProfilePassword extends AppCompatActivity implements ServerResponse{
    static {
        System.loadLibrary("native-lib");
    }

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);
    static native void InitLinks();
    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    ImageView backbutton;
    EditText phone,password,confirmpassword;
    static EditText phoneverificationcode;
    TextView submitbutton,submitphoneverificationcode,submitnewpassword;
    CheckBox accesstophone;

    CountryCodePicker picker;

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }
    static String phonenumber;
    static String forgotid;

    public static void sendOtp(int forgot){
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
        backbutton = findViewById(R.id.backbutton);
        accesstophone = findViewById(R.id.accesstophone);
        picker = findViewById(R.id.countryNameHolder);
        submitphoneverificationcode = findViewById(R.id.submitphoneverificationcode);
        phoneverificationcode = findViewById(R.id.phoneverificationcode);
        mAuth = FirebaseAuth.getInstance();
        serverResponse = this;
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        submitnewpassword = findViewById(R.id.setnewpassword);



        phone.setText(picker.getSelectedCountryCodeWithPlus());


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        picker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                phone.setText(picker.getSelectedCountryCodeWithPlus());

            }
        });


        submitnewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordstr = password.getText().toString();
                String conpasswordstr = confirmpassword.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("forgot_id",forgotid);
                    jsonObject.put("password",passwordstr);
                    jsonObject.put("password_confirmation",conpasswordstr);
                    globalRequest(serverResponse,"POST",Important.getChangeforgottenpassword(),jsonObject,40,context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


        submitphoneverificationcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = phoneverificationcode.getText().toString();
                verifyCode(code);
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phonenumber = phone.getText().toString();

                Log.e("countrycode",picker.getSelectedCountryCodeWithPlus());
                Log.e("countryname",picker.getSelectedCountryNameCode());
                Functions.athenticationRequired = false;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone_no",phonenumber);
                    jsonObject.put("flag1",(accesstophone.isChecked()?"1":"0"));
                    jsonObject.put("country_code",picker.getSelectedCountryNameCode());
                    jsonObject.put("type","profile");
                    Log.e("link",Important.getForgot_profile_password());
                    globalRequest(ForgotProfilePassword.this,"POST",Important.getForgot_profile_password(),jsonObject,25,ForgotProfilePassword.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });






    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
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
                                    Log.e("token",task.getResult().getToken());


                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("for","passwordReset");
                                        jsonObject.put("idToken",task.getResult().getToken());
                                        jsonObject.put("forgot_id",forgotid);

                                        globalRequest(serverResponse,"POST",Important.getCheckfirebaseotp(),jsonObject,39,context);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            //user.delete();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            ((Activity)context).runOnUiThread(new Runnable() {
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
                ((Activity)context), // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );

        ((Activity)context).runOnUiThread(new Runnable() {
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
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                phoneverificationcode.setText(code);

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
            ((Activity)context).runOnUiThread(new Runnable() {
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