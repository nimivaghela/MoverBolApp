package com.moverbol.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 22/12/17.
 * This class is only created to handle getMetadata api for material. The response for this
 * API is different than all other
 */

public class BaseResponseModelSecond <T, T2>{

    @SerializedName("code")
    protected int responseCode;

    @SerializedName("status")
    protected String status;

    @SerializedName("message")
    protected String message;

    @SerializedName(value = "data", alternate = {"details"})
    private T data;

    @SerializedName(value = "rateType", alternate = {"description"})
    private T2 secondData;


    /*public BaseResponseModel() {
    }

    public BaseResponseModel(int responseCode, String status, String message) {
        this.responseCode = responseCode;
        this.status = status;
        this.message = message;
    }*/

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T2 getSecondData() {
        return secondData;
    }

    public void setSecondData(T2 secondData) {
        this.secondData = secondData;
    }
}
