package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is a test class which tests the methods within the User object class.
 *
 * @author Hannah Desmarais
 */
public class userUnitTest {
    private String UID = "TEST";

    /**
     * This creates a mock user for testing
     * @return returns a User object
     */
    private User mockUser(){
        User user = new User();
        user.setUser(UID);
        return user;
    }

    /**
     * This is a test method which tests if the getters for the mock user object match what they
     * were set to
     */
    @Test
    void testUserGetter(){
        User user = mockUser();
        assertEquals(UID, user.getUser());
    }

    /**
     * This is a test method which tests if the setters are setting values to the ones specified
     */
    @Test
    void testUserSetter(){
        User user = mockUser();
        user.setUser("TEST SETTER");
        assertEquals("TEST SETTER", user.getUser());
    }
}
