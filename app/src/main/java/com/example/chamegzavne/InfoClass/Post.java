package com.example.chamegzavne.InfoClass;
import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.net.URL;

@IgnoreExtraProperties
public class Post {
    private String pName;
    private String pGel;
    private String pTitle;
    private String pUserPhotoURL;
    private String pPhotoURL;
    private String pComment;
    private String pUserID;
    private double pLatitude;
    private double pLongitude;



    public Post(String pName
            ,String pGel
            ,String pTitle
            ,String pComment
            ,String pUserPhotoURL
            ,String pPhotoURL
            ,String pUserID
            ,double pLatitude
            ,double pLongitude){
        this.pName=pName;
        this.pGel=pGel;
        this.pTitle=pTitle;
        this.pUserPhotoURL=pUserPhotoURL;
        this.pPhotoURL=pPhotoURL;
        this.pComment=pComment;
        this.pUserID=pUserID;
        this.pLatitude=pLatitude;
        this.pLongitude=pLongitude;
    }
    public  Post(){}
    public void setPost(String pName
            ,String pGel
            ,String pTitle
            ,String pComment
            ,double pLatitude
            ,double pLongitude){
        this.pName=pName;
        this.pGel=pGel;
        this.pTitle=pTitle;
        this.pComment=pComment;
        this.pLatitude=pLatitude;
        this.pLongitude=pLongitude;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpName() {
        return pName;
    }

    public void setpGel(String pGel) {
        this.pGel = pGel;
    }

    public String getpGel() {
        return pGel;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpPhotoURL(String pPhotoURL) {
        this.pPhotoURL = pPhotoURL;
    }

    public String getpPhotoURL() {
        return pPhotoURL;
    }

    public void setpComment(String pComment) {
        this.pComment = pComment;
    }

    public String getpComment() {
        return pComment;
    }

    public void setpUserID(String pUserID) {
        this.pUserID = pUserID;
    }

    public String getpUserID() {
        return pUserID;
    }

    public void setpLatitude(double pLatitude) {
        this.pLatitude = pLatitude;
    }

    public double getpLatitude() {
        return pLatitude;
    }

    public void setpLongitude(double pLongitude) {
        this.pLongitude = pLongitude;
    }

    public double getpLongitude() {
        return pLongitude;
    }

    public void setpUserPhotoURL(String pUserPhotoURL) {
        this.pUserPhotoURL = pUserPhotoURL;
    }

    public String getpUserPhotoURL() {
        return pUserPhotoURL;
    }
}
