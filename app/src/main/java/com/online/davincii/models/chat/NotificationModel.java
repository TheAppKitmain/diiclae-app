package com.online.davincii.models.chat;

public class NotificationModel {
    private String title;
    private String body;
    private String sender_id;
    private String sound;
    private String image;
    private String name;

    public NotificationModel(String title, String body, String sender_id, String sound, String image,String name) {
        this.title = title;
        this.body = body;
        this.sender_id = sender_id;
        this.sound = sound;
        this.image=image;
        this.name=name;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
