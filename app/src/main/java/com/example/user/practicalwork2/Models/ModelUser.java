package com.example.user.practicalwork2.Models;

public class ModelUser {


    private String USER_ID;
    private String USER_NAME;
    private String USER_PHONE;
    private String USER_PASSWORD;
    private String USER_mEMAIL;
    private String USER_STATUS;

    public ModelUser() {
    }

    public ModelUser(String USER_ID, String USER_NAME, String USER_PHONE, String USER_PASSWORD, String USER_mEMAIL, String USER_STATUS) {
        this.USER_ID = USER_ID;
        this.USER_NAME = USER_NAME;
        this.USER_PHONE = USER_PHONE;
        this.USER_PASSWORD = USER_PASSWORD;
        this.USER_mEMAIL = USER_mEMAIL;
        this.USER_STATUS = USER_STATUS;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getUSER_PHONE() {
        return USER_PHONE;
    }

    public String getUSER_PASSWORD() {
        return USER_PASSWORD;
    }

    public String getUSER_mEMAIL() {
        return USER_mEMAIL;
    }

    public String getUSER_STATUS() {
        return USER_STATUS;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public void setUSER_PHONE(String USER_PHONE) {
        this.USER_PHONE = USER_PHONE;
    }

    public void setUSER_PASSWORD(String USER_PASSWORD) {
        this.USER_PASSWORD = USER_PASSWORD;
    }

    public void setUSER_mEMAIL(String USER_mEMAIL) {
        this.USER_mEMAIL = USER_mEMAIL;
    }

    public void setUSER_STATUS(String USER_STATUS) {
        this.USER_STATUS = USER_STATUS;
    }
}
