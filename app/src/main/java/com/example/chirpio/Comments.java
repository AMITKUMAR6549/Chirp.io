package com.example.chirpio;

import java.io.Serializable;

public class Comments implements Serializable {

    String name,comment,date;

    public Comments(String name, String comment, String date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    public Comments() {
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
