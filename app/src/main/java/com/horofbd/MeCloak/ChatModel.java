package com.horofbd.MeCloak;


public class ChatModel {
    String body;
    String boundage;
    String timestamp;
    String hash1;
    String hash2;
    String expiry;
    String publicencription;

    public String getPublicencription() {
        return publicencription;
    }

    public void setPublicencription(String publicencription) {
        this.publicencription = publicencription;
    }

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

    public String getHash1() {
        return hash1;
    }

    public void setHash1(String hash1) {
        this.hash1 = hash1;
    }

    public String getHash2() {
        return hash2;
    }

    public void setHash2(String hash2) {
        this.hash2 = hash2;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
