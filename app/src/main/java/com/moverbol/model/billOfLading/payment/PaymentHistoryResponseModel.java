package com.moverbol.model.billOfLading.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

import java.text.ParseException;

public class PaymentHistoryResponseModel implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("actual_amount")
    private String amountWithCardConvenienceFeeNotAdded;

    @SerializedName("creditcard_conv_fee_type")
    private String cardConvenienceFeeType;

    @SerializedName("creditcard_conv_fee_val")
    private String cardConvenienceFeeValue;

    @SerializedName("r_amount")
    private String amount;

    @SerializedName("r_payment_date")
    private String paymentDate;

    @SerializedName("spreedly_status")
    private String spreedlyStatus;

    @SerializedName("r_payment_mode_id")
    private String paymentModeId;

    @SerializedName("payment_timestamp")
    private String paymentTimestamp;

    @SerializedName("payment_mode_name")
    private String paymentModeName;


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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean getSpreedlyStatus() {
        if(spreedlyStatus==null){
            return false;
        }
        return spreedlyStatus.equals("1");
    }

    public void setSpreedlyStatus(String spreedlyStatus) {
        this.spreedlyStatus = spreedlyStatus;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setPaymentTimestamp(String paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }

    public String getTimeStampString(){
        long paymentTimeMillis = 0;
        String stringToReturn = "";
        try {
            paymentTimeMillis = Util.getDateObjectFromStringDate(getPaymentTimestamp(), Constants.INPUT_DATE_FORMAT_COMMENTS).getTime();
            stringToReturn = Util.getFormattedTimeFromMillis(paymentTimeMillis, Constants.OUTPUT_DATE_FORMAT_TIMINGS_DETAILS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stringToReturn;
    }

    public String getPaymentModeName() {
        if(getSpreedlyStatus()){
            return "Card";
        }
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getAmountWithCardConvenienceFeeNotAdded() {
        return amountWithCardConvenienceFeeNotAdded;
    }

    public void setAmountWithCardConvenienceFeeNotAdded(String amountWithCardConvenienceFeeNotAdded) {
        this.amountWithCardConvenienceFeeNotAdded = amountWithCardConvenienceFeeNotAdded;
    }

    public String getCardConvenienceFeeType() {
        return cardConvenienceFeeType;
    }

    public void setCardConvenienceFeeType(String cardConvenienceFeeType) {
        this.cardConvenienceFeeType = cardConvenienceFeeType;
    }

    public String getCardConvenienceFeeValue() {
        return cardConvenienceFeeValue;
    }

    public void setCardConvenienceFeeValue(String cardConvenienceFeeValue) {
        this.cardConvenienceFeeValue = cardConvenienceFeeValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.opportunityId);
        dest.writeString(this.amountWithCardConvenienceFeeNotAdded);
        dest.writeString(this.cardConvenienceFeeType);
        dest.writeString(this.cardConvenienceFeeValue);
        dest.writeString(this.amount);
        dest.writeString(this.paymentDate);
        dest.writeString(this.spreedlyStatus);
        dest.writeString(this.paymentModeId);
        dest.writeString(this.paymentTimestamp);
        dest.writeString(this.paymentModeName);
    }

    public PaymentHistoryResponseModel() {
    }

    protected PaymentHistoryResponseModel(Parcel in) {
        this.id = in.readString();
        this.opportunityId = in.readString();
        this.amountWithCardConvenienceFeeNotAdded = in.readString();
        this.cardConvenienceFeeType = in.readString();
        this.cardConvenienceFeeValue = in.readString();
        this.amount = in.readString();
        this.paymentDate = in.readString();
        this.spreedlyStatus = in.readString();
        this.paymentModeId = in.readString();
        this.paymentTimestamp = in.readString();
        this.paymentModeName = in.readString();
    }

    public static final Parcelable.Creator<PaymentHistoryResponseModel> CREATOR = new Parcelable.Creator<PaymentHistoryResponseModel>() {
        @Override
        public PaymentHistoryResponseModel createFromParcel(Parcel source) {
            return new PaymentHistoryResponseModel(source);
        }

        @Override
        public PaymentHistoryResponseModel[] newArray(int size) {
            return new PaymentHistoryResponseModel[size];
        }
    };
}
