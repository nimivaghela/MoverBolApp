package com.moverbol.model.notes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 7/3/18.
 */

public class RawNotesRequest {

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
    @SerializedName("description")
    private String description;


    public RawNotesRequest() {
    }

    public RawNotesRequest(String subdomain, String opportunityId, String userId, String jobId, String description) {
        this.subdomain = subdomain;
        this.opportunityId = opportunityId;
        this.userId = userId;
        this.jobId = jobId;
        this.description = description;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
