package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 22/12/17.
 */

public class RateTypePojo {

    @SerializedName("1")
    private String unitRate;

    @SerializedName("2")
    private String packRate;

    @SerializedName("3")
    private String unpackRate;

    public String getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(String unitRate) {
        this.unitRate = unitRate;
    }

    public String getPackRate() {
        return packRate;
    }

    public void setPackRate(String packRate) {
        this.packRate = packRate;
    }

    public String getUnpackRate() {
        return unpackRate;
    }

    public void setUnpackRate(String unpackRate) {
        this.unpackRate = unpackRate;
    }
}
