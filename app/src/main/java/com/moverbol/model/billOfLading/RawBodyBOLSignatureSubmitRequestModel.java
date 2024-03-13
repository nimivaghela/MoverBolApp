package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

public class RawBodyBOLSignatureSubmitRequestModel {
    @SerializedName("subdomain")
    private String subdomain;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("bol_finalcharge_id")
    private String bolFinalChargeId;

    @SerializedName("bol_signature")
    private String bolSignature;

    /*public RawBodyBOLSignatureSubmitRequestModel() {
    }
*/
    public RawBodyBOLSignatureSubmitRequestModel(String subdomain, String opportunityId, String userId, String jobId, String bolFinalChargeId, String bolSignature) {
        this.subdomain = subdomain;
        this.opportunityId = opportunityId;
        this.userId = userId;
        this.bolFinalChargeId = bolFinalChargeId;
        this.bolSignature = bolSignature;
        this.jobId = jobId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBolFinalChargeId() {
        return bolFinalChargeId;
    }

    public void setBolFinalChargeId(String bolFinalChargeId) {
        this.bolFinalChargeId = bolFinalChargeId;
    }

    public String getBolSignature() {
        return bolSignature;
    }

    public void setBolSignature(String bolSignature) {
        this.bolSignature = bolSignature;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
