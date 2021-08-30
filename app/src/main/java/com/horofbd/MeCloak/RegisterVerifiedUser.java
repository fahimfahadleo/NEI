package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class RegisterVerifiedUser extends AppCompatActivity implements ServerResponse, ImageResponse {
    EditText password, confirmpassword, pagename;
    TextView proceed;
    static CircleImageView profilepicture;
    TextView username;
    TextView userphonenumber;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void InitLinks();

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);


    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    static ServerResponse serverResponse;

    Intent i;

    ImageView passunhide, conpassunhide;
    boolean ispasshidden = false;
    boolean isconpasshidden = false;
    static AlertDialog dialog;

    public static void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pagecreateworning, null, false);
        TextView cancel = view.findViewById(R.id.dismiss);
        TextView visit = view.findViewById(R.id.visit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(serverResponse, "POST", Important.getPagelogout(), new JSONObject(), 34, context);
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verified_user);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        proceed = findViewById(R.id.proceedbutton);
        profilepicture = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        passunhide = findViewById(R.id.showpass);
        conpassunhide = findViewById(R.id.showconfirmpass);
        userphonenumber = findViewById(R.id.userphonenumber);
        pagename = findViewById(R.id.pagename);

        new Functions(this);

        i = getIntent();
        InitLinks();
        context = this;

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passunhide.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                conpassunhide.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passunhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ispasshidden) {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye_blocked));
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    ispasshidden = false;
                } else {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    password.setTransformationMethod(null);
                    ispasshidden = true;
                }
            }
        });
        conpassunhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isconpasshidden) {
                    conpassunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye_blocked));
                    confirmpassword.setTransformationMethod(new PasswordTransformationMethod());
                    isconpasshidden = false;
                } else {
                    conpassunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    confirmpassword.setTransformationMethod(null);
                    isconpasshidden = true;
                }
            }
        });

        ImageRequest(this, profilepicture, "GET", Important.getViewprofilepicture(), new JSONObject(), 1);


        userphonenumber.setText(getLoginInfo("phone_no"));
        username.setText(getLoginInfo("user_name"));

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String confirmpass = confirmpassword.getText().toString();
                String name = pagename.getText().toString();

                boolean isOk = false;

                if (TextUtils.isEmpty(pass)) {
                    password.setError("Field can not be empty!");
                    password.requestFocus();
                    isOk = true;
                }
                if (TextUtils.isEmpty(confirmpass)) {
                    confirmpassword.requestFocus();
                    confirmpassword.setError("Field can not be empty!");
                    isOk = true;
                }
                if (TextUtils.isEmpty(name)) {
                    pagename.requestFocus();
                    pagename.setError("Field can not be empty!");
                    isOk = true;
                }

                if (pass.equals(confirmpass)) {


                    if (!isOk) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("name", name);
                            jsonObject.put("password", pass);
                            jsonObject.put("page_password_confirmation", confirmpass);

                            Log.e("link", Important.getPage_register());

                            if (i.hasExtra("newpage")) {
                                jsonObject.put("password_confirmation", i.getStringExtra("pass"));
                                globalRequest(RegisterVerifiedUser.this, "POST", Important.getPage_register(), jsonObject, 33, context);
                            } else {
                                jsonObject.put("password_confirmation", getLoginInfo("password"));
                                globalRequest(RegisterVerifiedUser.this, "POST", Important.getPage_register(), jsonObject, 3, context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {
                    password.setError("Password did not match!");
                    password.requestFocus();
                }

            }
        });
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("response", response);
        CheckResponse(this, this, response, 3);

    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterVerifiedUser.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });

    }


    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    Log.e("bitmap", "notnull");
                    setImage(imageView, bitmap);
                } else {
                    Log.e("bitmap", "null");
                }
            }
        });
    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    Bitmap bitmap;

    @Override
    public void onImageFailure(String failresponse) throws JSONException {

    }
}