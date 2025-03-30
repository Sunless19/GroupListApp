package com.example.groupmembersapp;

public class Member {
    private int id;
    private String name;
    private String role;
    private String imageUrl;
    private String details;
    private String webUrl;

    public Member(int id, String name, String role, String imageUrl, String details, String webUrl) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.imageUrl = imageUrl;
        this.details = details;
        this.webUrl = webUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getImageUrl() { return imageUrl; }
    public String getDetails() { return details; }
    public String getWebUrl() { return webUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDetails(String details) { this.details = details; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }

}
