package com.horofbd.nei;

import android.app.Activity;
import android.app.job.JobInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Functions {
    static Context context;
    static SharedPreferences preferences;
    public static boolean isActive = false;

    public Functions(Context context) {
        Functions.context = context;
        preferences = context.getSharedPreferences("MessagePreference", MODE_PRIVATE);
    }

    public static void setSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();// commit is important here.
    }

    public static String getSharedPreference(String key, String defaultvalue) {
        return preferences.getString(key, defaultvalue);
    }

    public static void ClearSharedPreference() {
        preferences.edit().clear().apply();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
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


    public static void Request(ServerResponse serverResponse, String requestType, String Link, JSONObject jsonObject, int requestcode) {
        OkHttpClient client = getClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody body = null;

        if (jsonObject != null) {
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
        }
        Request request = new Request.Builder()
                .url(Link)
                .method(requestType, body)
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

        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));
        if (expires - (expires - 10 * 60 * 1000) < 10 * 60 * 1000) {
            SetPrivateToken();
            Log.e("expiry", "mela khon baki");
            client1 = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + Functions.getSharedPreference("private_token", ""))
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        } else if (System.currentTimeMillis() > expires) {
            Log.e("expiry", "deri hoye geche");
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity) context).finish();
        } else {

            Log.e("expiry", "jothesto somoy ache");
            client1 = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + Functions.getSharedPreference("private_token", ""))
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
            String confirmpassword,
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
        if (!reference.equals("")) {
            builder.addFormDataPart("ref", reference);
        }
        builder.setType(MultipartBody.FORM);
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(Important.getProfile_register())
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


    public static void LoginRegisteredUser(ServerResponse serverResponse, String phone, String password, int requestcode) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone_no", phone)
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/login")
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

}
