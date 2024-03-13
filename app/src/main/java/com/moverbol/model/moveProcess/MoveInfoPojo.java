package com.moverbol.model.moveProcess;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 20/3/18.
 */

public class MoveInfoPojo {

    @SerializedName("r_schedule_job_id")
    private String id;

    @SerializedName("r_job_id")
    private String jobSubId;

    @SerializedName("r_start_date")
    private String startDate;

    @SerializedName("job_status")
    private String jobStatus;

    @SerializedName("job_clock_id")
    private String clockId;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("opp_opportunity_name")
    private String oppertunityName;

    @SerializedName("r_phone1")
    private String phoneNumber;

    @SerializedName("activity_flag")
    private String activityFlag;

    @SerializedName("clock_flag")
    private String clockRequired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobSubId() {
        return jobSubId;
    }

    public void setJobSubId(String jobSubId) {
        this.jobSubId = jobSubId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getClockId() {
        return clockId;
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOppertunityName() {
        return oppertunityName;
    }

    public void setOppertunityName(String oppertunityName) {
        this.oppertunityName = oppertunityName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
//        return "999";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFormattedStartDate(){
        return Util.getFormattedDate(this.startDate, Constants.INPUT_DATE_FORMAT_JOBS, Constants.OUTPUT_DATE_FORMAT_COMMENTS);
    }

    public String getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }

    public boolean jobHasSingleActivity(){
        if(activityFlag==null){
            return false;
        }
        return TextUtils.equals(activityFlag, Constants.ActivityFlags.JOB_HAS_SINGLE_ACTIVITY);
//        return true;
    }

    public boolean getClockRequired() {
        return TextUtils.equals(clockRequired, Constants.BooleanFlags.TRUE_VALUE);
    }

    public void setClockRequired(boolean clockRequired) {
        if(clockRequired) {
            this.clockRequired = Constants.BooleanFlags.TRUE_VALUE;
        } else {
            this.clockRequired = Constants.BooleanFlags.FALSE_VALUE;
        }
    }
}
