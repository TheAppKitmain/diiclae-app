package com.online.davincii.models.addresses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("building_name")
    @Expose
    private String buildingName;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zip")
    @Expose
    private String zip;

    public AddressRequest(Integer id) {
        this.id = id;
    }

    public AddressRequest(String name, String address, String phoneNumber, String buildingName, String state, String city, String zip) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.buildingName = buildingName;
        this.state = state;
        this.city = city;
        this.zip = zip;
    }

    public AddressRequest(Integer id, String name, String address, String phoneNumber, String buildingName, String state, String city, String zip) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.buildingName = buildingName;
        this.state = state;
        this.city = city;
        this.zip = zip;
    }
}
