package com.moverbol.model.notification;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.views.activities.moveprocess.bill_of_lading.BillOfLadingActivity;

public class NotificationMessageModel {

    @SerializedName("notification_type")
    private int notificationTypeIndex;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("bol_status")
    private String bolStatus;

    @SerializedName("description")
    private String description;

    @SerializedName("notes_id")
    private String notes_id;

    public int getNotificationTypeIndex() {
        return notificationTypeIndex;
    }

    public void setNotificationTypeIndex(int notificationTypeIndex) {
        this.notificationTypeIndex = notificationTypeIndex;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getBolStatus() {
        return bolStatus;
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

    public boolean isBolAccepted() {
        if (bolStatus == null) {
            return false;
        }
        return TextUtils.equals(bolStatus, BillOfLadingActivity.BOL_APPROVAL_STATUS_ACCEPTED);
    }

    public String getMessage() {
        String messageToReturn = "";

        if (notificationTypeIndex == Constants.NotificationTypeIndexes.TYPE_BOL_CONFIRMATION) {
            if (bolStatus != null && isBolAccepted()) {
                messageToReturn = "BOL has been approved for Job id:" + getJobId();
            } else {
                messageToReturn = "BOL has been rejected for Job id:" + getJobId();
            }
        } else if (notificationTypeIndex == Constants.NotificationTypeIndexes.TYPE_BOL_NEW_JOB) {
            messageToReturn = "A new job has been assigned to you. Job id: " + getJobId();
        } else if (notificationTypeIndex == Constants.NotificationTypeIndexes.TYPE_NOTES) {
            messageToReturn = "A new note has been added added for Job id: " + getJobId();
        } else if (notificationTypeIndex == Constants.NotificationTypeIndexes.TYPE_JOB_DELETE) {
            messageToReturn = "Job id: " + getJobId() + " has been deleted";
        } else if (notificationTypeIndex == Constants.NotificationTypeIndexes.TYPE_ESTIMATE_AND_BOL_CHANGED) {
            messageToReturn = "Estimate and BOL has been changed for Job id:" + getJobId();
        }

        return messageToReturn;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(String notes_id) {
        this.notes_id = notes_id;
    }
}
