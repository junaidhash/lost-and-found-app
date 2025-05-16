package com.example.lostandfoundnew;

public class Item {
    private int id;
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    // Updated Constructor
    public Item(int id, String postType, String name, String phone,
                String description, String date, String location) {
        this.id = id;
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // Getters
    public int getId() { return id; }
    public String getPostType() { return postType; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
}