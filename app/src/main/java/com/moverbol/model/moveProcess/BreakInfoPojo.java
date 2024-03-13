package com.moverbol.model.moveProcess;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 20/3/18.
 */

public class BreakInfoPojo {

    @SerializedName("c_id")
    private String id;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("job_clock_id")
    private String jobClockId;

    @SerializedName("break_status")
    private String breakStatus;

    @SerializedName("total_break_time")
    private String totalBreakTime;

    @SerializedName("break_timestamp")
    private String startBreakTime;

    @SerializedName("break_timestamp_app")
    private String timestampAppMilis;

    @SerializedName("added_date")
    private String addedDate;


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

    public String getJobClockId() {
        return jobClockId;
    }

    public void setJobClockId(String jobClockId) {
        this.jobClockId = jobClockId;
    }

    public String getBreakStatus() {
        return breakStatus;
    }

    public void setBreakStatus(String breakStatus) {
        this.breakStatus = breakStatus;
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

    public String getStartBreakTime() {
        return startBreakTime;
    }

    public void setStartBreakTime(String startBreakTime) {
        this.startBreakTime = startBreakTime;
    }

    public String getTimestampAppMilis() {
        return timestampAppMilis;
    }

    public void setTimestampAppMilis(String timestampAppMilis) {
        this.timestampAppMilis = timestampAppMilis;
    }
}
