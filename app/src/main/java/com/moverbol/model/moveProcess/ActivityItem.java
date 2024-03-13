package com.moverbol.model.moveProcess;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

import java.util.List;

public class ActivityItem {

    @SerializedName("rate_hour")
    private String rateHour;

    @SerializedName("activity_name")
    private String activityName;

    @SerializedName("resourceId")
    private String resourceId;

    @SerializedName("workerId")
    private String workerId;

    @SerializedName("itemize")
    private int itemize;

    @SerializedName("start_id")
    private String startId;

    @SerializedName("IsDoubleDrive")
    private String isDoubleDrive;

    @SerializedName("truck")
    private String truck;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("end_id")
    private String endId;

    @SerializedName("is_billable")
    private String isBillable;

    @SerializedName("total_break_time")
    private String totalBreakTime;

    @SerializedName("start_time_app")
    private String startTimeApp = String.valueOf(System.currentTimeMillis());

    @SerializedName("total_hours")
    private long totalHours;

    @SerializedName("end_time_app")
    private String endTimeApp = String.valueOf(System.currentTimeMillis());

    @SerializedName("job_clock_id")
    private String jobClockId;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("event_type")
    private String eventType;

    @SerializedName("men")
    private String men;

    @SerializedName("activity_type")
    private String activityType;

    @SerializedName("resourceData")
    private List<WorkerModel> resourceData;

    @SerializedName("activity_id")
    private String activityId;

    @SerializedName("name")
    private String name;

    @SerializedName("workers")
    private String workers;

    private double totalWorkedHours;
    private double totalBillableHours;

    public double getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public void setTotalWorkedHours(double totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public double getTotalBillableHours() {
        return totalBillableHours;
    }

    public void setTotalBillableHours(double totalBillableHours) {
        this.totalBillableHours = totalBillableHours;
    }

    private WorkerItemType ItemType;

    public String getRateHour() {
        return rateHour;
    }

    public void setRateHour(String rateHour) {
        this.rateHour = rateHour;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public int getItemize() {
        return itemize;
    }

    public void setItemize(int itemize) {
        this.itemize = itemize;
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public String getIsDoubleDrive() {
        return isDoubleDrive;
    }

    public void setIsDoubleDrive(String isDoubleDrive) {
        this.isDoubleDrive = isDoubleDrive;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public String getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(String totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public String getStartTimeApp() {
        return startTimeApp;
    }

    public void setStartTimeApp(String startTimeApp) {
        this.startTimeApp = startTimeApp;
    }

    public long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }

    public String getEndTimeApp() {
        return endTimeApp;
    }

    public void setEndTimeApp(String endTimeApp) {
        this.endTimeApp = endTimeApp;
    }

    public String getJobClockId() {
        return jobClockId;
    }

    public void setJobClockId(String jobClockId) {
        this.jobClockId = jobClockId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMen() {
        return men;
    }

    public void setMen(String men) {
        this.men = men;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public List<WorkerModel> getResourceData() {
        return resourceData;
    }

    public void setResourceData(List<WorkerModel> resourceData) {
        this.resourceData = resourceData;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public WorkerItemType getItemType() {
        return ItemType;
    }

    public void setItemType(WorkerItemType itemType) {
        ItemType = itemType;
    }

    public String fullStartTime() {
        return Util.getFormattedTimeFromMillis(Long.parseLong(startTimeApp), Constants.DD_MM_YYYY_HH_MM_AA);
    }

    public String fullEndTime() {
        if (endTimeApp.equals("0")) {
            return "On going";
        } else {
            return Util.getFormattedTimeFromMillis(Long.parseLong(endTimeApp), Constants.DD_MM_YYYY_HH_MM_AA);
        }
    }

    public String displayTotalBreakTime() {
        try {
            return Util.getTimeFormattedStringFromMillis(Long.parseLong(totalBreakTime));
        } catch (Exception e) {
            e.printStackTrace();
            return Util.getTimeFormattedStringFromMillis(0);
        }

    }

    public String totalHour() {
        double mTotalHours = totalHours;
        if (isDoubleDrive != null && isDoubleDrive.equalsIgnoreCase(Constants.TRUE)) {
            mTotalHours = mTotalHours * 2;
        }
        return Util.getGeneralFormattedDecimalString(mTotalHours / (1000.0 * 3600.0));
    }


    public long calculateTotalHour() {
        long mTotalHours;
        if (endTimeApp.equals("0")) {
            mTotalHours = 0;
        } else {
            long diff = Long.parseLong(endTimeApp) - Long.parseLong(startTimeApp);
            if (!TextUtils.isEmpty(totalBreakTime) && eventType.equalsIgnoreCase("clock")) {
                diff = diff - Long.parseLong(totalBreakTime);
            }
            if (activityId != null && isDoubleDrive.equalsIgnoreCase(Constants.TRUE)) {
                diff = diff * 2;
            }
            mTotalHours = diff;
        }
        return mTotalHours;
    }
}