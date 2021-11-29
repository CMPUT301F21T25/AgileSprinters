package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This is a test class which tests the methods within the User object class.
 *
 * @author Hannah Desmarais
 */
public class UserUnitTest {
    ArrayList<String> followerList = new ArrayList<>();
    ArrayList<String> followingList = new ArrayList<>();
    ArrayList<String> followRequestList = new ArrayList<>();
    /**
     * This creates a mock user for testing
     * @return returns a User object
     */
    private User mockUser(){
        User user = new User("TEST", "TEST", "TEST", "TEST", followerList, followingList, followRequestList);
        return user;
    }

    /**
     * This is a test method which tests if the getters for the mock user object match what they
     * were set to
     */
    @Test
    void testUserGetter(){
        User user = mockUser();
        assertEquals("TEST", user.getUserID());
    }

    @Test
    void testFirstNameGetter(){
        User user = mockUser();
        assertEquals("TEST", user.getFirstName());
    }

    @Test
    void testLastNameGetter(){
        User user = mockUser();
        assertEquals("TEST", user.getLastName());
    }

    @Test
    void testEmailGetter(){
        User user = mockUser();
        assertEquals("TEST", user.getEmailId());
    }

    @Test
    void testFollowerListGetter(){
        User user = mockUser();
        assertEquals(followerList, user.getFollowersList());
    }

    @Test
    void testFollowingListGetter(){
        User user = mockUser();
        assertEquals(followingList, user.getFollowingList());
    }

    @Test
    void testFollowReqListGetter(){
        User user = mockUser();
        assertEquals(followRequestList, user.getFollowRequestList());
    }

    /**
     * This is a test method which tests if the setters are setting values to the ones specified
     */
    @Test
    void testUserSetter(){
        User user = mockUser();
        user.setUserID("TEST SETTER");
        assertEquals("TEST SETTER", user.getUserID());
    }

    @Test
    void testFirstNameSetter(){
        User user = mockUser();
        user.setFirstName("TEST SETTER");
        assertEquals("TEST SETTER", user.getFirstName());
    }

    @Test
    void testLastNameSetter(){
        User user = mockUser();
        user.setLastName("TEST SETTER");
        assertEquals("TEST SETTER", user.getLastName());
    }

    @Test
    void testEmailSetter(){
        User user = mockUser();
        user.setEmailId("TEST SETTER");
        assertEquals("TEST SETTER", user.getEmailId());
    }

    @Test
    void testFollowerListSetter(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        User user = mockUser();
        user.setFollowersList(list);
        assertEquals(list, user.getFollowersList());
    }

    @Test
    void testFollowingListSetter(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        User user = mockUser();
        user.setFollowingList(list);
        assertEquals(list, user.getFollowingList());
    }

    @Test
    void testFollowReqListSetter(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        User user = mockUser();
        user.setFollowRequestList(list);
        assertEquals(list, user.getFollowRequestList());
    }
}
