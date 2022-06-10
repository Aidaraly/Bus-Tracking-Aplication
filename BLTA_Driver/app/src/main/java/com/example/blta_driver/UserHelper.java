package com.example.blta_driver;

public class UserHelper {
    public  String userName, busNum, userEmail, latitude, longitude;
    public UserHelper(){

    }
    public UserHelper(String userName, String busNum, String userEmail, String latitude, String longitude){
        this.userName = userName;
        this.busNum = busNum;
        this.userEmail = userEmail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
