package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

/**
 * Created by AkashM on 4/4/18.
 */
public class BillOfLadingRequestModel {

    @SerializedName("r_id")
    private String bolFinalChargeId;

    @SerializedName("actual_hours")
    private double actualHours;

    @SerializedName("actual_travel_time")
    private double actualTravelTime;

    @SerializedName("hourly_rate")
    private String hourlyRate;

    @SerializedName("total_final_amount")
    private double totalChargesAmount;

    @SerializedName("bottom_line_discount_type")
    private String bottomLineDiscountType;

    @SerializedName("bottom_line_discount_value")
    private String bottomLineDiscountValue;

    @SerializedName("move_charges_calculated")
    private CommonChargesRequestModel moveChargesCalculated;

    @SerializedName("valuation_charges_calculated")
    private CommonChargesRequestModel valuationChargesCalculated;
    @SerializedName("status")
    private int status;
    @SerializedName("bottom_line_calculated")
    private String bottomLineCalculated;
    @SerializedName("service_charge_calculated")
    private String serviceChargeCalculated;


//    @SerializedName("bol_signature")
//    private String bolSignature;


    public String getBolFinalChargeId() {
        return bolFinalChargeId;
    }

    public void setBolFinalChargeId(String bolFinalChargeId) {
        this.bolFinalChargeId = bolFinalChargeId;
    }

    public double getActualHours() {
        return actualHours;
    }

    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public double getActualTravelTime() {
        return actualTravelTime;
    }

    public void setActualTravelTime(double actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }

    public double getTotalChargesAmount() {
        return totalChargesAmount;
    }

    public void setTotalChargesAmount(double totalChargesAmount) {
        this.totalChargesAmount = totalChargesAmount;
    }

    public CommonChargesRequestModel getMoveChargesCalculated() {
        return moveChargesCalculated;
    }

    public void setMoveChargesCalculated(CommonChargesRequestModel moveChargesCalculated) {
        this.moveChargesCalculated = moveChargesCalculated;
    }

    public CommonChargesRequestModel getValuationChargesCalculated() {
        return valuationChargesCalculated;
    }

    public void setValuationChargesCalculated(CommonChargesRequestModel valuationChargesCalculated) {
        this.valuationChargesCalculated = valuationChargesCalculated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /*public String getBolSignature() {
        return bolSignature;
    }

    public void setBolSignature(String bolSignature) {
        this.bolSignature = bolSignature;
    }*/

    public String getBottomLineDiscountType() {
        return bottomLineDiscountType;
    }

    public void setBottomLineDiscountType(String bottomLineDiscountType) {
        this.bottomLineDiscountType = bottomLineDiscountType;
    }

    public String getBottomLineDiscountValue() {
        return bottomLineDiscountValue;
    }

    public void setBottomLineDiscountValue(String bottomLineDiscountValue) {
        this.bottomLineDiscountValue = bottomLineDiscountValue;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getBottomLineCalculated() {
        return bottomLineCalculated;
    }

    public void setBottomLineCalculated(String bottomLineCalculated) {
        this.bottomLineCalculated = bottomLineCalculated;
    }

    public String getServiceChargeCalculated() {
        return serviceChargeCalculated;
    }

    public void setServiceChargeCalculated(String serviceChargeCalculated) {
        this.serviceChargeCalculated = serviceChargeCalculated;
    }
}

