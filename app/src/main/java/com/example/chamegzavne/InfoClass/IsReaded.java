package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class IsReaded {
    private String ID;
    private String userID;
    private String messageTime;

    public IsReaded(){}
    public IsReaded(String ID,String userID,String messageTime){
        this.ID=ID;
        this.userID=userID;
        this.messageTime=messageTime;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTime() {
        return messageTime;
    }
}
