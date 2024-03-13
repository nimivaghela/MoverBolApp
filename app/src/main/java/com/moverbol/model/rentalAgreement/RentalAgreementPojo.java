package com.moverbol.model.rentalAgreement;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 5/2/18.
 */

public class RentalAgreementPojo extends BaseObservable {

    @SerializedName("r_id")
    private String id;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName(value = "est_volume", alternate = {"estimate_volume"})
    private String estimateVolume;

    @SerializedName(value = "est_weight", alternate = {"estimate_weight"})
    private String estimateWeight;

    @SerializedName("description")
    private String description;

    @SerializedName(value = "administration_fee", alternate = {"fees"})
    private String administrationFees;

    @SerializedName("date_of_lease")
    private String dateOfLease;

    @SerializedName("rate_per_month")
    private String rentalRatePerMonth;

    @SerializedName(value = "occupant_name", alternate = {"customer_name"})
    private String occupantsName;

    @SerializedName("phone")
    private String phoneNumber;

    @SerializedName("address")
    private String address;

    @SerializedName(value = "zip_code", alternate = {"r_origin_zip_code", "r_dest_zip_code"})
    private String zipCode;

    @SerializedName(value = "city", alternate = {"r_origin_city", "r_dest_city"})
    private String city;

    @SerializedName(value = "state", alternate = {"state_name"})
    private String state;

    @SerializedName("storage_signature")
    private String storageSignature;

    @SerializedName("created_by")
    private String userId;

    @SerializedName("storage_show_flag")
    private int storageShowFlag;


    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdministrationFees() {
        return administrationFees;
    }

    public void setAdministrationFees(String administrationFees) {
        this.administrationFees = administrationFees;
    }

    public String getDateOfLease() {
        return Util.getFormattedCurrentDate(Constants.OUTPUT_DATE_FORMAT_COMMENTS);
    }

    public void setDateOfLease(String dateOfLease) {
        this.dateOfLease = dateOfLease;
    }

    @Bindable
    public String getRentalRatePerMonth() {
        return rentalRatePerMonth;
    }

    public void setRentalRatePerMonth(String rentalRatePerMonth) {
        this.rentalRatePerMonth = rentalRatePerMonth;
        notifyPropertyChanged(BR.rentalRatePerMonth);
    }

    @Bindable
    public String getOccupantsName() {
        return occupantsName;
    }

    public void setOccupantsName(String occupantsName) {
        this.occupantsName = occupantsName;
        notifyPropertyChanged(BR.occupantsName);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
        notifyPropertyChanged(BR.zipCode);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }


    public String getStorageSignature() {
        return storageSignature;
    }

    public void setStorageSignature(String storageSignature) {
        this.storageSignature = storageSignature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setValuesFromRentalAgreementSubmittedDetails(RentalAgreementSubmittedDetailsPojo rentalAgreementSubmittedDetails) {
        if (rentalAgreementSubmittedDetails == null) {
            return;
        }
        this.id = rentalAgreementSubmittedDetails.getId();
        this.opportunityId = rentalAgreementSubmittedDetails.getOpportunityId();
        this.dateOfLease = rentalAgreementSubmittedDetails.getDateOfLease();
        this.administrationFees = rentalAgreementSubmittedDetails.getAdministrationFees();
        this.estimateVolume = rentalAgreementSubmittedDetails.getEstimateVolume();
        this.estimateWeight = rentalAgreementSubmittedDetails.getEstimateWeight();
        this.rentalRatePerMonth = rentalAgreementSubmittedDetails.getRentalRatePerMonth();
        this.occupantsName = rentalAgreementSubmittedDetails.getOccupantsName();
        this.phoneNumber = rentalAgreementSubmittedDetails.getPhoneNumber();

        this.address = rentalAgreementSubmittedDetails.getAddress();
        this.city = rentalAgreementSubmittedDetails.getCity();
        this.state = rentalAgreementSubmittedDetails.getState();
        this.zipCode = rentalAgreementSubmittedDetails.getZipCode();

        /*this.occupantsName = rentalAgreementSubmittedDetails.getOccupantsName();
        this.phoneNumber = rentalAgreementSubmittedDetails.getPhoneNumber();*/

        /*this.address = TextUtils.isEmpty(rentalAgreementSubmittedDetails.getAddress()) ? this.address : rentalAgreementSubmittedDetails.getAddress();
        this.city = TextUtils.isEmpty(rentalAgreementSubmittedDetails.getCity()) ? this.city : rentalAgreementSubmittedDetails.getCity();
        this.state = TextUtils.isEmpty(rentalAgreementSubmittedDetails.getState()) ? this.state : rentalAgreementSubmittedDetails.getState();
        this.zipCode = TextUtils.isEmpty(rentalAgreementSubmittedDetails.getZipCode()) ? this.zipCode : rentalAgreementSubmittedDetails.getZipCode();*/
    }

    public int getStorageShowFlag() {
        return storageShowFlag;
    }

    public void setStorageShowFlag(int storageShowFlag) {
        this.storageShowFlag = storageShowFlag;
    }
}
