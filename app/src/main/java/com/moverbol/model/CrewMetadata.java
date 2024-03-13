package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 21/12/17.
 */

public class CrewMetadata {

    @SerializedName("c_id")
    private String id;

    @SerializedName("c_metadata_designation")
    private String designation;

    @SerializedName("assigned")
    private List<CrewMetadataAssigned> assignedList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<CrewMetadataAssigned> getAssignedList() {
        return assignedList;
    }

    public void setAssignedList(List<CrewMetadataAssigned> assignedList) {
        this.assignedList = assignedList;
    }

    @Override
    public String toString() {
        return designation;
    }
}
