package com.kalu.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {
    private  String postKey;
    private String title;
    private String description;
    private String pictures;
    private String userId;
    private String userPhoto;
    private Object timestamp;

    public Post(String title, String description, String pictures, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
        this.pictures = pictures;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post() {

    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPictures() {
        return pictures;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
