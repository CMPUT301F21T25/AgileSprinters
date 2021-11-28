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

    /**
     * The constructor without any values.
     */
    public User() {
    }

    /**
     * The constructor with all the values provided.
     */
    public User(String userID, String firstName, String lastName, String emailId, ArrayList<String> followersList, ArrayList<String> followingList, ArrayList<String> followRequestList) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.followersList = followersList;
        this.followingList = followingList;
        this.followRequestList = followRequestList;
    }

    /**
     * This function returns the value of the array list, followRequestList
     *
     * @return
     */
    public ArrayList<String> getFollowRequestList() {
        return followRequestList;
    }

    /**
     * This method will set the followers request list, followRequestList, to a specified value.
     *
     * @param followRequestList
     */
    public void setFollowRequestList(ArrayList<String> followRequestList) {
        this.followRequestList = followRequestList;
    }

    /**
     * This will return the value of the user ID, UID.
     *
     * @return
     */
    public String getUserID() {
        return userID;
    }

    /**
     * This method will set the user ID, UID, to a specified value.
     *
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * This will return the value of the arrayList, followersList.
     *
     * @return
     */
    public ArrayList<String> getFollowersList() {
        return followersList;
    }

    /**
     * This method will set the followers list, followersList, to a specified value.
     *
     * @param followersList
     */
    public void setFollowersList(ArrayList<String> followersList) {
        this.followersList = followersList;
    }

    /**
     * This will return the value of the arrayList, followingList.
     *
     * @return
     */
    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    /**
     * This method will set the following list, followingList, to a specified value.
     *
     * @param followingList
     */
    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

    /**
     * This will return the value of the user firstname, firstName.
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method will set the first name, firstName, to a specified value.
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This will return the value of the user last name, lastName.
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method will set the last name, lastName, to a specified value.
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This will return the value of the user's email Id, emailID.
     *
     * @return
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * This method will set the email ID, emailID, to a specified value.
     *
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
