package com.moverbol.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 23/11/17.
 */

public class BaseResponseModel <T>{

    @SerializedName("code")
    protected int responseCode;

    @SerializedName("status")
    protected String status;

    @SerializedName(value = "message", alternate = {"error"})
    protected String message;

    @SerializedName(value = "data", alternate = {"details", "link", "r_id", "bol_id", "discount", "discount_list", "job_closed"})
    private T data;


    public BaseResponseModel() {
    }

    public BaseResponseModel(int responseCode, String status, String message) {
        this.responseCode = responseCode;
        this.status = status;
        this.message = message;
    }

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
        if(message==null){
            return "";
        }
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
}
