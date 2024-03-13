package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 21/12/17.
 */

public class MaterialMetadata {

    @SerializedName("id")
    private String materialId;

    @SerializedName("name")
    private String moveType;

    @SerializedName("material")
    private List<MaterialMetadatAssigned> metadatAssignedList;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public List<MaterialMetadatAssigned> getMetadatAssignedList() {
        return metadatAssignedList;
    }

    public void setMetadatAssignedList(List<MaterialMetadatAssigned> metadatAssignedList) {
        this.metadatAssignedList = metadatAssignedList;
    }


}
