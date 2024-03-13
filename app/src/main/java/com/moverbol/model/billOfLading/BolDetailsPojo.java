package com.moverbol.model.billOfLading;


import static com.moverbol.constants.Constants.FALSE;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.model.ExtraStopsPojo;
import com.moverbol.model.confirmationPage.TermsAndConditionPojo;
import com.moverbol.util.Util;

import java.util.ArrayList;

/**
 * Created by AkashM on 17/4/18.
 */
public class BolDetailsPojo {
    @SerializedName("r_schedule_job_id")
    private String jobId;

    @SerializedName("r_job_id")
    private String moveProcessJobId;

    @SerializedName("r_start_date")
    private String startDate;

    @SerializedName("job_status")
    private String jobStatus;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("opp_opportunity_name")
    private String opportunityName;

    @SerializedName("r_phone1")
    private String phoneNumber;

    @SerializedName("r_phone2")
    private String phoneNumber2;

    @SerializedName("origin_address")
    private String originalAddress;

    @SerializedName("destination_address")
    private String destinationAddress;

    @SerializedName("r_dest_state")
    private String destinationState;

    @SerializedName("r_dest_country")
    private String destinationCountry;

    @SerializedName("r_dest_city")
    private String destinationCity;

    @SerializedName("r_origin_city")
    private String originalCity;

    @SerializedName("r_origin_state")
    private String originalState;

    @SerializedName("r_origin_country")
    private String originalCountry;

    @SerializedName("r_origin_zip_code")
    private String originalZipCode;

    @SerializedName("r_dest_zip_code")
    private String destinationZipCode;

    @SerializedName("square_feet")
    private String squareFeet;

    @SerializedName("bedroom")
    private String bedroom;

    @SerializedName("r_estimated_units")
    private String estimatedUnits;

    @SerializedName("r_estimated_weight_unit")
    private String estimatedWeightUnits;

    @SerializedName("r_move_type_specification")
    private String moveTypeSpecification;

    @SerializedName("customer_comments")
    private String customerComments;

    @SerializedName("r_lead_number")
    private String leadIdNumber;

    @SerializedName("r_est_size")
    private String estimatedSize;

    @SerializedName("r_estimated_weight")
    private String estimatedWeight;

    @SerializedName("r_estimated_hours")
    private String r_estimatedHours;

    @SerializedName("r_additional_info")
    private String additionalInfo;

    @SerializedName("origin_state_name")
    private String originalStateName;

    @SerializedName("dest_state_name")
    private String destinationStateName;

    @SerializedName("origin_country_name")
    private String originalCountryName;

    @SerializedName("dest_country_name")
    private String destinationCountryName;

    @SerializedName("estimate_volume")
    private String estimatVolumeString;

    @SerializedName("estimate_weight")
    private String estimatweightString;

    @SerializedName("hourly_rate")
    private String hourlyRate;

    @SerializedName("start_time")
    private String startTimeString;

    @SerializedName("estimated_hours")
    private String estimatedHours;

    @SerializedName("no_men")
    private String numberOfMen;

    @SerializedName("no_trucks")
    private String numberOfTrucks;

    @SerializedName("move_charge_type")
    private String moveChargePriceType;

    @SerializedName("move_type_id")
    private String moveTypeId;

    @SerializedName("temp_volume")
    private String temp_Volume;

    @SerializedName("temp_weight")
    private String temp_Weight;

    @SerializedName("temp_miles")
    private String temp_Miles;

    @SerializedName("pickup-extra-stop")
    private ArrayList<ExtraStopsPojo> pickupExtraStops;

    @SerializedName("delivery-extra-stop")
    private ArrayList<ExtraStopsPojo> deliveryExtraStops;

    //    @SerializedName("actual_hours")
    private String actualHours;

    //    @SerializedName("actual_travel_time")
    private String actualTravelTime;

    @SerializedName("travel_time")
    private String travelTime;

    @SerializedName("grand_total")
    private String totalAmount = 0.00 + "";

    @SerializedName("has_coupon")
    private String hasCoupon;

    @SerializedName("deposite_ammount")
    private String depositeAmount;

    @SerializedName("default_min_hour")
    private String defaultMinHours;

    @SerializedName("move_charges_rate")
    private String moveChargesRate;

