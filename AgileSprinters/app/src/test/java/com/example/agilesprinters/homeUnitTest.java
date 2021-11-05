package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * This class provides unit testing for a mock habit list. We tried to get this to work with the
 * database but could not figure out how
 */
public class homeUnitTest {
    /**
     * This method creates a mock list for the test cases to work from
     * @return returns the habit list created
     */
    private HabitTestList mockHabitList(){
        HabitTestList habitList = new HabitTestList();
        habitList.addHabit(mockHabit());
        return habitList;
    }

    /**
     * This method creates a mock habit to insert in the mock city list
     * @return returns the habit created
     */
    private Habit mockHabit(){
        HashMap<String, Boolean> weekdays= new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit("Test0","Test0","Running","Get fit",
                "Date", weekdays, "Public");

        return habit;
    }


    /**
     * This test will test if the add habit function is working correctly
     */
    @Test
    void testAddHabit(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);
        HashMap<String,Boolean> weekdays = new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit("Test", "Test", "Running", "Get Fit",
                "Date", weekdays, "Public" );
        habitList.addHabit(habit);
        assertEquals(2, habitList.getHabits().size());
        assertTrue(habitList.getHabits().contains(habit));
    }

    /**
     * This test will try to edit a habit in the list
     */
    @Test
    void testEditHabit(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);
        HashMap<String,Boolean> weekdays = new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit("Test", "Test", "Running", "Get Fit",
                "Date", weekdays, "Public" );

        habitList.editHabit(0, habit);
        assertEquals(1, habitList.getHabits().size());
        assertTrue(habitList.getHabits().contains(habit));
    }

    /**
     * This tests that exceptions are thrown when appropriate
     */
    @Test
    void testEditHabitException() {
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);
        //make the list empty
        habitList.deleteHabit(habitList.getHabits().get(0));

        HashMap<String, Boolean> weekdays = new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit("Test", "Test", "Running", "Get Fit",
                "Date", weekdays, "Public");
        //list is empty so test that you cannot edit an empty list
        assertThrows(IllegalArgumentException.class, () -> {
            habitList.editHabit(0, habit);
        });

        //add a habit to the list
        habitList.addHabit(habit);
        assertEquals(1, habitList.getHabits().size());
        //Test that you cannot edit the list in a position that is out of bounds
        assertThrows(IndexOutOfBoundsException.class, () -> {
            habitList.editHabit(1, habit);
        });
    }

    @Test
    void testDeleteHabit(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);

        habitList.deleteHabit(habitList.getHabits().get(0));
        assertEquals(0, habitList.getHabits().size());
    }

    @Test
    void testDeleteHabitException(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);

        HashMap<String, Boolean> weekdays = new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        Habit habit = new Habit("Test", "Test", "Running", "Get Fit",
                "Date", weekdays, "Public");

        //test that we cannot delete a habit that does not exist in the list
        assertThrows(IllegalArgumentException.class, () -> {
            habitList.deleteHabit(habit);
        });

        //make the list empty
        habitList.deleteHabit(habitList.getHabits().get(0));
        //check that we cannot delete anything from an empty list
        assertThrows(IllegalArgumentException.class, () -> {
            habitList.deleteHabit(habit);
        });
    }
}
