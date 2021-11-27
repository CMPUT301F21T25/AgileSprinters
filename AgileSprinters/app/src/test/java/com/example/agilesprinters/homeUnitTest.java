package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * This class provides unit testing for a mock habit list. We tried to get this to work with the
 * database but could not figure out how
 *
 * @author Hannah Desmarais
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
                "Date", weekdays, "Public", 1, 0);

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
                "Date", weekdays, "Public" ,0,1);
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
                "Date", weekdays, "Public", 0,1);

        habitList.editHabit(0, habit);
        assertEquals(1, habitList.getHabits().size());
        assertTrue(habitList.getHabits().contains(habit));
    }

    /**
     * This tests that exceptions are thrown when appropriate while attempting to edit a habit.
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
                "Date", weekdays, "Public",1,1);
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

    /**
     * This is a test which will make sure that a habit can be deleted from the list.
     */
    @Test
    void testDeleteHabit(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);

        habitList.deleteHabit(habitList.getHabits().get(0));
        assertEquals(0, habitList.getHabits().size());
    }

    /**
     * This test will make sure that exceptions are thrown when appropriate while attempting to
     * delete a habit.
     */
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
                "Date", weekdays, "Public",0,1);

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

    /**
     * This is a method which wil test whether a habit can be moved to a new location and that
     * the other items will not become scrambled in the process.
     */
    @Test
    void testReorderHabit() {
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);

        Habit habit2 = mockHabit();
        habit2.setListPosition(1);
        habit2.setTitle("Swimming");
        habitList.addHabit(habit2);

        Habit habit3 = mockHabit();
        habit3.setListPosition(2);
        habit3.setTitle("Cleaning");
        habitList.addHabit(habit3);

        //Test that all habits are in their assigned positions
        assertTrue(habitList.getHabits().size() == 3);
        assertTrue(habitList.getHabits().get(0).getTitle().matches("Running"));
        assertTrue(habitList.getHabits().get(1).getTitle().matches("Swimming"));
        assertTrue(habitList.getHabits().get(2).getTitle().matches("Cleaning"));

        //Check that a habit can move from the bottom to the top of the list
        habitList.reorderHabit(habit3, 0);

        assertTrue(habitList.getHabits().get(0).getTitle().matches("Cleaning"));
        assertTrue(habitList.getHabits().get(1).getTitle().matches("Running"));
        assertTrue(habitList.getHabits().get(2).getTitle().matches("Swimming"));

        //Check that a habit may move from the bottom to the top of the list
        habitList.reorderHabit(habit3, 2);

        assertTrue(habitList.getHabits().get(2).getTitle().matches("Cleaning"));
        assertTrue(habitList.getHabits().get(0).getTitle().matches("Running"));
        assertTrue(habitList.getHabits().get(1).getTitle().matches("Swimming"));

        //Check that a habit may move from the middle to the top
        habitList.reorderHabit(habit2, 0);

        assertTrue(habitList.getHabits().get(2).getTitle().matches("Cleaning"));
        assertTrue(habitList.getHabits().get(1).getTitle().matches("Running"));
        assertTrue(habitList.getHabits().get(0).getTitle().matches("Swimming"));

        //Check that a habit may move from the middle to the bottom
        habitList.reorderHabit(habit2, 1);
        habitList.reorderHabit(habit2, 2);

        assertTrue(habitList.getHabits().get(1).getTitle().matches("Cleaning"));
        assertTrue(habitList.getHabits().get(0).getTitle().matches("Running"));
        assertTrue(habitList.getHabits().get(2).getTitle().matches("Swimming"));
    }

    /**
     * This is w method that will test that exceptions will be thrown if arguments are given
     * that will cause the reorderHabit() method to break.
     */
    @Test
    void testReorderHabitExceptions(){
        HabitTestList habitList = mockHabitList();
        assertTrue(habitList.getHabits().size() == 1);

        habitList.deleteHabit(habitList.getHabits().get(0));
        assertTrue(habitList.getHabits().size() == 0);

        Habit habit = mockHabit();

        //Check that an empty list cannot be reordered
        assertThrows(IllegalArgumentException.class, () -> {
            habitList.reorderHabit(habit, 0);
        });

        habitList.addHabit(habit);

        Habit habit1 = mockHabit();
        habit1.setTitle("Swimming");

        //Check that a habit cannot be moved that doesn't exist in the list
        assertThrows(IllegalArgumentException.class, () ->{
            habitList.reorderHabit(habit1, 0);
        });

        habitList.addHabit(habit1);

        //Check that a habit cannot be moved to a negative position
        assertThrows(IndexOutOfBoundsException.class, () ->{
           habitList.reorderHabit(habit1, -1);
        });

        //Check that a habit cannot be moved to a position that is larger than the list
        assertThrows(IndexOutOfBoundsException.class, () -> {
           habitList.reorderHabit(habit1, 2);
        });
    }
}
