package com.horofbd.MeCloak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class PhoneNumberVerificationActivity extends AppCompatActivity implements ServerResponse {
    EditText verificationcode;
    TextView button;
    FirebaseAuth mAuth;
    private String verificationId;

    static {
        System.loadLibrary("native-lib");
    }
    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    static native void StartActivity(Context context, String activity,String flag);
    static native void InitLinks();
    static native void CheckResponse(ServerResponse serverResponse,Context context, String response,int requestcode);
    static native void RequestPhoneverification(ServerResponse serverResponse,String requesttype,String link,JSONObject jsonObject,int requestcode,Context context);

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verification);
        verificationcode = findViewById(R.id.verifycode);
        button = findViewById(R.id.verifybutton);
        mAuth = FirebaseAuth.getInstance();
        context = this;
        new Functions(this);

        InitLinks();

        if(Important.getActivate_previous_premium()==null){
            Log.e("tag","notworking");
        }else {
            Log.e("code",Important.getActivate_previous_premium());
        }

        String phonenumber = Functions.getSharedPreference("phone_no", "+880");
        if (!phonenumber.contains("+88")) {
            phonenumber = "+88" + phonenumber;
        }

       code =Functions.getSharedPreference("code","0");
        Log.e("code",code);


        //sendVerificationCode(phonenumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationcodestr = verificationcode.getText().toString();
               // verifyCode(verificationcodestr);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("code",code);

                    Log.e("tag",Important.getPhone_verification());
                    RequestPhoneverification(PhoneNumberVerificationActivity.this,"POST",Important.getPhone_verification(),jsonObject,2,PhoneNumberVerificationActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("tag",Important.getPhone_verification());

            }
        });
    }



    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("verification", response);

        CheckResponse(this,this,response,2);

    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PhoneNumberVerificationActivity.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("code",code);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Functions.isActive = true;
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.delete();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(PhoneNumberVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                PhoneNumberVerificationActivity.this, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );

        Toast.makeText(this, "Verification Code sent!", Toast.LENGTH_SHORT).show();
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
                verificationcode.setText(code);

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
            Toast.makeText(PhoneNumberVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }


}