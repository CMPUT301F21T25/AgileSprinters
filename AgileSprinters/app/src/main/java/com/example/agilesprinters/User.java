package com.example.agilesprinters;

import java.io.Serializable;

public class User implements Serializable {

    private String userID;

    public String getUser() {
        return userID;
    }

    public void setUser(String user) {
        this.userID = user;
    }
}
