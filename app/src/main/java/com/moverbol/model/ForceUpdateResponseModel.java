package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

public class ForceUpdateResponseModel {

    @SerializedName("status")
    private String forceUpdateStatus; //Values can be installed and update new version.

    @SerializedName("force_update")
    private String shouldForceUpdate;

    @SerializedName("close_popup")
    private String shouldClosePopup;


    public String getForceUpdateStatus() {
        return forceUpdateStatus;
    }

    public void setForceUpdateStatus(String forceUpdateStatus) {
        this.forceUpdateStatus = forceUpdateStatus;
    }

    public boolean shouldForceUpdate() {
        return shouldForceUpdate != null && shouldForceUpdate.equals("true");
    }

    public void setShouldForceUpdate(boolean shouldForceUpdate) {
        if (shouldForceUpdate) {
            this.shouldForceUpdate = "true";
        } else {
            this.shouldForceUpdate = "false";
        }
    }

    public boolean shouldClosePopup() {
        return shouldClosePopup != null && shouldClosePopup.equals("true");
    }

    public void setShouldClosePopup(boolean shouldClosePopup) {
        if (shouldClosePopup) {
            this.shouldClosePopup = "true";
        } else {
            this.shouldClosePopup = "false";
        }
    }

}
