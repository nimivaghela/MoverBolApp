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

public class ValuationChargesPojo extends BaseObservable implements Parcelable {

    @SerializedName("quote_item_id")
    private String itemId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("quote_unit")
    private String quoteUnit;

    @SerializedName("quote_quantity")
    private String quantity;

    @SerializedName("quote_rate")
    private String rate;

    @SerializedName("quote_total")
    private String totalAmount;

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

    @Bindable
    public String getQuoteUnit() {
        return quoteUnit;
    }

    public void setQuoteUnit(String quoteUnit) {
        this.quoteUnit = quoteUnit;
        notifyPropertyChanged(BR.quoteUnit);
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
    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
        notifyPropertyChanged(BR.totalAmount);
    }

    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }


    @Override
    public String toString() {
        return "ValuationChargesPojo{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quoteUnit='" + quoteUnit + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rate='" + rate + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
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
        dest.writeString(this.quoteUnit);
        dest.writeString(this.quantity);
        dest.writeString(this.rate);
        dest.writeString(this.totalAmount);
    }

    public ValuationChargesPojo() {
    }

    protected ValuationChargesPojo(Parcel in) {
        this.itemId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.quoteUnit = in.readString();
        this.quantity = in.readString();
        this.rate = in.readString();
        this.totalAmount = in.readString();
    }

    public static final Parcelable.Creator<ValuationChargesPojo> CREATOR = new Parcelable.Creator<ValuationChargesPojo>() {
        @Override
        public ValuationChargesPojo createFromParcel(Parcel source) {
            return new ValuationChargesPojo(source);
        }

        @Override
        public ValuationChargesPojo[] newArray(int size) {
            return new ValuationChargesPojo[size];
        }
    };
}
