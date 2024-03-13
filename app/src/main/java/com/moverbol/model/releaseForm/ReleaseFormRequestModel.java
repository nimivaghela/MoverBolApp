package com.moverbol.model.releaseForm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 7/2/18.
 */

public class ReleaseFormRequestModel {

    @SerializedName("r_id")
    private String selectionId;

    @SerializedName("releaseform_id")
    private String releaseFormId;

    @SerializedName("address_null")
    private String address;

    @SerializedName("address")
    private List<String> addressList;

    @SerializedName("description")
    private String description;

    /*@SerializedName("created_by")
    private String userId;

    @SerializedName("releaseform_signature")
    private String signature;*/

    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }

    public String getReleaseFormId() {
        return releaseFormId;
    }

    public void setReleaseFormId(String releaseFormId) {
        this.releaseFormId = releaseFormId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }
}
