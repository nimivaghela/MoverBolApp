package com.moverbol.model.photoes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageItemModel implements Parcelable {
    public static final Parcelable.Creator<ImageItemModel> CREATOR = new Parcelable.Creator<ImageItemModel>() {
        @Override
        public ImageItemModel createFromParcel(Parcel source) {
            return new ImageItemModel(source);
        }

        @Override
        public ImageItemModel[] newArray(int size) {
            return new ImageItemModel[size];
        }
    };
    @SerializedName("r_id")
    private String rId;
    @SerializedName("r_quantity")
    private String rQuantity;
    @SerializedName("r_group_id")
    private String rGroupId;
    @SerializedName("r_item_id")
    private String rItemId;
    @SerializedName("item_name")
    private String itemName;
    private String density;
    @SerializedName("item_type")
    private String itemType;
    @SerializedName("room_flag")
    private String roomFlag;
    @SerializedName("room_name")
    private String roomName;
    private ArrayList<ImagesModel> images;

    public ImageItemModel() {
    }

    protected ImageItemModel(Parcel in) {
        this.rId = in.readString();
        this.rQuantity = in.readString();
        this.rGroupId = in.readString();
        this.rItemId = in.readString();
        this.itemName = in.readString();
        this.density = in.readString();
        this.itemType = in.readString();
        this.roomFlag = in.readString();
        this.roomName = in.readString();
        this.images = in.createTypedArrayList(ImagesModel.CREATOR);
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getrQuantity() {
        return rQuantity;
    }

    public void setrQuantity(String rQuantity) {
        this.rQuantity = rQuantity;
    }

    public String getrGroupId() {
        return rGroupId;
    }

    public void setrGroupId(String rGroupId) {
        this.rGroupId = rGroupId;
    }

    public String getrItemId() {
        return rItemId;
    }

    public void setrItemId(String rItemId) {
        this.rItemId = rItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getRoomFlag() {
        return roomFlag;
    }

    public void setRoomFlag(String roomFlag) {
        this.roomFlag = roomFlag;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<ImagesModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesModel> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rId);
        dest.writeString(this.rQuantity);
        dest.writeString(this.rGroupId);
        dest.writeString(this.rItemId);
        dest.writeString(this.itemName);
        dest.writeString(this.density);
        dest.writeString(this.itemType);
        dest.writeString(this.roomFlag);
        dest.writeString(this.roomName);
        dest.writeTypedList(this.images);
    }
}
