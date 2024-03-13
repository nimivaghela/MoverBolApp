package com.moverbol.model.billOfLading.requestCharges;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.StorageChargesPojo;

/**
 * Created by AkashM on 4/4/18.
 */
public class StorageChargesBolRequestPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("bol_finalcharge_id")
    private String bolFinalchargeId;

    @SerializedName("description")
    private String description;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("unit")
    private String unit;

    @SerializedName("rate")
    private String rate;

    @SerializedName("amount")
    private String amount;

    @SerializedName("days")
    private String days;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
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

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }


    public void setDataFromStorageChargesPojo(StorageChargesPojo storageChargesPojo) {
        this.description = storageChargesPojo.getDescription();
        this.quantity = storageChargesPojo.getQuantity();
        this.unit = storageChargesPojo.getUnit();
        this.rate = storageChargesPojo.getRate();
        this.amount = storageChargesPojo.getTotal();
        this.days = storageChargesPojo.getDays();
    }
}
