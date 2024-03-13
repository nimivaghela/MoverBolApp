package com.moverbol.model.billOfLading.requestCharges;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.PackingChargesPojo;

/**
 * Created by AkashM on 4/4/18.
 */
public class PackingChargesBolRequestPojo {

    @SerializedName("r_id")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDataFromPackingChargesPojo(PackingChargesPojo packingChargesPojo) {
        this.description = packingChargesPojo.getPackDescription();
        this.quantity = packingChargesPojo.getPackQuantity();
        this.unit = packingChargesPojo.getRateTypeName();
        this.rate = packingChargesPojo.getQuoteRate();
        this.amount = packingChargesPojo.getPackTotal();
    }
}
