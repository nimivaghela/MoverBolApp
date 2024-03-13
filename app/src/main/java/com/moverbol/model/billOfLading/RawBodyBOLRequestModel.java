package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

public class RawBodyBOLRequestModel {

    @SerializedName("subdomain")
    private String subdomain;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("move_charges_calculated")
    private CommonChargesRequestModel moveChargesCalculated;

    @SerializedName("valuation_charges_calculated")
    private CommonChargesRequestModel valuationChargesCalculated;

    public RawBodyBOLRequestModel(String subdomain, String opportunityId, String jobId, String userId,
                                  CommonChargesRequestModel moveChargesCalculated,
                                  CommonChargesRequestModel valuationChargesCalculated) {
        this.subdomain = subdomain;
        this.opportunityId = opportunityId;
        this.jobId = jobId;
        this.userId = userId;
        this.moveChargesCalculated = moveChargesCalculated;
        this.valuationChargesCalculated = valuationChargesCalculated;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CommonChargesRequestModel getMoveChargesCalculated() {
        return moveChargesCalculated;
    }

    public void setMoveChargesCalculated(CommonChargesRequestModel moveChargesCalculated) {
        this.moveChargesCalculated = moveChargesCalculated;
    }

    public CommonChargesRequestModel getValuationChargesCalculated() {
        return valuationChargesCalculated;
    }

    public void setValuationChargesCalculated(CommonChargesRequestModel valuationChargesCalculated) {
        this.valuationChargesCalculated = valuationChargesCalculated;
    }
}
