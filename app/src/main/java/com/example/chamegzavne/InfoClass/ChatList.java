package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatList {
    private String chatID;
    private String chatListName;
    private String chatListMessage;
    public ChatList(String chatListName
            ,String chatListMessage
            ,String chatID){
        this.chatListName=chatListName;
        this.chatListMessage=chatListMessage;
        this.chatID=chatID;
    }
    public ChatList(){}

    public void setChatListName(String chatListName) {
        this.chatListName = chatListName;
    }

    public String getChatListName() {
        return chatListName;
    }

    public void setChatListMessage(String chatListMessage) {
        this.chatListMessage = chatListMessage;
    }

    public String getChatListMessage() {
        return chatListMessage;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChatID() {
        return chatID;
    }
}
