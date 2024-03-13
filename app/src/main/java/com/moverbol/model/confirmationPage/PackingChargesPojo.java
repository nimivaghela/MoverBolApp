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

public class PackingChargesPojo extends BaseObservable implements Parcelable {

    @SerializedName("pack_item_id")
    private String packItemId;

    @SerializedName("c_stock_material_name")
    private String stockMatrialName;

    @SerializedName("pack_quantity")
    private String packQuantity;

    @SerializedName("quote_rate")
    private String quoteRate;

    @SerializedName("pack_total")
    private String packTotal;

    @SerializedName("pack_description")
    private String packDescription;

    @SerializedName("pack_mover")
    private String packMover;

    @SerializedName("quote_id")
    private String quoteId;

    @SerializedName("rate_type_name")
    private String rateTypeName;

    private boolean showEditOption;

    public String getPackItemId() {
        return packItemId;
    }

    public void setPackItemId(String packItemId) {
        this.packItemId = packItemId;
    }

    public String getStockMatrialName() {
        return stockMatrialName;
    }

    public void setStockMatrialName(String stockMatrialName) {
        this.stockMatrialName = stockMatrialName;
    }

    @Bindable
    public String getPackQuantity() {
        return packQuantity;
    }

    public void setPackQuantity(String packQuantity) {
        this.packQuantity = packQuantity;
        notifyPropertyChanged(BR.packQuantity);
    }

    @Bindable
    public String getQuoteRate() {
        return quoteRate;
    }

    public void setQuoteRate(String quoteRate) {
        this.quoteRate = quoteRate;
        notifyPropertyChanged(BR.quoteRate);
    }

    @Bindable
    public String getPackTotal() {
        return packTotal;
    }

    public void setPackTotal(String packTotal) {
        this.packTotal = packTotal;
        notifyPropertyChanged(BR.packTotal);
    }

    public String getPackDescription() {
        return packDescription;
    }

    public void setPackDescription(String packDescription) {
        this.packDescription = packDescription;
    }

    public String getPackMover() {
        return packMover;
    }

    public void setPackMover(String packMover) {
        this.packMover = packMover;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    @Bindable
    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
        notifyPropertyChanged(BR.rateTypeName);
    }


    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }

    @Override
    public String toString() {
        return "PackingChargesPojo{" +
                "packItemId='" + packItemId + '\'' +
                ", stockMatrialName='" + stockMatrialName + '\'' +
                ", packQuantity='" + packQuantity + '\'' +
                ", quoteRate='" + quoteRate + '\'' +
                ", packTotal='" + packTotal + '\'' +
                ", packDescription='" + packDescription + '\'' +
                ", packMover='" + packMover + '\'' +
                ", quoteId='" + quoteId + '\'' +
                ", rateTypeName='" + rateTypeName + '\'' +
                ", showEditOption=" + showEditOption +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packItemId);
        dest.writeString(this.stockMatrialName);
        dest.writeString(this.packQuantity);
        dest.writeString(this.quoteRate);
        dest.writeString(this.packTotal);
        dest.writeString(this.packDescription);
        dest.writeString(this.packMover);
        dest.writeString(this.quoteId);
        dest.writeString(this.rateTypeName);
    }

    public PackingChargesPojo() {
    }

    protected PackingChargesPojo(Parcel in) {
        this.packItemId = in.readString();
        this.stockMatrialName = in.readString();
        this.packQuantity = in.readString();
        this.quoteRate = in.readString();
        this.packTotal = in.readString();
        this.packDescription = in.readString();
        this.packMover = in.readString();
        this.quoteId = in.readString();
        this.rateTypeName = in.readString();
    }

    public static final Parcelable.Creator<PackingChargesPojo> CREATOR = new Parcelable.Creator<PackingChargesPojo>() {
        @Override
        public PackingChargesPojo createFromParcel(Parcel source) {
            return new PackingChargesPojo(source);
        }

        @Override
        public PackingChargesPojo[] newArray(int size) {
            return new PackingChargesPojo[size];
        }
    };
}
