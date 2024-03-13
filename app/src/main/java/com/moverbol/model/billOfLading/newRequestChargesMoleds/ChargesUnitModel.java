package com.moverbol.model.billOfLading.newRequestChargesMoleds;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 20/4/18.
 */
public class ChargesUnitModel implements Parcelable {

    @SerializedName("unit_id")
    private String unitId;
    @SerializedName(value = "unit_name", alternate = {"unit"})
    private String unitName;
    @SerializedName("description")
    private String description;
    @SerializedName("qty")
    private String qty;
    @SerializedName("rate")
    private String rate;
    @SerializedName("unit_num")
    private double unitNum = 1.0;


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    protected ChargesUnitModel(Parcel in) {
        this.unitId = in.readString();
        this.unitName = in.readString();
        this.description = in.readString();
        this.qty = in.readString();
        this.rate = in.readString();
        this.unitNum = in.readDouble();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return unitName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public ChargesUnitModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unitId);
        dest.writeString(this.unitName);
        dest.writeString(this.description);
        dest.writeString(this.qty);
        dest.writeString(this.rate);
        dest.writeDouble(this.unitNum);
    }

    public static final Parcelable.Creator<ChargesUnitModel> CREATOR = new Parcelable.Creator<ChargesUnitModel>() {
        @Override
        public ChargesUnitModel createFromParcel(Parcel source) {
            return new ChargesUnitModel(source);
        }

        @Override
        public ChargesUnitModel[] newArray(int size) {
            return new ChargesUnitModel[size];
        }
    };

    public double getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(double unitNum) {
        this.unitNum = unitNum;
    }
}
