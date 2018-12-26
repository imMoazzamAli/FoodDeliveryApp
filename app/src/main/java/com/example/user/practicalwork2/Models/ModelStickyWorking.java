package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelStickyWorking implements Serializable {

    private int mId;
    private int mPrice;

    private String mName, mDescription, mCompanyName;

    private ModelMainMenu modelMainMenu;

    public ModelStickyWorking() {
    }

    public ModelStickyWorking(int mId, int mPrice, String mName, String mDescription, String mCompanyName, String mmName, ModelMainMenu modelMainMenu) {
        this.mId = mId;
        this.mPrice = mPrice;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCompanyName = mCompanyName;
        this.modelMainMenu = modelMainMenu;
    }

    public int getmId() {
        return mId;
    }

    public int getmPrice() {
        return mPrice;
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public int getMmId() {
        return modelMainMenu.getId();
    }

    public String getMmName() {
        return modelMainMenu.getMmName();
    }

    public String getMmCompanyTitle() {
        return modelMainMenu.getMmcompanyTitle();
    }

}
