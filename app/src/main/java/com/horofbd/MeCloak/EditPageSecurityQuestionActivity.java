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
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView action;


    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static Context context;
    Intent i;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    static ArrayList<String> questions;

    Spinner questionone, questiontwo, questionthree;
    TextView answerone, answertwo, answerthree;
    TextView questiononeview,questiontwoview,questionthreeview;

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

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        questions); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        questionone.setAdapter(spinnerArrayAdapter);
        questiontwo.setAdapter(spinnerArrayAdapter);
        questionthree.setAdapter(spinnerArrayAdapter);

        questionone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("itemselected",adapterView.getItemAtPosition(i).toString());
                questiononeview.setText(questionone.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        questiontwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                questiontwoview.setText(questiontwo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        questionthree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                questionthreeview.setText(questionthree.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject.put("password_confirmation", i.getStringExtra("pass"));
                    JSONArray array = new JSONArray();
                    array.put(answerone.getText().toString());
                    array.put(answertwo.getText().toString());
                    array.put(answerthree.getText().toString());
                    JSONArray array1 = new JSONArray();
                    array1.put(idmap.get(questionone.getSelectedItem().toString()));
                    array1.put(idmap.get(questiontwo.getSelectedItem().toString()));
                    array1.put(idmap.get(questionthree.getSelectedItem().toString()));
                    jsonObject1.put("ids", array1);
                    jsonObject1.put("value", array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                globalRequest(EditPageSecurityQuestionActivity.this, "POST", Important.getUpload_page_security_question(), jsonObject1, 30, EditPageSecurityQuestionActivity.this);
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