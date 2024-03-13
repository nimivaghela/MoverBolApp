package com.moverbol.model.billOfLading.newRequestChargesMoleds;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 11/4/18.
 */
public class RawChargesSaveRequestModel <T>{

    @SerializedName("subdomain")
    private String subDomain;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("model")
    private String modelName;

    @SerializedName("chargeDetails")
    private List<T> chargeDetailList;


    public RawChargesSaveRequestModel() {
    }

    public RawChargesSaveRequestModel(String subDomain, String opportunityId, String userId, String modelName, String jobId, List<T> chargeDetailList) {
        this.subDomain = subDomain;
        this.opportunityId = opportunityId;
        this.userId = userId;
        this.modelName = modelName;
        this.chargeDetailList = chargeDetailList;
        this.jobId = jobId;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<T> getChargeDetailList() {
        return chargeDetailList;
    }

    public void setChargeDetailList(List<T> chargeDetailList) {
        this.chargeDetailList = chargeDetailList;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
