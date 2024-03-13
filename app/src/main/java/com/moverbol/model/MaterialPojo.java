package com.moverbol.model;

import androidx.databinding.ObservableBoolean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/11/17.
 */

public class MaterialPojo {

    @SerializedName(value = "material_id", alternate = {"r_material_id"})
    private String materialId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("job_sub_id")
    private String moveProcessJobId;

    @SerializedName("r_quantity")
    private String quantity;

    @SerializedName(value = "description", alternate = {"r_remarks"})
    private String remarks;

    @SerializedName("r_material_unit")
    private String materialUnit;

    private String materialUnitName;

    @SerializedName("r_rate")
    private String rate;

    @SerializedName("comp_rate")
    private String compRate;

    @SerializedName("r_total")
    private String total;

    @SerializedName("material_name")
    private String materialName;

    @SerializedName("r_material")
    private String materialNameId;

    @SerializedName("r_bol_material")
    private String bolMaterialNameId;

    @SerializedName("r_bol_quantity")
    private String bolQuantity;

    @SerializedName("r_bol_material_unit")
    private String bolMaterialUnit;

    @SerializedName("r_bol_rate")
    private String bolRate;

    @SerializedName("r_bol_total")
    private String bolTotal;

    @SerializedName(value = "r_bol_remarks", alternate = {"bol_description"})
    private String bolRemarks;

    @SerializedName("bol_material_name")
    private String bolMaterialName;

    @SerializedName("c_id")
    private int cId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("r_bol_status")
    private int status;


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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return bolRemarks == null || bolRemarks.equals("") ? remarks : bolRemarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMaterialUnit() {
        return bolMaterialUnit == null || bolMaterialUnit.equals("") ? materialUnit : bolMaterialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getRate() {
        return bolRate == null || bolRate.equals("") ? rate : bolRate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTotal() {
        return bolTotal == null || bolTotal.equals("") ? total: bolTotal;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMaterialName() {
        return materialName != null && !materialName.isEmpty() ? materialName : "Unknown Material";
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setMaterialUnitName(String materialUnitName) {
        this.materialUnitName = materialUnitName;
    }

    public String getMaterialNameId() {
        return bolMaterialNameId == null || bolMaterialNameId.equals("") ? materialNameId : bolMaterialNameId;
    }

    public void setMaterialNameId(String materialNameId) {
        this.materialNameId = materialNameId;
    }

    public String getBolMaterialNameId() {
        return bolMaterialNameId;
    }

    public void setBolMaterialNameId(String bolMaterialNameId) {
        this.bolMaterialNameId = bolMaterialNameId;
    }

    public String getBolQuantity() {
        return bolQuantity;
    }

    public void setBolQuantity(String bolQuantity) {
        this.bolQuantity = bolQuantity;
    }

    public String getBolMaterialUnit() {
        return bolMaterialUnit;
    }

    public void setBolMaterialUnit(String bolMaterialUnit) {
        this.bolMaterialUnit = bolMaterialUnit;
    }

    public String getBolRate() {
        return bolRate;
    }

    public void setBolRate(String bolRate) {
        this.bolRate = bolRate;
    }

    public String getBolTotal() {
        return bolTotal;
    }

    public void setBolTotal(String bolTotal) {
        this.bolTotal = bolTotal;
    }

    public String getBolRemarks() {
        return bolRemarks;
    }

    public void setBolRemarks(String bolRemarks) {
        this.bolRemarks = bolRemarks;
    }

    public String getCompRate() {
        return compRate;
    }

    public void setCompRate(String compRate) {
        this.compRate = compRate;
    }

    public String getBolMaterialName() {
        return bolMaterialName != null && !bolMaterialName.isEmpty() ? bolMaterialName : "Unknown Material";
    }

    public void setBolMaterialName(String bolMaterialName) {
        this.bolMaterialName = bolMaterialName;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getCategoryName() {
        return categoryName != null && !categoryName.isEmpty() ? categoryName : "Unknown Category";
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String displayMaterialName() {
        if (status == 1) {
            return getBolMaterialName();
        } else {
            return getMaterialName();
        }
    }

    public String displayQuantity() {
        if (status == 1) {
            return getBolQuantity();
        } else {
            return getQuantity();
        }
    }

    public String getMaterialUnitName() {
        String unitId = getMaterialUnit();
        if (unitId != null) {
            if (unitId.equals("1")) {
                materialUnitName = "Unit Rate";
            } else if (unitId.equals("2")) {
                materialUnitName = "Pack Rate";
            } else if (unitId.equals("3")) {
                materialUnitName = "Unpack Rate";
            }
        }
        return materialUnitName;
    }


}
