package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("collector_id")
    @Expose
    private String collectorId;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("delivery_address_id")
    @Expose
    private Integer deliveryAddressId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
