package com.moverbol.model;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

import java.util.List;

/**
 * Created by AkashM on 29/12/17.
 */

public class MoveStageDetailsPojo {

    @SerializedName("activity")
    private String moveStageId;

    @SerializedName("parent_job_id")
    private String jobId;

    @SerializedName("move_type")
    private String moveTypeId;

    @SerializedName("move_type_name")
    private String moveTypeName;

    @SerializedName("job_id")
    private String moveProcessJobId;

    @SerializedName("job_date")
    private String jobDate;

    @SerializedName("activity_name")
    private String moveStageName;

    @SerializedName("status")
    private String status;

    @SerializedName("men")
    private String numberOfMen;

    @SerializedName("date")
    private String date;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("trucks")
    private String numberOfTrucks;

    @SerializedName("estimate_volume")
    private String estimateVolume;

    @SerializedName("estimate_weight")
    private String estimateWeight;

    @SerializedName("estimate_hour")
    private String estimatedHours;

    @SerializedName("hourly_rate")
    private String hourlyRate;

    @SerializedName("workorder_name")
    private String workOrderName;

    @SerializedName("truck")
    private List<TruckPojo> trucks;

    @SerializedName("material")
    private List<MaterialPojo> materials;

    @SerializedName("crew")
    private List<ManPowerPojo> crews;

    @SerializedName("comments")
    private List<CommentPojo> comments;

    @SerializedName("min_hours")
    private String minHours;

    @SerializedName("min_charges")
    private String minCharges;

    @SerializedName("total_charges")
    private String TotalCharges;

    @SerializedName("hourly_rate_flag")
    private int hourlyRateFlag = 1;

    @SerializedName("estimate_show_flag")
    private int estimateShowFlag = 1;

    public int getHourlyRateFlag() {
        return hourlyRateFlag;
    }

    public void setHourlyRateFlag(int hourlyRateFlag) {
        this.hourlyRateFlag = hourlyRateFlag;
    }

    public String getMinHours() {
        return minHours;
    }

    public void setMinHours(String minHours) {
        this.minHours = minHours;
    }

    public String getMinCharges() {
        return minCharges;
    }

    public void setMinCharges(String minCharges) {
        this.minCharges = minCharges;
    }

    public String getTotalCharges() {
        return TotalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.TotalCharges = totalCharges;
    }

    public String getMoveTypeId() {
        return moveTypeId;
    }

    public void setMoveTypeId(String moveTypeId) {
        this.moveTypeId = moveTypeId;
    }

    public String getMoveTypeName() {
        return moveTypeName;
    }

    public void setMoveTypeName(String moveTypeName) {
        this.moveTypeName = moveTypeName;
    }

    public String getMoveProcessJobId() {
        return moveProcessJobId;
    }

    public void setMoveProcessJobId(String moveProcessJobId) {
        this.moveProcessJobId = moveProcessJobId;
    }

    public String getMoveStageId() {
        return moveStageId;
    }

    public void setMoveStageId(String moveStageId) {
        this.moveStageId = moveStageId;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getMoveStageName() {
        return moveStageName;
    }

    public void setMoveStageName(String moveStageName) {
        this.moveStageName = moveStageName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberOfMen() {
        return numberOfMen;
    }

    public void setNumberOfMen(String numberOfMen) {
        this.numberOfMen = numberOfMen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public void setNumberOfTrucks(String numberOfTrucks) {
        this.numberOfTrucks = numberOfTrucks;
    }

    public String getEstimateVolume() {
        return estimateVolume;
    }

    public void setEstimateVolume(String estimateVolume) {
        this.estimateVolume = estimateVolume;
    }

    public String getEstimateWeight() {
        return estimateWeight;
    }

    public void setEstimateWeight(String estimateWeight) {
        this.estimateWeight = estimateWeight;
    }

    public String getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(String estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setComments(List<CommentPojo> comments) {
        this.comments = comments;
    }

    public List<TruckPojo> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<TruckPojo> trucks) {
        this.trucks = trucks;
    }

    public List<MaterialPojo> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialPojo> materials) {
        this.materials = materials;
    }

    public List<ManPowerPojo> getCrews() {
        return crews;
    }

    public void setCrews(List<ManPowerPojo> crews) {
        this.crews = crews;
    }

    public List<CommentPojo> getComments() {
        return comments;
    }

    public String getWorkOrderName() {
        return workOrderName;
    }

    public void setWorkOrderName(String workOrderName) {
        this.workOrderName = workOrderName;
    }

    public String getFormattedDate() {
        return Util.getFormattedDate(this.date, Constants.INPUT_DATE_FORMAT_JOBS, Constants.OUTPUT_DATE_FORMAT_COMMENTS);
    }

    public int getEstimateShowFlag() {
        return estimateShowFlag;
    }

    public void setEstimateShowFlag(int estimateShowFlag) {
        this.estimateShowFlag = estimateShowFlag;
    }
}
