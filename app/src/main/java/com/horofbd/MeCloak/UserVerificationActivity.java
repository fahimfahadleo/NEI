package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

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
    ImageView nameedit;
    static TextView username;
    CardView view;
    static PopupWindow popupWindow;
    static AlertDialog dialog;

    static {
        System.loadLibrary("native-lib");
    }

    static native void StartActivity(Context context, String activity);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void UploadFile(ServerResponse serverResponse, String requesttype, String link, File file, JSONObject jsonObject, int requestcode, Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, String requestType, String Link, JSONObject jsonObject,int requestcode);


    int point = 0;
    static Uri resultUri;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.e("CropResult", "result successfull");
                File file = new File(resultUri.getPath());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

//Returns null, sizes are in the options variable
                BitmapFactory.decodeFile(resultUri.getPath(), options);
                int width = options.outWidth;
                int height = options.outHeight;
//If you want, the MIME type will also be decoded (if possible)
                String type = options.outMimeType;


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

    Dialog avatardialog;

    void showAvatarDialog() {
        View view = getLayoutInflater().inflate(R.layout.showavatardialog, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.avaratrecyclerview);


    }

    Dialog dialog2;
    ImageView pro;

    private void initDialogue() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View VI = inflater.inflate(R.layout.nameeditmenu, null, false);
        CardView viewprofileview = VI.findViewById(R.id.viewprofileview);
        TextView viewprofile = VI.findViewById(R.id.viewprofile);
        CardView changeprofileview = VI.findViewById(R.id.changeprofileview);
        TextView changeprofile = VI.findViewById(R.id.changeprofile);
        CardView changeavatarview = VI.findViewById(R.id.changeavatarview);
        TextView changeavatar = VI.findViewById(R.id.changeavatar);

        // create the popup window


        changeavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .setAllowFlipping(false)
                        .setAspectRatio(1, 1)
//                        .setMinCropResultSize(128, 128)
//                        .setMaxCropResultSize(128, 128)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setOutputCompressQuality(90)
                        .start(UserVerificationActivity.this);
            }
        });

        changeprofileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .setAllowFlipping(false)
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(128, 128)
                        .setMaxCropResultSize(128, 128)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setOutputCompressQuality(90)
                        .start(UserVerificationActivity.this);
            }
        });


        viewprofileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2 = null;
                dialog2 = new Dialog(UserVerificationActivity.this){
                    @Override
                    public void onBackPressed() {
                        if(dialog2!=null && dialog2.isShowing()){
                            dialog2.dismiss();
                        }
                        super.onBackPressed();
                    }
                };
                View vi = getLayoutInflater().inflate(R.layout.viewprofilepicture,null,false);
                pro = vi.findViewById(R.id.profilepicture);

                ImageRequest(UserVerificationActivity.this, "GET", Important.getViewprofilepicture(), new JSONObject(),2);

                dialog.setContentView(vi);
                dialog.show();
            }
        });

        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog2 = null;

                dialog2 = new Dialog(UserVerificationActivity.this){
                    @Override
                    public void onBackPressed() {
                        if(dialog2!=null && dialog2.isShowing()){
                            dialog2.dismiss();
                        }

                        super.onBackPressed();
                    }
                };
                View vi = getLayoutInflater().inflate(R.layout.viewprofilepicture,null,false);
                pro = vi.findViewById(R.id.profilepicture);


                ImageRequest(UserVerificationActivity.this, "GET", Important.getViewprofilepicture(), new JSONObject(),2);

                dialog.setContentView(vi);
                dialog.show();
            }
        });

        builder.setView(VI);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
        dialog.show();


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
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(VI, width, height, focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAsDropDown(username);

    }

    static String newnam;

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
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

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
        nameedit = findViewById(R.id.nameedit);
        username = findViewById(R.id.username);
        view = findViewById(R.id.view);
        InitLinks();
        new Functions(this);


        nameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeName();
            }
        });


        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogue();
            }
        });


        username.setText(getLoginInfo("user_name"));
        ImageRequest(this, "GET", Important.getViewprofilepicture(), new JSONObject(),1);


//        String s = "~+8801914616453@MeCloak> auth status\n" +
//                "~+8801914616453@MeCloak> connected to Fahim Fahad Leon\n" +
//                "~+8801914616453@MeCloak> requesting session\n" +
//                "~+8801914616453@MeCloak> accepted with (" + getSaltString() + ")\n" +
//                "~+8801914616453@MeCloak> generating request id\n" +
//                "~+8801914616453@MeCloak> successful!!\n" +
//                "~+8801914616453@MeCloak> loading horoftech crypto\n" +
//                "~+8801914616453@MeCloak> ... ok\n" +
//                "~+8801914616453@MeCloak> loading MeCloak premium icon\n" +
//                "~+8801914616453@MeCloak> failed/... ok\n" +
//                "~+8801914616453@MeCloak> loading boundage policy\n" +
//                "~+8801914616453@MeCloak> ... ok\n" +
//                "~+8801914616453@MeCloak> loading modules\n" +
//                "~+8801914616453@MeCloak> https ... ok\n" +
//                "~+8801914616453@MeCloak> wss ... ok\n" +
//                "~+8801914616453@MeCloak> LDAP ... ok\n" +
//                "~+8801914616453@MeCloak> SqLite ... ok\n" +
//                "~+8801914616453@MeCloak> XML ... ok\n" +
//                "~+8801914616453@MeCloak> bash ... ok\n" +
//                "~+8801914616453@MeCloak> loading certificates ... ok\n" +
//                "~+8801914616453@MeCloak> establishing cryptographic structure ... ok\n" +
//                "~+8801914616453@MeCloak> starting MeCloak\n";
//        String[] chararray = s.split("\n");
//
//
//        int length = chararray.length - 1;
//
//
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(
//                new TimerTask() {
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                autotext.setCursorVisible(true);
//                                autotext.setText(autotext.getText().toString() + "\n" + chararray[point]);
//                                autotext.setSelection(autotext.getText().length());
//
//                                if (point == length) {
//                                    t.cancel();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    autotext.setVisibility(View.GONE);
//                                                    passwordfieldlayout.setVisibility(View.VISIBLE);
//                                                    initialize.setText("Page Login");
//                                                }
//                                            });
//                                        }
//                                    }, 1000);
//                                }
//                                point++;
//                            }
//                        });
//                    }
//                },
//                0,      // run first occurrence immediatetly
//                150); // run every two seconds

        autotext.setVisibility(View.GONE);
        passwordfieldlayout.setVisibility(View.VISIBLE);


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

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password", pass);
                    globalRequest(UserVerificationActivity.this, "POST", Important.getPage_login(), jsonObject, 4, context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(pass.equals(userpass)){
//                    ServerRequest.PageLogin(UserVerificationActivity.this,pass,1);
//                }
            }
        });
        //  c.close();

        context = this;
    }

    String pass;
    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
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
    ImageView imageView;
    @Override
    public void onImageResponse(Response response, int code,int requestcode) throws JSONException {

        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(requestcode == 1){
                    imageView = profilepicture;
                }else if(requestcode == 2){
                    imageView = pro;
                }
                if (bitmap != null) {
                    setImage(imageView,bitmap);
                } else {
                    setImage(imageView,BitmapFactory.decodeResource(getResources(),
                            R.drawable.person));
                }
            }
        });


    }

    @Override
    public void onImageFailure(String failresponse) throws JSONException {
        Log.e("failed", failresponse);
    }


    void setImage(ImageView imageView,Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);

    }
}