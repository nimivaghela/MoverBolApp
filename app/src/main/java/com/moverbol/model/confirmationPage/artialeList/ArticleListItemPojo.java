package com.moverbol.model.confirmationPage.artialeList;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 29/1/18.
 */

public class ArticleListItemPojo extends BaseObservable implements Parcelable {


    public static final Parcelable.Creator<ArticleListItemPojo> CREATOR = new Parcelable.Creator<ArticleListItemPojo>() {
        @Override
        public ArticleListItemPojo createFromParcel(Parcel source) {
            return new ArticleListItemPojo(source);
        }

        @Override
        public ArticleListItemPojo[] newArray(int size) {
            return new ArticleListItemPojo[size];
        }
    };
    @SerializedName("r_id")
    private String id;
    @SerializedName(value = "r_inventory_id")
    private String inventoryId;
    @SerializedName("r_group_id")
    private String groupId;
    @SerializedName("r_item_id")
    private String itemId;
    @SerializedName("r_quantity")
    private String quantity;
    @SerializedName("r_actual_qty")
    private String actualQty;
    @SerializedName("r_weight")
    private String weight;
    @SerializedName("r_bol_weight")
    private String bol_weight;
    @SerializedName("r_volume")
    private String volume;
    @SerializedName("r_bol_volume")
    private String bol_volume;
    @SerializedName("r_bol_status")
    private String bolStatus;
    @SerializedName("r_bol_created_by")
    private String bolCreatedBy;
    @SerializedName("r_bol_created_date")
    private String bolCreatedDate;
    @SerializedName(value = "item_name", alternate = {"r_item_name"})
    private String ItemName;
    @SerializedName("item_name_display")
    private String itemNameDisplay;
    //Refer to Constants.ArticleListItemTypes interface
    @SerializedName("item_type")
    private int itemType;
    @SerializedName("is_shipping")
    private String isShipping;
    @SerializedName("roomname")
    private String roomName;
    private String density;

    protected ArticleListItemPojo(Parcel in) {
        this.id = in.readString();
        this.inventoryId = in.readString();
        this.groupId = in.readString();
        this.itemId = in.readString();
        this.quantity = in.readString();
        this.actualQty = in.readString();
        this.weight = in.readString();
        this.bol_weight = in.readString();
        this.volume = in.readString();
        this.bol_volume = in.readString();
        this.bolStatus = in.readString();
        this.bolCreatedBy = in.readString();
        this.bolCreatedDate = in.readString();
        this.ItemName = in.readString();
        this.itemType = in.readInt();
        this.isShipping = in.readString();
        this.roomName = in.readString();
        this.density = in.readString();
        this.itemNameDisplay = in.readString();
    }

    public String getItemNameDisplay() {
        return itemNameDisplay;
    }

    public void setItemNameDisplay(String itemNameDisplay) {
        this.itemNameDisplay = itemNameDisplay;
    }

    public String getIsShipping() {
        return isShipping;
    }

    public void setIsShipping(String isShipping) {
        this.isShipping = isShipping;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDensity() {
        return density;
    }


    public ArticleListItemPojo() {
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQuantity() {
//        return quantity;
        return Util.getRoundedUpNumericStringFromDoubleString(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Bindable
    public String getActualQty() {
        //The backend developer told me to do this

//        return Util.getRoundedUpNumericStringFromDoubleString(actualQty);
        if (actualQty == null) {
            return "0";
        }
        return actualQty;
    }

    /*@Bindable
    public String getVolume() {

        if(getBol_volume()!=null){
            return getBol_volume();
        }

        return volume;
//        return Util.getRoundedUpNumericStringFromDoubleString(volume);
    }

    public void setVolume(String volume) {
        this.volume = volume;
//        setWeightUsingVolumeAndQuantity();
        notifyPropertyChanged(BR.volume);
    }

    @Bindable
    public String getWeight() {
//        return weight;
        if(getBol_weight()!=null){
            return Util.getRoundedUpNumericStringFromDoubleString(getBol_weight());
        }
        return Util.getRoundedUpNumericStringFromDoubleString(weight);
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }*/

    public void setActualQty(String actualQty) {
        this.actualQty = actualQty;
        notifyPropertyChanged(BR.actualQty);
    }

    public void setActualQty() {
        if (this.actualQty == null || actualQty.equals("")) {
            this.actualQty = this.quantity;
        }

        /*try {
            if (this.actualQty != null && Integer.parseInt(this.actualQty) == 0) {
                this.actualQty = this.quantity;
            }
        } catch (NumberFormatException ignored) {
            actualQty = this.quantity;
        }*/

//        setWeightUsingVolumeAndQuantity();
    }

    public void setWeightUsingVolumeAndQuantity() {

        //To make sure that actual quantity is already set as it needs to be set.
        setActualQty();

        double quantityNum = Util.getDoubleFromString(getActualQty());
        double volumeNum = Util.getDoubleFromString(getBol_volume());
        double weightNum = ((volumeNum * quantityNum) * 7);

        setBol_weight(weightNum + "");
        setWeight(weightNum + "");
    }

    @Bindable
    public String getItemName() {
        return ItemName;
    }

    public String displayItemName() {
        if (isShipping.equalsIgnoreCase("0")) {
            return getItemNameDisplay();
        } else {
            return getItemName();
        }
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getBolStatus() {
        return bolStatus;
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

    public String getBolCreatedBy() {
        return bolCreatedBy;
    }

    public void setBolCreatedBy(String bolCreatedBy) {
        this.bolCreatedBy = bolCreatedBy;
    }

    public String getBolCreatedDate() {
        return bolCreatedDate;
    }

    public void setBolCreatedDate(String bolCreatedDate) {
        this.bolCreatedDate = bolCreatedDate;
    }

    public String getTotalVolume() {

        //To make sure that actual quantity is already set as it needs to be set.
        setActualQty();

        double volOfSingleItem = Util.getDoubleFromString(getBol_volume());
        double quantityNum = Util.getDoubleFromString(getActualQty());

        double volumeToReturn = volOfSingleItem * quantityNum;

        return Util.getGeneralFormattedDecimalString(volumeToReturn);
    }

    @Bindable
    public String getBol_weight() {

        if (bol_weight == null) {
            return Util.getRoundedUpNumericStringFromDoubleString(weight);
        }
        return Util.getRoundedUpNumericStringFromDoubleString(bol_weight);
    }

    public void setBol_weight(String weight) {
        this.bol_weight = weight;
        notifyPropertyChanged(BR.bol_weight);
    }

    @Bindable
    public String getBol_volume() {
        if (bol_volume == null) {
            return volume;
        }
        return bol_volume;
    }

    public void setBol_volume(String volume) {
        this.bol_volume = volume;
        notifyPropertyChanged(BR.bol_volume);
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.inventoryId);
        dest.writeString(this.groupId);
        dest.writeString(this.itemId);
        dest.writeString(this.quantity);
        dest.writeString(this.actualQty);
        dest.writeString(this.weight);
        dest.writeString(this.bol_weight);
        dest.writeString(this.volume);
        dest.writeString(this.bol_volume);
        dest.writeString(this.bolStatus);
        dest.writeString(this.bolCreatedBy);
        dest.writeString(this.bolCreatedDate);
        dest.writeString(this.ItemName);
        dest.writeInt(this.itemType);
        dest.writeString(this.isShipping);
        dest.writeString(this.roomName);
        dest.writeString(this.density);
        dest.writeString(this.itemNameDisplay);
    }
}
