package com.horofbd.nei;

import org.json.JSONException;

public interface ServerResponse {
    void onResponse(String response) throws JSONException;
    void onFailure(String failresponse) throws JSONException;
}
