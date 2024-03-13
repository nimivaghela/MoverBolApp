package com.moverbol.model.moveProcess;

import com.google.gson.annotations.SerializedName;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 20/3/18.
 */

public class ClockInfoPojo {

    @SerializedName("c_id")
    private String id;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("activity")
    private String activity;

    @SerializedName("status")
    private String status;

    @SerializedName("c_timestamp")
    private String timestampMilis;

    @SerializedName("c_timestamp_app")
    private String timestampAppMilis;

    @SerializedName("total_break_time")
    private String totalBreakTime;

    @SerializedName("added_date")
    private String addedDate;

    @SerializedName("c_out_timestamp")
    private String clockOutTimeMillis;

    @SerializedName("c_in_timestamp")
    private String clockInTimeMillis;

    @SerializedName("activity_name")
    private String activityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestampMilis() {
        return Util.getLongFromString(timestampMilis);
//        return Util.getMillisFromSecondsString(timestampMilis);
    }

    public void setTimestampMilis(String timestampMilis) {
        this.timestampMilis = timestampMilis;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(String totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public String getClockOutTimeMillis() {
        return clockOutTimeMillis;
    }

    public void setClockOutTimeMillis(String clockOutTimeMillis) {
        this.clockOutTimeMillis = clockOutTimeMillis;
    }

    public String getClockInTimeMillis() {
        return clockInTimeMillis;
    }

    public void setClockInTimeMillis(String clockInTimeMillis) {
        this.clockInTimeMillis = clockInTimeMillis;
    }

    public String getTimestampAppMilis() {
        return timestampAppMilis;
    }

    public void setTimestampAppMilis(String timestampAppMilis) {
        this.timestampAppMilis = timestampAppMilis;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
