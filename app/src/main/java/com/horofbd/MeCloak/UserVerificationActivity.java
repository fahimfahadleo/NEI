package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;


public class UserVerificationActivity extends AppCompatActivity implements ServerResponse, ImageResponse {

    EditText code;
    TextView proceedbutton;
    DatabaseHelper helper;
    TextView registernewpage,forgotpagepassword;
    LinearLayout passwordfieldlayout;
    static CircleImageView profilepicture;
    static TextView username;


    String pass;
    static Context context;
    static Uri resultUri;
    Dialog dialog2;
    ImageView pro;

    TextView userphonenumber;
    ImageView passunhide;
    boolean ispasshidden = false;
    TextView signout;


    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);
    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void UploadFile(ServerResponse serverResponse, String requesttype, String link, String filetype, File file, JSONObject jsonObject, int requestcode, Context context);



    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                File file = new File(resultUri.getPath());


                Log.e("path",resultUri.getPath());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//Returns null, sizes are in the options variable
                BitmapFactory.decodeFile(resultUri.getPath(), options);
                int width = options.outWidth;
                int height = options.outHeight;
//If you want, the MIME type will also be decoded (if possible)
                String type = options.outMimeType;

                Log.e("mimetype", type);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("width", width);
                    jsonObject.put("height", height);





                    UploadFile(this, "POST", Important.getChangeprofilepicture(), "avatar", file, jsonObject, 18, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        registernewpage = findViewById(R.id.registernewpage);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);
        passwordfieldlayout = findViewById(R.id.passwordfieldlayout);
        userphonenumber = findViewById(R.id.userphonenumber);
        passunhide = findViewById(R.id.showpass);
        signout = findViewById(R.id.signout);

        profilepicture = findViewById(R.id.profile_image);
        forgotpagepassword = findViewById(R.id.forgotpassword);

        username = findViewById(R.id.username);

        context = this;
        InitLinks();
        new Functions(this);

        userphonenumber.setText(getLoginInfo("phone_no"));


        forgotpagepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserVerificationActivity.this,ForgotPagePassword.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(UserVerificationActivity.this, "POST", Important.getProfile_logout(), new JSONObject(), 8, context);

            }
        });
        passunhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ispasshidden) {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye_blocked));
                    code.setTransformationMethod(new PasswordTransformationMethod());
                    ispasshidden = false;
                } else {
                    passunhide.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    code.setTransformationMethod(null);
                    ispasshidden = true;
                }
            }
        });

        code.addTextChangedListener(new TextWatcher() {
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


        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2 = null;
                dialog2 = new Dialog(UserVerificationActivity.this) {
                    @Override
                    public void onBackPressed() {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        super.onBackPressed();
                    }
                };
                View vi = getLayoutInflater().inflate(R.layout.viewprofilepicture, null, false);
                pro = vi.findViewById(R.id.profilepicture);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                pro.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                pro.getLayoutParams().width = width;
                pro.requestLayout();
                pro.setImageBitmap(bitmap);
                dialog2.setContentView(vi);
                dialog2.show();
            }
        });
        username.setText(getLoginInfo("user_name"));
        ImageRequest(this, profilepicture, "GET", Important.getViewprofilepicture(), new JSONObject(), 1);


        registernewpage = findViewById(R.id.registernewpage);

        registernewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(UserVerificationActivity.this, "RegisterVerifiedUser");
            }
        });

//        Cursor c = helper.getUserPassword(1);
//        if (c.moveToFirst()){
//             userpass = c.getString(1);
//
//        }


        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                    if(code.getError()!=null){
                        code.setError(null);
                    }
                    passunhide.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = code.getText().toString();

                if (TextUtils.isEmpty(pass)) {
                    code.setError("Password Must Be Filled!");
                    code.requestFocus();
                    passunhide.setVisibility(View.INVISIBLE);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("password", pass);
                        globalRequest(UserVerificationActivity.this, "POST", Important.getPage_login(), jsonObject, 4, context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if(pass.equals(userpass)){
//                    ServerRequest.PageLogin(UserVerificationActivity.this,pass,1);
//                }
            }
        });
        //  c.close();


    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("Userverification", response);
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserVerificationActivity.this, failresponse, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onImageFailure(String failresponse) throws JSONException {
        Log.e("failed", failresponse);
    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }
    Bitmap bitmap;
}