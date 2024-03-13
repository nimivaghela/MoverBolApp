package com.moverbol.model.photoes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImagesModel implements Parcelable {
    @SerializedName("c_id")
    private String cId;
    @SerializedName("photo_title")
    private String photoTitle;
    @SerializedName("photo_description")
    private String photoDescription;
    private String link;
    @SerializedName("room_id")
    private String roomId;
    @SerializedName("room_name")
    private String roomName;
    @SerializedName("items")
    private ArrayList<ImageItemModel> items;
    private ArrayList<ImagesModel> images;
    private boolean selectedForDelete;
    private boolean showSelection;
    public static final Creator<ImagesModel> CREATOR = new Creator<ImagesModel>() {
        @Override
        public ImagesModel createFromParcel(Parcel source) {
            return new ImagesModel(source);
        }

        @Override
        public ImagesModel[] newArray(int size) {
            return new ImagesModel[size];
        }
    };
    private String item;

    protected ImagesModel(Parcel in) {
        this.cId = in.readString();
        this.photoTitle = in.readString();
        this.photoDescription = in.readString();
        this.link = in.readString();
        this.roomId = in.readString();
        this.roomName = in.readString();
        this.items = in.createTypedArrayList(ImageItemModel.CREATOR);
        this.images = in.createTypedArrayList(ImagesModel.CREATOR);
        this.selectedForDelete = in.readByte() != 0;
        this.showSelection = in.readByte() != 0;
        this.item = in.readString();
    }

    public ImagesModel() {
    }

    public boolean isSelectedForDelete() {
        return selectedForDelete;
    }

    public void setSelectedForDelete(boolean selectedForDelete) {
        this.selectedForDelete = selectedForDelete;
    }

    public boolean isShowSelection() {
        return showSelection;
    }

    public void setShowSelection(boolean showSelection) {
        this.showSelection = showSelection;
    }

    public ArrayList<ImagesModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesModel> images) {
        this.images = images;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<ImageItemModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ImageItemModel> items) {
        this.items = items;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    public String getItem() {
        return item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getImageLink() {
        if (items != null) {

            for (ImageItemModel imageItemModel : items) {
                for (ImagesModel imagesModel : imageItemModel.getImages()) {
                    if (!imagesModel.getLink().isEmpty()) {
                        return imagesModel.getLink();
                    }
                }
            }

        } else if (images != null && !images.isEmpty() && !images.get(0).getImages().isEmpty()) {
            for (ImagesModel imagesModel : images) {
                for (ImagesModel innerImageModel : imagesModel.getImages()) {
                    if (!innerImageModel.getLink().isEmpty()) {
                        return innerImageModel.getLink();
                    }
                }
            }


        } else if (images != null && !images.isEmpty()) {
            return images.get(0).getLink();
        }
        return null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cId);
        dest.writeString(this.photoTitle);
        dest.writeString(this.photoDescription);
        dest.writeString(this.link);
        dest.writeString(this.roomId);
        dest.writeString(this.roomName);
        dest.writeTypedList(this.items);
        dest.writeTypedList(this.images);
        dest.writeByte(this.selectedForDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showSelection ? (byte) 1 : (byte) 0);
        dest.writeString(this.item);
    }
}
