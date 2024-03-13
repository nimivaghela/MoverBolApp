package com.moverbol.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 06-11-2017.
 */

public class SignInData {

    @SerializedName("c_hr_emp_designation")
    private String userDesignation;

    @SerializedName("c_hr_emp_email_address")
    private String userEmailId;

    @SerializedName("c_id")
    private String userId;

    @SerializedName("c_hr_emp_name")
    private String userName;

    @SerializedName("password_flag")
    private String shouldShowSetPassword;

    @SerializedName("currency")
    private String currencySymbol;

    @SerializedName("flag")
    private String userDesignationType;

    @SerializedName("profile_img")
    private String profileImageUrl;

    public String getShouldShowSetPassword() {
        return shouldShowSetPassword;
    }

    public void setShouldShowSetPassword(String shouldShowSetPassword) {
        this.shouldShowSetPassword = shouldShowSetPassword;
    }

    public boolean isShowSetPassword() {
        if (shouldShowSetPassword != null) {
            return !shouldShowSetPassword.equals("1");
        }
        return false;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserDesignationType() {
        return userDesignationType;
    }

    public void setUserDesignationType(String userDesignationType) {
        this.userDesignationType = userDesignationType;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "ClassPojo [userDesignation = " + userDesignation + ", userEmailId = " + userEmailId + ", userId = " + userId + ", userName = " + userName + "]";
    }
}
