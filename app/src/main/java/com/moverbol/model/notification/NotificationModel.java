package com.moverbol.model.notification;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("r_id")
    private String rId;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("read_status")
    private String readStatus;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("opportunityId")
    private String opportunityId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("bol_status")
    private String bolStatus;

    @SerializedName("notification_type")
    private int notificationType;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("sound")
    private String soundIndex;

    @SerializedName("vibrate")
    private String vibrationIndex;


    public String getBolStatus() {
        return bolStatus;
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSoundIndex() {
        return soundIndex;
    }

    public void setSoundIndex(String soundIndex) {
        this.soundIndex = soundIndex;
    }

    public String getVibrationIndex() {
        return vibrationIndex;
    }

    public void setVibrationIndex(String vibrationIndex) {
        this.vibrationIndex = vibrationIndex;
    }
}
