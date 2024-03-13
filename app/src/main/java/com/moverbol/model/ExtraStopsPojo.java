package com.moverbol.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AkashM on 28/11/17.
 */

public class ExtraStopsPojo implements Parcelable {

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("zipcode")
    private String zipCode;

    @SerializedName("r_Dist_from_door_to_vehicle")
    private String r_Dist_from_door_to_vehicle;

    @SerializedName("fights_stair")
    private String flightsOfStrairs;

    @SerializedName(value = "r_pickup_location_type", alternate = {"r_delivery_location_type"})
    private String r_location_type;

    @SerializedName("gate_code")
    private String gateCode;

    @SerializedName("contact_name")
    private String contactName;

    @SerializedName(value = "a_org_phone", alternate = {"a_des_phone"})
    private String organisationPhone;

    @SerializedName(value = "a_org_notes", alternate = {"a_des_notes"})
    private String organisationNotes;

    @SerializedName("state")
    private String stateName;

    @SerializedName("country_name")
    private String countryName;

    @SerializedName("r_distance_type")
    private String distanceType;

    @SerializedName("parking_move_day")
    private String isParkingAvilable;

    @SerializedName("location_type")
    private String locationType;

    @SerializedName("distance_vehicle")
    private String distance;

    @SerializedName("stairs_carry")
    private String stairsCarry;

    @SerializedName("long_carry")
    private String longCarry;


    @SerializedName("longcarry_distance")
    private String longCarryDistance;

    @SerializedName("elevetor_disatnce")
    private String elevetorDisatnce;

    @SerializedName("elevator")
    private String isElevatorAvailable;


    protected ExtraStopsPojo(Parcel in) {
        this.address = in.readString();
        this.city = in.readString();
        this.zipCode = in.readString();
        this.r_Dist_from_door_to_vehicle = in.readString();
        this.isElevatorAvailable = in.readString();
        this.flightsOfStrairs = in.readString();
        this.r_location_type = in.readString();
        this.gateCode = in.readString();
        this.contactName = in.readString();
        this.organisationPhone = in.readString();
        this.organisationNotes = in.readString();
        this.stateName = in.readString();
        this.countryName = in.readString();
        this.distanceType = in.readString();
        this.isParkingAvilable = in.readString();
        this.locationType = in.readString();
        this.distance = in.readString();
        this.stairsCarry = in.readString();
        this.longCarry = in.readString();
        this.longCarryDistance = in.readString();
        this.elevetorDisatnce = in.readString();
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFlightsOfStrairs() {
        return flightsOfStrairs;
    }

    public void setFlightsOfStrairs(String flightsOfStrairs) {
        this.flightsOfStrairs = flightsOfStrairs;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getGateCode() {
        return gateCode;
    }

    public void setGateCode(String gateCode) {
        this.gateCode = gateCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOrganisationPhone() {
        return organisationPhone;
    }

    public void setOrganisationPhone(String organisationPhone) {
        this.organisationPhone = organisationPhone;
    }

    public String getOrganisationNotes() {
        return organisationNotes;
    }

    public void setOrganisationNotes(String organisationNotes) {
        this.organisationNotes = organisationNotes;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName == null ? "" : stateName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIsParkingAvilable() {
        return isParkingAvilable;
    }

    public void setIsParkingAvilable(String isParkingAvilable) {
        this.isParkingAvilable = isParkingAvilable;
    }

    public String getR_Dist_from_door_to_vehicle() {
        return r_Dist_from_door_to_vehicle;
    }

    public void setR_Dist_from_door_to_vehicle(String r_Dist_from_door_to_vehicle) {
        this.r_Dist_from_door_to_vehicle = r_Dist_from_door_to_vehicle;
    }

    public String getR_location_type() {
        return r_location_type;
    }

    public void setR_location_type(String r_location_type) {
        this.r_location_type = r_location_type;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public String getCountryName() {
        return countryName == null ? "" : countryName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode == null ? "" : zipCode;
    }

    public String getFullAddressString() {
        ArrayList<String> fullAddress = new ArrayList<>(0);

        if (!TextUtils.isEmpty(address)) {
            fullAddress.add(address.trim());
        }

        if (!TextUtils.isEmpty(city)) {
            fullAddress.add(city);
        }

        if (!TextUtils.isEmpty(stateName)) {
            fullAddress.add(stateName);
        }

        if (!TextUtils.isEmpty(zipCode)) {
            fullAddress.add(zipCode);
        }
        return TextUtils.join(", ", fullAddress);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ExtraStopsPojo() {
    }

    @Override
    public String toString() {
        return "ExtraStopsPojo{" +
                "smallAddress='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", r_Dist_from_door_to_vehicle='" + r_Dist_from_door_to_vehicle + '\'' +
                ", isElevatorAvilable='" + isElevatorAvailable + '\'' +
                ", flightsOfStrairs='" + flightsOfStrairs + '\'' +
                ", r_location_type='" + r_location_type + '\'' +
                ", gateCode='" + gateCode + '\'' +
                ", contactName='" + contactName + '\'' +
                ", organisationPhone='" + organisationPhone + '\'' +
                ", organisationNotes='" + organisationNotes + '\'' +
                ", stateName='" + stateName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", distanceType='" + distanceType + '\'' +
                ", isParkingAvilable='" + isParkingAvilable + '\'' +
                ", locationType='" + locationType + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getStairsCarry() {
        return stairsCarry;
    }

    public void setStairsCarry(String stairsCarry) {
        this.stairsCarry = stairsCarry;
    }

    public String getLongCarry() {
        return longCarry;
    }

    public void setLongCarry(String longCarry) {
        this.longCarry = longCarry;
    }

    public String getLongCarryDistance() {
        return longCarryDistance;
    }

    public void setLongCarryDistance(String longCarryDistance) {
        this.longCarryDistance = longCarryDistance;
    }

    public String getElevetorDisatnce() {
        return elevetorDisatnce;
    }

    public void setElevetorDisatnce(String elevetorDisatnce) {
        this.elevetorDisatnce = elevetorDisatnce;
    }

    public String getIsElevatorAvailable() {
        return isElevatorAvailable;
    }

    public void setIsElevatorAvailable(String isElevatorAvailable) {
        this.isElevatorAvailable = isElevatorAvailable;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.zipCode);
        dest.writeString(this.r_Dist_from_door_to_vehicle);
        dest.writeString(this.isElevatorAvailable);
        dest.writeString(this.flightsOfStrairs);
        dest.writeString(this.r_location_type);
        dest.writeString(this.gateCode);
        dest.writeString(this.contactName);
        dest.writeString(this.organisationPhone);
        dest.writeString(this.organisationNotes);
        dest.writeString(this.stateName);
        dest.writeString(this.countryName);
        dest.writeString(this.distanceType);
        dest.writeString(this.isParkingAvilable);
        dest.writeString(this.locationType);
        dest.writeString(this.distance);
        dest.writeString(this.stairsCarry);
        dest.writeString(this.longCarry);
        dest.writeString(this.longCarryDistance);
        dest.writeString(this.elevetorDisatnce);
    }

    public static final Parcelable.Creator<ExtraStopsPojo> CREATOR = new Parcelable.Creator<ExtraStopsPojo>() {
        @Override
        public ExtraStopsPojo createFromParcel(Parcel source) {
            return new ExtraStopsPojo(source);
        }

        @Override
        public ExtraStopsPojo[] newArray(int size) {
            return new ExtraStopsPojo[size];
        }
    };
}
