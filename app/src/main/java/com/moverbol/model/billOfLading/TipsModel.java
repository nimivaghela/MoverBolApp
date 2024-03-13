package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 07/6/18.
 */

public class TipsModel {

    @SerializedName("tip_percentage")
    private String tipPercentage;

    public String getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(String tipPercentage) {
        this.tipPercentage = tipPercentage;
    }
}
