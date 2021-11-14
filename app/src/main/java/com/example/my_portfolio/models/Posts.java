package com.example.my_portfolio.models;

public class Posts {
    private String id;
    private String name;
    private String caption;
    private String profileImageUrl;
    private String postImageUrl;

    public Posts() {
    }

    public Posts(String id, String name, String caption, String profileImageUrl, String postImageUrl) {
        this.id = id;
        this.name = name;
        this.caption = caption;
        this.profileImageUrl = profileImageUrl;
        this.postImageUrl = postImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }
}
