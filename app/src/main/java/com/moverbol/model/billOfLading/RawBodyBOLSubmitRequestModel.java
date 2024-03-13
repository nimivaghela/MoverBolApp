package com.moverbol.model.billOfLading;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 17/4/18.
 */
public class RawBodyBOLSubmitRequestModel <T>{

    @Expose
    @SerializedName("subdomain")
    private String subdomain;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("confirmationDetails")
    private T confirmationDetails;


    public RawBodyBOLSubmitRequestModel(String subdomain, String opportunityId, String userId, String jobId, T confirmationDetails) {
        this.subdomain = subdomain;
        this.opportunityId = opportunityId;
        this.userId = userId;
        this.confirmationDetails = confirmationDetails;
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

    public T getConfirmationDetails() {
        return confirmationDetails;
    }

    public void setConfirmationDetails(T confirmationDetails) {
        this.confirmationDetails = confirmationDetails;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
