package com.online.davincii.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderData {
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

    public String getCollectorId() {
        return collectorId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getTotal() {
        return total;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
