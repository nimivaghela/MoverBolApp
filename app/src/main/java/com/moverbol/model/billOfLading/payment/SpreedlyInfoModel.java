package com.moverbol.model.billOfLading.payment;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.billOfLading.requestCharges.TipDetailsPojo;

import java.util.ArrayList;

public class SpreedlyInfoModel {

    @SerializedName("screct_key")
    private String environmentKey;

    @SerializedName("access_key")
    private String access_key;

    @SerializedName("payment_method_token")
    private String paymentMethodToken;

    @SerializedName("payment_history")
    private ArrayList<PaymentHistoryResponseModel> paymentHistoryList;

    @SerializedName("tips")
    private TipDetailsPojo tipDetailsPojo;

    @SerializedName("opportunityDetails")
    private OpportunityDetails opportunityDetails;

    public OpportunityDetails getOpportunityDetails() {
        return opportunityDetails;
    }

    public void setOpportunityDetails(OpportunityDetails opportunityDetails) {
        this.opportunityDetails = opportunityDetails;
    }

    public String getEnvironmentKey() {
        return environmentKey;
    }

    public void setEnvironmentKey(String environmentKey) {
        this.environmentKey = environmentKey;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getPaymentMethodToken() {
        return paymentMethodToken;
    }

    public void setPaymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
    }

    public ArrayList<PaymentHistoryResponseModel> getPaymentHistoryList() {
        return paymentHistoryList;
    }

    public void setPaymentHistoryList(ArrayList<PaymentHistoryResponseModel> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
    }

    public TipDetailsPojo getTipDetailsPojo() {
        return tipDetailsPojo;
    }

    public void setTipDetailsPojo(TipDetailsPojo tipDetailsPojo) {
        this.tipDetailsPojo = tipDetailsPojo;
    }
}
