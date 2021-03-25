package com.example.workmanagerimplementation.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Md.harun or rashid on 24,March,2021
 * BABL, Bangladesh,
 */
public class Sales {
    @SerializedName("sales_order_id")
    @Expose
    private int salesOrderId;
    @SerializedName("so_oracle_id")
    @Expose
    private String soOracleId;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_date_time")
    @Expose
    private String orderDateTime;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;

    public int getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(int salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public String getSoOracleId() {
        return soOracleId;
    }

    public void setSoOracleId(String soOracleId) {
        this.soOracleId = soOracleId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

}
