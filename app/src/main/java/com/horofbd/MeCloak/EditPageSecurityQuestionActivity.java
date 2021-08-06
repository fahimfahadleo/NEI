package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditPageSecurityQuestionActivity extends AppCompatActivity implements ServerResponse {
    static {
        System.loadLibrary("native-lib");
    }


    static ServerResponse serverResponse;
    static TextView action;


    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static Context context;
    Intent i;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }
    ImageView backbutton;
    static ArrayList<String> questions;

    static Spinner questionone, questiontwo, questionthree;
    static TextView answerone, answertwo, answerthree;
    static TextView questiononeview, questiontwoview, questionthreeview;
    static int id1 = 0, id2 = 0, id3 = 0;
    static String q1 = "", q2 = "", q3 = "";

    static HashMap<String, Integer> idmap;

    public static void setUpQuestionParse(ArrayList<JSONObject> list) {

        for (JSONObject j : list) {
            try {
                int id = j.getInt("id");
                String question = j.getString("question");
                questions.add(question);
                idmap.put(question, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item,
                                questions); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                questionone.setAdapter(spinnerArrayAdapter);
                questiontwo.setAdapter(spinnerArrayAdapter);
                questionthree.setAdapter(spinnerArrayAdapter);

                Log.e("Questions", questions.toString());


                questionone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!q1.equals("")) {
                            questiononeview.setText(q1);
                            id1 = idmap.get(q1);
                            q1 = "";
                        } else {
                            questiononeview.setText(questionone.getSelectedItem().toString());
                            id1 = idmap.get(questionone.getSelectedItem().toString());
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        questiononeview.setText("Choose a question.");
                    }
                });


                questiontwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if(!q2.equals("")){
                            questiontwoview.setText(q2);
                            id2 = idmap.get(q2);
                            q2 = "";
                        }else {
                            questiontwoview.setText(questiontwo.getSelectedItem().toString());
                            id2 = idmap.get(questiontwo.getSelectedItem().toString());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        questiontwoview.setText("Choose a question.");
                    }
                });

                questionthree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       if(!q3.equals("")){
                           questionthreeview.setText(q3);
                           id3 = idmap.get(q3);
                           q3 = "";
                       }else {
                           questionthreeview.setText(questionthree.getSelectedItem().toString());
                           id3 = idmap.get(questionthree.getSelectedItem().toString());
                       }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        questionthreeview.setText("Choose a question.");
                    }
                });


            }
        });

    }


    static ArrayList<JSONObject> questionobject;


    public static void setUpAnsweredQuestions(ArrayList<JSONObject> someobjec) {
        questionobject = someobjec;

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    q1 = someobjec.get(0).getString("question");
                    questiononeview.setText(someobjec.get(0).getString("question"));
                    answerone.setText(someobjec.get(0).getString("answer"));
                    questionone.setVisibility(View.INVISIBLE);

                    q2 = someobjec.get(1).getString("question");
                    questiontwoview.setText(someobjec.get(1).getString("question"));
                    answertwo.setText(someobjec.get(1).getString("answer"));
                    questiontwo.setVisibility(View.INVISIBLE);

                    q3 = someobjec.get(2).getString("question");
                    questionthreeview.setText(someobjec.get(2).getString("question"));
                    answerthree.setText(someobjec.get(2).getString("answer"));
                    questionthree.setVisibility(View.INVISIBLE);

                    globalRequest(serverResponse, "GET", Important.getGetpagesecurityquestions(), new JSONObject(), 29, context);


                    action.setText("Edit");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page_security_question);
        context = this;
        InitLinks();
        i = getIntent();
        serverResponse = this;
        questionone = findViewById(R.id.questionone);
        questiontwo = findViewById(R.id.questiontwo);
        questionthree = findViewById(R.id.questionthree);
        answerone = findViewById(R.id.answerone);
        answertwo = findViewById(R.id.answertwo);
        answerthree = findViewById(R.id.answerthree);
        action = findViewById(R.id.action);
        questiononeview = findViewById(R.id.questiononeview);
        questiontwoview = findViewById(R.id.questiontwoview);
        questionthreeview = findViewById(R.id.questionthreeview);
        idmap = new HashMap<>();
        questions = new ArrayList<>();

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password_confirmation", i.getStringExtra("pass"));
            globalRequest(this, "POST", Important.getViewansweredsecurityquestions(), jsonObject, 28, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (action.getText().equals("Edit")) {
                    questionone.setVisibility(View.VISIBLE);
                    questiontwo.setVisibility(View.VISIBLE);
                    questionthree.setVisibility(View.VISIBLE);
                    action.setText("Save");
                } else {
                    if (id1 == id2 || id2 == id3 || id1 == id3) {
                        Toast.makeText(EditPageSecurityQuestionActivity.this, "Can no use same question twice.", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("password_confirmation", i.getStringExtra("pass"));
                            JSONArray array = new JSONArray();
                            array.put(answerone.getText().toString());
                            array.put(answertwo.getText().toString());
                            array.put(answerthree.getText().toString());
                            JSONArray array1 = new JSONArray();
                            array1.put(id1);
                            array1.put(id2);
                            array1.put(id3);
                            jsonObject1.put("ids", array1);
                            jsonObject1.put("value", array);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        globalRequest(EditPageSecurityQuestionActivity.this, "POST", Important.getAnswerpagesecurityquestion(), jsonObject1, 30, EditPageSecurityQuestionActivity.this);

                    }
                }
            }
        });


    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("questionresponse", response);
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}