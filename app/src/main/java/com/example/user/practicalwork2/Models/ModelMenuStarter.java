package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelMenuStarter implements Serializable {

    private int starterId;
    private int starterPrice;

    private String starterName, starterDescription, startercompanyName;

    public ModelMenuStarter() {
    }

    public ModelMenuStarter(int starterId, int starterPrice, String starterName, String starterDescription, String startercompanyName) {
        this.starterId = starterId;
        this.starterPrice = starterPrice;
        this.starterName = starterName;
        this.starterDescription = starterDescription;
        this.startercompanyName = startercompanyName;
    }

    public int getStarterId() {
        return starterId;
    }

    public int getStarterPrice() {
        return starterPrice;
    }

    public String getStarterName() {
        return starterName;
    }

    public String getStarterDescription() {
        return starterDescription;
    }

    public String getStartercompanyName() {
        return startercompanyName;
    }
}
