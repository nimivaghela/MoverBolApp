package com.moverbol.model.billOfLading.newRequestChargesMoleds;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 12/4/18.
 */
public class CommonChargesRequestModel implements Parcelable {

    @SerializedName("r_id")
    private String id;

    @SerializedName("bol_finalcharge_id")
    private String bolFinalchargeId;

    @SerializedName("description")
    private String description;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("unit_name")
    private String unit;

    @SerializedName("unit")
    private String unitId;

    @SerializedName("rate")
    private String rate;

    @SerializedName("amount")
    private String amount;

    @SerializedName("days") //Only needed for storage charges
    private String days;

    @SerializedName("created_by") //Only needed for storage recurring charges
    private String createdByUserId;

    @SerializedName("created_date") //Only needed for storage recurring charges and crating charges
    private String createdDate;

    @SerializedName("recurring_type") //Only needed for storage recurring charges
    private String recurringTypeIndex;

    @SerializedName("billing_calculation")//Only needed for storage recurring charges
    private String billingCalculationTypeIndex = "";

    private String worker;
    private String truck;

    @SerializedName("c_id")
    private int cId;

    @SerializedName("select_rate")
    private String selectRate;


    public static final Creator<CommonChargesRequestModel> CREATOR = new Creator<CommonChargesRequestModel>() {
        @Override
        public CommonChargesRequestModel createFromParcel(Parcel in) {
            return new CommonChargesRequestModel(in);
        }

        @Override
        public CommonChargesRequestModel[] newArray(int size) {
            return new CommonChargesRequestModel[size];
        }
    };


    private transient boolean showEditOption;
    private String taxable = "0";

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bolFinalchargeId);
        dest.writeString(description);
        dest.writeString(quantity);
        dest.writeString(unit);
        dest.writeString(unitId);
        dest.writeString(rate);
        dest.writeString(amount);
        dest.writeString(days);
        dest.writeString(createdByUserId);
        dest.writeString(createdDate);
        dest.writeString(recurringTypeIndex);
        dest.writeString(billingCalculationTypeIndex);
        dest.writeString(worker);
        dest.writeString(truck);
        dest.writeInt(cId);
        dest.writeString(selectRate);
        dest.writeString(taxable);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBolFinalchargeId() {
        return bolFinalchargeId;
    }

    public void setBolFinalchargeId(String bolFinalchargeId) {
        this.bolFinalchargeId = bolFinalchargeId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return Util.getGeneralFormattedDecimalString(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRecurringTypeIndex() {
        return recurringTypeIndex;
    }

    public void setRecurringTypeIndex(String recurringTypeIndex) {
        this.recurringTypeIndex = recurringTypeIndex;
    }

    public String getBillingCalculationTypeIndex() {
        if (billingCalculationTypeIndex != null) {
            return billingCalculationTypeIndex;
        } else {
            return "";
        }

    }

    public String getDays() {
        if (!getBillingCalculationTypeIndex().equals("1")) {
            return "";
        }
        return days;
    }


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setBillingCalculationTypeIndex(String billingCalculationTypeIndex) {
        this.billingCalculationTypeIndex = billingCalculationTypeIndex;
    }

    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }

    public String getBillingCalculationTypeIndexText() {
        if (billingCalculationTypeIndex != null) {
            if (billingCalculationTypeIndex.equals("1")) {
                return "Daily";
            } else {
                return "Monthly";
            }
        }
        return "";
    }


    protected CommonChargesRequestModel(Parcel in) {
        this.id = in.readString();
        this.bolFinalchargeId = in.readString();
        this.description = in.readString();
        this.quantity = in.readString();
        this.unit = in.readString();
        this.unitId = in.readString();
        this.rate = in.readString();
        this.amount = in.readString();
        this.days = in.readString();
        this.createdByUserId = in.readString();
        this.createdDate = in.readString();
        this.recurringTypeIndex = in.readString();
        this.billingCalculationTypeIndex = in.readString();
        this.worker = in.readString();
        this.truck = in.readString();
        this.selectRate = in.readString();
        this.cId = in.readInt();
    }

    public void setFieldsForValuationCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, boolean showEditOption) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = null;
        this.billingCalculationTypeIndex = null;
        this.recurringTypeIndex = null;
        this.createdByUserId = null;
        this.createdDate = null;
    }

    public void setFieldsForMoveCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, boolean showEditOption, int cId, String selectRate) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = null;
        this.billingCalculationTypeIndex = null;
        this.recurringTypeIndex = null;
        this.createdByUserId = null;
        this.createdDate = null;
        this.cId = cId;
        this.selectRate = selectRate;
    }

    public CommonChargesRequestModel() {
    }

    public void setFieldsForPackingCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, boolean showEditOption, int cId, String selectRate, String taxable) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = null;
        this.billingCalculationTypeIndex = null;
        this.recurringTypeIndex = null;
        this.createdByUserId = null;
        this.createdDate = null;
        this.cId = cId;
        this.selectRate = selectRate;
        this.taxable = taxable;
    }

    public void setFieldsForAdditionalCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, boolean showEditOption, int cId, String selectRate) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = null;
        this.billingCalculationTypeIndex = null;
        this.recurringTypeIndex = null;
        this.createdByUserId = null;
        this.createdDate = null;
        this.cId = cId;
        this.selectRate = selectRate;
    }

    public void setFieldsForStorageCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, String days, boolean showEditOption, int cId, String selectRate) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = days;
        this.billingCalculationTypeIndex = "0";
        this.recurringTypeIndex = "0";
        this.createdByUserId = null;
        this.createdDate = null;
        this.cId = cId;
        this.selectRate = selectRate;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public void setFieldsForStorageRecurringCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, String days, String billing, boolean showEditOption, int cId, String selectRate) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = days;
        this.billingCalculationTypeIndex = billing;
        this.recurringTypeIndex = "1";
        this.createdByUserId = null;
        this.createdDate = null;
        this.cId = cId;
        this.selectRate = selectRate;
    }

    public void setFieldsForCratingCharges(String id, String bolFinalchargeId, String description, String quantity, String unit, String unitId, String rate, String amount, String createdDate, boolean showEditOption, int cId, String selectRate) {
        this.id = id;
        this.bolFinalchargeId = bolFinalchargeId;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.unitId = unitId;
        this.rate = rate;
        this.amount = amount;
        this.showEditOption = showEditOption;
        this.days = null;
        this.billingCalculationTypeIndex = null;
        this.recurringTypeIndex = null;
        this.createdByUserId = null;
        this.createdDate = createdDate;
        this.cId = cId;
        this.selectRate = selectRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSelectRate() {
        return selectRate;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public void setSelectRate(String selectRate) {
        this.selectRate = selectRate;
    }


}
