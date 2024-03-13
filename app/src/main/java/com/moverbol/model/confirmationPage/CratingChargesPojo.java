package com.moverbol.model.confirmationPage;


import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 09/5/18.
 */

public class CratingChargesPojo {

    @SerializedName("name")
    private String name;
    
    @SerializedName("quantity")
    private String quantity;

    @SerializedName("rate")
    private String rate;

    @SerializedName("total")
    private String total;

    @SerializedName("unit")
    private String unit;

    @SerializedName("unit_name")
    private String unit_name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
