package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is a class which tests the Habit Instance object class
 *
 * @author Riyaben Patel
 */
public class habitInstanceUnitTest {
    private String EID = "34567890";
    private String UID = "23456789";
    private String HID = "123456789";
    private String IID = null;
    private String FID = "456789012";
    private String opt_comment = "Read 5 pages";
    private String dateOfEvent = "11/05/2021";
    private int duration = 30;
    private Boolean isShared = false;
    private String optLoc = "Edmonton";

    /**
     * This method creates a mock Habit Instance object for testing
     * @return returns a habit instance object
     */
    private HabitInstance mockHabitInstance(){

        HabitInstance habitInstance = new HabitInstance(EID,UID,HID, opt_comment, dateOfEvent, duration,IID, FID, isShared, optLoc);

        return habitInstance;
    }

    /**
     * This is a test that checks if the getters in the Habit Instance class are returning
     * the same values that their parameters were set to
     */
    @Test
    void testHabitInstanceGetters(){
        HabitInstance habitInstance = mockHabitInstance();
        assertEquals(EID, habitInstance.getEID());
        assertEquals(UID, habitInstance.getUID());
        assertEquals(HID, habitInstance.getHID());
        assertEquals(IID, habitInstance.getIID());
        assertEquals(FID, habitInstance.getFID());
        assertEquals(opt_comment, habitInstance.getOpt_comment());
        assertEquals(dateOfEvent, habitInstance.getDate());
        assertEquals(duration, habitInstance.getDuration());
        assertEquals(isShared, habitInstance.getShared());
        assertEquals(optLoc, habitInstance.getOptLoc());
    }

    /**
     * This is a method which tests if the setters in the Habit Instance class
     * actually set the parameters called to the values given
     */
    @Test
    void testHabitInstanceSetters(){
        HabitInstance habitInstance = mockHabitInstance();

        habitInstance.setOpt_comment("Hike in mountains");
        assertEquals("Hike in mountains", habitInstance.getOpt_comment());
        habitInstance.setDate("11/04/2021");
        assertEquals("11/04/2021", habitInstance.getDate());
        habitInstance.setDuration(90);
        assertEquals(90, habitInstance.getDuration());
        habitInstance.setShared(true);
        assertEquals(true, habitInstance.getShared());
        habitInstance.setOptLoc("Vancouver");
        assertEquals("Vancouver", habitInstance.getOptLoc());
        habitInstance.setHID("1");
        assertEquals("1", habitInstance.getHID());
        habitInstance.setUID("2");
        assertEquals("2", habitInstance.getUID());
        habitInstance.setEID("3");
        assertEquals("3", habitInstance.getEID());
        habitInstance.setIID("4");
        assertEquals("4", habitInstance.getIID());
        habitInstance.setFID("5");
        assertEquals("5", habitInstance.getFID());
    }
}