package com.horofbd.MeCloak;

import android.widget.ImageView;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public interface ImageResponse {
    void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException;
    void onImageFailure(String failresponse) throws JSONException;
}
