package com.example.chirpio;

import java.io.Serializable;

public class Posts implements Serializable {

    private String name, post, date,id,user_id;
    private long comment_count;
    private long like_count;

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public Posts(String name, String post, String date, String id, long comment_count, long like_count) {
        this.name = name;
        this.post = post;
        this.date = date;
        this.id = id;
        this.comment_count = comment_count;
        this.like_count = like_count;
    }

    public Posts(String name, String post, String date, String id, String user_id, long comment_count, long like_count) {
        this.name = name;
        this.post = post;
        this.date = date;
        this.id = id;
        this.user_id = user_id;
        this.comment_count = comment_count;
        this.like_count = like_count;
    }

    public Posts() {
    }
    public Posts(String name, String post, String date, int comment_count, int like_count) {
        this.name = name;
        this.post = post;
        this.date = date;
        this.comment_count = comment_count;
        this.like_count = like_count;
    }

    public String getName() {
        return name;
    }

    public String getPost() {
        return post;
    }

    public String getDate() {
        return date;
    }

    public long getComment_count() {
        return comment_count;
    }

    public long getLike_count() {
        return like_count;
    }
}