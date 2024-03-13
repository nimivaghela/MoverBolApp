package com.moverbol.model.billOfLading;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 2/4/18.
 */
public class CouponDetailsModel implements Parcelable {

    @SerializedName("r_id")
    private String id;

    @SerializedName("coupon_name")
    private String couponName;

    @SerializedName("coupon_code")
    private String couponCode;

    @SerializedName("coupon_type")
    private String couponType;

    @SerializedName("coupon_value")
    private String couponValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
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

    public double getCouponValue() {
        return Util.getDoubleFromString(couponValue);
    }

    public void setCouponValue(double couponValue) {
        this.couponValue = couponValue + "";
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.couponName);
        dest.writeString(this.couponCode);
        dest.writeString(this.couponType);
        dest.writeString(this.couponValue);
    }

    public CouponDetailsModel() {
    }

    protected CouponDetailsModel(Parcel in) {
        this.id = in.readString();
        this.couponName = in.readString();
        this.couponCode = in.readString();
        this.couponType = in.readString();
        this.couponValue = in.readString();
    }

    public static final Parcelable.Creator<CouponDetailsModel> CREATOR = new Parcelable.Creator<CouponDetailsModel>() {
        @Override
        public CouponDetailsModel createFromParcel(Parcel source) {
            return new CouponDetailsModel(source);
        }

        @Override
        public CouponDetailsModel[] newArray(int size) {
            return new CouponDetailsModel[size];
        }
    };
}
