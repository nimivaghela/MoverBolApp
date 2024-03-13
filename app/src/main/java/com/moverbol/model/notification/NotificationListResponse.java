package com.moverbol.model.notification;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 11/06/18.
 */

public class NotificationListResponse {

    @SerializedName("Notification_list")
    List<NotificationModel> notificationModelList;

    public void setNotificationModelList(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    public List<NotificationModel> getNotificationModelList() {
        return notificationModelList;
    }
}
