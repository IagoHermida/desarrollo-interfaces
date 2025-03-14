package com.example.aerocatalogo.models;

public class Item {
    private String title;
    private String description;
    private String url;
    private String id;

    public Item() {} // Constructor vacío necesario para Firebase

    public Item(String id, String title, String description, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
