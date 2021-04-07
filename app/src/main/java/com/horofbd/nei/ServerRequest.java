package com.horofbd.nei;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ServerRequest {
    private static Context context;

    public static void getNewAuthToken(){

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
                .url("http://10.0.2.2:8000/api/auth/refresh")
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Functions.setSharedPreference("private_token", token);
                Functions.setSharedPreference("expires_in", String.valueOf(expiry));

            }
        });
    }

    public static void ServerRequest(Context getcontext) {
        context = getcontext;
    }

    private static MediaType mediaType = MediaType.parse("text/plain");

    private static OkHttpClient getClient() {


         OkHttpClient client1 = null;

        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));
        Log.e("expiry",String.valueOf(expires));


        if (expires - (expires - 10 * 60 * 1000) < 10 * 60 * 1000) {
            getNewAuthToken();


            Log.e("expiry","mela khon baki");
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

        }else if(System.currentTimeMillis()>expires){
            Log.e("expiry","deri hoye geche");
           context.startActivity(new Intent(context,LoginActivity.class));
            ((Activity)context).finish();
        } else {

            Log.e("expiry","jothesto somoy ache");
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

    public static void getUserPhoneVerificationCode(ServerResponse serverResponse){
        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/phone-verification-code")
                .method("GET", null)
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
                    serverResponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void VerifyPhoneNumber(ServerResponse serverResponse,String verificationCode){
        OkHttpClient client = getClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("code",verificationCode)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/verify-phone")
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
                    serverResponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void SetUserAppPassword(ServerResponse serverresponse,String password,String confirmpassword){
        OkHttpClient client = getClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("password",password)
                .addFormDataPart("password_confirmation",confirmpassword)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/set-page-password")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    serverresponse.onFailure(e.getMessage());
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    serverresponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void RegisterNewUser(ServerResponse serverResponse, String phone, String name, String password, String confirmpassword, String reference) {

        Log.e("request","something"+name+phone+password+confirmpassword+reference);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("phone_no", phone)
                .addFormDataPart("password", password)
                .addFormDataPart("password_confirmation", confirmpassword)
                .addFormDataPart("ref", reference)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/register")
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
                    serverResponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void LoginRegisteredUser(ServerResponse serverResponse, String phone, String password) {

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
                    serverResponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void GetAllMessage(ServerResponse serverResponse) {
        OkHttpClient client = getClient();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/message?page=1")
                .method("GET", null)
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
                    serverResponse.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }



}
