package com.moverbol.model.rentalAgreement;

import com.google.gson.annotations.SerializedName;

public class RentalAgreementResponseModel {
    @SerializedName("agreement")
    private RentalAgreementPojo agreement;
    @SerializedName("charges")
    private StorageChargeModel charges;

    public RentalAgreementPojo getAgreement() {
        return agreement;
    }

    public void setAgreement(RentalAgreementPojo agreement) {
        this.agreement = agreement;
    }

    public StorageChargeModel getCharges() {
        return charges;
    }

    public void setCharges(StorageChargeModel charges) {
        this.charges = charges;
    }
}
