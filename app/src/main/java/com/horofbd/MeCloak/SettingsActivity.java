package com.horofbd.MeCloak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements ServerResponse {

    CardView premiumview, profilepasswordview, pageresetcodeview, pageresetcrtview, profileresetcrtview, pagerecoveryphotoview,
            pagerecoveryquestionview, logininformationview, blocklistview, vaultview, createnewpageview, deletepageview,
            aboutview;

    TextView premium, profilepassword, pageresetcode, pageresetcrt, profileresetcrt, pagerecoveryphoto,
            pagerecoveryquestion, logininformation, blocklist, vault, createnewpage, deletepage,
            about;

    AlertDialog updateprofilepassworddialog, passwordconfirmation,resetcodedialog;

    static {
        System.loadLibrary("native-lib");
    }

    ImageView backbutton;

    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();





    void changeProfilePassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.resetprofilepassword, null, false);
        EditText oldpass = view.findViewById(R.id.oldpassword);
        EditText newpass = view.findViewById(R.id.newpassword);
        EditText connewpass = view.findViewById(R.id.confirmpassword);
        TextView submit = view.findViewById(R.id.submitbutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old = oldpass.getText().toString();
                String newp = newpass.getText().toString();
                String conf = connewpass.getText().toString();

                if (TextUtils.isEmpty(old)) {
                    oldpass.setError("Field Can Not Be Empty!");
                    oldpass.requestFocus();
                } else if (TextUtils.isEmpty(newp)) {
                    newpass.setError("Field Can Not Be Emptye!");
                    newpass.requestFocus();
                } else if (TextUtils.isEmpty(conf)) {
                    connewpass.setError("Field Can Not Be Empty!");
                    connewpass.requestFocus();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("old_password", old);
                        jsonObject.put("password", newp);
                        jsonObject.put("password_confirmation", conf);
                        globalRequest(SettingsActivity.this, "POST", Important.getUpdateprofilepassword(), jsonObject, 23, SettingsActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    updateprofilepassworddialog.dismiss();
                }
            }
        });

        builder.setView(view);
        updateprofilepassworddialog = builder.create();
        updateprofilepassworddialog.show();
    }


    void getResetCode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword,null,false);
        EditText passfield = vi.findViewById(R.id.password);
        TextView cancel = vi.findViewById(R.id.cancelbutton);
        TextView proceed = vi.findViewById(R.id.sealbutton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passfield.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    passfield.setError("Field Can Not Be Empty!");
                    passfield.requestFocus();
                }else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("password_confirmation", pass);
                        globalRequest(SettingsActivity.this, "POST", Important.getCreatepagerecoverycode(), jsonObject, 21, SettingsActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resetcodedialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetcodedialog.dismiss();
            }
        });

        builder.setView(vi);
        resetcodedialog = builder.create();
        resetcodedialog.show();

    }

    void movetoPageResetQuestion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword,null,false);
        EditText passfield = vi.findViewById(R.id.password);
        TextView cancel = vi.findViewById(R.id.cancelbutton);
        TextView proceed = vi.findViewById(R.id.sealbutton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passfield.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    passfield.setError("Field Can Not Be Empty!");
                    passfield.requestFocus();
                }else {
                    Intent i = new Intent(SettingsActivity.this,EditPageSecurityQuestionActivity.class);
                    i.putExtra("pass",pass);
                    i.putExtra("action","uploadorview");
                    startActivity(i);
                    passwordconfirmation.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordconfirmation.dismiss();
            }
        });
        builder.setView(vi);
        passwordconfirmation = builder.create();
        passwordconfirmation.show();
    }


    void movetoPageResetImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword,null,false);
        EditText passfield = vi.findViewById(R.id.password);
        TextView cancel = vi.findViewById(R.id.cancelbutton);
        TextView proceed = vi.findViewById(R.id.sealbutton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passfield.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    passfield.setError("Field Can Not Be Empty!");
                    passfield.requestFocus();
                }else {
                    Intent i = new Intent(SettingsActivity.this,PageResetImage.class);
                    i.putExtra("pass",pass);
                    i.putExtra("action","uploadorview");
                    startActivity(i);
                    passwordconfirmation.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordconfirmation.dismiss();
            }
        });
        builder.setView(vi);
        passwordconfirmation = builder.create();
        passwordconfirmation.show();

    }


    void movetoCreateNewPage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword,null,false);
        EditText passfield = vi.findViewById(R.id.password);
        TextView cancel = vi.findViewById(R.id.cancelbutton);
        TextView proceed = vi.findViewById(R.id.sealbutton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passfield.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    passfield.setError("Field Can Not Be Empty!");
                    passfield.requestFocus();
                }else {
                    Intent i = new Intent(SettingsActivity.this,RegisterVerifiedUser.class);
                    i.putExtra("pass",pass);
                    i.putExtra("newpage","createnewpage");
                    startActivity(i);
                    passwordconfirmation.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordconfirmation.dismiss();
            }
        });
        builder.setView(vi);
        passwordconfirmation = builder.create();
        passwordconfirmation.show();

    }


    void DeletePage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vi = getLayoutInflater().inflate(R.layout.requireprofilepassword,null,false);
        EditText passfield = vi.findViewById(R.id.password);
        TextView cancel = vi.findViewById(R.id.cancelbutton);
        TextView proceed = vi.findViewById(R.id.sealbutton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = passfield.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    passfield.setError("Field Can Not Be Empty!");
                    passfield.requestFocus();
                }else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("password_confirmation", pass);
                        globalRequest(SettingsActivity.this,"POST",Important.getPage_delete(),jsonObject,36,SettingsActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    passwordconfirmation.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordconfirmation.dismiss();
            }
        });
        builder.setView(vi);
        passwordconfirmation = builder.create();
        passwordconfirmation.show();

    }

    void init() {


        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this, "GetPremium", "");
            }
        });
        premiumview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this, "GetPremium", "");
            }
        });
        profilepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePassword();
            }
        });

        profilepasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePassword();
            }
        });
        pageresetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResetCode();
            }
        });
        pageresetcodeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResetCode();
            }
        });
        pageresetcrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //page reset crt create
                // globalRequest(SettingsActivity.this,"GET",Important.getCreatepagerecoverycode(),new JSONObject(),24,SettingsActivity.this);
            }
        });

        pageresetcrtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //page reset crt create
                //globalRequest(SettingsActivity.this,"GET",Important.getCreatepagerecoverycode(),new JSONObject(),24,SettingsActivity.this);
            }
        });
        profileresetcrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //profile certificate
                //globalRequest(SettingsActivity.this,"GET",Important.getCreatepagerecoverycode(),new JSONObject(),24,SettingsActivity.this);

            }
        });

        profileresetcrtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //profile certificate
                //globalRequest(SettingsActivity.this,"GET",Important.getCreatepagerecoverycode(),new JSONObject(),24,SettingsActivity.this);

            }
        });
        pagerecoveryphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 movetoPageResetImage();

            }
        });
        pagerecoveryphotoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoPageResetImage();

            }
        });
        pagerecoveryquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoPageResetQuestion();
            }
        });
        pagerecoveryquestionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoPageResetQuestion();
            }
        });
        logininformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,LoggedInDevices.class));
            }
        });
        logininformationview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,LoggedInDevices.class));

            }
        });
        blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"BlockList","");
            }
        });
        blocklistview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"BlockList","");
            }
        });

        vault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "vault er link", Toast.LENGTH_SHORT).show();
            }
        });
        vaultview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "vault er link", Toast.LENGTH_SHORT).show();
            }
        });
        createnewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoCreateNewPage();
            }
        });
        createnewpageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoCreateNewPage();
            }
        });
        deletepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletePage();
            }
        });
        deletepageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletePage();
            }
        });
        aboutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"About","");
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(SettingsActivity.this,"About","");
            }
        });
    }


    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
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
        vault = findViewById(R.id.vault);
        vaultview = findViewById(R.id.vaultview);
        createnewpage = findViewById(R.id.createnewpage);
        createnewpageview = findViewById(R.id.createnewpageview);
        deletepage = findViewById(R.id.deletepage);
        deletepageview = findViewById(R.id.deletepageview);
        about = findViewById(R.id.about);
        aboutview = findViewById(R.id.aboutview);
        InitLinks();
        init();


        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
}