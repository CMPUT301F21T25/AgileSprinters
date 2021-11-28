package com.example.agilesprinters;

import java.util.ArrayList;

/**
 * This is a class specifically for testing a list of follow requesting.
 *
 * @author Hannah Desmarais
 */
public class NotificationTestList {
    private ArrayList<User> notificationList = new ArrayList<>();

    /**
     * This method will add a user to the notification list and to the current user's follow request
     * list.
     * @param requestingUser The user that is requesting to follow.
     * @param currentUser The user currently signed in the app.
     */
    public void addNotification(User requestingUser, User currentUser){
        if (notificationList.contains(requestingUser)){
            throw new IllegalArgumentException();
        }
        notificationList.add(requestingUser);
        currentUser.getFollowRequestList().add(requestingUser.getUserID());
    }

    /**
     * This method will accept a follow request from another user. It will remove the request and
     * add the requesting user to the current users followers list and the current user to the
     * requesting users following list.
     * @param requestingUser The user requesting to follow.
     * @param currentUser The current user signed into the app.
     */
    public void acceptUser(User requestingUser, User currentUser){
        if (!notificationList.contains(requestingUser)){
            throw new IllegalArgumentException();
        } else if (notificationList.isEmpty()){
            throw new IllegalArgumentException();
        } else {
            notificationList.remove(requestingUser);
            currentUser.getFollowRequestList().remove(requestingUser.getUserID());
            currentUser.getFollowersList().add(requestingUser.getUserID());
            requestingUser.getFollowingList().add(currentUser.getUserID());
        }
    }

    /**
     * This method will decline a follow request. It will only remove the request from the
     * notification list.
     * @param requestingUser The user who is requesting to follow.
     * @param currentUser The user who is currently signed into the app.
     */
    public void declineUser(User requestingUser, User currentUser){
        if(notificationList.isEmpty()){
            throw new IllegalArgumentException();
        } else if (!notificationList.contains(requestingUser)){
            throw new IllegalArgumentException();
        } else{
            notificationList.remove(requestingUser);
            currentUser.getFollowRequestList().remove(requestingUser.getUserID());
        }
    }

    /**
     * This method will get the list of users requesting to follow the current user.
     * @return
     * Returns the list of requesting users as an array list of User objects.
     */
    public ArrayList<User> getRequestingUsers() {
        ArrayList<User> list = notificationList;
        return list;
    }
}
