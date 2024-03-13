package com.moverbol.model.confirmationPage.artialeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 30/1/18.
 */

public class RawBodyRequestModel <T>{

    @Expose
    @SerializedName("subdomain")
    private String subdomain;

    @Expose
    @SerializedName("opportunity_id")
    private String opportunityId;

    @Expose
    @SerializedName("user_id")
    private String userId;

    @Expose
    @SerializedName("job_id")
    private String jobId;

    @Expose
    @SerializedName("inputdetails")
    private T inputDetails;

    @Expose
    @SerializedName("releaseform_signature")
    private String signature;


    public RawBodyRequestModel() {
    }

    public RawBodyRequestModel(String subdomain, String userId, String opportunityId, String jobId, T inputDetails) {
        this.subdomain = subdomain;
        this.opportunityId = opportunityId;
        this.inputDetails = inputDetails;
        this.userId = userId;
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

    public T getInputDetails() {
        return inputDetails;
    }

    public void setInputDetails(T inputDetails) {
        this.inputDetails = inputDetails;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
