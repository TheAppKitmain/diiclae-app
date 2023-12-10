package com.online.davincii.models.chat;

public class ChatObject {

    String message;
    String sender;
    String receiver;
    String time;
    String key;
    String type;
    String canvasImage;
    String canvasId;
    String art_by;

    public ChatObject(){}

    public ChatObject(String message, String sender,String receiver, String time, String key, String type,String canvasImage,
                      String canvasId, String art_by) {
        this.message = message;
        this.sender = sender;
        this.receiver=receiver;
        this.time = time;
        this.key = key;
        this.type = type;
        this.canvasImage=canvasImage;
        this.canvasId=canvasId;
        this.art_by=art_by;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTime() {
        return time;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getCanvasImage() {
        return canvasImage;
    }

    public void setCanvasImage(String canvasImage) {
        this.canvasImage = canvasImage;
    }

    public String getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(String canvasId) {
        this.canvasId = canvasId;
    }

    public String getArt_by() {
        return art_by;
    }

    public void setArt_by(String art_by) {
        this.art_by = art_by;
    }
}
