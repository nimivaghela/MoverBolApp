package com.moverbol.model.billOfLading.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OpportunityDetails implements Parcelable {

    @SerializedName("creditcard_conv_type")
    private String creditcardConvType;
    @SerializedName("bolFinalChargeId")
    private String bolFinalChargeId;
    @SerializedName("totalcharges")
    private String totalcharges;
    @SerializedName("creditcard_conv_val")
    private String creditcardConvVal;
    @SerializedName("reviewDiscountAmount")
    private String reviewDiscountAmount;
    @SerializedName("deposit")
    private String deposit;
    @SerializedName("multidate_flag")
    private int multidateFlag;
    @SerializedName("first_job_flag")
    private int firstJobFlag;
    @SerializedName("tips_flag")
    private int tipsFlag;
    @SerializedName("payments_movetype_flag")
    private int paymentsMoveTypeFlag;
    public static final Creator<OpportunityDetails> CREATOR = new Creator<OpportunityDetails>() {
        @Override
        public OpportunityDetails createFromParcel(Parcel in) {
            return new OpportunityDetails(in);
        }

        @Override
        public OpportunityDetails[] newArray(int size) {
            return new OpportunityDetails[size];
        }
    };
    @SerializedName("last_multidate_job")
    private int lastMultiDateJob;
    @SerializedName("amount_due")
    private String amountDue;
    @SerializedName("payment_carry_forward")
    private int paymentCarryForward;

    public OpportunityDetails() {
    }

    protected OpportunityDetails(Parcel in) {
        creditcardConvType = in.readString();
        bolFinalChargeId = in.readString();
        totalcharges = in.readString();
        creditcardConvVal = in.readString();
        reviewDiscountAmount = in.readString();
        deposit = in.readString();
        multidateFlag = in.readInt();
        firstJobFlag = in.readInt();
        tipsFlag = in.readInt();
        paymentsMoveTypeFlag = in.readInt();
        lastMultiDateJob = in.readInt();
        amountDue = in.readString();
        paymentCarryForward = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creditcardConvType);
        dest.writeString(bolFinalChargeId);
        dest.writeString(totalcharges);
        dest.writeString(creditcardConvVal);
        dest.writeString(reviewDiscountAmount);
        dest.writeString(deposit);
        dest.writeInt(multidateFlag);
        dest.writeInt(firstJobFlag);
        dest.writeInt(tipsFlag);
        dest.writeInt(paymentsMoveTypeFlag);
        dest.writeInt(lastMultiDateJob);
        dest.writeString(amountDue);
        dest.writeInt(paymentCarryForward);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getFirstJobFlag() {
        return firstJobFlag;
    }

    public void setFirstJobFlag(int firstJobFlag) {
        this.firstJobFlag = firstJobFlag;
    }

    public String getCreditcardConvType() {
        return creditcardConvType;
    }

    public void setCreditcardConvType(String creditcardConvType) {
        this.creditcardConvType = creditcardConvType;
    }

    public String getBolFinalChargeId() {
        return bolFinalChargeId;
    }

    public void setBolFinalChargeId(String bolFinalChargeId) {
        this.bolFinalChargeId = bolFinalChargeId;
    }

    public String getTotalcharges() {
        return totalcharges;
    }

    public void setTotalcharges(String totalcharges) {
        this.totalcharges = totalcharges;
    }

    public String getCreditcardConvVal() {
        return creditcardConvVal;
    }

    public void setCreditcardConvVal(String creditcardConvVal) {
        this.creditcardConvVal = creditcardConvVal;
    }

    public String getReviewDiscountAmount() {
        return reviewDiscountAmount;
    }

    public void setReviewDiscountAmount(String reviewDiscountAmount) {
        this.reviewDiscountAmount = reviewDiscountAmount;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public int getMultidateFlag() {
        return multidateFlag;
    }

    public void setMultidateFlag(int multidateFlag) {
        this.multidateFlag = multidateFlag;
    }

    public int getTipsFlag() {
        return tipsFlag;
    }

    public void setTipsFlag(int tipsFlag) {
        this.tipsFlag = tipsFlag;
    }


    public int getPaymentsMoveTypeFlag() {
        return paymentsMoveTypeFlag;
    }

    public void setPaymentsMoveTypeFlag(int paymentsMoveTypeFlag) {
        this.paymentsMoveTypeFlag = paymentsMoveTypeFlag;
    }

    public int getLastMultiDateJob() {

        return lastMultiDateJob;
    }

    public void setLastMultiDateJob(int lastMultiDateJob) {
        this.lastMultiDateJob = lastMultiDateJob;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public int getPaymentCarryForward() {
        return paymentCarryForward;
    }

    public void setPaymentCarryForward(int paymentCarryForward) {
        this.paymentCarryForward = paymentCarryForward;
    }
}