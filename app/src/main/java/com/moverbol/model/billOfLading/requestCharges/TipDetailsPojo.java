package com.moverbol.model.billOfLading.requestCharges;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

public class TipDetailsPojo {

    @SerializedName("tips_discount_type")
    private String tipDiscountType;

    @SerializedName("tips_discount_amount")
    private String tipDiscountAmount;

    @SerializedName("tips_flag")
    private String tipFlag;



    public boolean isTipPercentage() {
        if(tipDiscountType == null){
            return false;
        }
        return tipDiscountType.equals(Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "");
    }

    public void setIsTipPercentage(boolean isTipPercentage) {
        if(isTipPercentage) {
            tipDiscountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            tipDiscountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }
    }

    public void setTipDiscountType(String tipDiscountType) {
        this.tipDiscountType = tipDiscountType;
    }

    public Double getTipDiscountAmount() {
        return Util.getDoubleFromString(tipDiscountAmount);
    }

    public void setTipDiscountAmount(String tipDiscountAmount) {
        this.tipDiscountAmount = tipDiscountAmount;
    }

    public String getTipFlag() {
        return tipFlag;
    }

    public void setTipFlag(String tipFlag) {
        this.tipFlag = tipFlag;
    }
}
