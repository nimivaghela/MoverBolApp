package com.moverbol.model.billOfLading.requestCharges;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.confirmationPage.AdditionalChargesPojo;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;

/**
 * Created by AkashM on 4/4/18.
 */
public class ValuationChargesBolRequestPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("bol_finalcharge_id")
    private String bolFinalChargeId;

    @SerializedName("description")
    private String description;

    @SerializedName("unit")
    private String unit;

    @SerializedName("rate")
    private String rate;

    @SerializedName("valuation_type")
    private String valuationType;

    @SerializedName("valuation_value")
    private String valuationValue;

    @SerializedName("amount")
    private String amount;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBolFinalChargeId() {
        return bolFinalChargeId;
    }

    public void setBolFinalChargeId(String bolFinalChargeId) {
        this.bolFinalChargeId = bolFinalChargeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValuationType() {
        return valuationType;
    }

    public void setValuationType(String valuationType) {
        this.valuationType = valuationType;
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

    public String getValuationValue() {
        return valuationValue;
    }

    public void setValuationValue(String valuationValue) {
        this.valuationValue = valuationValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public void setDataFromValuationChargesPojo(ValuationChargesPojo valuationChargesPojo) {
        this.description = valuationChargesPojo.getDescription();
//        this.valuationType = valuationChargesPojo.get;
        this.unit = valuationChargesPojo.getQuoteUnit();
        this.rate = valuationChargesPojo.getRate();
//        this.valuationValue = ;
        this.amount = valuationChargesPojo.getTotalAmount();
    }
}
