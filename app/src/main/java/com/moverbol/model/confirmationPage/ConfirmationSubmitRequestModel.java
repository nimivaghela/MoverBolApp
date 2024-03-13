package com.moverbol.model.confirmationPage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 9/2/18.
 */

public class ConfirmationSubmitRequestModel {

    @SerializedName("r_id")
    private String id;

    @SerializedName("agree_reschedule")
    private String rescheduleAgreed;

    @SerializedName("agree_policy")
    private String policyAgreed;

    @SerializedName("created_by")
    private String userId;

    @SerializedName("confirmation_signature")
    private String signatureImageBase64String;

    public ConfirmationSubmitRequestModel() {
    }

    public ConfirmationSubmitRequestModel(String id, String rescheduleAgreed, String policyAgreed, String userId, String signatureImageBase64String) {
        this.id = id;
        this.rescheduleAgreed = rescheduleAgreed;
        this.policyAgreed = policyAgreed;
        this.userId = userId;
        this.signatureImageBase64String = signatureImageBase64String;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRescheduleAgreed() {
        return rescheduleAgreed;
    }

    public void setRescheduleAgreed(String rescheduleAgreed) {
        this.rescheduleAgreed = rescheduleAgreed;
    }

    public String getPolicyAgreed() {
        return policyAgreed;
    }

    public void setPolicyAgreed(String policyAgreed) {
        this.policyAgreed = policyAgreed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignatureImageBase64String() {
        return signatureImageBase64String;
    }

    public void setSignatureImageBase64String(String signatureImageBase64String) {
        this.signatureImageBase64String = signatureImageBase64String;
    }
}
