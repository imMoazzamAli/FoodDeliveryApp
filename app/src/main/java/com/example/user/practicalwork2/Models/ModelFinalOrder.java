package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelFinalOrder implements Serializable {

    private int orderQuantity;
    private String orderName;
    private int orderPrice;

    private String orderDate;
    private String orderTime;

    private String orderCompanyName;
    private String orderCompanyUrl;

    private String orderUserName;
    private String orderUserEmail;
    private String orderUserNumber;


    public ModelFinalOrder() {
    }

    public ModelFinalOrder(int orderQuantity, String orderName, int orderPrice, String orderDate, String orderTime, String orderCompanyName, String orderCompanyUrl, String orderUserName, String orderUserEmail, String orderUserNumber) {
        this.orderQuantity = orderQuantity;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderCompanyName = orderCompanyName;
        this.orderCompanyUrl = orderCompanyUrl;
        this.orderUserName = orderUserName;
        this.orderUserEmail = orderUserEmail;
        this.orderUserNumber = orderUserNumber;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public String getOrderName() {
        return orderName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderCompanyName() {
        return orderCompanyName;
    }

    public String getOrderCompanyUrl() {
        return orderCompanyUrl;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public String getOrderUserEmail() {
        return orderUserEmail;
    }

    public String getOrderUserNumber() {
        return orderUserNumber;
    }
}
