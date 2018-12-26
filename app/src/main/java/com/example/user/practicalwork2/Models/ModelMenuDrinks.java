package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelMenuDrinks implements Serializable {

    int drinkId;

    private String drinkName, drinkDescription, drinkcompanyName;

    private int drinkPrice;

    public ModelMenuDrinks() {
    }

    public ModelMenuDrinks(int drinkId, String drinkName, String drinkDescription, String drinkcompanyName, int drinkPrice) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.drinkDescription = drinkDescription;
        this.drinkcompanyName = drinkcompanyName;
        this.drinkPrice = drinkPrice;
    }

    public int getDrinkId() {
        return drinkId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public String getDrinkDescription() {
        return drinkDescription;
    }

    public String getDrinkcompanyName() {
        return drinkcompanyName;
    }

    public int getDrinkPrice() {
        return drinkPrice;
    }
}
