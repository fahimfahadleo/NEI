package com.horofbd.MeCloak.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendListModel {
    private String id;
    private String name;
    private String ref;
    private String phone_no;
    private String theme;
    private String created_at;
    private String updated_at;
    private JSONObject piovot;
    private String pivot_id;
    private String with;

    public String getPivot_id() {
        return pivot_id;
    }

    private void setPivot_id(String pivot_id) {
        this.pivot_id = pivot_id;
    }

    private String user_id;
    private String status;
    private String user_page_no;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public JSONObject getPiovot() {
        return piovot;
    }

    public void setPiovot(JSONObject piovot) {
        this.piovot = piovot;
        try {
            setWith(piovot.getString("with"));
            setUser_id(piovot.getString("user_id"));
            setStatus(piovot.getString("status"));
            setUser_page_no(piovot.getString("user_page_no"));
            setPivot_id(piovot.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getWith() {
        return with;
    }

    private void setWith(String with) {
        this.with = with;
    }

    public String getUser_id() {
        return user_id;
    }

    private void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getUser_page_no() {
        return user_page_no;
    }

    private void setUser_page_no(String user_page_no) {
        this.user_page_no = user_page_no;
    }
}
