package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class userUnitTest {
    private String UID = "TEST";

    private User mockUser(){
        User user = new User();
        user.setUser(UID);
        return user;
    }

    @Test
    void testUserGetter(){
        User user = mockUser();
        assertEquals(UID, user.getUser());
    }

    @Test
    void testUserSetter(){
        User user = mockUser();
        user.setUser("TEST SETTER");
        assertEquals("TEST SETTER", user.getUser());
    }
}
