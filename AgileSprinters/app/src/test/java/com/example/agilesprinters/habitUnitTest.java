package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * This is a class which tests the Habit object class
 *
 * @author Hannah Desmarais
 */
public class habitUnitTest {
    private String UID = "23456789";
    private String HID = "123456789";
    private String title = "Running";
    private String reason = "Get fit";
    private String dateToStart = "11/03/2021";
    private HashMap<String, Boolean> weekdays = new HashMap<>();
    private String privacySetting = "Private";

    /**
     * This method creates a mock Habit object for testing
     * @return returns a habit object
     */
    private Habit mockHabit(){
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit(HID,UID, title, reason, dateToStart, weekdays, privacySetting, 0, 0);

        return habit;
    }

    /**
     * This is a test that checks if the getters in the Habit class are returning the same values
     * that their parameters were set to
     */
    @Test
    void testHabitGetters(){
        Habit habit = mockHabit();
        assertEquals(UID, habit.getUID());
        assertEquals(HID, habit.getHID());
        assertEquals(title, habit.getTitle());
        assertEquals(reason, habit.getReason());
        assertEquals(weekdays, habit.getWeekdays());
        assertEquals(privacySetting, habit.getPrivacySetting());
        assertEquals(dateToStart, habit.getDateToStart());
    }

    /**
     * This is a method which tests if the setters in the Habit class actually set the parameters
     * called to the values given
     */
    @Test
    void testHabitSetters(){
        Habit habit = mockHabit();

        habit.setTitle("Hiking");
        assertEquals("Hiking", habit.getTitle());
        habit.setPrivacySetting("Private");
        assertEquals("Private", habit.getPrivacySetting());
        habit.setReason("See sights");
        assertEquals("See sights", habit.getReason());
        habit.setDateToStart("11/02/2021");
        assertEquals("11/02/2021", habit.getDateToStart());
        habit.setHID("1");
        assertEquals("1", habit.getHID());
        habit.setUID("2");
        assertEquals("2", habit.getUID());

        HashMap<String, Boolean> newWeekdays = new HashMap<>();
        weekdays.put("MONDAY", false);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);
        habit.setWeekdays(newWeekdays);
        assertEquals(newWeekdays, habit.getWeekdays());
    }
}
