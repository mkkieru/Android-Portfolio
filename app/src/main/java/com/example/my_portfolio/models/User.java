package com.example.my_portfolio.models;

import org.parceler.Parcel;

@Parcel
public class User {

    private String id;
    private String name;
    private String imageUrl;
    private String profession;
    private String phone;
    private String status;

    public User() {
    }

    public User(String id, String name, String imageUrl, String profession, String phone, String status) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.profession = profession;
        this.phone = phone;
        this.status = status;
    }

    public User(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfession() {
        return profession;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
