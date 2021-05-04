package com.horofbd.nei;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForgotProfilePassword extends AppCompatActivity {
    LinearLayout toolbar;
    ImageView backbutton;
    ImageView menu;
    TextView titleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_profile_password);
        toolbar = findViewById(R.id.toolbar);
        backbutton = toolbar.findViewById(R.id.backbutton);
        menu = toolbar.findViewById(R.id.options);
        titleview = toolbar.findViewById(R.id.title);
        backbutton.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        titleview.setText("Forgot Password");



    }
}