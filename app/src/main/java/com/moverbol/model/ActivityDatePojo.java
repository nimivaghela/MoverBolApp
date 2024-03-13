package com.moverbol.model;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 29/12/17.
 */

public class ActivityDatePojo {

    @SerializedName("r_date")
    private String date;

    @SerializedName("r_day")
    private String day;

    @SerializedName("activity_name")
    private String activityName;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return Util.getFormattedDate(this.date, Constants.INPUT_DATE_FORMAT_JOBS, Constants.OUTPUT_DATE_FORMAT_JOBS) + "\t \t" + this.activityName;
    }
}
