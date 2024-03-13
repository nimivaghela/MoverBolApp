package com.moverbol.model.confirmationPage;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;

/**
 * Created by AkashM on 27/1/18.
 */

public class MoveChargesAutoPricingPojo extends BaseObservable implements Parcelable {

    /*@SerializedName("r_price_id")
    private String id;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("r_move_type")
    private String moveTypeId;

    @SerializedName("r_price_type")
    private String priceType;

    @SerializedName("pack_est_volume")
    private String packEstimatedVolume;

    @SerializedName("pack_est_weight")
    private String packEstimatedWeight;

    @SerializedName("pack_men")
    private String numberOfMen;

    @SerializedName("pack_tot_rate_per_hrs")
    private String ratePerHour;

    @SerializedName("pack_comments")
    private String comments;

    @SerializedName("pack_tot_price")
    private String packTotalPrice;

    @SerializedName("pack_grand_tot_price")
    private String packGrandTotalPrice;

    @SerializedName("grand_tot_price")
    private String totalAmount;*/

    @SerializedName("r_id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("qty")
    private String quantity;

    @SerializedName("unit")
    private String unit;

    @SerializedName("rate")
    private String rate;

    @SerializedName("amount")
    private String amount;

    private boolean showEditOption;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        notifyPropertyChanged(BR.unit);
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
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);
    }

    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.quantity);
        dest.writeString(this.unit);
        dest.writeString(this.rate);
        dest.writeString(this.amount);
        dest.writeByte(this.showEditOption ? (byte) 1 : (byte) 0);
    }

    public MoveChargesAutoPricingPojo() {
    }

    protected MoveChargesAutoPricingPojo(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.quantity = in.readString();
        this.unit = in.readString();
        this.rate = in.readString();
        this.amount = in.readString();
        this.showEditOption = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MoveChargesAutoPricingPojo> CREATOR = new Parcelable.Creator<MoveChargesAutoPricingPojo>() {
        @Override
        public MoveChargesAutoPricingPojo createFromParcel(Parcel source) {
            return new MoveChargesAutoPricingPojo(source);
        }

        @Override
        public MoveChargesAutoPricingPojo[] newArray(int size) {
            return new MoveChargesAutoPricingPojo[size];
        }
    };
}
