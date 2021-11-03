package com.example.agilesprinters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    private String userID;

    public String getUser() {
        System.out.println("user"+userID);
        return userID;
    }

    public void setUser(String user) {
        this.userID = user;
    }
}
