package com.horofbd.MeCloak;

import org.json.JSONException;

import okhttp3.Response;

public interface ImageResponse {
    void onImageResponse(Response response, int code,int requestcode) throws JSONException;
    void onImageFailure(String failresponse) throws JSONException;
}
