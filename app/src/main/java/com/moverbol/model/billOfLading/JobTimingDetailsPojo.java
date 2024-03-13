package com.moverbol.model.billOfLading;

/**
 * Created by AkashM on 28/4/18.
 */
public class JobTimingDetailsPojo {

    private String jobActivity;

    private String clockInTime;

    private String clockOutTime;

    private String breakTime;

    private String totalJobHours;


    public JobTimingDetailsPojo() {
    }

    public JobTimingDetailsPojo(String jobActivity, String clockInTime, String clockOutTime, String breakTime, String totalJobHours) {
        this.jobActivity = jobActivity;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.breakTime = breakTime;
        this.totalJobHours = totalJobHours;
    }

    public String getJobActivity() {
        return jobActivity;
    }

    public void setJobActivity(String jobActivity) {
        this.jobActivity = jobActivity;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getTotalJobHours() {
        return totalJobHours;
    }

    public void setTotalJobHours(String totalJobHours) {
        this.totalJobHours = totalJobHours;
    }
}
