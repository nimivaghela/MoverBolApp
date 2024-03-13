package com.moverbol.model.moveProcess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;


public class WorkerModel implements Parcelable {
    public static final Creator<WorkerModel> CREATOR = new Creator<WorkerModel>() {
        public WorkerModel createFromParcel(Parcel source) {
            return new WorkerModel(source);
        }

        public WorkerModel[] newArray(int size) {
            return new WorkerModel[size];
        }
    };
    String workerId;
    String name;
    String resourceId;

    protected WorkerModel(Parcel in) {
        workerId = in.readString();
        name = in.readString();
        resourceId = in.readString();

    }

    public WorkerModel() {

    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workerId);
        dest.writeString(name);
        dest.writeString(resourceId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerModel)) return false;
        WorkerModel that = (WorkerModel) o;
        return Objects.equals(getWorkerId(), that.getWorkerId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getResourceId(), that.getResourceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorkerId(), getName(), getResourceId());
    }
}
