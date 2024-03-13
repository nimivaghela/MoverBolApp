package com.moverbol.model;

import androidx.databinding.ObservableBoolean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/11/17.
 */

public class TruckPojo{

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("job_sub_id")
    private String moveProcessJobId;

    @SerializedName(value = "vehicle_type", alternate = {"r_vehicle_type"})
    private String vehicleTypeId;

    @SerializedName("vehicle_type_name")
    private String vehicleTypeName;

    @SerializedName(value = "description", alternate = {"r_remarks"})
    private String remarks;

    /*@SerializedName("truck_id")
    private String truckId;*/
    @SerializedName("r_vehicle_no")
    private String vehicleNo;

    @SerializedName("truck_name")
    private String truckName;

    @SerializedName("r_temp_vehicle")
    private String tempName;

    @SerializedName("r_bol_vehicle_type")
    private String bolVehicleTypeId;

    @SerializedName("r_bol_vehicle_no")
    private String bolVehicleNumber;

    @SerializedName("r_bol_temp_vehicle")
    private String bolTempVehicleName;

    @SerializedName("r_bol_remarks")
    private String bolRemarks;

    @SerializedName("bol_vehicle_type_name")
    private String bolVehicleTypeName;

    @SerializedName("bol_truck_name")
    private String bolTruckName;

    @SerializedName("r_bol_status")
    private String bolStatus;


    private ObservableBoolean selectedForDelete = new ObservableBoolean();

    public boolean isSelectedForDelete() {
        return selectedForDelete.get();
    }

    public void setSelectedForDelete(boolean selectedForDelete) {
        this.selectedForDelete.set(selectedForDelete);
    }

    public String getMoveProcessJobId() {
        return moveProcessJobId;
    }

    public void setMoveProcessJobId(String moveProcessJobId) {
        this.moveProcessJobId = moveProcessJobId;
    }

    public String getTempName() {
//        return bolTempVehicleName == null || bolTempVehicleName.equals("") ? tempName : bolTempVehicleName;
        return getBolStatus() ? bolTempVehicleName : tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    public String getVehicleNo() {
//        return bolVehicleNumber == null || bolVehicleNumber.equals("") ? vehicleNo : bolVehicleNumber;
        return getBolStatus() ? bolVehicleNumber : vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    /*public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }*/

    public String getRemarks() {
//        return bolRemarks == null || bolRemarks.equals("") ? remarks : bolRemarks;
        return getBolStatus() ? bolRemarks : remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

   /* public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }*/

    public String getVehicleTypeId() {
//        return bolVehicleTypeId == null || bolVehicleTypeId.equals("") ? vehicleTypeId : bolVehicleTypeId;
        return getBolStatus() ? bolVehicleTypeId : vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeName() {
//        return bolVehicleTypeName == null || bolVehicleTypeName.equals("") ? vehicleTypeName : bolVehicleTypeName;
        return getBolStatus() ? bolVehicleTypeName : vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getTruckName() {
//        return bolTruckName == null || bolTruckName.equals("") ? truckName : bolTruckName;
        return getBolStatus() ? bolTruckName : truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }


    public void setBolVehicleTypeId(String bolVehicleTypeId) {
        this.bolVehicleTypeId = bolVehicleTypeId;
    }

    public void setBolVehicleNumber(String bolVehicleNumber) {
        this.bolVehicleNumber = bolVehicleNumber;
    }

    public void setBolTempVehicle(String bolTempVehicleName) {
        this.bolTempVehicleName = bolTempVehicleName;
    }

    public void setBolRemarks(String bolRemarks) {
        this.bolRemarks = bolRemarks;
    }

    public String getBolVehicleTypeId() {
        return bolVehicleTypeId;
    }

    public String getBolVehicleNumber() {
        return bolVehicleNumber;
    }

    public String getBolTempVehicleName() {
        return bolTempVehicleName;
    }

    public void setBolTempVehicleName(String bolTempVehicleName) {
        this.bolTempVehicleName = bolTempVehicleName;
    }

    public String getBolRemarks() {
        return bolRemarks;
    }

    public String getBolVehicleTypeName() {
        return bolVehicleTypeName;
    }

    public void setBolVehicleTypeName(String bolVehicleTypeName) {
        this.bolVehicleTypeName = bolVehicleTypeName;
    }

    public String getBolTruckName() {
        return bolTruckName;
    }

    public void setBolTruckName(String bolTruckName) {
        this.bolTruckName = bolTruckName;
    }

    public boolean getBolStatus() {
        return (bolStatus != null && bolStatus.equals("1")) ;
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

}
