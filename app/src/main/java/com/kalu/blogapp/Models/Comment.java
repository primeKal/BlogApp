package com.kalu.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Comment {
    private  String uname, uimg,uid,content;
    private Object timestamp;

    public Comment() {
    }

    public Comment(String name, String uid, String uimg, String content) {
        this.uname = name;
        this.uid = uid;
        this.content = content;
        this.uimg=uimg;
        this.timestamp= ServerValue.TIMESTAMP;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return uname;
    }

    public void setName(String name) {
        this.uname = name;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
