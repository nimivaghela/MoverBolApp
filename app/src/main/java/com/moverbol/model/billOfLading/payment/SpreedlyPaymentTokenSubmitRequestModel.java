package com.moverbol.model.billOfLading.payment;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;

public class SpreedlyPaymentTokenSubmitRequestModel {

    @SerializedName("subdomain")
    private String subdomain;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("bol_finalcharge_id")
    private String bolFinalChargeId;

    @SerializedName("payment_token")
    private String paymentToken;

    @SerializedName("actual_amount")
    private String amountWithCardConvenienceFeeNotAdded;

    @SerializedName("credit_card_fee_type")
    private String cardConvenienceFeeType;

    @SerializedName("credit_card_fee_value")
    private String cardConvenienceFeeValue;

    @SerializedName("amount")
    private String amount;

    @SerializedName("original_amount")
    private String originalAmount;

    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("payment_signature")
    private String paymentSignature;

    @SerializedName("additional_image")
    private String additionalImage;

    @SerializedName("deposit_amount")
    private String depositeAmount;


    /*public SpreedlyPaymentTokenSubmitRequestModel(String subdomain, String userId, String opportunityId, String jobId, String bolFinalChargeId, String paymentToken, String amount, String originalAmount, String paymentType, String paymentMethod, String paymentSignature, String additionalImage) {
        this.subdomain = subdomain;
        this.userId = userId;
        this.opportunityId = opportunityId;
        this.jobId = jobId;
        this.bolFinalChargeId = bolFinalChargeId;
        this.paymentToken = paymentToken;
        this.amount = amount;
        this.originalAmount = originalAmount;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.paymentSignature = paymentSignature;
        this.additionalImage = additionalImage;
    }*/


    public SpreedlyPaymentTokenSubmitRequestModel(String subdomain, String userId, String opportunityId, String jobId, String bolFinalChargeId, String paymentToken, String amountWithCardConvenienceFeeNotAdded, String cardConvenienceFeeType, String cardConvenienceFeeValue, String amount, String originalAmount, String paymentType, String paymentMethod, String paymentSignature, String additionalImage, String depositeAmount) {
        this.subdomain = subdomain;
        this.userId = userId;
        this.opportunityId = opportunityId;
        this.jobId = jobId;
        this.bolFinalChargeId = bolFinalChargeId;
        this.paymentToken = paymentToken;
        this.amountWithCardConvenienceFeeNotAdded = amountWithCardConvenienceFeeNotAdded;
        this.cardConvenienceFeeType = cardConvenienceFeeType;
        this.cardConvenienceFeeValue = cardConvenienceFeeValue;
        this.amount = amount;
        this.originalAmount = originalAmount;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.paymentSignature = paymentSignature;
        this.additionalImage = additionalImage;
        this.depositeAmount = depositeAmount;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getBolFinalChargeId() {
        return bolFinalChargeId;
    }

    public void setBolFinalChargeId(String bolFinalChargeId) {
        this.bolFinalChargeId = bolFinalChargeId;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentSignature() {
        return paymentSignature;
    }

    public void setPaymentSignature(String paymentSignature) {
        this.paymentSignature = paymentSignature;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAdditionalImage() {
        return additionalImage;
    }

    public void setAdditionalImage(String additionalImage) {
        this.additionalImage = additionalImage;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAmountWithCardConvenienceFeeNotAdded() {
        return amountWithCardConvenienceFeeNotAdded;
    }

    public void setAmountWithCardConvenienceFeeNotAdded(String amountWithCardConvenienceFeeNotAdded) {
        this.amountWithCardConvenienceFeeNotAdded = amountWithCardConvenienceFeeNotAdded;
    }

    public boolean isCardConvenienceFeeTypePercentage() {
        if(cardConvenienceFeeType==null){
            return false;
        }
        return TextUtils.equals(cardConvenienceFeeType, Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "");
    }

    public void setIsCardConvenienceFeeTypePercentage(boolean isCardConvenienceFeeTypePercentage) {
        if(isCardConvenienceFeeTypePercentage){
            this.cardConvenienceFeeType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            this.cardConvenienceFeeType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }
    }

    public String getCardConvenienceFeeValue() {
        return cardConvenienceFeeValue;
    }

    public void setCardConvenienceFeeValue(String cardConvenienceFeeValue) {
        this.cardConvenienceFeeValue = cardConvenienceFeeValue;
    }

    public String getCardConvenienceFeeType() {
        return cardConvenienceFeeType;
    }

    public void setCardConvenienceFeeType(String cardConvenienceFeeType) {
        this.cardConvenienceFeeType = cardConvenienceFeeType;
    }

    public String getDepositeAmount() {
        return depositeAmount;
    }

    public void setDepositeAmount(String depositeAmount) {
        this.depositeAmount = depositeAmount;
    }
}