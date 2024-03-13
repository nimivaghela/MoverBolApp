package com.moverbol.model.confirmationPage;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/3/18.
 */

public class TermsAndConditionPojo implements Parcelable {

    @SerializedName("r_cancel_reshedule")
    private String cancellationAndReschedulePolicy;

    @SerializedName("bol_cancel_reshedule")
    private String cancellationAndReschedulePolicyBOL;

    @SerializedName("bol_company_policy")
    private String companyPolicyBOL;

    @SerializedName("r_company_policy")
    private String companyPolicy;

    @SerializedName("r_cancel_reshedule_title")
    private String rescheduleTittle;

    @SerializedName("r_company_policy_title")
    private String companyPolicyTittle;
    @SerializedName("tc_show_flag1")
    private int tcShowFlag1;
    @SerializedName("tc_show_flag3")
    private int tcShowFlag3;
    @SerializedName("tc_show_flag4")
    private int tcShowFlag4;
    @SerializedName("ratings_flag")
    private int ratingsFlag;
    @SerializedName("tips_flag")
    private int tipsFlag;
    @SerializedName("bol_cancel_title")
    private String bolCancelTitle;
    @SerializedName("bol_company_title")
    private String bolCompanyTitle;

    public static final Creator<TermsAndConditionPojo> CREATOR = new Creator<TermsAndConditionPojo>() {
        @Override
        public TermsAndConditionPojo createFromParcel(Parcel source) {
            return new TermsAndConditionPojo(source);
        }

        @Override
        public TermsAndConditionPojo[] newArray(int size) {
            return new TermsAndConditionPojo[size];
        }
    };


    protected TermsAndConditionPojo(Parcel in) {
        this.cancellationAndReschedulePolicy = in.readString();
        this.cancellationAndReschedulePolicyBOL = in.readString();
        this.companyPolicyBOL = in.readString();
        this.companyPolicy = in.readString();
        this.rescheduleTittle = in.readString();
        this.companyPolicyTittle = in.readString();
        this.tcShowFlag1 = in.readInt();
        this.tcShowFlag3 = in.readInt();
        this.tcShowFlag4 = in.readInt();
        this.ratingsFlag = in.readInt();
        this.tipsFlag = in.readInt();
        this.bolCancelTitle = in.readString();
        this.bolCompanyTitle = in.readString();
        this.tcShowFlag2 = in.readInt();
    }

    public String getBolCancelTitle() {
        return bolCancelTitle;
    }

    public void setBolCancelTitle(String bolCancelTitle) {
        this.bolCancelTitle = bolCancelTitle;
    }

    public TermsAndConditionPojo() {
    }

    public String getCancellationAndReschedulePolicy() {
        return cancellationAndReschedulePolicy;
    }

    public void setCancellationAndReschedulePolicy(String cancellationAndReschedulePolicy) {
        this.cancellationAndReschedulePolicy = cancellationAndReschedulePolicy;
    }

    public String getCancellationAndReschedulePolicyBOL() {
        return cancellationAndReschedulePolicyBOL;
    }

    public void setCancellationAndReschedulePolicyBOL(String cancellationAndReschedulePolicyBOL) {
        this.cancellationAndReschedulePolicyBOL = cancellationAndReschedulePolicyBOL;
    }

    public int getTcShowFlag3() {
        return tcShowFlag3;
    }

    public String getcancellationAndReschedulePolicy() {
        return cancellationAndReschedulePolicy;
    }

    public void setcancellationAndReschedulePolicy(String cancellationAndReschedulePolicy) {
        this.cancellationAndReschedulePolicy = cancellationAndReschedulePolicy;
    }

    public String getcancellationAndReschedulePolicyBOL() {
        return cancellationAndReschedulePolicyBOL;
    }

    public void setcancellationAndReschedulePolicyBOL(String cancellationAndReschedulePolicyBOL) {
        this.cancellationAndReschedulePolicyBOL = cancellationAndReschedulePolicyBOL;
    }

    public String getCompanyPolicyBOL() {
        return companyPolicyBOL;
    }

    public void setCompanyPolicyBOL(String companyPolicyBOL) {
        this.companyPolicyBOL = companyPolicyBOL;
    }

    public String getCompanyPolicy() {
        return companyPolicy;
    }

    public void setCompanyPolicy(String companyPolicy) {
        this.companyPolicy = companyPolicy;
    }

    public String getRescheduleTittle() {
        return rescheduleTittle;
    }

    public void setRescheduleTittle(String rescheduleTittle) {
        this.rescheduleTittle = rescheduleTittle;
    }

    public String getCompanyPolicyTittle() {
        return companyPolicyTittle;
    }

    public void setCompanyPolicyTittle(String companyPolicyTittle) {
        this.companyPolicyTittle = companyPolicyTittle;
    }

    @SerializedName("tc_show_flag2")
    private int tcShowFlag2;

    public int getTcShowFlag1() {
        return tcShowFlag1;
    }

    public void setTcShowFlag1(int tcShowFlag1) {
        this.tcShowFlag1 = tcShowFlag1;
    }

    public void setTcShowFlag3(int tcShowFlag3) {
        this.tcShowFlag3 = tcShowFlag3;
    }

    public int getTcShowFlag4() {
        return tcShowFlag4;
    }

    public void setTcShowFlag4(int tcShowFlag4) {
        this.tcShowFlag4 = tcShowFlag4;
    }

    public int getRatingsFlag() {
        return ratingsFlag;
    }

    public void setRatingsFlag(int ratingsFlag) {
        this.ratingsFlag = ratingsFlag;
    }

    public int getTipsFlag() {
        return tipsFlag;
    }

    public void setTipsFlag(int tipsFlag) {
        this.tipsFlag = tipsFlag;
    }

    public String getBolCompanyTitle() {
        return bolCompanyTitle;
    }

    public void setBolCompanyTitle(String bolCompanyTitle) {
        this.bolCompanyTitle = bolCompanyTitle;
    }

    public int getTcShowFlag2() {
        return tcShowFlag2;
    }

    public void setTcShowFlag2(int tcShowFlag2) {
        this.tcShowFlag2 = tcShowFlag2;
    }

    public String getRescheduleTitleText(String companyName) {
        return "I Agree with the " + companyName + " " + getBolCancelTitle();
    }

    public String getCompanyPolicyTitleText(String companyName) {
        return "I Agree with the " + companyName + " " + getBolCompanyTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cancellationAndReschedulePolicy);
        dest.writeString(this.cancellationAndReschedulePolicyBOL);
        dest.writeString(this.companyPolicyBOL);
        dest.writeString(this.companyPolicy);
        dest.writeString(this.rescheduleTittle);
        dest.writeString(this.companyPolicyTittle);
        dest.writeInt(this.tcShowFlag1);
        dest.writeInt(this.tcShowFlag3);
        dest.writeInt(this.tcShowFlag4);
        dest.writeInt(this.ratingsFlag);
        dest.writeInt(this.tipsFlag);
        dest.writeString(this.bolCancelTitle);
        dest.writeString(this.bolCompanyTitle);
        dest.writeInt(this.tcShowFlag2);
    }
}
