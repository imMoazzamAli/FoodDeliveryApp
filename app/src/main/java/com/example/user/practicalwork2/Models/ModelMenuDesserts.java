package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelMenuDesserts implements Serializable {

    private int dessertId;

    private String dessertName, dessertDescription, dessertcompanyName;

    private int dessertPrice;


    public ModelMenuDesserts() {
    }

    public ModelMenuDesserts(int dessertId, String dessertName, String dessertDescription, String dessertcompanyName, int dessertPrice) {
        this.dessertId = dessertId;
        this.dessertName = dessertName;
        this.dessertDescription = dessertDescription;
        this.dessertcompanyName = dessertcompanyName;
        this.dessertPrice = dessertPrice;
    }

    public int getDessertId() {
        return dessertId;
    }

    public String getDessertName() {
        return dessertName;
    }

    public String getDessertDescription() {
        return dessertDescription;
    }

    public String getDessertcompanyName() {
        return dessertcompanyName;
    }

    public int getDessertPrice() {
        return dessertPrice;
    }

    public void setDessertId(int dessertId) {
        this.dessertId = dessertId;
    }

    public void setDessertName(String dessertName) {
        this.dessertName = dessertName;
    }

    public void setDessertDescription(String dessertDescription) {
        this.dessertDescription = dessertDescription;
    }

    public void setDessertcompanyName(String dessertcompanyName) {
        this.dessertcompanyName = dessertcompanyName;
    }

    public void setDessertPrice(int dessertPrice) {
        this.dessertPrice = dessertPrice;
    }
}
