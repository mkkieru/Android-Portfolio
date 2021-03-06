package com.example.my_portfolio.models;

import org.parceler.Parcel;

@Parcel
public class User {

    private String name;
    private String imageUrl;
    private String profession;
    private String phone;

    public User() {
    }

    public User(String name, String imageUrl, String profession, String phone) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.profession = profession;
        this.phone = phone;
    }

    private Double rating;

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
}
