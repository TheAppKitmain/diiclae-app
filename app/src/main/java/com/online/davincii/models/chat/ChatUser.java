package com.online.davincii.models.chat;

public class ChatUser {
    private String id;
    private String name;
    private String image;
    private String message;
    private String last_update;

    public ChatUser(String id, String name, String image, String message, String last_update) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.message = message;
        this.last_update = last_update;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }

    public String getLast_update() {
        return last_update;
    }
}