    @SerializedName("valuation_flag")
    private String valuationFlag;

    @SerializedName("releaseform_flag")
    private String releaseFormFlag;

    @SerializedName("storage_flag")
    private String storageFlag;

    @SerializedName("photo_flag")
    private String photoFlag;


    @SerializedName("company_name")
    private String companyName;

    /*@SerializedName("important_notes")
    private String importantNotes;*/

    @SerializedName("terms_conditions")
    private TermsAndConditionPojo termsAndConditionPojo;

    @SerializedName("move_charges")
    private String moveCharges = "0.00";

    @SerializedName("packing_material")
    private String packingMaterialCharges = "0.00";

    @SerializedName("storage")
    private String storageCharges = "0.00";

    @SerializedName("storage_recuring")
    private String storageChargesRecurring = "0.00";

    @SerializedName("additional")
    private String additionalCharges = "0.00";

    @SerializedName("valuation")
    private String valuation = "0.00";

    @SerializedName("crating")
    private String cratingCharges = "0.00";

    @SerializedName("bol_final_charges")
    private BolFinalChargesPojo bolFinalChargesPojo;

    @SerializedName("move_charge_discount_type")
    private int moveChargeDiscountType;

    @SerializedName("move_charge_discount_value")
    private String moveChargeDiscountValue;

    @SerializedName("packing_charge_discount_type")
    private int packingChargeDiscountType;

    @SerializedName("packing_charge_discount_value")
    private String packingChargeDiscountValue;

    @SerializedName("packing_charge_sales_tax")
    private String packingChargeSalesTax;

    @SerializedName("crating_charge_discount_type")
    private int cratingChargeDiscountType;

    @SerializedName("crating_charge_discount_value")
    private String cratingChargeDiscountValue;

    @SerializedName("additional_charge_discount_type")
    private int additionalChargeDiscountType;

    @SerializedName("additional_charge_discount_value")
    private String additionalChargeDiscountValue;

    @SerializedName("storage_charge_discount_type")
    private int storageChargeDiscountType;

    @SerializedName("storage_charge_discount_value")
    private String storageChargeDiscountValue;

    @SerializedName("storage_recuring_charge_discount_type")
    private int storageRecurringChargeDiscountType;

    @SerializedName("storage_recuring_charge_discount_value")
    private String storageRecurringChargeDiscountValue;

    @SerializedName("bottom_line_discount_type")
    private int bottomLineChargeDiscountType;

    @SerializedName("bottom_line_discount_value")
    private String bottomLineChargeDiscountValue;

    @SerializedName("service_tax_type")
    private int serviceTaxPercentageValue;

    @SerializedName("service_tax_value")
    private String serviceTaxValue;

    @SerializedName("creditcard_conv_type")
    private int creditCardConvinienceFeeType;

    @SerializedName("creditcard_conv_value")
    private String creditCardConvinienceFeeValue;

    @SerializedName("clock_status_flag")
    private String clockRequired;

    @SerializedName("clock_activity_flag")
    private String clockActivityFlag;

    @SerializedName("multidate_flag")
    private String multidateJobFlag;

    @SerializedName("first_job_flag")
    private String firstJobOfMultidateFlag;

    @SerializedName("min_hours")
    private String minHours;
    @SerializedName("min_charges")
    private String minCharges;
    @SerializedName("total_charges")
    private String totalCharges;
    @SerializedName("clock_start_flag")
    private boolean clockStartFlag = false;
    @SerializedName("activity_flag")
    private String activityFlag = "1";
    @SerializedName("review_movetype_flag")
    private int reviewMoveTypeFlag = 0;
    @SerializedName("tips_movetype_flag")
    private int tipsMoveTypeFlag = 0;
    @SerializedName("payment_carry_forward")
    private String paymentCarryForward = FALSE;
    @SerializedName("amount_due")
    private String amountDue = "0";
    @SerializedName("bol_sent_for_approval")
    private String bolSentForApproval = "0";
    @SerializedName("hourly_rate_flag")
    private int hourlyRateFlag = 1;
    @SerializedName("estimate_show_flag")
    private int estimateShowFlag = 1;


    public int getHourlyRateFlag() {
        return hourlyRateFlag;
    }

    public void setHourlyRateFlag(int hourlyRateFlag) {
        this.hourlyRateFlag = hourlyRateFlag;
    }

