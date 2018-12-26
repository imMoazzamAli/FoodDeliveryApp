package com.example.user.practicalwork2.Models;

import java.io.Serializable;

public class ModelExpandAble implements Serializable {

    private int mId;
    private int mPrice;

    private String mName, mDescription, mCompanyName, mmName;

    public ModelExpandAble() {
    }

    public ModelExpandAble(int mId, int mPrice, String mName, String mDescription, String mCompanyName, String mmName) {
        this.mId = mId;
        this.mPrice = mPrice;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCompanyName = mCompanyName;
        this.mmName = mmName;
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

    public String getMmName() {
        return mmName;
    }
}
