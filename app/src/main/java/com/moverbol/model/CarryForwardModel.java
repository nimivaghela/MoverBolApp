package com.moverbol.model;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

public class CarryForwardModel {
    double amount;
    @SerializedName("job_sub_id")
    String jobSubId;
    @SerializedName("r_comments")
    String comments;
    @SerializedName("r_date")
    String date;
    String woid;

    public String getWoid() {
        return woid;
    }

    public void setWoid(String woid) {
        this.woid = woid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getJobSubId() {
        return jobSubId;
    }

    public void setJobSubId(String jobSubId) {
        this.jobSubId = jobSubId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String displayDate() {
        return Util.getFormattedDate(date, Constants.INPUT_DATE_FORMAT_JOBS, Constants.OUTPUT_DATE_FORMAT_JOBS);
    }
}
