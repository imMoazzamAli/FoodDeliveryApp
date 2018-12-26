package com.example.user.practicalwork2.Models;

public class ModelChat {

    private String userMsg;
    private String userName;
    private String userEmail;

    public ModelChat() {
    }

    public ModelChat(String userMsg, String userName, String userEmail) {
        this.userMsg = userMsg;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserMsg() {

        return userMsg;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
