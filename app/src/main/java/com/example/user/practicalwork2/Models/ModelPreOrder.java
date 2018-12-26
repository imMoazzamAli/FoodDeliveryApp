package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelPreOrder implements Serializable {

    private int price;
    private String title;
    private String description;

    private String companyName;

    public ModelPreOrder() {
    }

    public ModelPreOrder(int price, String title, String description, String companyName) {
        this.price = price;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
    }

    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyName() {
        return companyName;
    }
}
