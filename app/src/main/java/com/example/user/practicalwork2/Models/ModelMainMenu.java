package com.example.user.practicalwork2.Models;

public class ModelMainMenu {

    private int id;
    private String mmName, mmcompanyTitle;

    public ModelMainMenu() {
        //needed for firebase work
        //without this empty constructor firebase will not work
    }

    public ModelMainMenu(int id, String mmName, String mmcompanyTitle) {
        this.id = id;
        this.mmName = mmName;
        this.mmcompanyTitle = mmcompanyTitle;
    }

    public int getId() {
        return id;
    }

    public String getMmName() {
        return mmName;
    }

    public String getMmcompanyTitle() {
        return mmcompanyTitle;
    }
}
