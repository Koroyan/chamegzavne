package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat {
    private String userID;
    private String userName;
    private String userMessage;
    private String userPhotoUri;
    private String sendTime;
    public Chat(){
    }
    public Chat(String userName,String userMessage,String userID,String userPhotoUri,String sendTime){
        this.userName=userName;
        this.userMessage=userMessage;
        this.userID=userID;
        this.userPhotoUri=userPhotoUri;
        this.sendTime=sendTime;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserPhotoUri(String userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendTime() {
        return sendTime;
    }
}
