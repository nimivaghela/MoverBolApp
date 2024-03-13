package com.moverbol.model.photoes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 5/2/18.
 */

public class PhotoUploadPojo {

    @SerializedName("photo_title")
    private String title;

    @SerializedName("photo_description")
    private String description;

    @SerializedName("created_by")
    private String userId;

    @SerializedName("photo_image")
    private String imageEncodedString;

    public String getImageEncodedString() {
        return imageEncodedString;
    }

    public void setImageEncodedString(String imageEncodedString) {
        this.imageEncodedString = imageEncodedString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
