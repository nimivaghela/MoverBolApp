package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;

/**
 * Created by AkashM on 4/4/18.
 */
class BolInformationRequestPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("actual_hours")
    private String actualHours;

    @SerializedName("actual_travel_time")
    private String actual_travelTime;

    @SerializedName("coupon_id")
    private String couponId;

    @SerializedName("coupon_type")
    private String couponType;

    @SerializedName("coupon_code")
    private String couponCode;

    @SerializedName("discount_value")
    private String discountValue;

    @SerializedName("total_discount_amount")
    private String totalDiscountAmount;

    @SerializedName("total_final_amount")
    private String totalFinalAmount;





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActualHours() {
        return actualHours;
    }

    public void setActualHours(String actualHours) {
        this.actualHours = actualHours;
    }

    public String getActual_travelTime() {
        return actual_travelTime;
    }

    public void setActual_travelTime(String actual_travelTime) {
        this.actual_travelTime = actual_travelTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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


    public void setDataFromJobConfirmationAndCouponPojo(JobConfirmationDetailsPojo jobConfirmationDetailsPojo, CouponDetailsModel couponDetailsModel, String totalDiscountAmount, String totalFinalAmount) {
        this.actualHours = jobConfirmationDetailsPojo.getActualHours();
        this.actual_travelTime = jobConfirmationDetailsPojo.getActualTravelTime();
        this.couponId = couponDetailsModel.getId();
        this.couponType = couponDetailsModel.getCouponType();
        this.couponCode = couponDetailsModel.getCouponCode();
        this.discountValue = couponDetailsModel.getCouponValue() + "";
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalFinalAmount = totalFinalAmount;
    }

}
