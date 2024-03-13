package com.moverbol.model.confirmationPage;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;

/**
 * Created by AkashM on 23/1/18.
 */

public class AdditionalChargesPojo extends BaseObservable implements Parcelable {

    @SerializedName("quote_item_id")
    private String itemId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("quote_unit")
    private String unitId;

    @SerializedName("quote_quantity")
    private String quantity;

    @SerializedName("quote_rate")
    private String rate;

    @SerializedName("quote_total")
    private String totalAmmount;

    @SerializedName("unit_name")
    private String unitName;

    private boolean showEditOption;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Bindable
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    @Bindable
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
        notifyPropertyChanged(BR.rate);
    }

    @Bindable
    public String getTotalAmmount() {
        return totalAmmount;
    }

    public void setTotalAmmount(String totalAmmount) {
        this.totalAmmount = totalAmmount;
        notifyPropertyChanged(BR.totalAmount);
    }

    @Bindable
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
        notifyPropertyChanged(BR.unitName);
    }

    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }


    @Override
    public String toString() {
        return "AdditionalChargesPojo{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unitId='" + unitId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rate='" + rate + '\'' +
                ", totalAmmount='" + totalAmmount + '\'' +
                ", unitName='" + unitName + '\'' +
                ", showEditOption=" + showEditOption +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.unitId);
        dest.writeString(this.quantity);
        dest.writeString(this.rate);
        dest.writeString(this.totalAmmount);
        dest.writeString(this.unitName);
    }

    public AdditionalChargesPojo() {
    }

    protected AdditionalChargesPojo(Parcel in) {
        this.itemId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.unitId = in.readString();
        this.quantity = in.readString();
        this.rate = in.readString();
        this.totalAmmount = in.readString();
        this.unitName = in.readString();
    }

    public static final Parcelable.Creator<AdditionalChargesPojo> CREATOR = new Parcelable.Creator<AdditionalChargesPojo>() {
        @Override
        public AdditionalChargesPojo createFromParcel(Parcel source) {
            return new AdditionalChargesPojo(source);
        }

        @Override
        public AdditionalChargesPojo[] newArray(int size) {
            return new AdditionalChargesPojo[size];
        }
    };
}
