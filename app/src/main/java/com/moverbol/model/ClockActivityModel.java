package com.moverbol.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.Objects;

public class ClockActivityModel implements Parcelable {

    @SerializedName("Activity_id")
    private String id = "";
    @SerializedName("Activity_name")
    private String activityName = "";
    @SerializedName("Crew")
    private String crew = "0";
    @SerializedName("Equipment")
    private String equipment = "0";
    private String isBilable = "1";
    private String isItemize = "0";
    @SerializedName("rate_hour")
    private String rateHour = "0";
    @SerializedName("IsDoubleDrive")
    private String isDoubleDrive = "0";
    private long totalJobTime = 0;
    private long totalBreakTime = 0;
    public static final Creator<ClockActivityModel> CREATOR = new Creator<ClockActivityModel>() {
        @Override
        public ClockActivityModel createFromParcel(Parcel in) {
            return new ClockActivityModel(in);
        }

        @Override
        public ClockActivityModel[] newArray(int size) {
            return new ClockActivityModel[size];
        }
    };
    private ArrayList<WorkerModel> resourceData;

    protected ClockActivityModel(Parcel in) {
        id = in.readString();
        activityName = in.readString();
        crew = in.readString();
        equipment = in.readString();
        isBilable = in.readString();
        isItemize = in.readString();
        rateHour = in.readString();
        isDoubleDrive = in.readString();
        totalJobTime = in.readLong();
        totalBreakTime = in.readLong();
        resourceData = in.createTypedArrayList(WorkerModel.CREATOR);
    }

    public ClockActivityModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(activityName);
        dest.writeString(crew);
        dest.writeString(equipment);
        dest.writeString(isBilable);
        dest.writeString(isItemize);
        dest.writeString(rateHour);
        dest.writeString(isDoubleDrive);
        dest.writeLong(totalJobTime);
        dest.writeLong(totalBreakTime);
        dest.writeTypedList(resourceData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<WorkerModel> getResourceData() {
        return resourceData;
    }

    public void setResourceData(ArrayList<WorkerModel> resourceData) {
        this.resourceData = resourceData;
    }


    public long getTotalJobTime() {
        return totalJobTime;
    }

    public void setTotalJobTime(long totalJobTime) {
        this.totalJobTime = totalJobTime;
    }

    public long getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(long totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getIsBilable() {
        return isBilable;
    }

    public void setIsBilable(String isBilable) {
        this.isBilable = isBilable;
    }

    public String getIsItemize() {
        return isItemize;
    }

    public void setIsItemize(String isItemize) {
        this.isItemize = isItemize;
    }

    public String getRateHour() {
        return rateHour;
    }

    public String getIsDoubleDrive() {
        return isDoubleDrive;
    }

    public void setIsDoubleDrive(String isDoubleDrive) {
        this.isDoubleDrive = isDoubleDrive;
    }

    public void setRateHour(String rateHour) {
        this.rateHour = rateHour;
    }



    public String diaplayTotalJobTime() {
        return Util.getTimeFormattedStringFromMillis(totalJobTime);
    }

    public String diaplayTotalBreakTime() {
        return Util.getTimeFormattedStringFromMillis(totalBreakTime);
    }

    @NonNull
    @Override
    public String toString() {
        return activityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClockActivityModel)) return false;
        ClockActivityModel that = (ClockActivityModel) o;
        return getId().equals(that.getId()) &&
                getActivityName().equals(that.getActivityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getActivityName());
    }
}
