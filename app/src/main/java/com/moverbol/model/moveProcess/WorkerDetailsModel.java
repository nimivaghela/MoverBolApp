package com.moverbol.model.moveProcess;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkerDetailsModel {

    @SerializedName("workerId")
    private String workerId;

    @SerializedName("activity")
    private List<ActivityItem> activity;

    @SerializedName("total_billableHours")
    private long totalBillableHours;

    @SerializedName("workername")
    private String workername;

    @SerializedName("total_worked_Hours")
    private double totalWorkedHours;

    @SerializedName("total_cal_billableHours")
    private double totalCalBillableHours;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public List<ActivityItem> getActivity() {
        return activity;
    }

    public void setActivity(List<ActivityItem> activity) {
        this.activity = activity;
    }

    public long getTotalBillableHours() {
        return totalBillableHours;
    }

    public void setTotalBillableHours(long totalBillableHours) {
        this.totalBillableHours = totalBillableHours;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }

    public double getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public void setTotalWorkedHours(double totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public double getTotalCalBillableHours() {
        return totalCalBillableHours;
    }

    public void setTotalCalBillableHours(double totalCalBillableHours) {
        this.totalCalBillableHours = totalCalBillableHours;
    }
}