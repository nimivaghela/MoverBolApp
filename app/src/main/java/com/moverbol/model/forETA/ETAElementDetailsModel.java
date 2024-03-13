package com.moverbol.model.forETA;

import com.google.gson.annotations.SerializedName;

class ETAElementDetailsModel {

    @SerializedName("distance")
    private ETAElementDetailsValuesModel distanceDetailsValuesModel;

    @SerializedName("duration")
    private ETAElementDetailsValuesModel durationDetailsValuesModel;

    @SerializedName("status")
    private String status;

    public ETAElementDetailsValuesModel getDistanceDetailsValuesModel() {
        return distanceDetailsValuesModel;
    }

    public void setDistanceDetailsValuesModel(ETAElementDetailsValuesModel distanceDetailsValuesModel) {
        this.distanceDetailsValuesModel = distanceDetailsValuesModel;
    }

    public ETAElementDetailsValuesModel getDurationDetailsValuesModel() {
        return durationDetailsValuesModel;
    }

    public void setDurationDetailsValuesModel(ETAElementDetailsValuesModel durationDetailsValuesModel) {
        this.durationDetailsValuesModel = durationDetailsValuesModel;
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

}
