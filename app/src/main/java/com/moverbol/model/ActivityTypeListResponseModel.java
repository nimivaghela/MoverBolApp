package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActivityTypeListResponseModel {
    private ArrayList<ClockActivityModel> activityList;
    @SerializedName("hourly_rate_flag")
    private int hourlyRateFlag;

    public ArrayList<ClockActivityModel> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<ClockActivityModel> activityList) {
        this.activityList = activityList;
    }

    public int getHourlyRateFlag() {
        return hourlyRateFlag;
    }

    public void setHourlyRateFlag(int hourlyRateFlag) {
        this.hourlyRateFlag = hourlyRateFlag;
    }
}
