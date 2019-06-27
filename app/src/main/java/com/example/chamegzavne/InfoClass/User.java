package com.example.chamegzavne.InfoClass;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private double geoLat;
    private double geoLng;
    private String userName;
    public User(){}
    public User(double geoLat,double geoLng,String userName){
        this.geoLng=geoLng;
        this.geoLat=geoLat;
        this.userName=userName;
    }
    public void setUsers(User user){
        this.geoLat=user.getGeoLat();
        this.geoLng=user.getGeoLng();
        this.userName=user.getUserName();
    }

    public double getGeoLat() {
        return geoLat;
    }

    public double getGeoLng() {
        return geoLng;
    }

    public String getUserName() {
        return userName;
    }
}
