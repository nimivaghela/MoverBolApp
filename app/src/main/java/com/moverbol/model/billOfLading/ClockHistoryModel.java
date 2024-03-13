package com.moverbol.model.billOfLading;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ClockHistoryModel implements Parcelable {
    private String men;
    private String truck;
    @SerializedName("is_billable")
    private String isBillable;
    private String itemize = "1";
    @SerializedName("start_id")
    private String startId = "";
    private String rate_hour = "0";
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("start_time_app")
    private String startTimeApp = String.valueOf(System.currentTimeMillis());
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("end_time_app")
    private String endTimeApp = String.valueOf(System.currentTimeMillis());
    @SerializedName("total_clock_time")
    private String totalClockTime;
    @SerializedName("event_type")
    private String eventType = "Clock";
    @SerializedName("activity_id")
    private String activityId = "0";
    @SerializedName("activity_name")
    private String activityName;
    @SerializedName("end_id")
    private String endId = "";
    @SerializedName("total_break_time")
    private String totalBreakTime = "0";
    @SerializedName("activity_type")
    private String activityType = "1";
    @SerializedName("IsDoubleDrive")
    private String isDoubleDrive = "0";
    public static final Creator<ClockHistoryModel> CREATOR = new Creator<ClockHistoryModel>() {
        @Override
        public ClockHistoryModel createFromParcel(Parcel in) {
            return new ClockHistoryModel(in);
        }

        @Override
        public ClockHistoryModel[] newArray(int size) {
            return new ClockHistoryModel[size];
        }
    };
    private ArrayList<WorkerModel> resourceData;

    public ClockHistoryModel() {

    }

    protected ClockHistoryModel(Parcel in) {
        men = in.readString();
        truck = in.readString();
        isBillable = in.readString();
        itemize = in.readString();
        startId = in.readString();
        rate_hour = in.readString();
        startTime = in.readString();
        startTimeApp = in.readString();
        endTime = in.readString();
        endTimeApp = in.readString();
        totalClockTime = in.readString();
        eventType = in.readString();
        activityId = in.readString();
        activityName = in.readString();
        endId = in.readString();
        totalBreakTime = in.readString();
        activityType = in.readString();
        isDoubleDrive = in.readString();
        resourceData = in.createTypedArrayList(WorkerModel.CREATOR);
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public String getMen() {
        return men;
    }

    public void setMen(String men) {
        this.men = men;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public String getItemize() {
        return itemize;
    }

    public void setItemize(String itemize) {
        this.itemize = itemize;
    }

    public String getRate_hour() {
        return rate_hour;
    }

    public void setRate_hour(String rate_hour) {
        this.rate_hour = rate_hour;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeApp() {
        return startTimeApp;
    }

    public void setStartTimeApp(String startTimeApp) {
        this.startTimeApp = startTimeApp;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeApp() {
        return endTimeApp;
    }

    public void setEndTimeApp(String endTimeApp) {
        this.endTimeApp = endTimeApp;
    }

    public String getTotalClockTime() {
        return totalClockTime;
    }

    public void setTotalClockTime(String totalClockTime) {
        this.totalClockTime = totalClockTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public String displayStartDate() {
        return Util.getFormattedTimeFromMillis(Long.parseLong(startTimeApp), Constants.DD_MMM_YYYY);
    }

    public String displayEndDate() {
        return Util.getFormattedTimeFromMillis(Long.parseLong(endTimeApp), Constants.DD_MMM_YYYY);

    }

    public String displayStartTime() {
        return Util.getFormattedTimeFromMillis(Long.parseLong(startTimeApp), Constants.HH_MM_AA);
    }

    public String displayEndTime() {
        return Util.getFormattedTimeFromMillis(Long.parseLong(endTimeApp), Constants.HH_MM_AA);

    }


    public String totalHour(Context context) {
        return Util.getGeneralFormattedDecimalString((double) calculateTotalHour() / (1000 * 3600));
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

    public String totalJobTime() {
        if (endTimeApp.equals("0")) {
            return Util.getTimeFormattedStringFromMillis(0);
        } else {
            long diff = Long.parseLong(endTimeApp) - Long.parseLong(startTimeApp);
            if (!TextUtils.isEmpty(totalBreakTime) && eventType.equalsIgnoreCase(Constants.CLOCK)) {
                diff = diff - Long.parseLong(totalBreakTime);
            }

            if (isDoubleDrive.equalsIgnoreCase(Constants.TRUE)) {
                diff = diff * 2;
            }

            return Util.getTimeFormattedStringFromMillis(diff);
        }
    }


    public String getTotalBreakTime() {
        return totalBreakTime;
    }

    public String getBreakTimeInMinute() {
        try {
            return String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(totalBreakTime)));
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public void setTotalBreakTime(String totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public String displayTotalBreakTime() {
        try {
            return Util.getTimeFormattedStringFromMillis(Long.parseLong(totalBreakTime));
        } catch (Exception e) {
            e.printStackTrace();
            return Util.getTimeFormattedStringFromMillis(0);
        }

    }

    public String displayCharge(Context context, String activityFlag) {
        double charges;
        if (isBillable != null && isBillable.equalsIgnoreCase(Constants.FALSE)) {
            charges = 0;
        } else {
            charges = Double.parseDouble(rate_hour) * Double.parseDouble(totalHour(context));
        }
        return Util.getGeneralFormattedDecimalString(charges);
    }

    public long calculateDifference() {
        if (endTimeApp.equals("0")) {
            return 0;
        } else {
            return Long.parseLong(endTimeApp) - Long.parseLong(startTimeApp);
        }
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIsDoubleDrive() {
        return isDoubleDrive;
    }

    public void setIsDoubleDrive(String isDoubleDrive) {
        this.isDoubleDrive = isDoubleDrive;
    }

    public ArrayList<WorkerModel> getResourceData() {
        return resourceData;
    }

    public void setResourceData(ArrayList<WorkerModel> resourceData) {
        this.resourceData = resourceData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(men);
        parcel.writeString(truck);
        parcel.writeString(isBillable);
        parcel.writeString(itemize);
        parcel.writeString(startId);
        parcel.writeString(rate_hour);
        parcel.writeString(startTime);
        parcel.writeString(startTimeApp);
        parcel.writeString(endTime);
        parcel.writeString(endTimeApp);
        parcel.writeString(totalClockTime);
        parcel.writeString(eventType);
        parcel.writeString(activityId);
        parcel.writeString(activityName);
        parcel.writeString(endId);
        parcel.writeString(totalBreakTime);
        parcel.writeString(activityType);
        parcel.writeString(isDoubleDrive);
        parcel.writeTypedList(resourceData);
    }

    public String displayCrewName() {
        ArrayList<String> name = new ArrayList<>(0);
        if (resourceData != null) {
            for (WorkerModel workerModel : resourceData) {
                name.add(workerModel.getName());
            }
        }
        return TextUtils.join("<br>", name);
    }
}
