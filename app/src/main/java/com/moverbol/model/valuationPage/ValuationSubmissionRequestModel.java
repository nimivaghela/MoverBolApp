package com.moverbol.model.valuationPage;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

/**
 * Created by AkashM on 31/1/18.
 */

public class ValuationSubmissionRequestModel {

    @SerializedName("r_id")
    private String id;

    @SerializedName("valuation_settings_id")
    private String valuationSettingsId;

    @SerializedName("declared_amount")
    private String declaredAmount;

    @SerializedName("valuation_rate")
    private String valuationRate;

    @SerializedName("valuation_unit")
    private String valuationUnit;

    @SerializedName("created_by")
    private String userId;

    @SerializedName("valuation_signature")
    private String signatureBitmapBase64String;

    @SerializedName("description")
    private String description;

    @SerializedName("label")
    private String label;

    @SerializedName("valuation_charges_calculated")
    private CommonChargesRequestModel valuationChargesCalculated;

    public ValuationSubmissionRequestModel() {
    }

    public ValuationSubmissionRequestModel(String id, String valuationSettingsId, String declaredAmount, String valuationRate, String valuationUnit, String userId, String signatureBitmapBase64String, CommonChargesRequestModel valuationChargesCalculated) {
        this.id = id;
        this.valuationSettingsId = valuationSettingsId;
        this.declaredAmount = declaredAmount;
        this.valuationRate = valuationRate;
        this.valuationUnit = valuationUnit;
        this.userId = userId;
        this.signatureBitmapBase64String = signatureBitmapBase64String;
        this.valuationChargesCalculated = valuationChargesCalculated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValuationSettingsId() {
        return valuationSettingsId;
    }

    public void setValuationSettingsId(String valuationSettingsId) {
        this.valuationSettingsId = valuationSettingsId;
    }

    public String getDeclaredAmount() {
        return declaredAmount;
    }

    public void setDeclaredAmount(String declaredAmount) {
        this.declaredAmount = declaredAmount;
    }

    public String getValuationRate() {
        return valuationRate;
    }

    public void setValuationRate(String valuationRate) {
        this.valuationRate = valuationRate;
    }

    public String getValuationUnit() {
        return valuationUnit;
    }

    public void setValuationUnit(String valuationUnit) {
        this.valuationUnit = valuationUnit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignatureBitmapBase64String() {
        return signatureBitmapBase64String;
    }

    public void setSignatureBitmapBase64String(String signatureBitmapBase64String) {
        this.signatureBitmapBase64String = signatureBitmapBase64String;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CommonChargesRequestModel getValuationChargesCalculated() {
        return valuationChargesCalculated;
    }

    public void setValuationChargesCalculated(CommonChargesRequestModel valuationChargesCalculated) {
        this.valuationChargesCalculated = valuationChargesCalculated;
    }
}
