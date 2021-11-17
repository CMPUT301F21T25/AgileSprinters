/**
 * This activity is used when a user is signing up for the app. It takes the user details and
 * creates an account fo them in the authentication database.
 *
 * @author Hari Bheesetti
 */
package com.example.agilesprinters;

import java.io.Serializable;

/**
 * This is an object class for a user called User. It is responsible for storing user information.
 *
 * @author Hari Bheesetti
 */
public class User implements Serializable {

    private String userID;

    public String getUser() {
        return userID;
    }

    public void setUser(String user) {
        this.userID = user;
    }
}
