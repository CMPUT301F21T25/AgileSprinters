package com.example.agilesprinters;

import java.util.ArrayList;

public class NotificationTestList {
    private ArrayList<User> notificationList = new ArrayList<>();

    public void addNotification(User requestingUser, User currentUser){
        if (notificationList.contains(requestingUser)){
            throw new IllegalArgumentException();
        }
        notificationList.add(requestingUser);
        currentUser.getFollowRequestList().add(requestingUser.getUserID());
    }

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

    public ArrayList<User> getRequestingUsers() {
        ArrayList<User> list = notificationList;
        return list;
    }
}
