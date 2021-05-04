package com.horofbd.nei;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ServerRequest {
    private static Context context;

    public static void getNewAuthToken() {

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


                    Functions.setSharedPreference("private_token", token);
                    Functions.setSharedPreference("expires_in", String.valueOf(expiry));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }

    public static void ServerRequest(Context getcontext) {
        context = getcontext;
    }


    private static OkHttpClient getClient() {


        OkHttpClient client1 = null;

        long expires = Long.parseLong(Functions.getSharedPreference("expires_in", "0"));
        Log.e("expiry", String.valueOf(expires));


        if (expires - (expires - 10 * 60 * 1000) < 10 * 60 * 1000) {
            getNewAuthToken();


            Log.e("expiry", "mela khon baki");
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

    public static void getUserPhoneVerificationCode(ServerResponse serverResponse, int requestcode) {
        OkHttpClient client = getClient();

        if (client != null) {
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
                        serverResponse.onResponse(response.body().string(), response.code(), requestcode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void VerifyPhoneNumber(ServerResponse serverResponse, String verificationCode, int requestcode) {
        OkHttpClient client = getClient();

        if (client != null) {


            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("code", verificationCode)
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
                        serverResponse.onResponse(response.body().string(), response.code(), requestcode);


                    } catch (Exception e) {
                        Log.e("error message", e.getMessage());
                    }
                }
            });
        }
    }

    public static void SetUserAppPassword(ServerResponse serverresponse, String password, String confirmpassword, int requestcode) {
        OkHttpClient client = getClient();

        if (client != null) {


            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("password", password)
                    .addFormDataPart("password_confirmation", confirmpassword)
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
                        serverresponse.onResponse(response.body().string(), response.code(), requestcode);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void RegisterNewUser(ServerResponse serverResponse, String phone, String name, String password, String confirmpassword, String reference, int requestcode) {

        Log.e("request", "something" + name + " " + phone + " " + password + " " + reference + " " + confirmpassword + " ");

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

    public static void GetAllMessage(ServerResponse serverResponse, String page, int requestcode) {

        OkHttpClient client = getClient();

        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("page", page)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/message/get-friends")
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

    public static void getPremiumStatus(ServerResponse serverResponse, int requestcode) {
        OkHttpClient client = getClient();

        if (client != null) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/premium")
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
                        serverResponse.onResponse(response.body().string(), response.code(), requestcode);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void PageLogin(ServerResponse serverResponse, String password, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/auth/page-login")
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

    public static void CheckPhoneNumber(ServerResponse serverResponse, String phone, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("number", phone)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/check-phone")
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

    public static void AddorSendNewMessage(ServerResponse serverResponse, String message, String to, String userpagenumber, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("to", to)
                    .addFormDataPart("message", message)
                    .addFormDataPart("user_page_no", userpagenumber)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/message/send-new-message")
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

    public static void RetriveFriendRequest(ServerResponse serverResponse, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {


            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/retrieve-request")
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
                        serverResponse.onResponse(response.body().string(), response.code(), requestcode);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void ResponseToFriendRequest(ServerResponse serverResponse, String friendid, String status,String page, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {

            Log.e("serverrequest",friendid+" "+status+" "+page);

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("friend_id", friendid)
                    .addFormDataPart("status", status)
                    .addFormDataPart("page",page)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/response-request")
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

    public static void GetFriendsList(ServerResponse serverResponse, String page, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("page", page)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/get-friends")
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

    public static void GetMessageFromFriend(ServerResponse serverResponse, String Friendid, String pageid, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("friend_id", Friendid)
                    .addFormDataPart("page", pageid)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/message/old-messages")
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

    public static void UpdatePassword(ServerResponse serverResponse, String oldpass, String newPassword, String confirmpassword, int requestcode) {
        OkHttpClient client = getClient();

        if (client != null) {

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("old_password", oldpass)
                    .addFormDataPart("password", newPassword)
                    .addFormDataPart("password_confirmation", confirmpassword)
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/auth/update-password")
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

    public static void LogOut(ServerResponse serverResponse, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/auth/logout")
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


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void UploadPhoto(ServerResponse serverResponse, String filepath, File file, String to, String message, int requestcode) {
        OkHttpClient client = getClient();
        if (client != null) {

            Log.e("path", filepath);
            Log.e("mimetype", getMimeType(filepath));
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse(getMimeType(filepath)),
                                    file))
                    .addFormDataPart("to", to)
                    .addFormDataPart("message", message)
                    .addFormDataPart("type", getMimeType(filepath))
                    .build();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8000/api/premium/upload")
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

//    public static void GetUploadedPhoto(ServerResponse serverResponse){
//        OkHttpClient client = getClient()
//        Request request = new Request.Builder()
//                .url("http://localhost:8000/api/message/show-upload?id=12&challenge=17dc1c88810c13857978478b6d907f14")
//                .method("GET", null)
//                .build();
//        Response response = client.newCall(request).execute();
//    }


    public static void GetUserProfile(ServerResponse serverResponse, int requestcode) {
        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/auth/user-profile")
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
                    serverResponse.onResponse(response.body().string(), response.code(), requestcode);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void Recharge(ServerResponse serverResponse, String amount, int requestcode) {
        OkHttpClient client = getClient();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("amount", amount)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/recharge")
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

    public static void BuyPremiumfromRecharge(ServerResponse serverResponse, String days, String type, int requestcode) {
        OkHttpClient client = getClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("days", days)
                .addFormDataPart("method", type)
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api/buy-premium")
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
