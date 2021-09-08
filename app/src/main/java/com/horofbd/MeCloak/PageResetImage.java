package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class PageResetImage extends AppCompatActivity implements ServerResponse, ImageResponse {

    Context context;
    Bitmap bitmap;

    static {
        System.loadLibrary("native-lib");
    }

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks(Context context);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);

    static native void UploadFile(ServerResponse serverResponse, String requesttype, String link, String filetype, File file, JSONObject jsonObject, int requestcode, Context context);

//
//    ImageView   ivImage1 = (ImageView ) findViewById(R.id.img_add1_send );
//    getStringImage( ( (BitmapDrawable) ivImage1.getDrawable( ) ).getBitmap( ) ),
//
//
//
//    public String getStringImage(Bitmap bm){
//        ByteArrayOutputStream ba=new ByteArrayOutputStream(  );
//        bm.compress( Bitmap.CompressFormat.PNG,90,ba );
//        byte[] by=ba.toByteArray();
//        String encod= Base64.encodeToString( by, Base64.DEFAULT );
//        return encod;
//    }


    ImageView backbutton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                uploadedImage.setImageBitmap(bitmap);
                uploadimage.setVisibility(View.GONE);
                uploadedImage.buildDrawingCache();
                Bitmap temp = Bitmap.createBitmap(uploadedImage.getDrawingCache(), 0, 0, uploadedImage.getWidth(), uploadedImage.getHeight());
                mutableBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
                Uri tempUri = getImageUri(getApplicationContext(), mutableBitmap);
                Log.e("path",getRealPathFromURI(tempUri));
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(tempUri));
                Log.e("mimetype",getMimeType(getRealPathFromURI(tempUri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



    Intent i;
    ImageView uploadimage;
    static ImageView uploadedImage;
    TextView actionbutton;
    TextView deleteposition;
    ArrayList<String> list;
    TextView deletephoto;
    Bitmap mutableBitmap;
    File file;

    public static void setUpImage(String s){

        Log.e("called","Called");
        InputStream is =new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        uploadedImage.setImageBitmap(bitmap);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_reset_image);
        i = getIntent();
        context = this;
        uploadedImage = findViewById(R.id.uploadimage);
        uploadimage = findViewById(R.id.uploadbutton);
        actionbutton = findViewById(R.id.action);
        deleteposition = findViewById(R.id.deleteposition);
        deleteposition.setVisibility(View.GONE);
        deletephoto = findViewById(R.id.deletephoto);
        list = new ArrayList<>();

        switch (i.getStringExtra("action")) {
            case "uploadorview": {
                actionbutton.setText("Save");
                break;
            }
            case "chellenge": {
                actionbutton.setText("Submit");


                //get challenge image;

                break;
            }
        }
      backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password_confirmation",i.getStringExtra("pass"));
                    globalRequest(PageResetImage.this,"POST",Important.getDeletepagesecurityimage(),jsonObject,27,PageResetImage.this);
                    uploadedImage.setImageBitmap(null);
                    deletephoto.setVisibility(View.GONE);
                    uploadimage.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("challenge","0");
            jsonObject.put("password_confirmation",i.getStringExtra("pass"));
            ImageRequest(this,new CircleImageView(this),"POST",Important.getDownloadpagesecurityimage(),jsonObject,1);

        }catch (JSONException e){
            e.printStackTrace();
        }

        actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(i.getStringExtra("action").equals("uploadorview")){
                    if (list.size() < 4) {
                        Toast.makeText(context, "At least 4 points are neeeded.", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray arrayX = new JSONArray();
                        JSONArray arrayY = new JSONArray();

                        try {
                            for (String s : list) {
                                arrayX.put(s.split(",")[0]);
                                arrayY.put(s.split(",")[1]);
                            }

                            jsonObject.put("x",arrayX);
                            jsonObject.put("y",arrayX);
                            jsonObject.put("password_confirmation",i.getStringExtra("pass"));


                            Log.e("filetype",getMimeType(file.getPath()));

                            UploadFile(PageResetImage.this, "POST", Important.getUploadpagerecoveryimage(), "file", file, jsonObject, 26, context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    //  todo chillenge
                }
            }
        });

        uploadedImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (bitmap != null) {
                        Paint paint = new Paint();
                        int radius = 24;
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(getResources().getColor(R.color.red_500));
                        Canvas canvas = new Canvas(mutableBitmap);
                        canvas.drawCircle(x, y, radius, paint);
                        ImageView iv = (ImageView) v;
                        iv.setImageBitmap(mutableBitmap);
                        String s = x + "," + y;
                        list.add(s);
                        deleteposition.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "Choose a Photo first!", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });

        deleteposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                uploadedImage.setImageBitmap(bitmap);
                Bitmap temp = Bitmap.createBitmap(uploadedImage.getDrawingCache(), 0, 0, uploadedImage.getWidth(), uploadedImage.getHeight());
                mutableBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
            }
        });


        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, 1);
            }
        });

    }


    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
        try {
            String s = response.body().string();
            Log.e("response",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.has("password_confirmation")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Unauthorised!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }catch (Exception e){
                Log.e("error",e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Log.e("bitmap2", "null");
        runOnUiThread(() -> {

            if (bitmap != null) {
                Log.e("bitmap", "notnull");
                uploadedImage.setImageBitmap(bitmap);
                uploadimage.setVisibility(View.GONE);
                deletephoto.setVisibility(View.VISIBLE);
                actionbutton.setVisibility(View.GONE);


            } else {
                Log.e("bitmap", "null");


            }
        });
    }

    @Override
    public void onImageFailure(String failresponse) throws JSONException {

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this,this,response,requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}