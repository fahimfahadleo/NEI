package com.horofbd.MeCloak;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;


public class NotificationFragment extends Fragment implements ServerResponse {
    Context context;

    public NotificationFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {

    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }
}