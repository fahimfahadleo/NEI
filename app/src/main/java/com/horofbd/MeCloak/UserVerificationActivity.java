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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;


public class UserVerificationActivity extends AppCompatActivity implements ServerResponse, ImageResponse {

    EditText code;
    TextView proceedbutton;
    DatabaseHelper helper;
    TextView registernewpage;
    String userpass;
    EditText autotext;
    LinearLayout passwordfieldlayout;
    TextView initialize;
    static CircleImageView profilepicture;
    static TextView username;
    ImageView editname,editphone;
    CircleImageView logout;
    CardView view;
    static PopupWindow popupWindow;
    CardView changeprofilepictureview, logoutview, changeavatarview, changenameveiw, changephonenumberview;
    TextView changeprofilepicture, changeavatar, changename, changephonenumber;


    String pass;
    static Context context;
    ImageView imageView;
    int point = 0;
    int count = 0;
    static Uri resultUri;
    Dialog avatardialog;
    Dialog dialog2;
    ImageView pro;

    static String newnam;
    DrawerLayout drawerLayout;


    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void UploadFile(ServerResponse serverResponse, String requesttype, String link, File file, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse,CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//Returns null, sizes are in the options variable
                BitmapFactory.decodeFile(resultUri.getPath(), options);
                int width = options.outWidth;
                int height = options.outHeight;
//If you want, the MIME type will also be decoded (if possible)
                String type = options.outMimeType;

                Log.e("mimetype",type);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("width", width);
                    jsonObject.put("height", height);
                    UploadFile(this, "POST", Important.getChangeprofilepicture(), file, jsonObject, 18, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 40) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void showAvatarDialog() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.showavatardialog, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.avaratrecyclerview);


    }

    private void ChangeName() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.changename, null, false);
        EditText namefield = VI.findViewById(R.id.profilename);
        TextView save = VI.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = namefield.getText().toString();
                if (TextUtils.isEmpty(newname)) {
                    namefield.setError("New name can not be empty!");
                    namefield.requestFocus();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("new_name", newname);
                        newnam = newname;
                        globalRequest(UserVerificationActivity.this, "POST", Important.getChangeprofilename(), jsonObject, 17, context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(VI, width, height, focusable);
        popupWindow.showAsDropDown(username);
    }

    public static void setNewName() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText(newnam);
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });

    }

    public static void setProfilePicture() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                profilepicture.setImageURI(resultUri);
            }
        });

    }

    void changeNumber() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
        code = findViewById(R.id.passwordfield);
        registernewpage = findViewById(R.id.registernewpage);
        proceedbutton = findViewById(R.id.proceedbutton);
        helper = new DatabaseHelper(this);
        autotext = findViewById(R.id.autotext);
        passwordfieldlayout = findViewById(R.id.passwordfieldlayout);
        initialize = findViewById(R.id.initialize);
        profilepicture = findViewById(R.id.profile_image);

        username = findViewById(R.id.username);
        view = findViewById(R.id.view);
        changename = findViewById(R.id.changename);
        changenameveiw = findViewById(R.id.changenameview);
        changeprofilepicture = findViewById(R.id.changeprifilepicture);
        changeprofilepictureview = findViewById(R.id.profilepictrechangeview);
        changeavatar = findViewById(R.id.changeavatar);
        changeavatarview = findViewById(R.id.changeavatarview);
        logout = findViewById(R.id.profilelogout);
        logoutview = findViewById(R.id.logoutview);
        drawerLayout = findViewById(R.id.drawer);
        changephonenumber = findViewById(R.id.changenumber);
        changephonenumberview = findViewById(R.id.changenumberview);
        context = this;
        InitLinks();
        new Functions(this);


        logoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(UserVerificationActivity.this,"POST",Important.getProfile_logout(),new JSONObject(),8,context);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalRequest(UserVerificationActivity.this,"POST",Important.getProfile_logout(),new JSONObject(),8,context);

            }
        });
        changephonenumberview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNumber();
            }
        });
        changephonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNumber();
            }
        });


        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeName();
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        changenameveiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeName();
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });


        changeprofilepictureview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .setAllowFlipping(false)
                        .setAspectRatio(1, 1)

                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setOutputCompressQuality(90)
                        .start(UserVerificationActivity.this);
            }
        });
        changeprofilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .setAllowFlipping(false)
                        .setAspectRatio(1, 1)

                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setOutputCompressQuality(90)
                        .start(UserVerificationActivity.this);
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
        ImageRequest(this,profilepicture, "GET", Important.getViewprofilepicture(), new JSONObject(), 1);


        String s = "~+8801914616453@MeCloak> auth status\n" +
                "~+8801914616453@MeCloak> connected to Fahim Fahad Leon\n" +
                "~+8801914616453@MeCloak> requesting session\n" +
                "~+8801914616453@MeCloak> accepted with (" + getSaltString() + ")\n" +
                "~+8801914616453@MeCloak> generating request id\n" +
                "~+8801914616453@MeCloak> successful!!\n" +
                "~+8801914616453@MeCloak> loading horoftech crypto\n" +
                "~+8801914616453@MeCloak> ... ok\n" +
                "~+8801914616453@MeCloak> loading MeCloak premium icon\n" +
                "~+8801914616453@MeCloak> failed/... ok\n" +
                "~+8801914616453@MeCloak> loading boundage policy\n" +
                "~+8801914616453@MeCloak> ... ok\n" +
                "~+8801914616453@MeCloak> loading modules\n" +
                "~+8801914616453@MeCloak> https ... ok\n" +
                "~+8801914616453@MeCloak> wss ... ok\n" +
                "~+8801914616453@MeCloak> LDAP ... ok\n" +
                "~+8801914616453@MeCloak> SqLite ... ok\n" +
                "~+8801914616453@MeCloak> XML ... ok\n" +
                "~+8801914616453@MeCloak> bash ... ok\n" +
                "~+8801914616453@MeCloak> loading certificates ... ok\n" +
                "~+8801914616453@MeCloak> establishing cryptographic structure ... ok\n" +
                "~+8801914616453@MeCloak> starting MeCloak\n";
        String[] chararray = s.split("\n");


        int length = chararray.length - 1;


        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                autotext.setCursorVisible(true);
                                autotext.setText(autotext.getText().toString() + "\n" + chararray[point]);
                                autotext.setSelection(autotext.getText().length());

                                if (point == length) {
                                    t.cancel();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    autotext.setVisibility(View.GONE);
                                                    passwordfieldlayout.setVisibility(View.VISIBLE);
                                                    initialize.setText("Page Login");
                                                }
                                            });
                                        }
                                    }, 1000);
                                }
                                point++;
                            }
                        });
                    }
                },
                0,      // run first occurrence immediatetly
                150); // run every two seconds

//        autotext.setVisibility(View.GONE);
//        passwordfieldlayout.setVisibility(View.VISIBLE);


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
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = code.getText().toString();

                if (TextUtils.isEmpty(pass)) {
                    code.setError("Password Must Be Filled!");
                    code.requestFocus();
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
                }else {
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