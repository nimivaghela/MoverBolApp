package com.moverbol.model.confirmationPage;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;


/**
 * Created by AkashM on 27/1/18.
 */

public class MoveChargesManualPricingPojo extends BaseObservable implements Parcelable {

    @SerializedName("move_item_id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("rate")
    private String addRate;

    @SerializedName(value = "quantity", alternate = {"qty"})
    private String quantity;

    @SerializedName(value = "total", alternate = {"amount"})
    private String totalAmount;

    @SerializedName(value = "unit_name", alternate = "unit")
    private String unitName;

    private boolean showEditOption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getAddRate() {
        return addRate;
    }

    public void setAddRate(String addRate) {
        this.addRate = addRate;
        notifyPropertyChanged(BR.addRate);
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
    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
        return "MoveChargesManualPricingPojo{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", addRate='" + addRate + '\'' +
                ", quantity='" + quantity + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
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
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.addRate);
        dest.writeString(this.quantity);
        dest.writeString(this.totalAmount);
        dest.writeString(this.unitName);
    }

    public MoveChargesManualPricingPojo() {
    }

    protected MoveChargesManualPricingPojo(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.addRate = in.readString();
        this.quantity = in.readString();
        this.totalAmount = in.readString();
        this.unitName = in.readString();
    }

    public static final Parcelable.Creator<MoveChargesManualPricingPojo> CREATOR = new Parcelable.Creator<MoveChargesManualPricingPojo>() {
        @Override
        public MoveChargesManualPricingPojo createFromParcel(Parcel source) {
            return new MoveChargesManualPricingPojo(source);
        }

        @Override
        public MoveChargesManualPricingPojo[] newArray(int size) {
            return new MoveChargesManualPricingPojo[size];
        }
    };
}
