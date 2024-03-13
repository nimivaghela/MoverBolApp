package com.moverbol.model.confirmationPage.artialeList;

import com.google.gson.annotations.SerializedName;

public class ArticleRoomModel {
    @SerializedName("room_id")
    private String roomId;
    @SerializedName("room_name")
    private String roomName;
    @SerializedName("inventory_id")
    private String inventoryId;

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

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }
}
