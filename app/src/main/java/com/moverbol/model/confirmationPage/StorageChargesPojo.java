package com.moverbol.model.confirmationPage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 23/1/18.
 */

public class StorageChargesPojo {

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


    @Override
    public String toString() {
        return "StorageChargesPojo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", rate='" + rate + '\'' +
                ", quantity='" + quantity + '\'' +
                ", days='" + days + '\'' +
                ", total='" + total + '\'' +
                ", unitName='" + unitName + '\'' +
                ", showEditOption=" + showEditOption +
                '}';
    }
}
