package com.online.davincii.models.addresses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("address")
    @Expose
    private List<ListAddress> address = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public List<ListAddress> getAddress() {
        return address;
    }

    public String getMessage() {
        return message;
    }
}
