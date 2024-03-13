package com.moverbol.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AkashM on 28/11/17.
 */

public class AdressPojo implements Parcelable {

    @SerializedName("address")
    private String smallAddress;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("zipcode")
    private String zipcode;

    @SerializedName("location_info")
    private LocationInfoPojo locationInfo;

    public String getSmallAddress() {
        return smallAddress;
    }

    public void setSmallAddress(String smallAddress) {
        this.smallAddress = smallAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public LocationInfoPojo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfoPojo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getFullAddressString(){
        ArrayList<String> fullAddress = new ArrayList<>(0);

        if (!TextUtils.isEmpty(smallAddress)) {
            fullAddress.add(smallAddress.trim());
        }

        if (!TextUtils.isEmpty(city)) {
            fullAddress.add(city);
        }

        if (!TextUtils.isEmpty(state)) {
            fullAddress.add(state);
        }

        if (!TextUtils.isEmpty(zipcode)) {
            fullAddress.add(zipcode);
        }
        return TextUtils.join(", ", fullAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.smallAddress);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.zipcode);
        dest.writeParcelable(this.locationInfo, flags);
    }

    public AdressPojo() {
    }

    protected AdressPojo(Parcel in) {
        this.smallAddress = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.zipcode = in.readString();
        this.locationInfo = in.readParcelable(LocationInfoPojo.class.getClassLoader());
    }

    public static final Parcelable.Creator<AdressPojo> CREATOR = new Parcelable.Creator<AdressPojo>() {
        @Override
        public AdressPojo createFromParcel(Parcel source) {
            return new AdressPojo(source);
        }

        @Override
        public AdressPojo[] newArray(int size) {
            return new AdressPojo[size];
        }
    };
}
