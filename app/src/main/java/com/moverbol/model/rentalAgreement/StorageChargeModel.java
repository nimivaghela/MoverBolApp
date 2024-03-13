package com.moverbol.model.rentalAgreement;

import com.google.gson.annotations.SerializedName;
import com.moverbol.util.Util;

public class StorageChargeModel {
    @SerializedName("storage")
    private String storageCharges = "0.00";
    @SerializedName("storage_recuring")
    private String storageChargesRecurring = "0.00";
    @SerializedName("storage_charge_discount_type")
    private int storageChargeDiscountType;
    @SerializedName("storage_charge_discount_value")
    private String storageChargeDiscountValue;
    @SerializedName("storage_recuring_charge_discount_type")
    private int storageRecurringChargeDiscountType;
    @SerializedName("storage_recuring_charge_discount_value")
    private String storageRecurringChargeDiscountValue;

    public double getStorageCharges() {
        return Util.getDoubleFromString(this.storageCharges);
    }

    public void setStorageCharges(String storageCharges) {
        this.storageCharges = storageCharges;
    }

    public double getStorageChargesRecurring() {
        return Util.getDoubleFromString(storageChargesRecurring);
    }

    public void setStorageChargesRecurring(String storageChargesRecurring) {
        this.storageChargesRecurring = storageChargesRecurring;
    }

    public int getStorageChargeDiscountType() {
        return storageChargeDiscountType;
    }

    public void setStorageChargeDiscountType(int storageChargeDiscountType) {
        this.storageChargeDiscountType = storageChargeDiscountType;
    }

    public double getStorageChargeDiscountValue() {
        return Util.getDoubleFromString(storageChargeDiscountValue);
    }

    public void setStorageChargeDiscountValue(String storageChargeDiscountValue) {
        this.storageChargeDiscountValue = storageChargeDiscountValue;
    }

    public int getStorageRecurringChargeDiscountType() {
        return storageRecurringChargeDiscountType;
    }

    public void setStorageRecurringChargeDiscountType(int storageRecurringChargeDiscountType) {
        this.storageRecurringChargeDiscountType = storageRecurringChargeDiscountType;
    }

    public double getStorageRecurringChargeDiscountValue() {
        return Util.getDoubleFromString(storageRecurringChargeDiscountValue);
    }

    public void setStorageRecurringChargeDiscountValue(String storageRecurringChargeDiscountValue) {
        this.storageRecurringChargeDiscountValue = storageRecurringChargeDiscountValue;
    }

    public String displayDiscountString(double discountValue, int discountType) {
        if (discountValue == 0.00) {
            return "";
        }
        if (discountType == 2) {
            return "discount " + discountValue + "%";
        }
        return "discount " + "$" + discountValue;
    }


}
