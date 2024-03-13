package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 21/12/17.
 */

public class TruckMetadataAssigned {

    @SerializedName("c_id")
    private String id;

    @SerializedName("c_vehicle_no")
    private String vehicleNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    @Override
    public String toString() {
        return vehicleNumber  ;
    }
}
