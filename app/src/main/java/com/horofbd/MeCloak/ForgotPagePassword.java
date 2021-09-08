package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ForgotPagePassword extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }

    static String forgotid;
    static ServerResponse serverResponse;
    EditText Code;

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);


    public static void setUpPasswordChangeField(String forgot) {
        testlayout.setVisibility(View.INVISIBLE);
        passwordlayout.setVisibility(View.VISIBLE);
        Log.e("forgotid", forgot);
        forgotid = forgot;
    }

    static EditText answerone, answertwo, answerthree, password, confirmpassword, code;
    static TextView questionone, questiontwo, questionthree, challengerecoveryImage, submitbutton, setnewpassword;
    static Spinner questiononespinner, questiontwospinner, questionthreespinner;

    RadioButton buttonone, buttontwo, buttonthree;

    LinearLayout questionlayout;
    static LinearLayout testlayout, passwordlayout;

    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    RadioGroup radioGroup;

    static ArrayList<JSONObject> questionlist;

    public static void setUpChallengeQuestions(ArrayList<JSONObject> list) {
        questionlist = list;

        Log.e("data", list.toString());

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //   id1 = Integer.parseInt(list.get(0).getString("id"));
                    questionone.setText("1.What is your page name?");
                    questiononespinner.setVisibility(View.INVISIBLE);

                    id2 = Integer.parseInt(list.get(0).getString("id"));
                    questiontwo.setText("2."+list.get(0).getString("question"));
                    questiontwospinner.setVisibility(View.INVISIBLE);

                    id3 = Integer.parseInt(list.get(1).getString("id"));
                    questionthree.setText("3."+list.get(1).getString("question"));
                    questionthreespinner.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    static int id2;
    static int id3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_page_password);
        context = this;
        code = findViewById(R.id.code);
        answerone = findViewById(R.id.answerone);
        testlayout = findViewById(R.id.layout1);
        submitbutton = findViewById(R.id.submitanswer);
        answertwo = findViewById(R.id.answertwo);
        answerthree = findViewById(R.id.answerthree);
        questionlayout = findViewById(R.id.questionlayout);
        password = findViewById(R.id.password);
        challengerecoveryImage = findViewById(R.id.challengeimage);
        passwordlayout = findViewById(R.id.setnewpasswordview);
        confirmpassword = findViewById(R.id.confirmpassword);
        questionone = findViewById(R.id.questiononeview);
        questiontwo = findViewById(R.id.questiontwoview);
        questionthree = findViewById(R.id.questionthreeview);
        setnewpassword = findViewById(R.id.setnewpassword);
        questiononespinner = findViewById(R.id.questiononespinner);
        questiontwospinner = findViewById(R.id.questiontwospinner);
        questionthreespinner = findViewById(R.id.questionthreespinner);
        buttonone = findViewById(R.id.radiobuttonone);
        buttontwo = findViewById(R.id.radiobuttontwo);
        buttonthree = findViewById(R.id.radiobuttonthree);
        radioGroup = findViewById(R.id.radiogroup);


        code.setVisibility(View.GONE);
        challengerecoveryImage.setVisibility(View.GONE);
        questionlayout.setVisibility(View.GONE);


        serverResponse = this;
        InitLinks();




        challengerecoveryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPagePassword.this, PageResetImage.class);
                i.putExtra("action", "chellenge");
                startActivity(i);
            }
        });


        globalRequest(ForgotPagePassword.this, "GET", Important.getGetchillengerpagesecurityquestions(), new JSONObject(), 41, ForgotPagePassword.this);


        submitbutton.setEnabled(false);
        submitbutton.setBackground(ContextCompat.getDrawable(ForgotPagePassword.this, R.drawable.buttonshapetpgray));


        buttonone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code.setVisibility(View.VISIBLE);
                challengerecoveryImage.setVisibility(View.GONE);
                questionlayout.setVisibility(View.GONE);
                submitbutton.setEnabled(true);
                submitbutton.setBackground(ContextCompat.getDrawable(ForgotPagePassword.this, R.drawable.buttonshapetp));

                //make rest invisible
            }
        });

        buttontwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengerecoveryImage.setVisibility(View.VISIBLE);
                code.setVisibility(View.GONE);
                questionlayout.setVisibility(View.GONE);
                submitbutton.setEnabled(true);
                submitbutton.setBackground(ContextCompat.getDrawable(ForgotPagePassword.this, R.drawable.buttonshapetp));

                //make rest invisible
            }
        });

        buttonthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionlayout.setVisibility(View.VISIBLE);
                code.setVisibility(View.GONE);
                challengerecoveryImage.setVisibility(View.GONE);
                submitbutton.setEnabled(true);
                submitbutton.setBackground(ContextCompat.getDrawable(ForgotPagePassword.this, R.drawable.buttonshapetp));

                if(questionlist.isEmpty()){
                    globalRequest(ForgotPagePassword.this, "GET", Important.getGetchillengerpagesecurityquestions(), new JSONObject(), 41, ForgotPagePassword.this);
                }
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonone.isChecked()) {
                    if (TextUtils.isEmpty(code.getText().toString())) {
                        code.setError("Code Must Be Filled!");
                        code.requestFocus();
                    } else {
                        //request for page recovery by code
                    }
                } else if (buttonthree.isChecked()) {
                    if (TextUtils.isEmpty(answerone.getText().toString())) {
                        answerone.setError("Field Can Not Be Empty!");
                        answerone.requestFocus();
                    } else if (TextUtils.isEmpty(answertwo.getText().toString())) {
                        answertwo.setError("Field Can Not Be Empty!");
                        answertwo.requestFocus();
                    } else if (TextUtils.isEmpty(answerthree.getText().toString())) {
                        answerthree.setError("Field Can Not Be Empty!");
                        answerthree.requestFocus();
                    } else {
                        //request for page reset password

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("page_name",answerone.getText().toString());

                            JSONArray array = new JSONArray();
                            array.put(answertwo.getText().toString());
                            array.put(answerthree.getText().toString());
                            JSONArray array1 = new JSONArray();
                            array1.put(id2);
                            array1.put(id3);
                            jsonObject.put("ids", array1);
                            jsonObject.put("ans", array);
                            globalRequest(ForgotPagePassword.this, "POST", Important.getChillengerpagesecurityquestions(), jsonObject, 42, ForgotPagePassword.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (buttonthree.isChecked()) {
                    Intent i = new Intent(ForgotPagePassword.this, PageResetImage.class);
                    i.putExtra("action", "chellenge");
                    startActivity(i);
                }
            }
        });


        setnewpassword.setOnClickListener(new View.OnClickListener() {
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


    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(serverResponse, context, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}