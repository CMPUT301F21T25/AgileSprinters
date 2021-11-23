/**
 * This activity is used when a user is signing up for the app. It takes the user details and
 * creates an account fo them in the authentication database.
 *
 * @author Hari Bheesetti
 */
package com.example.agilesprinters;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is an object class for a user called User. It is responsible for storing user information.
 *
 * @author Hari Bheesetti
 */
public class User implements Serializable {

    private String userID;
    private String firstName;
    private String lastName;
    private String emailId;
    private ArrayList<String> followersList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequestList;
    private String profilePhotoPath;

    public User() { }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public User(String userID, String firstName, String lastName, String emailId, ArrayList<String> followersList, ArrayList<String> followingList, ArrayList<String> followRequestList, String profilePhotoPath) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.followersList = followersList;
        this.followingList = followingList;
        this.followRequestList = followRequestList;
        this.profilePhotoPath = profilePhotoPath;
    }

    public ArrayList<String> getFollowRequestList() {
        return followRequestList;
    }

    public void setFollowRequestList(ArrayList<String> followRequestList) {
        this.followRequestList = followRequestList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<String> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(ArrayList<String> followersList) {
        this.followersList = followersList;
    }

    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

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
