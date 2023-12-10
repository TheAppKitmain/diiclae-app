package com.online.davincii.models.chat;

public class DataModel {

    private String title;
    private String sender_id;
    private String message;
    private String image;
    private String name;

    public DataModel(String title, String sender_id, String message, String image,String name) {
        this.title = title;
        this.sender_id = sender_id;
        this.message = message;
        this.image = image;
        this.name=name;
    }
}
