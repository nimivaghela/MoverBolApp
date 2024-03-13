package com.moverbol.model.moveProcess;


import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.TermsAndConditionPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 5/1/18.
 */

public class MoveProcessStatusPojo {

    @SerializedName("move")
    private MoveInfoPojo moveInfoPojo;

    @SerializedName("clock")
    private List<ClockInfoPojo> clockInfoPojoList;

    @SerializedName("last_clock_activity")
    private ClockInfoPojo lastClockInfo;

    @SerializedName("last_break_activity")
    private BreakInfoPojo lastBreakInfo;

    @SerializedName("bol_sent_for_approval")
    private String bolSentForApprovalStatus;

    @SerializedName("bol_started_flag")
    private String bolStarted;

    @SerializedName("clock_activity_details")
    private List<ClockInfoPojo> allClockList;

    @SerializedName("break_activity_details")
    private List<BreakInfoPojo> allBreakList;

    @SerializedName("bol_allow_flag")
    private String bolAllowed;

    @SerializedName("default_min_hours")
    private String minHours;

    @SerializedName("increment_min_value")
    private long incrementMinValue;

    @SerializedName("actual_hours_app")
    private long actualHourApp;
    @SerializedName("actual_drive_timeapp")
    private long actualDriveTimeApp;
    @SerializedName("bol_movetype_flag")
    private int bolMoveTypeFlag;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("terms_conditions")
    private TermsAndConditionPojo termsAndConditionPojo;
    @SerializedName("r_id")
    private String rId;
    @SerializedName("review_movetype_flag")
    private int reviewMoveTypeFlag;
    @SerializedName("account_type_flag")
    private final int accountTypeFlag = 2;
    @SerializedName("account_name")
    private String accountName;


    public String getBolAllowed() {
        return bolAllowed;
    }

    public long getActualHourApp() {
        return actualHourApp;
    }

    public void setActualHourApp(long actualHourApp) {
        this.actualHourApp = actualHourApp;
    }

    public long getActualDriveTimeApp() {
        return actualDriveTimeApp;
    }

    public void setActualDriveTimeApp(long actualDriveTimeApp) {
        this.actualDriveTimeApp = actualDriveTimeApp;
    }

    public String getMinHours() {
        return minHours;
    }

    public void setMinHours(String minHours) {
        this.minHours = minHours;
    }

    public long getIncrementMinValue() {
        return incrementMinValue;
    }

    public void setIncrementMinValue(long incrementMinValue) {
        this.incrementMinValue = incrementMinValue;
    }

    public MoveInfoPojo getMoveInfoPojo() {
        return moveInfoPojo;
    }

    public void setMoveInfoPojo(MoveInfoPojo moveInfoPojo) {
        this.moveInfoPojo = moveInfoPojo;
    }

    public List<ClockInfoPojo> getClockInfoPojoList() {
        if (clockInfoPojoList == null) {
            clockInfoPojoList = new ArrayList<>();
        }
        return clockInfoPojoList;
    }

    public void setClockInfoPojoList(List<ClockInfoPojo> clockInfoPojoList) {
        this.clockInfoPojoList = clockInfoPojoList;
    }

    public ClockInfoPojo getLastClockInfo() {
        if (lastClockInfo == null) {
            lastClockInfo = new ClockInfoPojo();
        }
        return lastClockInfo;
    }

    public void setLastClockInfo(ClockInfoPojo lastClockInfo) {
        this.lastClockInfo = lastClockInfo;
    }

    public BreakInfoPojo getLastBreakInfo() {
        if (lastBreakInfo == null) {
            lastBreakInfo = new BreakInfoPojo();
        }
        return lastBreakInfo;
    }

    public void setLastBreakInfo(BreakInfoPojo lastBreakInfo) {
        this.lastBreakInfo = lastBreakInfo;
    }

    public String getBolSentForApprovalStatus() {
        if (bolSentForApprovalStatus == null) {
            bolSentForApprovalStatus = "";
        }
        return bolSentForApprovalStatus;
    }

    public void setBolSentForApprovalStatus(String bolSentForApprovalStatus) {
        this.bolSentForApprovalStatus = bolSentForApprovalStatus;
    }

    public boolean getBolStarted() {
        if (bolStarted == null) {
            return false;
        }
        return bolStarted.equals("1");
    }

    public void setBolStarted(boolean bolStarted) {
        if (bolStarted) {
            this.bolStarted = "1";
        } else {
            this.bolStarted = "0";
        }
    }

    public List<ClockInfoPojo> getAllClockList() {
        return allClockList;
    }

    public void setAllClockList(List<ClockInfoPojo> allClockList) {
        this.allClockList = allClockList;
    }

    public List<BreakInfoPojo> getAllBreakList() {
        return allBreakList;
    }

    public void setAllBreakList(List<BreakInfoPojo> allBreakList) {
        this.allBreakList = allBreakList;
    }

    public void setBolStarted(String bolStarted) {
        this.bolStarted = bolStarted;
    }

    public boolean isBolAllowed() {
        if (bolAllowed == null) {
            return true;
        }
        return bolAllowed.equals("1");
    }

    public void setBolAllowed(String bolAllowed) {
        this.bolAllowed = bolAllowed;
    }

    public int getBolMoveTypeFlag() {
        return bolMoveTypeFlag;
    }

    public void setBolMoveTypeFlag(int bolMoveTypeFlag) {
        this.bolMoveTypeFlag = bolMoveTypeFlag;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public TermsAndConditionPojo getTermsAndConditionPojo() {
        return termsAndConditionPojo;
    }

    public void setTermsAndConditionPojo(TermsAndConditionPojo termsAndConditionPojo) {
        this.termsAndConditionPojo = termsAndConditionPojo;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public int getReviewMoveTypeFlag() {
        return reviewMoveTypeFlag;
    }

    public void setReviewMoveTypeFlag(int reviewMoveTypeFlag) {
        this.reviewMoveTypeFlag = reviewMoveTypeFlag;
    }

    public String displayCustomerName() {
        if (accountTypeFlag == 1) {
            return accountName + "\n" + moveInfoPojo.getOppertunityName();
        } else {
            return moveInfoPojo.getOppertunityName();
        }
    }
}
