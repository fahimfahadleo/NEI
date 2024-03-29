package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smackx.httpfileupload.HttpFileUploadManager;
import org.jivesoftware.smackx.httpfileupload.element.Slot;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class Functions {
    static Context context;
    static SharedPreferences preferences;
    public static boolean isActive = false;
    private final String sSs = "MessagePreference";

    public Functions(Context context) {
        Functions.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);


    }


    public static String getTimeWithZone(String time) {
        DateTime dateTimeInUTC = DateTime.parse(time);
        DateTimeZone timeZoneTongatapu = DateTimeZone.forID(TimeZone.getDefault().getID());
        DateTime tongatapuDateTime = dateTimeInUTC.toDateTime(timeZoneTongatapu);
        //2021-09-07T22:28:55.000000Z
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(tongatapuDateTime.toLocalDateTime().toString());
            return new SimpleDateFormat("hh-mm a   dd/MM/yyyy").format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tongatapuDateTime.toLocalDateTime().toString();
    }

    public static String getMessageTime(String time) {

        Date datetime;
        //09-15-2021 02:43:33 +0600
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss Z");

        try {
            datetime = df.parse(time);
            return new SimpleDateFormat("hh:mma dd/MM/yyyy").format(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static JSONArray pc(Cursor c) {

        JSONArray array = new JSONArray();
        if (c != null) {
            int i = 0;
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    int length = c.getColumnCount();
                    for (int p = 0; p < length; p++) {
                        jsonObject.put(c.getColumnName(p), c.getString(p));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(jsonObject);
            }
            c.close();
            return array;
        }
        return new JSONArray();
    }


    public static void CloseActivity(Context context) {
        ((Activity) context).finish();
    }

    public static boolean isJSONArrayNull(JSONArray jsonArray) {
        if (jsonArray != null) {
            return jsonArray.length() == 0;
        } else {
            return false;
        }
    }

    static Dialog dialog;

    public static void showProgress(Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    if (!dialog.isShowing()) {
                        dialog = new Dialog(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View vi = inflater.inflate(R.layout.progressbar, null, false);
                        dialog.setContentView(vi);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                        //   dialog.setCancelable(false);
                        Log.e("context", context.toString());
                        dialog.show();
                    }
                } else {
                    dialog = new Dialog(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.progressbar, null, false);
                    dialog.setContentView(vi);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
                    //   dialog.setCancelable(false);
                    Log.e("context", context.toString());
                    dialog.show();
                }
            }
        });

    }

    public static void dismissDialogue() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    Log.e("contextdialogue", "dismissed");
                    Log.e("contextcontext", context.toString());
                    dialog.dismiss();
                }
            }
        });

    }


    public static JSONObject map(String title, String id, JSONObject jsonObject) {
        try {
            jsonObject.put(title, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getInstanse() {
        return new JSONObject();
    }

    public static void Logcat(String tag, String s) {
        Log.e(tag, s);
    }

    public static void setSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();// commit is important here.
    }

    public static boolean dataContains(String key) {
        return preferences.contains(key);
    }

    public static String getSharedPreference(String key, String defaultvalue) {
        return preferences.getString(key, defaultvalue);
    }

    public static String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    public static void ClearSharedPreference() {
        if(preferences.edit().clear().commit()){
            Log.e("preference","cleared");
        }else {
            Log.e("preference","not cleared");
        }

        preferences.edit().clear().apply();
    }

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String Base64decode(String text) {
        byte[] data = new byte[0];
        String base64 = null;
        data = Base64.decode(base64, Base64.DEFAULT);
        base64 = new String(data, StandardCharsets.UTF_8);
        return base64;
    }


    public static String Base64encode(String text) {
        byte[] data = new byte[0];
        String base64 = null;
        data = text.getBytes(StandardCharsets.UTF_8);
        base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }


    static String TAG = "Functions";


    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            Log.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            Log.e(TAG, "calculatedDigest null");
            return false;
        }

        Log.v(TAG, "Calculated digest: " + calculatedDigest);
        Log.v(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Sha1(String clearString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }


    public static void getTokenevery50minuts() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        SetPrivateToken();
                    }
                },
                0,
                50 * 1000);
    }


    public static boolean athenticationRequired = true;

    public static void Request(ServerResponse serverResponse, String requestType, String Link, JSONObject jsonObject, int requestcode) {
        OkHttpClient client;
        if (athenticationRequired) {
            client = getClient();
        } else {
            client = new OkHttpClient()
                    .newBuilder()
                    .build();
        }

        Log.e("link", Link);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody body = null;

        if (jsonObject.length() != 0) {
            Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            while (iter.hasNext()) {
                String key = iter.next();


                try {
                    Log.e("formdata", key + " : " + jsonObject.getString(key));
                    Object o = jsonObject.get(key);
                    if (o instanceof JSONArray) {
                        for (int i = 0; i < jsonObject.getJSONArray(key).length(); i++) {
                            builder.addFormDataPart(key + "[]", ((JSONArray) o).getString(i));
                        }
                    } else {
                        builder.addFormDataPart(key, jsonObject.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                builder.setType(MultipartBody.FORM);
                body = builder.build();

            }
        } else {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            body = RequestBody.create(JSON, "{}");
        }


        Request request;
        if (requestType.equals("GET")) {
            request = new Request.Builder().url(Link).method("GET", null).build();
        } else {
            request = new Request.Builder()
                    .url(Link)
                    .method(requestType, body)
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    serverResponse.onFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    serverResponse.onResponse(response.body().string(), response.code(), requestcode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void SetPrivateToken() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + Functions.getSharedPreference("private_token", ""))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(Important.getToken_refresh())
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responsestr = response.body().string();
                JSONObject jsonObject;
                String token = "";
                long expiry = 0;

                try {
                    jsonObject = new JSONObject(responsestr);
                    token = jsonObject.getString("access_token");
                    expiry = System.currentTimeMillis() + Long.parseLong(jsonObject.getString("expires_in"));


                    Functions.setSharedPreference("private_token", token);
                    Functions.setSharedPreference("expires_in", String.valueOf(expiry));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }

    private static OkHttpClient getClient() {
        OkHttpClient client1 = null;

        long expires = (Long.parseLong(Functions.getSharedPreference("expires_in", "0")) * 1000) + Long.parseLong(Functions.getSharedPreference("login_time", "0"));


        if (expires - (expires - 10 * 60 * 1000) < 10 * 60 * 1000) {
            SetPrivateToken();
            Log.e("expiry", "mela khon baki");
            client1 = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + Functions.getSharedPreference("access_token", ""))
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        } else if (System.currentTimeMillis() > expires) {


            Log.e("Expiry", String.valueOf(Long.parseLong(Functions.getSharedPreference("expires_in", "0")) * (long) 1000));
            Log.e("Expiry", Functions.getSharedPreference("login_time", "0"));
            Log.e("Expiry", String.valueOf(expires));
            Log.e("currenttime", String.valueOf(System.currentTimeMillis()));


            Log.e("expiry1111111111", "deri hoye geche");
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity) context).finish();
        } else {

            Log.e("expiry", "jothesto somoy ache");
            client1 = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + Functions.getSharedPreference("access_token", ""))
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }
        return client1;
    }


    public static void RequestForProfileRegistration(
            ServerResponse serverResponse,
            String phone,
            String name,
            String password,
            String link,
            String confirmpassword,
            String termsandconditions,
            String policy,
            @NotNull String reference,
            int requestcode) {

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("name", name);
        builder.addFormDataPart("phone_no", phone);
        builder.addFormDataPart("password", password);
        builder.addFormDataPart("password_confirmation", confirmpassword);
        builder.addFormDataPart("terms-conditions", termsandconditions);
        builder.addFormDataPart("policy", policy);
        Log.e("reference", reference);
        if (!reference.equals("")) {
            builder.addFormDataPart("ref", reference);
        }

        Log.e("reference", reference);


        builder.setType(MultipartBody.FORM);
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(link)
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    serverResponse.onFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    serverResponse.onResponse(response.body().string(), response.code(), requestcode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static String formattime(String time) {

        String timea = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", System.currentTimeMillis());
        return timea;
    }

    public static void LoginRegisteredUser(ServerResponse serverResponse, String phone, String password, String link, String devicetype, String devicename, String deviceip, String devicetime, int requestcode) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone_no", phone)
                .addFormDataPart("password", password)
                .addFormDataPart("device_type", devicetype)
                .addFormDataPart("device_name", devicename)
                .addFormDataPart("device_ip", deviceip)
                .addFormDataPart("device_time", devicetime)
                .build();
        Request request = new Request.Builder()
                .url(link)
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    serverResponse.onFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    serverResponse.onResponse(response.body().string(), response.code(), requestcode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void ShowToast(Context context, String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }


    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static String getCurrentTimeInMilliseconds() {
        return String.valueOf(System.currentTimeMillis());
    }


    public static void UploadFile(ServerResponse serverResponse, String requestType, String Link, String filetype, File file, JSONObject jsonObject, int requestcode) {
        OkHttpClient client = getClient();

        Log.e("link", Link);
        Log.e("jsonobject", jsonObject.toString());

        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody body = null;

        if (jsonObject.length() != 0) {
            Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object o = jsonObject.get(key);
                    if (o instanceof JSONArray) {
                        for (int i = 0; i < jsonObject.getJSONArray(key).length(); i++) {
                            builder.addFormDataPart(key + "[]", ((JSONArray) o).getString(i));
                        }
                    } else {
                        builder.addFormDataPart(key, jsonObject.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart(filetype, file.getName(),
                    RequestBody.create(MediaType.parse("application/octet-stream"),
                            file));
            body = builder.build();
        } else {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            body = RequestBody.create(JSON, "{}");
        }
        Request request;
        if (requestType.equals("GET")) {
            request = new Request.Builder().url(Link).method("GET", null).build();
        } else {
            request = new Request.Builder()
                    .url(Link)
                    .method(requestType, body)
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    serverResponse.onFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String s = response.body().string();
                    serverResponse.onResponse(s, response.code(), requestcode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void ImageViewImageRequest(ImageResponseImageView imageResponseImageView, ImageView imageView, String Link, int requestcode) {
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request;

        Log.e("testing ", "testing");
        request = new Request.Builder().url(Link).build();

        Log.e("testing ", "testing2");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("exceptiong", e.getMessage());
                try {
                    imageResponseImageView.onImageViewImageFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    Log.e("data", response.body().string());
                    imageResponseImageView.onImageViewImageResponse(response, response.code(), requestcode, imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode) {
        OkHttpClient client = getClient();
        Request request;
        HttpUrl.Builder httpBuilder = HttpUrl.parse(Link).newBuilder();
        if (jsonObject.length() != 0) {
            Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    httpBuilder.addQueryParameter(key, jsonObject.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody body = null;

        if (jsonObject.length() != 0) {
            Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    builder.addFormDataPart(key, jsonObject.getString(key));
                    builder.setType(MultipartBody.FORM);
                    body = builder.build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            body = RequestBody.create(JSON, "{}");
        }


        if (requestType.equals("GET")) {
            request = new Request.Builder().url(httpBuilder.build()).method("GET", null).build();
        } else {
            request = new Request.Builder()
                    .url(Link)
                    .method(requestType, body)
                    .build();
        }


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    imageResponse.onImageFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    imageResponse.onImageResponse(response, response.code(), requestcode, imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void writeToFile(String data, Context context, String name) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context, String name) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(name);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static boolean fileExists(String name, Context context) {
        try {
            context.openFileInput(name);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }


    public static X509TrustManager getTrustFactory(Context context) {
        return trustfactory;
    }


    public static void INITCRTS(Context context) {
        ArrayList<Integer> certs = new ArrayList<>();
        certs.add(R.raw.root);
        certs.add(R.raw.network);
        certs.add(R.raw.component);
        for (int i : certs) {
            CertificateFactory cf = null;
            try {
                cf = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            InputStream caInput = null;
            caInput = new BufferedInputStream(context.getResources().openRawResource(i));
            Certificate ca = null;
            try {
                try {
                    if (cf != null) {
                        ca = cf.generateCertificate(caInput);
                        try {
                            X509Certificate myCert = (X509Certificate) ca;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("certificatefactiroy", "null");
                    }
                } catch (CertificateException e) {
                    Log.e("certificateerror", e.getMessage());
                }
            } finally {
                try {
                    if (caInput != null) {
                        caInput.close();
                    } else {
                        Log.e("error", "cainputnull");
                    }
                } catch (IOException e) {
                    Log.e("ioerror", e.getMessage());
                }
            }
            if (ca != null) {
                setTrust(ca);
            } else {
                Log.e("crtname", String.valueOf(i));
                //Log.e("certificate",String.valueOf(crt));
            }
        }
    }


    static SSLContext sslContext;
    static X509TrustManager trustfactory;


    static void setTrust(Certificate ca) {
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            trustfactory = getManager(tmf);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            sslContext = context;
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException | IOException | CertificateException e) {
            Log.e("error", e.getMessage());
        }
    }

    static X509TrustManager getManager(TrustManagerFactory tmf) throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager defaultTm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                defaultTm = (X509TrustManager) tm;
                break;
            }
        }
        X509TrustManager myTm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                myTm = (X509TrustManager) tm;
                break;
            }
        }
        final X509TrustManager finalDefaultTm = defaultTm;
        final X509TrustManager finalMyTm = myTm;
        X509TrustManager customTm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // If you're planning to use client-cert auth,
                // merge results from "defaultTm" and "myTm".
                return finalDefaultTm.getAcceptedIssuers();
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                try {
                    finalMyTm.checkServerTrusted(chain, authType);
                } catch (CertificateException e) {
                    // This will throw another CertificateException if this fails too.
                    finalDefaultTm.checkServerTrusted(chain, authType);
                }
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                finalDefaultTm.checkClientTrusted(chain, authType);
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{customTm}, null);
        SSLContext.setDefault(sslContext);
        return customTm;
    }


    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static void uploadProtectedFile(ServerResponse serverResponse, AbstractXMPPConnection connection, File file, int requestcode) {
        try {
            HttpFileUploadManager httpFileUploadManager = HttpFileUploadManager.getInstanceFor(connection);
            if (httpFileUploadManager.discoverUploadService()) {
                if (httpFileUploadManager.isUploadServiceDiscovered()) {


                    long totalSize = file.length();
                    // RequestBody requestBody = RequestBody.create(file, MediaType.parse(FileUtils.getMimeType(file)));
                    RequestBody requestBody = new CountingFileRequestBody(file, FileUtils.getMimeType(file), new CountingFileRequestBody.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            float progress = (num / (float) totalSize) * 100;
                            // uploadData.progressValue = (int) progress;
                            Log.e("progress", String.valueOf(progress));


                        }
                    });

                    Log.e("mimetype", FileUtils.getMimeType(file));

                    final Slot slot = httpFileUploadManager.requestSlot(file.getName(), requestBody.contentLength(), FileUtils.getMimeType(file));

                    OkHttpClient client = getUnsafeOkHttpClient();

                    Request request = new Request.Builder()
                            .url(slot.getPutUrl())
                            .put(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(final Call call, final IOException e) {
                            try {
                                serverResponse.onFailure(e.getMessage());
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponse(final Call call, final Response response) throws IOException {

                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("body", slot.getGetUrl());
                                if (FileUtils.getMimeType(file).contains("image")) {
                                    jsonObject.put("type", "image");
                                } else if (FileUtils.getMimeType(file).contains("video")) {
                                    jsonObject.put("type", "video");
                                } else {
                                    jsonObject.put("type", "file");
                                }

                                serverResponse.onResponse(jsonObject.toString(), 200, requestcode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                } else {
                    Log.e("uploadservice", "false");
                }
            } else {
                Log.e("httpuploadservice", "false");
            }


        } catch (Exception ex) {
            Log.e("exceptionasdf", ex.toString());
        }

    }

    public static class CountingFileRequestBody extends RequestBody {

        private static final int SEGMENT_SIZE = 2048; // okio.Segment.SIZE

        private final File file;
        private final ProgressListener listener;
        private final String contentType;

        public CountingFileRequestBody(File file, String contentType, ProgressListener listener) {
            this.file = file;
            this.contentType = contentType;
            this.listener = listener;
        }

        @Override
        public long contentLength() {
            return file.length();
        }

        @Override
        public MediaType contentType() {
            return MediaType.parse(contentType);
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Source source = null;
            try {
                source = Okio.source(file);
                long total = 0;
                long read;

                while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                    total += read;
                    sink.flush();
                    this.listener.transferred(total);

                }
            } finally {
                Util.closeQuietly(source);
            }
        }

        public interface ProgressListener {
            void transferred(long num);
        }

    }


}
