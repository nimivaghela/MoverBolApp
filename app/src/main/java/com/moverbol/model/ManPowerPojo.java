package com.moverbol.model;

import androidx.databinding.ObservableBoolean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 28/11/17.
 */

public class ManPowerPojo implements Parcelable {

    @SerializedName(value = "r_man_power_id", alternate = {"man_power_id"})
    private String manPowerId;

    @SerializedName(value = "user_id", alternate = {"r_responsible_user"})
    private String userId;

    @SerializedName(value = "desination_id", alternate = {"r_worker_type"})
    private String designationId;

    @SerializedName("r_temp_worker")
    private String isTempWorker;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("job_sub_id")
    private String moveProcessJobId;

    @SerializedName(value = "description", alternate = {"r_remarks"})
    private String description;

    @SerializedName("c_hr_emp_name")
    private String name;

    @SerializedName("c_hr_emp_email_address")
    private String emailAddress;

    @SerializedName("desination_name")
    private String designationName;

    @SerializedName("bol_desination_name")
    private String bolDesignationName;

    @SerializedName(value = "r_bol_worker_type", alternate = {"bol_desination_id"})
    private String bolDesignationId;

    @SerializedName(value = "r_bol_responsible_user", alternate = {"bol_user_id"})
    private String bolUserId;

    @SerializedName("r_bol_temp_worker")
    private String bolIsTempWorker;

    @SerializedName(value = "r_bol_remarks", alternate = {"bol_description"})
    private String bolDescription;

    @SerializedName("bol_hr_emp_name")
    private String bolName;

    @SerializedName("hr_emp_email_address")
    private String bolEmailAddress;

    @SerializedName("r_job_activity")
    private String jobActivity;

    @SerializedName("r_bol_status")
    private String bolStatus;

    private ObservableBoolean selectedForDelete = new ObservableBoolean();

    public String getManPowerId() {
        return manPowerId;
    }

    public void setManPowerId(String manPowerId) {
        this.manPowerId = manPowerId;
    }

    public String getUserId() {
//        return bolUserId == null || bolUserId.equals("") ? userId : bolUserId;
        return getBolStatus() ? bolUserId : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDesignationId() {
//        return bolDesignationId == null || bolDesignationId.equals("") ? designationId : bolDesignationId;
        return getBolStatus() ? bolDesignationId : designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getIsTempWorker() {
//        return bolIsTempWorker == null || bolIsTempWorker.equals("") ? isTempWorker : bolIsTempWorker;
        return getBolStatus() ? bolIsTempWorker : isTempWorker;
    }

    public void setIsTempWorker(String isTempWorker) {
        this.isTempWorker = isTempWorker;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMoveProcessJobId() {
        return moveProcessJobId;
    }

    public void setMoveProcessJobId(String moveProcessJobId) {
        this.moveProcessJobId = moveProcessJobId;
    }

    public String getName() {
//        return bolName == null || bolName.equals("") ? name : bolName;
        return getBolStatus() ? bolName : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
//        return bolEmailAddress == null || bolEmailAddress.equals("") ? emailAddress : bolEmailAddress;
        return getBolStatus() ? bolEmailAddress : emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDesignationName() {
//        return bolDesignationName == null || bolDesignationName.equals("") ? designationName : bolDesignationName;
        return getBolStatus() ? bolDesignationName : designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDescription() {
//        return bolDescription == null || bolDescription.equals("") ? description : bolDescription;
        return getBolStatus() ? bolDescription : description;
//        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelectedForDelete() {
        return selectedForDelete.get();
    }

    public void setSelectedForDelete(boolean selectedForDelete) {
        this.selectedForDelete.set(selectedForDelete);
    }

    public void setBolDesignationId(String bolDesignationId) {
        this.bolDesignationId = bolDesignationId;
    }

    public void setBolUserId(String bolUserId) {
        this.bolUserId = bolUserId;
    }

    public void setBolIsTempWorker(String bolIsTempWorker) {
        this.bolIsTempWorker = bolIsTempWorker;
    }

    public void setBolDescription(String bolDescription) {
        this.bolDescription = bolDescription;
    }

    public String getBolDesignationName() {
        return bolDesignationName;
    }

    public void setBolDesignationName(String bolDesignationName) {
        this.bolDesignationName = bolDesignationName;
    }

    public String getBolDesignationId() {
        return bolDesignationId;
    }

    public String getBolUserId() {
        return bolUserId;
    }

    public String getBolIsTempWorker() {
        return bolIsTempWorker;
    }

    public String getBolDescription() {
        return bolDescription;
    }

    public String getBolName() {
        return bolName;
    }

    public void setBolName(String bolName) {
        this.bolName = bolName;
    }

    public String getBolEmailAddress() {
        return bolEmailAddress;
    }

    public void setBolEmailAddress(String bolEmailAddress) {
        this.bolEmailAddress = bolEmailAddress;
    }

    public String getJobActivity() {
        return jobActivity;
    }

    public void setJobActivity(String jobActivity) {
        this.jobActivity = jobActivity;
    }

    public boolean getBolStatus() {
        return (bolStatus!=null && bolStatus.equals("1"));
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.manPowerId);
        dest.writeString(this.userId);
        dest.writeString(this.designationId);
        dest.writeString(this.isTempWorker);
        dest.writeString(this.jobId);
        dest.writeString(this.moveProcessJobId);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.emailAddress);
        dest.writeString(this.designationName);
        dest.writeString(this.bolDesignationName);
        dest.writeString(this.bolDesignationId);
        dest.writeString(this.bolUserId);
        dest.writeString(this.bolIsTempWorker);
        dest.writeString(this.bolDescription);
        dest.writeString(this.bolName);
        dest.writeString(this.bolEmailAddress);
        dest.writeString(this.jobActivity);
        dest.writeString(this.bolStatus);
        dest.writeParcelable(this.selectedForDelete, flags);
    }

    public ManPowerPojo() {
    }

    protected ManPowerPojo(Parcel in) {
        this.manPowerId = in.readString();
        this.userId = in.readString();
        this.designationId = in.readString();
        this.isTempWorker = in.readString();
        this.jobId = in.readString();
        this.moveProcessJobId = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.emailAddress = in.readString();
        this.designationName = in.readString();
        this.bolDesignationName = in.readString();
        this.bolDesignationId = in.readString();
        this.bolUserId = in.readString();
        this.bolIsTempWorker = in.readString();
        this.bolDescription = in.readString();
        this.bolName = in.readString();
        this.bolEmailAddress = in.readString();
        this.jobActivity = in.readString();
        this.bolStatus = in.readString();
        this.selectedForDelete = in.readParcelable(ObservableBoolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ManPowerPojo> CREATOR = new Parcelable.Creator<ManPowerPojo>() {
        @Override
        public ManPowerPojo createFromParcel(Parcel source) {
            return new ManPowerPojo(source);
        }

        @Override
        public ManPowerPojo[] newArray(int size) {
            return new ManPowerPojo[size];
        }
    };
}
