package com.moverbol.model.billOfLading.payment;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentHistoryDetailsModel implements Parcelable {

    private double paidAmount;
    private double cardConvinienceFee;
    private String paymentMethod;
    private String timeStamp;

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getCardConvinienceFee() {
        return cardConvinienceFee;
    }

    public void setCardConvinienceFee(double cardConvinienceFee) {
        this.cardConvinienceFee = cardConvinienceFee;
    }

    public double displayAmount() {
        return paidAmount - cardConvinienceFee;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.paidAmount);
        dest.writeDouble(this.cardConvinienceFee);
        dest.writeString(this.paymentMethod);
        dest.writeString(this.timeStamp);
    }

    public PaymentHistoryDetailsModel() {
    }

    protected PaymentHistoryDetailsModel(Parcel in) {
        this.paidAmount = in.readDouble();
        this.cardConvinienceFee = in.readDouble();
        this.paymentMethod = in.readString();
        this.timeStamp = in.readString();
    }

    public static final Parcelable.Creator<PaymentHistoryDetailsModel> CREATOR = new Parcelable.Creator<PaymentHistoryDetailsModel>() {
        @Override
        public PaymentHistoryDetailsModel createFromParcel(Parcel source) {
            return new PaymentHistoryDetailsModel(source);
        }

        @Override
        public PaymentHistoryDetailsModel[] newArray(int size) {
            return new PaymentHistoryDetailsModel[size];
        }
    };
}
