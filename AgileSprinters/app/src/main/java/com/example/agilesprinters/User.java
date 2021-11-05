/**
 * This activity is used when a user is signing up for the app. It takes the user details and
 * creates an account fo them in the authentication database.
 *
 * @author Hari Bheesetti
 */
package com.example.agilesprinters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
