package com.moverbol.model;


import com.google.gson.annotations.SerializedName;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

import static com.moverbol.constants.Constants.DD_MM_YYYY_HH_MM_AA;

/**
 * Created by sumeet on 11/9/17.
 */

public class CommentPojo {

    @SerializedName("n_id")
    private String id;

    @SerializedName("added_date")
    private String date;

    @SerializedName("description")
    private String description;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedDate() {
        return Util.getFormattedDate(date, Constants.INPUT_DATE_FORMAT_COMMENTS, DD_MM_YYYY_HH_MM_AA);
    }

    /*public static void main(String[] args) {
        Date date = new Date("yyyy-MM-dd' 'HH:mm:ss");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy' 'HH:mm:ss");

    }*/
}
