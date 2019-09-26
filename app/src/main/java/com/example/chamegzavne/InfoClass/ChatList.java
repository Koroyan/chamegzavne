package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatList {
    private String chatID;
    private String chatListName;
    private String chatListMessage;
    private String chatListPhotoUri;
    private String chatListUserPhotoUri;
    private String hasUnread="false";
    public ChatList(String chatListName
            ,String chatListMessage
            ,String chatID
            ,String chatListPhotoUri
            ,String chatListUserPhotoUri){
        this.chatListName=chatListName;
        this.chatListMessage=chatListMessage;
        this.chatID=chatID;
        this.chatListPhotoUri=chatListPhotoUri;
        this.chatListUserPhotoUri=chatListUserPhotoUri;
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

    public void setChatListPhotoUri(String chatListPhotoUri) {
        this.chatListPhotoUri = chatListPhotoUri;
    }

    public String getChatListPhotoUri() {
        return chatListPhotoUri;
    }

    public void setHasUnread(String hasUnread) {
        this.hasUnread = hasUnread;
    }

    public String getHasUnread() {
        return hasUnread;
    }

    public void setChatListUserPhotoUri(String chatListUserPhotoUri) {
        this.chatListUserPhotoUri = chatListUserPhotoUri;
    }

    public String getChatListUserPhotoUri() {
        return chatListUserPhotoUri;
    }
}
