package com.moverbol.model.rentalAgreement;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 6/2/18.
 */

public class RentalAgreementSubmittedDetailsPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("date_of_lease")
    private String dateOfLease;

    @SerializedName("administration_fee")
    private String administrationFees;

    @SerializedName("est_volume")
    private String estimateVolume;

    @SerializedName("est_weight")
    private String estimateWeight;

    @SerializedName("rate_per_month")
    private String rentalRatePerMonth;

    @SerializedName("occupant_name")
    private String occupantsName;

    @SerializedName("phone")
    private String phoneNumber;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip_code")
    private String zipCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getDateOfLease() {
        return dateOfLease;
    }

    public void setDateOfLease(String dateOfLease) {
        this.dateOfLease = dateOfLease;
    }

    public String getAdministrationFees() {
        return administrationFees;
    }

    public void setAdministrationFees(String administrationFees) {
        this.administrationFees = administrationFees;
    }

    public String getEstimateVolume() {
        return estimateVolume;
    }

    public void setEstimateVolume(String estimateVolume) {
        this.estimateVolume = estimateVolume;
    }

    public String getEstimateWeight() {
        return estimateWeight;
    }

    public void setEstimateWeight(String estimateWeight) {
        this.estimateWeight = estimateWeight;
    }

    public String getRentalRatePerMonth() {
        return rentalRatePerMonth;
    }

    public void setRentalRatePerMonth(String rentalRatePerMonth) {
        this.rentalRatePerMonth = rentalRatePerMonth;
    }

    public String getOccupantsName() {
        return occupantsName;
    }

    public void setOccupantsName(String occupantsName) {
        this.occupantsName = occupantsName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