    public String getBolSentForApproval() {
        return bolSentForApproval;
    }

    public void setBolSentForApproval(String bolSentForApproval) {
        this.bolSentForApproval = bolSentForApproval;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public ArrayList<ExtraStopsPojo> getPickupExtraStops() {
        return pickupExtraStops;
    }

    public void setPickupExtraStops(ArrayList<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }

    public void setClockRequired(String clockRequired) {
        this.clockRequired = clockRequired;
    }

    public String getClockActivityFlag() {
        return clockActivityFlag;
    }

    public void setClockActivityFlag(String clockActivityFlag) {
        this.clockActivityFlag = clockActivityFlag;
    }

    public String getMultidateJobFlag() {
        return multidateJobFlag;
    }

    public void setMultidateJobFlag(String multidateJobFlag) {
        this.multidateJobFlag = multidateJobFlag;
    }

    public String getFirstJobOfMultidateFlag() {
        return firstJobOfMultidateFlag;
    }

    public void setFirstJobOfMultidateFlag(String firstJobOfMultidateFlag) {
        this.firstJobOfMultidateFlag = firstJobOfMultidateFlag;
    }

    public String getMinHours() {
        return minHours;
    }

    public void setMinHours(String minHours) {
        this.minHours = minHours;
    }

    public String getMinCharges() {
        return minCharges;
    }

    public void setMinCharges(String minCharges) {
        this.minCharges = minCharges;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMoveProcessJobId() {
        return moveProcessJobId;
    }

    public void setMoveProcessJobId(String moveProcessJobId) {
        this.moveProcessJobId = moveProcessJobId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getNumberOfMen() {
        return numberOfMen;
    }

    public void setNumberOfMen(String numberOfMen) {
        this.numberOfMen = numberOfMen;
    }

    public String getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public void setNumberOfTrucks(String numberOfTrucks) {
        this.numberOfTrucks = numberOfTrucks;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getOriginalCity() {
        return originalCity;
    }

    public void setOriginalCity(String originalCity) {
        this.originalCity = originalCity;
    }

    public String getOriginalState() {
        return originalState;
    }

    public void setOriginalState(String originalState) {
        this.originalState = originalState;
    }

    public String getOriginalCountry() {
        return originalCountry;
    }

    public void setOriginalCountry(String originalCountry) {
        this.originalCountry = originalCountry;
    }

    public String getOriginalZipCode() {
        return originalZipCode;
    }

    public void setOriginalZipCode(String originalZipCode) {
        this.originalZipCode = originalZipCode;
    }

    public String getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(String squareFeet) {
        this.squareFeet = squareFeet;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getEstimatedUnits() {
        return estimatedUnits;
    }

    public void setEstimatedUnits(String estimatedUnits) {
        this.estimatedUnits = estimatedUnits;
    }

    public String getMoveTypeSpecification() {
        return moveTypeSpecification;
    }

    public void setMoveTypeSpecification(String moveTypeSpecification) {
        this.moveTypeSpecification = moveTypeSpecification;
    }

    public String getCustomerComments() {
        return customerComments;
    }

    public void setCustomerComments(String customerComments) {
        this.customerComments = customerComments;
    }

    public String getOriginalStateName() {
        return originalStateName;
    }

    public void setOriginalStateName(String originalStateName) {
        this.originalStateName = originalStateName;
    }

    public String getOriginalCountryName() {
        return originalCountryName;
    }

    public void setOriginalCountryName(String originalCountryName) {
        this.originalCountryName = originalCountryName;
    }

    public String getDestinationCountryName() {
        return destinationCountryName;
    }

    public void setDestinationCountryName(String destinationCountryName) {
        this.destinationCountryName = destinationCountryName;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    /*public String getMoveCharges() {
        return moveCharges==null ? "0.00" : moveCharges;
    }

    public void setMoveCharges(String moveCharges) {
        this.moveCharges = moveCharges;
    }

    public String getPackingMaterialCharges() {
        return packingMaterialCharges==null ? "0.00" : packingMaterialCharges;
    }

    public void setPackingMaterialCharges(String packingMaterialCharges) {
        this.packingMaterialCharges = packingMaterialCharges;
    }

    public String getStorageCharges() {
        return storageCharges==null ? "0.00" : storageCharges;
    }

    public void setStorageCharges(String storageCharges) {
        this.storageCharges = storageCharges;
    }

    public String getAdditionalCharges() {
        return additionalCharges==null ? "0.00" : additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getValuation() {
        return valuation==null ? "0.00" : valuation;
    }

    public void setValuation(String valuation) {
        this.valuation = valuation;
    }*/

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getMoveChargePriceType() {
        return moveChargePriceType;
    }

    public void setMoveChargePriceType(String moveChargePriceType) {
        this.moveChargePriceType = moveChargePriceType;
    }

    public String getDestinationZipCode() {
        return destinationZipCode;
    }

    public void setDestinationZipCode(String destinationZipCode) {
        this.destinationZipCode = destinationZipCode;
    }

    public String getDestinationStateName() {
        return destinationStateName;
    }

    /*public String getDiscount() {
        return discount==null ? "0.00" : discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalAmount() {
        return totalAmount==null ? "0.00" : totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }*/

    public void setDestinationStateName(String destinationStateName) {
        this.destinationStateName = destinationStateName;
    }

    /*public String getDiscountName() {
        return discountName == null ? "" : discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }*/

    public double getMoveChargesRate() {
        return Util.getDoubleFromString(moveChargesRate);
//        return 2.00;
    }

    public void setMoveChargesRate(String moveChargesRate) {
        this.moveChargesRate = moveChargesRate;
    }

    public void setMoveChargesRate(double moveChargesRate) {
        this.moveChargesRate = moveChargesRate + "";
    }

    public boolean getValuationFlag() {
        return valuationFlag.equals("1");
    }

    public void setValuationFlag(String valuationFlag) {
        this.valuationFlag = valuationFlag;
    }

    public boolean getReleaseFormFlag() {
        return releaseFormFlag.equals("1");
    }

    public void setReleaseFormFlag(String releaseFormFlag) {
        this.releaseFormFlag = releaseFormFlag;
    }

    public boolean getStorageFlag() {
        return storageFlag.equals("1");
    }

    public void setStorageFlag(String storageFlag) {
        this.storageFlag = storageFlag;
    }

    public double getMoveCharges() {
        return Util.getDoubleFromString(moveCharges);
    }

    public void setMoveCharges(String moveCharges) {
        this.moveCharges = moveCharges;
    }

    public void setMoveCharges(double moveCharges) {
        this.moveCharges = moveCharges + "";
    }

    public double getMoveChargesDiscounted() {

        return calculateTotalWithDiscount(getMoveCharges(), moveChargeDiscountType, getMoveChargeDiscountValue());
    }

    public double getPackingMaterialCharges() {
        return Util.getDoubleFromString(this.packingMaterialCharges);
    }

    public void setPackingMaterialCharges(String packingMaterialCharges) {
        this.packingMaterialCharges = packingMaterialCharges;
    }

    public void setPackingMaterialCharges(double packingMaterialCharges) {
        this.packingMaterialCharges = packingMaterialCharges + "";
    }

    public double getPackingMaterialChargesDiscounted() {

        double discountedPrice = calculateTotalWithDiscount(getPackingMaterialCharges(), packingChargeDiscountType, getPackingChargeDiscountValue());

        double salesTax = Double.parseDouble(packingChargeSalesTax);

        return discountedPrice + salesTax;
    }

    public double getStorageCharges() {
        return Util.getDoubleFromString(this.storageCharges);
    }

    public void setStorageCharges(String storageCharges) {
        this.storageCharges = storageCharges;
    }

    public void setStorageCharges(double storageCharges) {
        this.storageCharges = storageCharges + "";
    }

    public double getStorageChargesDiscounted() {

        return calculateTotalWithDiscount(getStorageCharges(), storageChargeDiscountType, getStorageChargeDiscountValue());
    }

    public double getAdditionalCharges() {
        return Util.getDoubleFromString(this.additionalCharges);
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges + "";
    }

    public double getAdditionalChargesDiscounted() {

        return calculateTotalWithDiscount(getAdditionalCharges(), additionalChargeDiscountType, getAdditionalChargeDiscountValue());
    }

    public double getValuation() {
        return Util.getDoubleFromString(valuation);
    }

    public void setValuation(String valuation) {
        this.valuation = valuation;
    }

    public void setValuation(double valuation) {
        this.valuation = valuation + "";
    }

   /* public double getDiscount() {
        return Util.getDoubleFromString(this.discount);
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount + "";
    }*/

    public double getDepositeAmount() {
        return Util.getDoubleFromString(this.depositeAmount);
    }

    public void setDepositeAmount(String depositeAmount) {
        this.depositeAmount = depositeAmount;
    }

    public void setDepositeAmount(double depositeAmount) {
        this.depositeAmount = depositeAmount + "";
    }


    public double getDefaultMinHours() {
        return Util.getDoubleFromString(defaultMinHours);
    }

    public void setDefaultMinHours(String defaultMinHours) {
        this.defaultMinHours = defaultMinHours;
    }

    public boolean getHasCoupon() {
        return hasCoupon != null && hasCoupon.equals("1");
    }

    public void setHasCoupon(String hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getEstimatedWeightUnits() {
        return estimatedWeightUnits;
    }

    public void setEstimatedWeightUnits(String estimatedWeightUnits) {
        this.estimatedWeightUnits = estimatedWeightUnits;
    }

    public String getLeadIdNumber() {
        return leadIdNumber;
    }

    public void setLeadIdNumber(String leadIdNumber) {
        this.leadIdNumber = leadIdNumber;
    }

    public String getEstimatedSize() {
        return estimatedSize;
    }

    public void setEstimatedSize(String estimatedSize) {
        this.estimatedSize = estimatedSize;
    }

    public String getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(String estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public String getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(String estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getEstimatVolumeString() {
        return estimatVolumeString;
    }

    public void setEstimatVolumeString(String estimatVolumeString) {
        this.estimatVolumeString = estimatVolumeString;
    }

    public String getEstimatweightString() {
        return estimatweightString;
    }

    public void setEstimatweightString(String estimatweightString) {
        this.estimatweightString = estimatweightString;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }


    public boolean getClockRequired() {
        return TextUtils.equals(clockRequired, Constants.BooleanFlags.TRUE_VALUE);
    }

    public void setClockRequired(boolean clockRequired) {
        if (clockRequired) {
            this.clockRequired = Constants.BooleanFlags.TRUE_VALUE;
        } else {
            this.clockRequired = Constants.BooleanFlags.FALSE_VALUE;
        }
    }



    /*public boolean getSubmitFlag() {
        return submitFlag > 0;
//        return false;
    }

    public void setSubmitFlag(int submitFlag) {
        this.submitFlag = submitFlag;
    }*/

    public String getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(String moveTypeId) {
        this.moveTypeId = moveTypeId;
    }



   /* public double getTotal() {
        double total = getSubTotal();

        double discount = 0;

        if (this.bottomLineChargeDiscountType == Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT) {
            discount = getBottomLineChargeDiscountValue();
        } else *//*if (this.bottomLineChargeDiscountType == Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE)*//* {
            discount = ((getBottomLineChargeDiscountValue() * total) / 100);
        }

        double tax = 0;

        if (getServiceTaxValueType()) {
            tax = getServiceTaxValue();
        } else {
            tax = (getServiceTaxValue() * total) / 100;
        }


        return total + tax - discount;
    }*/

    public String getActualHours() {
        return actualHours;
    }

    public void setActualHours(String actualHours) {
        this.actualHours = actualHours;
    }

    public String getActualTravelTime() {
        return actualTravelTime;
    }

    public void setActualTravelTime(String actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }

    public String getR_estimatedHours() {
        return r_estimatedHours;
    }

    public void setR_estimatedHours(String r_estimatedHours) {
        this.r_estimatedHours = r_estimatedHours;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /*public boolean getDiscountFlag() {
//        return discountFlag ==  1;
        return discountFlag ==  Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
    }

    public void setDiscountFlag(int discountFlag) {
        this.discountFlag = discountFlag;
    }

    public void setDiscountFlag(boolean isPercentage) {
        if(isPercentage){
            this.discountFlag = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE;
        } else {
            this.discountFlag = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
        }

    }*/

    public String getFullOriginalAddressString() {
        ArrayList<String> fullAddress = new ArrayList<>(0);

        if (!TextUtils.isEmpty(originalAddress)) {
            fullAddress.add(originalAddress.trim());
        }

        if (!TextUtils.isEmpty(originalCity)) {
            fullAddress.add(originalCity);
        }

        if (!TextUtils.isEmpty(originalStateName)) {
            fullAddress.add(originalStateName);
        }

        if (!TextUtils.isEmpty(originalZipCode)) {
            fullAddress.add(originalZipCode);
        }
        return TextUtils.join(", ", fullAddress);
    }

    public String getFullDestinationAddressString() {
        ArrayList<String> fullAddress = new ArrayList<>(0);

        if (!TextUtils.isEmpty(destinationAddress)) {
            fullAddress.add(destinationAddress.trim());
        }

        if (!TextUtils.isEmpty(destinationCity)) {
            fullAddress.add(destinationCity);
        }

        if (!TextUtils.isEmpty(destinationStateName)) {
            fullAddress.add(destinationStateName);
        }

        if (!TextUtils.isEmpty(destinationZipCode)) {
            fullAddress.add(destinationZipCode);
        }
        return TextUtils.join(", ", fullAddress);
    }

    public ArrayList<ExtraStopsPojo> getpickupExtraStops() {
        return pickupExtraStops;
    }

    public void setpickupExtraStops(ArrayList<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }

    public ArrayList<ExtraStopsPojo> getDeliveryExtraStops() {
        return deliveryExtraStops;
    }

    public void setDeliveryExtraStops(ArrayList<ExtraStopsPojo> deliveryExtraStops) {
        this.deliveryExtraStops = deliveryExtraStops;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getPicKupExtraStopListSize() {
        if (this.pickupExtraStops == null) {
            return 0;
        }

        return this.pickupExtraStops.size();
    }

    public int getDeliveryExtraStopListSize() {
        if (this.deliveryExtraStops == null) {
            return 0;
        }

        return this.deliveryExtraStops.size();
    }

    public TermsAndConditionPojo getTermsAndConditionPojo() {
        return termsAndConditionPojo;
    }

    public void setTermsAndConditionPojo(TermsAndConditionPojo termsAndConditionPojo) {
        this.termsAndConditionPojo = termsAndConditionPojo;
    }

    /*public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }*/

    public boolean getPhotoFlag() {
        return photoFlag.equals("1");
    }

    public void setPhotoFlag(String photoFlag) {
        this.photoFlag = photoFlag;
    }

    /*public ArrayList<ExtraStopsPojo> getPickupExtraStops() {
        return pickupExtraStops;
    }

    public void setPickupExtraStops(ArrayList<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }*/

    public BolFinalChargesPojo getBolFinalChargesPojo() {
        return bolFinalChargesPojo;
    }

    public void setBolFinalChargesPojo(BolFinalChargesPojo bolFinalChargesPojo) {
        this.bolFinalChargesPojo = bolFinalChargesPojo;
    }


    public int getMoveChargeDiscountType() {
        return moveChargeDiscountType;
    }

    public void setMoveChargeDiscountType(int moveChargeDiscountType) {
        this.moveChargeDiscountType = moveChargeDiscountType;
    }

    public double getMoveChargeDiscountValue() {
        return Util.getDoubleFromString(moveChargeDiscountValue);
    }

    public void setMoveChargeDiscountValue(String moveChargeDiscountValue) {
        this.moveChargeDiscountValue = moveChargeDiscountValue;
    }

    public int getPackingChargeDiscountType() {
        return packingChargeDiscountType;
    }

    public void setPackingChargeDiscountType(int packingChargeDiscountType) {
        this.packingChargeDiscountType = packingChargeDiscountType;
    }

    public double getPackingChargeDiscountValue() {
        return Util.getDoubleFromString(packingChargeDiscountValue);
    }

    public void setPackingChargeDiscountValue(String packingChargeDiscountValue) {
        this.packingChargeDiscountValue = packingChargeDiscountValue;
    }

    public int getCratingChargeDiscountType() {
        return cratingChargeDiscountType;
    }

    public void setCratingChargeDiscountType(int cratingChargeDiscountType) {
        this.cratingChargeDiscountType = cratingChargeDiscountType;
    }

    public double getCratingChargeDiscountValue() {
        return Util.getDoubleFromString(cratingChargeDiscountValue);
    }

    public void setCratingChargeDiscountValue(String cratingChargeDiscountValue) {
        this.cratingChargeDiscountValue = cratingChargeDiscountValue;
    }

    public int getAdditionalChargeDiscountType() {
        return additionalChargeDiscountType;
    }

    public void setAdditionalChargeDiscountType(int additionalChargeDiscountType) {
        this.additionalChargeDiscountType = additionalChargeDiscountType;
    }

    public double getAdditionalChargeDiscountValue() {
        return Util.getDoubleFromString(additionalChargeDiscountValue);
    }

    public void setAdditionalChargeDiscountValue(String additionalChargeDiscountValue) {
        this.additionalChargeDiscountValue = additionalChargeDiscountValue;
    }

    public int getStorageChargeDiscountType() {
        return storageChargeDiscountType;
    }

    public void setStorageChargeDiscountType(int storageChargeDiscountType) {
        this.storageChargeDiscountType = storageChargeDiscountType;
    }

    public double getStorageChargeDiscountValue() {
        return Util.getDoubleFromString(storageChargeDiscountValue);
    }

    public void setStorageChargeDiscountValue(String storageChargeDiscountValue) {
        this.storageChargeDiscountValue = storageChargeDiscountValue;
    }

    public int getStorageRecurringChargeDiscountType() {
        return storageRecurringChargeDiscountType;
    }

    public void setStorageRecurringChargeDiscountType(int storageRecurringChargeDiscountType) {
        this.storageRecurringChargeDiscountType = storageRecurringChargeDiscountType;
    }

    public double getStorageRecurringChargeDiscountValue() {
        return Util.getDoubleFromString(storageRecurringChargeDiscountValue);
    }

    public void setStorageRecurringChargeDiscountValue(String storageRecurringChargeDiscountValue) {
        this.storageRecurringChargeDiscountValue = storageRecurringChargeDiscountValue;
    }

    public boolean getBottomLineChargeDiscountType() {
        return bottomLineChargeDiscountType == Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
    }

    public void setBottomLineChargeDiscountType(int bottomLineChargeDiscountType) {
        this.bottomLineChargeDiscountType = bottomLineChargeDiscountType;
    }

    public void setBottomLineChargeDiscountType(boolean isPercentage) {
        if (isPercentage) {
            this.bottomLineChargeDiscountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE;
        } else {
            this.bottomLineChargeDiscountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
        }
    }

    public double getBottomLineChargeDiscountValue() {
        return Util.getDoubleFromString(this.bottomLineChargeDiscountValue);
    }

    public void setBottomLineChargeDiscountValue(double bottomLineChargeDiscountValue) {
        this.bottomLineChargeDiscountValue = bottomLineChargeDiscountValue + "";
    }

    public void setBottomLineChargeDiscountValue(String bottomLineChargeDiscountValue) {
        this.bottomLineChargeDiscountValue = bottomLineChargeDiscountValue;
    }

    public int getServiceTaxPercentageValue() {
        return serviceTaxPercentageValue;
    }

    public boolean getServiceTaxValueType() {
        return serviceTaxPercentageValue == Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
    }

    public void setServiceTaxPercentageValue(int serviceTaxPercentageValue) {
        this.serviceTaxPercentageValue = serviceTaxPercentageValue;
    }

    public double getServiceTaxValue() {
        return Util.getDoubleFromString(serviceTaxValue);
    }

    public void setServiceTaxValue(String serviceTaxValue) {
        this.serviceTaxValue = serviceTaxValue;
    }


    public double getCratingCharges() {
        return Util.getDoubleFromString(cratingCharges);
    }

    public void setCratingCharges(String cratingCharges) {
        this.cratingCharges = cratingCharges;
    }

    public double getCratingChargesDiscounted() {

        return calculateTotalWithDiscount(getCratingCharges(), cratingChargeDiscountType, getCratingChargeDiscountValue());
    }

    public double getStorageChargesRecurring() {
        return Util.getDoubleFromString(storageChargesRecurring);
    }

    public void setStorageChargesRecurring(String storageChargesRecurring) {
        this.storageChargesRecurring = storageChargesRecurring;
    }

    public double getStorageChargesRecurringDiscounted() {

        return calculateTotalWithDiscount(getStorageChargesRecurring(), storageRecurringChargeDiscountType, getStorageRecurringChargeDiscountValue());
    }

    public double getPackingChargeSalesTax() {
        return Util.getDoubleFromString(packingChargeSalesTax);
    }

    public void setPackingChargeSalesTax(String packingChargeSalesTax) {
        this.packingChargeSalesTax = packingChargeSalesTax;
    }

    public boolean getCreditCardConvinienceFeeType() {
        return creditCardConvinienceFeeType == Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT;
    }

    public void setCreditCardConvinienceFeeType(int creditCardConvinienceFeeType) {
        this.creditCardConvinienceFeeType = creditCardConvinienceFeeType;
    }

    public double getCreditCardConvinienceFeeValue() {
        return Util.getDoubleFromString(creditCardConvinienceFeeValue);
    }

    public void setCreditCardConvinienceFeeValue(String creditCardConvinienceFeeValue) {
        this.creditCardConvinienceFeeValue = creditCardConvinienceFeeValue;
    }


    public double getTemp_Volume() {
        return Util.getDoubleFromString(temp_Volume);
    }

    public void setTemp_Volume(String temp_Volume) {
        this.temp_Volume = temp_Volume;
    }

    public double getTemp_Weight() {
        return Util.getDoubleFromString(temp_Weight);
    }

    public void setTemp_Weight(String temp_Weight) {
        this.temp_Weight = temp_Weight;
    }

    public double getTemp_Miles() {
        return Util.getDoubleFromString(temp_Miles);
    }

    public void setTemp_Miles(String temp_Miles) {
        this.temp_Miles = temp_Miles;
    }

    public String getDiscountString(double discountValue, int discountType, String currencySymbol) {

        if (discountValue == 0.00) {
            return "";
        }

        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            return "discount " + Util.getGeneralFormattedDecimalString(discountValue) + "%";
        }

        return "discount " + currencySymbol + Util.getGeneralFormattedDecimalString(discountValue);
    }

    public String getPackingMaterialSalesString(String currencySymbol) {
        double salesTax = getPackingChargeSalesTax();
        if (salesTax == 0.00) {
            return "";
        }
        String salesTaxToShow = Util.getGeneralFormattedDecimalString(salesTax);
        if (getPackingChargeDiscountValue() == 0.00) {
            return "Sales Tax " + currencySymbol + salesTaxToShow;
        }
        return "\nSales Tax " + currencySymbol + salesTaxToShow;
    }


    public double calculateTotalWithDiscount(double amountToSubtractDiscountFrom, int discountType, double discountValue) {
        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT) {
            return amountToSubtractDiscountFrom - discountValue;
        } else if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            return amountToSubtractDiscountFrom - Util.getFormattedDouble(((discountValue * amountToSubtractDiscountFrom) / 100), Constants.DoubleFormats.FORMAT_FOR_DIGITS);
        }
        return amountToSubtractDiscountFrom;
    }


    public boolean shouldHideValuationAndStorage() {
        if (multidateJobFlag == null || firstJobOfMultidateFlag == null) {
            return false;
        }
        return (multidateJobFlag.equals("1") && !firstJobOfMultidateFlag.equals("1"));
    }

    public boolean isClockStartFlag() {
        return clockStartFlag;
    }

    public void setClockStartFlag(boolean clockStartFlag) {
        this.clockStartFlag = clockStartFlag;
    }

    public String getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }

    public int getReviewMoveTypeFlag() {
        return reviewMoveTypeFlag;
    }

    public void setReviewMoveTypeFlag(int reviewMoveTypeFlag) {
        this.reviewMoveTypeFlag = reviewMoveTypeFlag;
    }

    public int getTipsMoveTypeFlag() {
        return tipsMoveTypeFlag;
    }

    public void setTipsMoveTypeFlag(int tipsMoveTypeFlag) {
        this.tipsMoveTypeFlag = tipsMoveTypeFlag;
    }

    public String getPaymentCarryForward() {
        return paymentCarryForward;
    }

    public void setPaymentCarryForward(String paymentCarryForward) {
        this.paymentCarryForward = paymentCarryForward;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public int getEstimateShowFlag() {
        return estimateShowFlag;
    }

    public void setEstimateShowFlag(int estimateShowFlag) {
        this.estimateShowFlag = estimateShowFlag;
    }
}
