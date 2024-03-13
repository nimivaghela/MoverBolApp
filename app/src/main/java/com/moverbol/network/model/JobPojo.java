package com.moverbol.network.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.model.ActivityDatePojo;
import com.moverbol.util.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AkashM on 23/11/17.
 */
public class JobPojo {

    /*autoGenerate = true*/
    public int id;

    public int userId;

    @SerializedName("r_man_power_id")
    public String manPowerId;

    @SerializedName("job_id")
    public String jobId;

    @SerializedName("job_sub_id")
    public String jobSubId;

    @SerializedName("r_date")
    public String date;

    @SerializedName("start_time")///----------------------------
    public String startTimeFrom;

    @SerializedName("estimated_hours")
    public String estimatedHours;

    @SerializedName("no_men")
    public String numberOfMen;

    @SerializedName("no_trucks")
    public String numberOfTrucks;

    @SerializedName("opportunity_id")
    public String opportunityId;

    @SerializedName("opp_opportunity_name")
    public String opportunityName;

    @SerializedName("r_email")
    public String email;

    @SerializedName("r_lead_number")
    public String leadNumber;

    @SerializedName("r_est_size")
    public String estimatedSize;

    @SerializedName("r_estimated_weight")
    public String estimatedWeight;

    @SerializedName("r_phone1")
    public String phoneNumber1;

    @SerializedName("r_phone2")
    public String phoneNumber2;

    @SerializedName("r_phone3")
    public String phoneNumber3;

    @SerializedName("origin_address")
    public String originalAddress;

    @SerializedName("destination_address")
    public String destinationAddress;

    @SerializedName("r_dest_state")
    public String destinationState;

    @SerializedName("r_dest_country")
    public String destinationCountry;

    @SerializedName("r_dest_city")
    public String destinationCity;

    @SerializedName("r_origin_city")
    public String originalCity;

    @SerializedName("r_origin_state")
    public String originalState;

    @SerializedName("r_origin_country")
    public String originalCountry;

    @SerializedName("r_origin_zip_code")//new added
    public String originalZipCode;

    @SerializedName("r_dest_zip_code")//new added
    public String destinationZipCode;

    @SerializedName("square_feet")
    public String squareFeet;

    @SerializedName("packing")
    public String packing;

    @SerializedName("r_estimated_units")
    public String estimatedUnits;

    @SerializedName("r_estimated_weight_unit")
    public String estimatedWeightUnit;

    @SerializedName("unpacking")
    public String unpacking;

    @SerializedName("r_customer_feedback")
    public String customerFeedback;

    @SerializedName("r_area_code5")
    public String areaCode5;

    @SerializedName("r_move_type_specification")
    public String moveTypeSpecification;

    @SerializedName("job_status")
    public String jobStatus;

    @SerializedName("bedroom")
    public String numberOfbedrooms;

    @SerializedName("origin_state_name")
    public String originalStateName;

    @SerializedName("dest_state_name")
    public String destinationStateName;

    @SerializedName("origin_country_name")
    public String originalCountryName;

    @SerializedName("dest_country_name")
    public String destinationCountryName;

    @SerializedName("estimate_volume")
    public String estimatedVolumeString;

    @SerializedName("estimate_weight")
    public String estimatedWeightString;

    @SerializedName("hourly_rate")
    public String estimatedHourRateString;

    @SerializedName("origin_additional")
    public String originAdditional;

    @SerializedName("destination_additional")
    public String destinationAdditional;

    @SerializedName("storage")
    public String storage;

    @SerializedName("move_type_id")
    public String moveTypeId;

    @SerializedName("move_type_name")
    public String moveTypeName;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("date_additional")
    public List<ActivityDatePojo> additionalDates;

    @SerializedName("min_hours")
    public String minHours;

    @SerializedName("min_charges")
    public String minCharges;

    @SerializedName("total_charges")
    public String totalCharges = "0";

    @SerializedName("hourly_rate_flag")
    public int hourlyRateflag = 1;

    @SerializedName("estimate_show_flag")
    private int estimateShowFlag = 1;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("account_type_flag")
    private int accountTypeFlag = 2;

    public int getHourlyRateflag() {
        return hourlyRateflag;
    }

    public void setHourlyRateflag(int hourlyRateflag) {
        this.hourlyRateflag = hourlyRateflag;
    }

    public String getFormattedDate() {
        return Util.getFormattedDate(this.date, Constants.INPUT_DATE_FORMAT_JOBS, Constants.OUTPUT_DATE_FORMAT_JOBS);

    }

    public Date getDateObject() {
        try {
            return Util.getDateObjectFromStringDate(this.date, Constants.INPUT_DATE_FORMAT_JOBS);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFirstStartTime() {
        try {
            String stringToReturn = this.startTimeFrom.substring(0, this.startTimeFrom.toLowerCase().indexOf("m") - 1).trim();
            Log.d(Constants.BASE_LOG_TAG + "hour", stringToReturn);
            return stringToReturn;
        } catch (Exception e) {
            return null;
        }

    }

    public String getStartTimeAmPm() {
        try {
            String stringToReturn = this.startTimeFrom.substring(this.startTimeFrom.toLowerCase().indexOf("m") - 1, this.startTimeFrom.toLowerCase().indexOf("m") + 1).trim();
            Log.d(Constants.BASE_LOG_TAG + "hour", stringToReturn);
            return stringToReturn;
        } catch (Exception e) {
            return null;
        }
    }

    public int getStartTimeHourIn24Format() {
        String firstStartTime = getFirstStartTime();
        String amPm = getStartTimeAmPm();
        int startTimeHourIn24Format = 0;

        if (firstStartTime != null) {
            try {
                firstStartTime = firstStartTime.substring(0, firstStartTime.indexOf(':'));
                startTimeHourIn24Format = Integer.parseInt(firstStartTime);
            } catch (Exception e) {
                startTimeHourIn24Format = 0;
            }

        }

        if (amPm != null) {
            if (amPm.equalsIgnoreCase("pm") && startTimeHourIn24Format != 12) {
                startTimeHourIn24Format = startTimeHourIn24Format + 12;
            }
        }

        return startTimeHourIn24Format;
    }

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

    public boolean getPaymentStatus() {
        if (paymentStatus == null) {
            return false;
        }
        return paymentStatus.equals("1");
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getEstimateShowFlag() {
        return estimateShowFlag;
    }

    public void setEstimateShowFlag(int estimateShowFlag) {
        this.estimateShowFlag = estimateShowFlag;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountTypeFlag() {
        return accountTypeFlag;
    }

    public void setAccountTypeFlag(int accountTypeFlag) {
        this.accountTypeFlag = accountTypeFlag;
    }

    public String displayCustomerName() {
        if (accountTypeFlag == 1) {
            return accountName + "\n" + opportunityName;
        } else {
            return opportunityName;
        }
    }
}
