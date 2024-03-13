package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by AkashM on 21/12/17.
 */

public class CrewMetadataAssigned {

    @SerializedName("c_id")
    private String crewId;

    @SerializedName("c_hr_emp_name")
    private String crewName;

    public String getCrewId() {
        return crewId;
    }

    public void setCrewId(String crewId) {
        this.crewId = crewId;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }


    @Override
    public String toString() {
        return crewName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrewMetadataAssigned)) return false;
        CrewMetadataAssigned that = (CrewMetadataAssigned) o;
        return Objects.equals(getCrewId(), that.getCrewId()) &&
                Objects.equals(getCrewName(), that.getCrewName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCrewId(), getCrewName());
    }
}
