package com.online.davincii.models.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RootModel {

    @SerializedName("to")
    @Expose
    private String token;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("notification")
    @Expose
    private NotificationModel notification;
    @SerializedName("data")
    @Expose
    private DataModel data;

//    public RootModel(String token, String priority, NotificationModel notification) {
//        this.token = token;
//        this.priority = priority;
//        this.notification = notification;
//    }

    public RootModel(String token, String priority, DataModel data) {
        this.token = token;
        this.priority = priority;
        this.data = data;
    }

    public RootModel(String token, String priority,NotificationModel notification) {
        this.token = token;
        this.priority = priority;
        this.data = data;
        this.notification = notification;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }
}
