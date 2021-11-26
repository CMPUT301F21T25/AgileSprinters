package com.example.agilesprinters;

import java.util.ArrayList;

public class NotificationsUnitTest {

    private User mockUser(){
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> following = new ArrayList<>();
        ArrayList<String> followerRequests = new ArrayList<>();
        User user = new User("Test", "TestFN","TestLN", "Test@email.com"
                , followers, following, followerRequests);
        return user;
    }



}
