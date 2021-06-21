package com.horofbd.MeCloak;


public class ChatModel {
    String body,boundage,timestamp,encryptionpass,expiry;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBoundage() {
        return boundage;
    }

    public void setBoundage(String boundage) {
        this.boundage = boundage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEncryptionpass() {
        return encryptionpass;
    }

    public void setEncryptionpass(String encryptionpass) {
        this.encryptionpass = encryptionpass;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
