package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {
    EditText features;
    ImageView dropdown;


    String[] list = {"Boundage", "Encryption", "Recharge", "Buy Premium", "Unblock", "Watch Video", "Buy Premium Congrats", ""};
    List<String> dogsList;
    PopupWindow popupWindow, popupWindow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        features = findViewById(R.id.features);
        dropdown = findViewById(R.id.dropdown);
        dogsList = new ArrayList<>(Arrays.asList(list));
        popupWindow = new PopupWindow(TutorialActivity.this);
        popupWindow2 = new PopupWindow(TutorialActivity.this);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    View vi = getLayoutInflater().inflate(R.layout.tutorialinfo, null, false);
                    ListView tutorials = vi.findViewById(R.id.tutorials);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TutorialActivity.this, android.R.layout.simple_list_item_1, dogsList);
                    tutorials.setAdapter(adapter);

                    tutorials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            features.setText(dogsList.get(i));
                            popupWindow.dismiss();
                        }
                    });

                    popupWindow.setContentView(vi);
                    popupWindow.showAsDropDown(features);
                }

            }
        });

        features.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<String> list = new ArrayList<>();
                for (String s : dogsList) {
                    if (s.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        list.add(s);
                    }

                }
                if (popupWindow2.isShowing()) {
                    popupWindow2.dismiss();
                } else {
                    View vi = getLayoutInflater().inflate(R.layout.tutorialinfo, null, false);
                    ListView tutorials = vi.findViewById(R.id.tutorials);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TutorialActivity.this, android.R.layout.simple_list_item_1, list);
                    tutorials.setAdapter(adapter);

                    tutorials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            features.setText(list.get(i));
                            popupWindow2.dismiss();
                        }
                    });
                    popupWindow2.setContentView(vi);
                    popupWindow2.showAsDropDown(features);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

}