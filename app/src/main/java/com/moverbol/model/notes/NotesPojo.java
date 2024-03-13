package com.moverbol.model.notes;

import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 7/3/18.
 */

public class NotesPojo {

    @SerializedName("r_id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("opportunity_id")
    private String opportunityId;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("added_date")
    private String creationDate;

    @SerializedName("user_name")
    private String userName;
    @SerializedName("notes_type")
    private String notesType;

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotesType() {
        return notesType;
    }

    public void setNotesType(String notesType) {
        this.notesType = notesType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFormattedCreationDate() {
        if (this.creationDate == null) {
            return "";
        }

        return Util.getFormattedDate(this.creationDate, Constants.INPUT_DATE_FORMAT_COMMENTS, Constants.NOTE_DATE_FORMAT);
    }

}
