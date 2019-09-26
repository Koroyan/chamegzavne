package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String userName;
    private String userPhone;
    private String userMail;
    private String userInfo;
    private String userID;
    private String userWeb;
    private String userProfilePhoto;
    public User(){}
    public User(String userName,String userPhone,String userMail,String userInfo,String userID,String userWeb,String userProfilePhoto){
        this.userName=userName;
        this.userPhone=userPhone;
        this.userMail=userMail;
        this.userInfo=userInfo;
        this.userID=userID;
        this.userWeb=userWeb;
        this.userProfilePhoto=userProfilePhoto;
    }
    public void setUsers(User user){
        this.userName=user.getUserName();
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserWeb(String userWeb) {
        this.userWeb = userWeb;
    }

    public String getUserWeb() {
        return userWeb;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }
}
