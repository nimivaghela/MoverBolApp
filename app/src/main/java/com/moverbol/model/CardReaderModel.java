package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

public class CardReaderModel {
    @SerializedName("access_username")
    String userName;
    @SerializedName("access_password")
    String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
