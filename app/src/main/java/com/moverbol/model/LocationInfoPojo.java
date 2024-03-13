package com.moverbol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/11/17.
 */


public class LocationInfoPojo implements Parcelable {

    @SerializedName("location_type")
    private String locationType;

    @SerializedName(value = "r_pickup_location_type", alternate = {"r_delivery_location_type"})
    private String r_location_type;

    @SerializedName(value = "r_distance_door_vehicle")
    private String r_distance_door_vehicle;

    @SerializedName("r_distance_type")
    private String r_distance_type;

    @SerializedName("gate_code")
    private String gateCode;

    @SerializedName("distance_vehicle")
    private String distance;

    @SerializedName("parking_move_day")
    private String isParkingOnMoveDayAvilable;

    @SerializedName("elevator")
    private String isElevatorAvailable;

    @SerializedName("flights_stair")
    private String flightsOfStairs;

    @SerializedName("contact_name")
    private String contactName;

    @SerializedName(value = "a_org_phone", alternate = "a_des_phone")
    private String organisationPhoneNumber;

    @SerializedName(value = "a_org_notes", alternate = "a_des_notes")
    private String organisationNotes;

    @SerializedName("stairs_carry")
    private String stairsCarry;

    @SerializedName("long_carry")
    private String longCarry;

    @SerializedName("longcarry_distance")
    private String longCarryDistance;

    @SerializedName("elevetor_disatnce")
    private String elevetorDisatnce;

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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIsParkingOnMoveDayAvilable() {
        return isParkingOnMoveDayAvilable;
    }

    public void setIsParkingOnMoveDayAvilable(String isParkingOnMoveDayAvilable) {
        this.isParkingOnMoveDayAvilable = isParkingOnMoveDayAvilable;
    }

    protected LocationInfoPojo(Parcel in) {
        this.locationType = in.readString();
        this.r_location_type = in.readString();
        this.r_distance_door_vehicle = in.readString();
        this.r_distance_type = in.readString();
        this.gateCode = in.readString();
        this.distance = in.readString();
        this.isParkingOnMoveDayAvilable = in.readString();
        this.isElevatorAvailable = in.readString();
        this.flightsOfStairs = in.readString();
        this.contactName = in.readString();
        this.organisationPhoneNumber = in.readString();
        this.organisationNotes = in.readString();
        this.stairsCarry = in.readString();
        this.longCarry = in.readString();
        this.longCarryDistance = in.readString();
        this.elevetorDisatnce = in.readString();
    }

    public LocationInfoPojo() {
    }

    public String getFlightsOfStairs() {
        return flightsOfStairs;
    }

    public void setFlightsOfStairs(String flightsOfStairs) {
        this.flightsOfStairs = flightsOfStairs;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOrganisationPhoneNumber() {
        return organisationPhoneNumber;
    }

    public void setOrganisationPhoneNumber(String organisationPhoneNumber) {
        this.organisationPhoneNumber = organisationPhoneNumber;
    }

    public String getOrganisationNotes() {
        return organisationNotes;
    }

    public void setOrganisationNotes(String organisationNotes) {
        this.organisationNotes = organisationNotes;
    }

    public String getR_location_type() {
        return r_location_type;
    }

    public void setR_location_type(String r_location_type) {
        this.r_location_type = r_location_type;
    }

    public String getR_distance_door_vehicle() {
        return r_distance_door_vehicle;
    }

    public void setR_distance_door_vehicle(String r_distance_door_vehicle) {
        this.r_distance_door_vehicle = r_distance_door_vehicle;
    }

    public String getR_distance_type() {
        return r_distance_type;
    }

    public void setR_distance_type(String r_distance_type) {
        this.r_distance_type = r_distance_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getIsElevatorAvailable() {
        return isElevatorAvailable;
    }

    public void setIsElevatorAvailable(String isElevatorAvailable) {
        this.isElevatorAvailable = isElevatorAvailable;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locationType);
        dest.writeString(this.r_location_type);
        dest.writeString(this.r_distance_door_vehicle);
        dest.writeString(this.r_distance_type);
        dest.writeString(this.gateCode);
        dest.writeString(this.distance);
        dest.writeString(this.isParkingOnMoveDayAvilable);
        dest.writeString(this.isElevatorAvailable);
        dest.writeString(this.flightsOfStairs);
        dest.writeString(this.contactName);
        dest.writeString(this.organisationPhoneNumber);
        dest.writeString(this.organisationNotes);
        dest.writeString(this.stairsCarry);
        dest.writeString(this.longCarry);
        dest.writeString(this.longCarryDistance);
        dest.writeString(this.elevetorDisatnce);
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

    public static final Parcelable.Creator<LocationInfoPojo> CREATOR = new Parcelable.Creator<LocationInfoPojo>() {
        @Override
        public LocationInfoPojo createFromParcel(Parcel source) {
            return new LocationInfoPojo(source);
        }

        @Override
        public LocationInfoPojo[] newArray(int size) {
            return new LocationInfoPojo[size];
        }
    };
}
