package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPagePassword extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }

    static String forgotid;
    static ServerResponse serverResponse;

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);


    public static void setUpPasswordChangeField(String forgot) {
        //visible the change password fields.
        Log.e("forgotid",forgot);
        forgotid = forgot;
    }

    EditText pagename, password, confirmpassword;
    TextView pagenamesubmit, recoverbycode, recoverbyquestion, recoverbyimage, setpasswordsubmit;

    AlertDialog passwordconfirmation;


    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

//    void movetoPageResetQuestion() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword, null, false);
//        EditText passfield = vi.findViewById(R.id.password);
//        TextView cancel = vi.findViewById(R.id.cancelbutton);
//        TextView proceed = vi.findViewById(R.id.sealbutton);
//        proceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String pass = passfield.getText().toString();
//                if (TextUtils.isEmpty(pass)) {
//                    passfield.setError("Field Can Not Be Empty!");
//                    passfield.requestFocus();
//                } else {
//                    Intent i = new Intent(context, EditPageSecurityQuestionActivity.class);
//                    i.putExtra("pass", pass);
//                    i.putExtra("action", "challenge");
//                    startActivity(i);
//                    passwordconfirmation.dismiss();
//                }
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                passwordconfirmation.dismiss();
//            }
//        });
//        builder.setView(vi);
//        passwordconfirmation = builder.create();
//        passwordconfirmation.show();
//    }


    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_page_password);
        context = this;
        // pagename = findViewById(R.id.pagename);
        // pagenamesubmit = findViewById(R.id.namesubmit);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        recoverbycode = findViewById(R.id.recoverycode);
        recoverbyimage = findViewById(R.id.securityimage);
        recoverbyquestion = findViewById(R.id.securityquestion);
        setpasswordsubmit = findViewById(R.id.setnewpassword);
        serverResponse = this;
        InitLinks();

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        pagenamesubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String pagenamestr = pagename.getText().toString();
//                if(TextUtils.isEmpty(pagenamestr)){
//                    pagename.setError("Field Can Not be empty!");
//                    pagename.requestFocus();
//                }else {
////request for page password reset;
//                }
//            }
//        });


        setpasswordsubmit.setOnClickListener(new View.OnClickListener() {
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

        recoverbyquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //movetoPageResetQuestion();
                Intent i = new Intent(context, EditPageSecurityQuestionActivity.class);
                //i.putExtra("pass", pass);
                i.putExtra("action", "challenge");
                startActivity(i);

                finish();
            }
        });


    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(serverResponse, context, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}