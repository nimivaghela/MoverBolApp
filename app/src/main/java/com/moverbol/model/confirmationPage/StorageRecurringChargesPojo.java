package com.moverbol.model.confirmationPage;

import com.google.gson.annotations.SerializedName;

public class StorageRecurringChargesPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("unit")
    private String unit;

    @SerializedName("rate")
    private String rate;

    @SerializedName("qty")
    private String quantity;

    @SerializedName("days")
    private String days;

    @SerializedName("total")
    private String total;

    @SerializedName("unit_name")
    private String unitName;

    @SerializedName("recurring_type")
    private String recurringTypeIndex;

    @SerializedName("billing_calculation")
    private String billingTypeIndex;

    private boolean showEditOption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isShowEditOption() {
        return showEditOption;
    }

    public void setShowEditOption(boolean showEditOption) {
        this.showEditOption = showEditOption;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRecurringTypeIndex() {
        return recurringTypeIndex;
    }

    public void setRecurringTypeIndex(String recurringTypeIndex) {
        this.recurringTypeIndex = recurringTypeIndex;
    }

    public String getBillingTypeIndex() {
        return billingTypeIndex;
    }

    public String getBillingTypeIndexText() {
        if(billingTypeIndex==null){
            return "";
        }

        if(billingTypeIndex.equals("1")){
            return "Daily";
        } else if(billingTypeIndex.equals("2")){
            return "Monthly";
        }
        return billingTypeIndex;
    }

    public void setBillingTypeIndex(String billingTypeIndex) {
        this.billingTypeIndex = billingTypeIndex;
    }
}
