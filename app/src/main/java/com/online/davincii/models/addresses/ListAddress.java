package com.online.davincii.models.addresses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListAddress {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("buildingname")
    @Expose
    private String buildingname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getAddress() {
        return address;
    }

    public String getBuildingname() {
        return buildingname;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
