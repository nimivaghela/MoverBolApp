package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 21/12/17.
 */

public class TruckMetadata {

    @SerializedName("c_id")
    private String id;

    @SerializedName("c_metadata_vehicle_type")
    private String vehicleType;

    @SerializedName("assigned")
    private List<TruckMetadataAssigned> truckMetadataAssignedList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<TruckMetadataAssigned> getTruckMetadataAssignedList() {
        return truckMetadataAssignedList;
    }

    public void setTruckMetadataAssignedList(List<TruckMetadataAssigned> truckMetadataAssignedList) {
        this.truckMetadataAssignedList = truckMetadataAssignedList;
    }

    @Override
    public String toString() {
        return vehicleType;
    }
}
