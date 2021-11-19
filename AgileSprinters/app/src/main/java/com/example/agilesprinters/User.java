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
    private String firstName;

    public User() { }

    public User(String userID, String firstName, String lastName, String emailId) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    private String lastName;
    private String emailId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUser() {
        return userID;
    }

    public void setUser(String user) {
        this.userID = user;
    }
}
