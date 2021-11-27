package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class ForumUnitTest {
    private String firstName = "Riya";
    private String lastName = "Patel";
    private String dateOfEvent = "11/05/2021";
    private String duration = "30";
    private String comment = "5 minutes journaling";
    private String location = "Edmonton";
    private String IID = "12345678";

    /**
     * This method creates a mock Forum object for testing
     * @return returns a forum object
     */
    private Forum mockForumInstance(){

        Forum forumInstance = new Forum(firstName, lastName, dateOfEvent, duration, comment, IID, location);

        return forumInstance;
    }

    /**
     * This is a test that checks if the getters in the Forum class are returning
     * the same values that their parameters were set to
     */
    @Test
    void testForumInstanceGetters(){
        Forum forumInstance = mockForumInstance();
        assertEquals(firstName, forumInstance.getFirstName());
        assertEquals(lastName, forumInstance.getLastName());
        assertEquals(dateOfEvent, forumInstance.getEventDate());
        assertEquals(duration, forumInstance.getDuration());
        assertEquals(comment, forumInstance.getComment());
        assertEquals(location, forumInstance.getLocation());
        assertEquals(IID, forumInstance.getImage());
    }

    /**
     * This is a method which tests if the setters in the Forum class
     * actually set the parameters called to the values given
     */
    @Test
    void testHabitInstanceSetters(){
        Forum forumInstance = mockForumInstance();

        forumInstance.setFirstName("Sai");
        assertEquals("Sai", forumInstance.getFirstName());
        forumInstance.setLastName("Ajerla");
        assertEquals("Ajerla", forumInstance.getLastName());
        forumInstance.setEventDate("11/10/2021");
        assertEquals("11/10/2021", forumInstance.getEventDate());
        forumInstance.setDuration("50");
        assertEquals("50", forumInstance.getDuration());
        forumInstance.setComment("Writing a book");
        assertEquals("Writing a book", forumInstance.getComment());
        forumInstance.setLocation("Toronto");
        assertEquals("Toronto", forumInstance.getLocation());
        forumInstance.setImage("image 1");
        assertEquals("image 1", forumInstance.getImage());
    }
}
