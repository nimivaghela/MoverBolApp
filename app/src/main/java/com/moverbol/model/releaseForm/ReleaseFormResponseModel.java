package com.moverbol.model.releaseForm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.AdressPojo;
import com.moverbol.model.ExtraStopsPojo;

import java.util.List;

/**
 * Created by AkashM on 7/2/18.
 */

public class ReleaseFormResponseModel implements Parcelable {

    @SerializedName("metadata")
    private List<ReleaseFormMetadataPojo> releaseFormMetadataPojoList;

    @SerializedName("pickupaddress")
    private AdressPojo pickupAddress;

    @SerializedName("pickup-extra-stop")
    private List<ExtraStopsPojo> pickupExtraStops;

    @SerializedName("deliveryaddress")
    private AdressPojo deliveryAddress;

    @SerializedName("delivery-extra-stop")
    private List<ExtraStopsPojo> deliveryExtraStops;

    public List<ReleaseFormMetadataPojo> getReleaseFormMetadataPojoList() {
        return releaseFormMetadataPojoList;
    }

    public void setReleaseFormMetadataPojoList(List<ReleaseFormMetadataPojo> releaseFormMetadataPojoList) {
        this.releaseFormMetadataPojoList = releaseFormMetadataPojoList;
    }

    public AdressPojo getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(AdressPojo pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public List<ExtraStopsPojo> getPickupExtraStops() {
        return pickupExtraStops;
    }

    public void setPickupExtraStops(List<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }

    public AdressPojo getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AdressPojo deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<ExtraStopsPojo> getDeliveryExtraStops() {
        return deliveryExtraStops;
    }

    public void setDeliveryExtraStops(List<ExtraStopsPojo> deliveryExtraStops) {
        this.deliveryExtraStops = deliveryExtraStops;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.releaseFormMetadataPojoList);
        dest.writeParcelable(this.pickupAddress, flags);
        dest.writeTypedList(this.pickupExtraStops);
        dest.writeParcelable(this.deliveryAddress, flags);
        dest.writeTypedList(this.deliveryExtraStops);
    }

    public ReleaseFormResponseModel() {
    }

    protected ReleaseFormResponseModel(Parcel in) {
        this.releaseFormMetadataPojoList = in.createTypedArrayList(ReleaseFormMetadataPojo.CREATOR);
        this.pickupAddress = in.readParcelable(AdressPojo.class.getClassLoader());
        this.pickupExtraStops = in.createTypedArrayList(ExtraStopsPojo.CREATOR);
        this.deliveryAddress = in.readParcelable(AdressPojo.class.getClassLoader());
        this.deliveryExtraStops = in.createTypedArrayList(ExtraStopsPojo.CREATOR);
    }

    public static final Parcelable.Creator<ReleaseFormResponseModel> CREATOR = new Parcelable.Creator<ReleaseFormResponseModel>() {
        @Override
        public ReleaseFormResponseModel createFromParcel(Parcel source) {
            return new ReleaseFormResponseModel(source);
        }

        @Override
        public ReleaseFormResponseModel[] newArray(int size) {
            return new ReleaseFormResponseModel[size];
        }
    };
}
