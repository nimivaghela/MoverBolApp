package com.moverbol.model.billOfLading;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 30/4/18.
 */
public class RatingDiscountPercentagePojo {

    @SerializedName("discount_percentage")
    private String discountPercentage;

    public RatingDiscountPercentagePojo() {
    }

    public RatingDiscountPercentagePojo(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountPercentage() {
        return (discountPercentage == null) ? "" : discountPercentage ;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentageDoubleValue() {
        return Util.getDoubleFromString(getDiscountPercentage());
    }

    @Override
    public String toString() {
        if(!TextUtils.isEmpty(getDiscountPercentage()) && TextUtils.isDigitsOnly(getDiscountPercentage())){
            return getDiscountPercentage() + "%";
        }

        return getDiscountPercentage();
    }
}
