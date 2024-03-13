package com.moverbol.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clockHistory")
public class ClockEntityModel {

    public int isBreak;
    @PrimaryKey(autoGenerate = true)
    private
    int id;
    private int occurrenceNum;
    private int breakOccurrenceNum = -1;
    private String jobActivity;
    private String clockActivity;
    private long timeStampMillis;
    private String jobId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOccurrenceNum() {
        return occurrenceNum;
    }

    public void setOccurrenceNum(int occurrenceNum) {
        this.occurrenceNum = occurrenceNum;
    }

    public String getJobActivity() {
        return jobActivity;
    }

    public void setJobActivity(String jobActivity) {
        this.jobActivity = jobActivity;
    }

    public String getClockActivity() {
        return clockActivity;
    }

    public void setClockActivity(String clockActivity) {
        this.clockActivity = clockActivity;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    public int getBreakOccurrenceNum() {
        return breakOccurrenceNum;
    }

    public void setBreakOccurrenceNum(int breakOccurrenceNum) {
        this.breakOccurrenceNum = breakOccurrenceNum;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean getIsBreak() {
        return isBreak==1;
    }

    public void setIsBreak(boolean isBreak) {
        if(isBreak) {
            this.isBreak = 1;
        } else {
            this.isBreak = 2;
        }
    }
}
