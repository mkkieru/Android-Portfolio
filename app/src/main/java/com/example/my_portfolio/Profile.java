package com.example.my_portfolio;

public class Profile {

    private String name;
    private String status;
    private String location;
    private String number;
    private String picStorage;
    private String mail;
    private String description;

    public Profile(String name, String status, String location, String number, String picStorage, String mail, String description) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.number = number;
        this.picStorage = picStorage;
        this.mail = mail;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPicStorage() {
        return picStorage;
    }

    public void setPicStorage(String picStorage) {
        this.picStorage = picStorage;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
