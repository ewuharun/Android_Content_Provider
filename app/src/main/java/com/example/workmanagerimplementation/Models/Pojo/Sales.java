package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 24,March,2021
 * BABL, Bangladesh,
 */
public class Sales {
    private int sales_order_id;
    private String so_oracle_id;
    private String dealer_name;
    private String name;
    private String order_date;
    private String order_date_time;
    private String delivery_date;

    public Sales(int sales_order_id, String so_oracle_id, String dealer_name, String name, String order_date, String order_date_time, String delivery_date) {
        this.sales_order_id = sales_order_id;
        this.so_oracle_id = so_oracle_id;
        this.dealer_name = dealer_name;
        this.name = name;
        this.order_date = order_date;
        this.order_date_time = order_date_time;
        this.delivery_date = delivery_date;
    }

    public void setSales_order_id(int sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public void setSo_oracle_id(String so_oracle_id) {
        this.so_oracle_id = so_oracle_id;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public int getSales_order_id() {
        return sales_order_id;
    }

    public String getSo_oracle_id() {
        return so_oracle_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public String getName() {
        return name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }
}
