package com.moverbol.model.billOfLading.requestCharges;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.MoveChargesAutoPricingPojo;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;

/**
 * Created by AkashM on 4/4/18.
 */
public class MoveChargesBolRequestPojo {

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

    public void setDataFromMoveChargesManualPricingPojo(MoveChargesManualPricingPojo moveChargesManualPricingPojo) {
        this.description = moveChargesManualPricingPojo.getDescription();
        this.quantity = moveChargesManualPricingPojo.getQuantity();
        this.unit = moveChargesManualPricingPojo.getUnitName();
        this.rate = moveChargesManualPricingPojo.getAddRate();
        this.amount = moveChargesManualPricingPojo.getTotalAmount();
    }

    public void setDataFromMoveChargesAutoPricingPojo(MoveChargesAutoPricingPojo moveChargesAutoPricingPojo) {
        this.description = moveChargesAutoPricingPojo.getDescription();
        this.quantity = moveChargesAutoPricingPojo.getQuantity();
        this.unit = moveChargesAutoPricingPojo.getUnit();
        this.rate = moveChargesAutoPricingPojo.getRate();
        this.amount = moveChargesAutoPricingPojo.getAmount();
    }
}
