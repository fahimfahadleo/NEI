package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    CardView premiumview,profilepasswordview,pageresetcodeview,pageresetcrtview,profileresetcrtview,pagerecoveryphotoview,
    pagerecoveryquestionview,logininformationview,blocklistview,prepareforofflineview,vaultview,createnewpageview,deletepageview,
    aboutview;

    TextView premium,profilepassword,pageresetcode,pageresetcrt,profileresetcrt,pagerecoveryphoto,
            pagerecoveryquestion,logininformation,blocklist,prepareforoffline,vault,createnewpage,deletepage,
            about;

    static{
        System.loadLibrary("native-lib");
    }
    static native void StartActivity(Context context, String activity, String data);


    void init(){
        premium = findViewById(R.id.premium);
        premiumview = findViewById(R.id.premiumview);
        profilepassword = findViewById(R.id.changeprofilepassword);
        profilepasswordview = findViewById(R.id.changeprofilepasswordview);
        pageresetcode = findViewById(R.id.getresetcode);
        pageresetcodeview = findViewById(R.id.getpageresetcodeview);
        pageresetcrt = findViewById(R.id.getpageresetcertificate);
        pageresetcrtview = findViewById(R.id.getpageresetcertificateview);
        profileresetcrt = findViewById(R.id.getprofileresetcertificate);
        profileresetcrtview = findViewById(R.id.getprofileresetcertificateview);
        pagerecoveryphoto = findViewById(R.id.pagerecoveryimage);
        pagerecoveryphotoview = findViewById(R.id.pagerecoveryimageview);
        pagerecoveryquestion = findViewById(R.id.pagerecoveryquestion);
        pagerecoveryquestionview = findViewById(R.id.pagerecoveryquestionview);
        logininformation = findViewById(R.id.logininfo);
        logininformationview = findViewById(R.id.logininfoview);
        blocklist = findViewById(R.id.blocklist);
        blocklistview = findViewById(R.id.blocklistview);
        prepareforoffline = findViewById(R.id.prepareforoffline);
        prepareforofflineview = findViewById(R.id.prepareforofflineview);
        vault = findViewById(R.id.vault);
        vaultview = findViewById(R.id.vaultview);
        createnewpage = findViewById(R.id.createnewpage);
        createnewpageview = findViewById(R.id.createnewpageview);
        deletepage = findViewById(R.id.delete);
        deletepageview = findViewById(R.id.deleteview);
        about = findViewById(R.id.about);
        aboutview = findViewById(R.id.aboutview);

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"GetPremium","");
            }
        });
        premiumview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"GetPremium","");
            }
        });
        profilepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profilepasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pageresetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pageresetcodeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pageresetcrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pageresetcrtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        profileresetcrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profileresetcrtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pagerecoveryphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pagerecoveryphotoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pagerecoveryquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pagerecoveryquestionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logininformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logininformationview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blocklistview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        prepareforoffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        prepareforofflineview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        vault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        vaultview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        createnewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        createnewpageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deletepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deletepageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        aboutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    static Context context;
    public static void closeActivtiy(){
        Functions.dismissDialogue();
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
    }
}