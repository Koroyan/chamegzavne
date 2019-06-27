package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat {
    private String userID;
    private String userName;
    private String userMessage;
    public Chat(){
    }
    public Chat(String userName,String userMessage,String userID){
        this.userName=userName;
        this.userMessage=userMessage;
        this.userID=userID;
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
}
