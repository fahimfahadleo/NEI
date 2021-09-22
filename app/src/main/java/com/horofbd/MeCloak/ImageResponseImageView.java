package com.horofbd.MeCloak;

import android.widget.ImageView;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public interface ImageResponseImageView {
    void onImageViewImageResponse(Response response, int code, int requestcode, ImageView imageView) throws JSONException;
    void onImageViewImageFailure(String failresponse) throws JSONException;
}
