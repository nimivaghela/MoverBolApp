package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 18/4/18.
 */
public class BolFinalChargesPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("actual_hours")
    private String actualHours;

    @SerializedName("actual_travel_time")
    private String actualTravelTime;

    @SerializedName("coupon_id")
    private String couponId;

    @SerializedName("coupon_code")
    private String couponCode;

    @SerializedName("coupon_type")
    private String couponType;

    @SerializedName("discount_value")
    private String discountValue;

    @SerializedName("ratings")
    private String ratings;

    @SerializedName("review_message")
    private String reviewMessage;

    @SerializedName("review_discount_amount")
    private String reviewDiscountAmount;

    @SerializedName("tips_discount_type")
    private String tipsDiscountType;

    @SerializedName("tips_discount_amount")
    private String tipsDiscountAmount;

    @SerializedName("total_discount_amount")
    private String totalDiscountAmount;

    @SerializedName("total_final_amount")
    private String totalFinalAmount;

    @SerializedName("move_charges_calculated")
//    private CommonChargesRequestModel moveChargesCalculated;
    private String moveChargesCalculated;

    @SerializedName("valuation_charges_calculated")
//    private CommonChargesRequestModel valuationChargesCalculated;
    private String valuationChargesCalculated;

    @SerializedName("bottomline_discount_type")
    private int bottomLineDiscountType;

    @SerializedName("bottomline_discount_value")
    private String bottomLineDiscountValue;

    @SerializedName("notification_flag")
    private String notificationFlag;

    @SerializedName("move_calculated_id")
    private String moveChargesCalculatedId;

    @SerializedName("valuation_calculated_id")
    private String valuationChargesCalculatedId;

    @SerializedName("status")
    private String status;

    @SerializedName("created_by")
    private String userId;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("bol_started_flag")
    private String bolStarted;

    @SerializedName("tips_flag")
    private String tipAdded;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getActualHours() {
        return actualHours;
    }

    public void setActualHours(String actualHours) {
        this.actualHours = actualHours;
    }

    public String getActualTravelTime() {
        return actualTravelTime;
    }

    public void setActualTravelTime(String actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getTotalFinalAmount() {
        return totalFinalAmount;
    }

    public void setTotalFinalAmount(String totalFinalAmount) {
        this.totalFinalAmount = totalFinalAmount;
    }

    public String getMoveChargesCalculated() {
        return moveChargesCalculated;
    }

    public void setMoveChargesCalculated(String moveChargesCalculated) {
        this.moveChargesCalculated = moveChargesCalculated;
    }

    public String getValuationChargesCalculated() {
        return valuationChargesCalculated;
    }

    public void setValuationChargesCalculated(String valuationChargesCalculated) {
        this.valuationChargesCalculated = valuationChargesCalculated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public String getReviewDiscountAmount() {
        return reviewDiscountAmount;
    }

    public void setReviewDiscountAmount(String reviewDiscountAmount) {
        this.reviewDiscountAmount = reviewDiscountAmount;
    }

    public String getTipsDiscountType() {
        return tipsDiscountType;
    }

    public void setTipsDiscountType(String tipsDiscountType) {
        this.tipsDiscountType = tipsDiscountType;
    }

    public String getTipsDiscountAmount() {
        return tipsDiscountAmount;
    }

    public void setTipsDiscountAmount(String tipsDiscountAmount) {
        this.tipsDiscountAmount = tipsDiscountAmount;
    }

    public int getBottomLineDiscountType() {
        return bottomLineDiscountType;
    }

    public void setBottomLineDiscountType(int bottomLineDiscountType) {
        this.bottomLineDiscountType = bottomLineDiscountType;
    }

    public String getBottomLineDiscountValue() {
        return bottomLineDiscountValue;
    }

    public void setBottomLineDiscountValue(String bottomLineDiscountValue) {
        this.bottomLineDiscountValue = bottomLineDiscountValue;
    }

    public String getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(String notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public String getMoveChargesCalculatedId() {
        return moveChargesCalculatedId;
    }

    public void setMoveChargesCalculatedId(String moveChargesCalculatedId) {
        this.moveChargesCalculatedId = moveChargesCalculatedId;
    }

    public String getValuationChargesCalculatedId() {
        return valuationChargesCalculatedId;
    }

    public void setValuationChargesCalculatedId(String valuationChargesCalculatedId) {
        this.valuationChargesCalculatedId = valuationChargesCalculatedId;
    }

    public boolean getBolStarted() {
        if(bolStarted==null){
            return false;
        }
        return bolStarted.equals("1");
    }

    public void setBolStarted(boolean bolStarted) {
        if(bolStarted) {
            this.bolStarted = "1";
        } else {
            this.bolStarted = "0";
        }
    }

    public boolean getTipAdded() {
        if(tipAdded==null){
            return false;
        }
        return tipAdded.equals("1");
    }

    public void setTipAdded(boolean tipAdded) {
        if(tipAdded) {
            this.tipAdded = "1";
        } else {
            this.tipAdded = "0";
        }
    }

}
