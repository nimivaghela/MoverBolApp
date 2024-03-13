package com.moverbol.model.forETA;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ETAResponseModel {

    @SerializedName("destination_addresses")
    private List<String> destinationAdrressList;

    @SerializedName("origin_addresses")
    private List<String> originAdrressList;

    @SerializedName("rows")
    private List<ETAElementModel> rows;

    @SerializedName("status")
    private String status;

    @SerializedName("error_message")
    private String errorMessage;

    public List<String> getDestinationAdrressList() {
        return destinationAdrressList;
    }

    public void setDestinationAdrressList(List<String> destinationAdrressList) {
        this.destinationAdrressList = destinationAdrressList;
    }

    public List<String> getOriginAdrressList() {
        return originAdrressList;
    }

    public void setOriginAdrressList(List<String> originAdrressList) {
        this.originAdrressList = originAdrressList;
    }

    public List<ETAElementModel> getRows() {
        return rows;
    }

    public void setRows(List<ETAElementModel> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStatusSuccess(){
        if(status==null){
            return false;
        }
        return status.equals("OK");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * As per project requirement we will only have one row and all it's details will have only one
     * item. Hence this method is created to easily just get that single element ETA and in case of
     * any error return null.
     */
    public String getFirstRowEtaInSeconds(){
        if(!getIsETAElementStatusSuccess() ||
                rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel() == null||
                rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel().getText() == null
                ){
            return null;
        }

        return rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel().getValue();
    }

    public String getFirstRowEtaInString(){
        if(!getIsETAElementStatusSuccess() ||
                rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel() == null||
                rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel().getText() == null
                ){
            return null;
        }

        return rows.get(0).getElementDetailsModelList().get(0).getDurationDetailsValuesModel().getText();
    }

    public boolean getIsETAElementStatusSuccess(){
        if (rows==null || rows.isEmpty() || rows.get(0)==null ||
                rows.get(0).getElementDetailsModelList() ==null ||
                rows.get(0).getElementDetailsModelList().isEmpty() ||
                rows.get(0).getElementDetailsModelList().get(0)==null) {
            return false;
        }

        return rows.get(0).getElementDetailsModelList().get(0).isStatusSuccess();
    }

    public boolean getIsZeroResultForETA(){
        if (rows==null || rows.isEmpty() || rows.get(0)==null ||
                rows.get(0).getElementDetailsModelList() ==null ||
                rows.get(0).getElementDetailsModelList().isEmpty() ||
                rows.get(0).getElementDetailsModelList().get(0)==null ||
                rows.get(0).getElementDetailsModelList().get(0).getStatus()==null
                ) {
            return false;
        }

//        return rows.get(0).getElementDetailsModelList().get(0).getStatus().equals("");
        return TextUtils.equals( rows.get(0).getElementDetailsModelList().get(0).getStatus(), "ZERO_RESULTS");
    }

}
