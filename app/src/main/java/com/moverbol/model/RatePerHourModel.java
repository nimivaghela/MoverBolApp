package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

public class RatePerHourModel {

    private String hours;
    @SerializedName("min_hours")
    private String minHours;
    private String rate;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinHours() {
        return minHours;
    }

    public void setMinHours(String minHours) {
        this.minHours = minHours;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
