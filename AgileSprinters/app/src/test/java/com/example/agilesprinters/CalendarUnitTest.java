package com.example.agilesprinters;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.HashMap;

/**
 * This class provides testing for the calendar activity
 *
 * @author Sai Rasazna Ajerla
 */
class CalendarUnitTest {
    /**
     * This creates a new mock list initiated with one habit event
     * @return
     * Return a new empty list of habits
     /
    private HabitInstanceList mockHabitInstanceList() {
        HabitInstanceList instanceList = new HabitInstanceList();
        instanceList.add(mockHabitInstance());
        return instanceList;
    }

    /**
     * This creates a new mock list used for testing of habit objects
     * @return
     * Return a new empty list of habits

    private Habit_list mockHabitList() {
        return new Habit_list();
    }
/**
    /**
     * This creates a new habit instance
     * @return
     * Return a habit instance
     /
    private HabitInstance mockHabitInstance() {
        return new HabitInstance("uniqueInstanceID1", "uniqueUserID1", "uniqueHabitID1",
                "Read 5 pages", "11/04/2021", 30, null, null, Boolean.TRUE);
    }

    /**
     * This test will check to make sure that all the to do habits displayed
     * are planned for that day (user story 01.07.01)
     /
    @Test
    void testSeeHabitsToDoToday() throws ParseException {
        Habit_list myHabitsForToday = mockHabitList();

        // create a map of weekdays to show which days the habit should take place
        HashMap<String, Boolean> weekdays = new HashMap<>();
        weekdays.put("MONDAY", true);
        weekdays.put("TUESDAY", false);
        weekdays.put("WEDNESDAY", true);
        weekdays.put("THURSDAY", true);
        weekdays.put("FRIDAY", false);
        weekdays.put("SATURDAY", true);
        weekdays.put("SUNDAY", false);

        // Creates a new habit
        Habit habit1 = new Habit("uniqueHabitID1", "uniqueUserID1",
                "Reading 20 pages a day", "To get better at reading",
                "11/01/2021", weekdays, "Public", 0, 0 );

        // Adds the habit if it is planned for today
        myHabitsForToday.addToday(habit1, "11/04/2021", "THURSDAY");
        assertEquals(1, myHabitsForToday.getHabits().size());

        weekdays.put("THURSDAY", false);
        Habit habit2 = new Habit("uniqueHabitID2", "uniqueUserID1", "Go for a walk",
                "To maintain fitness", "10/20/2021", weekdays, "Public", 0, 0 );

        // Throws assertion if the habit is not planned for the current today
        assertThrows(IllegalArgumentException.class, () ->
                myHabitsForToday.addToday(habit2, "11/04/2021", "THURSDAY"));
        assertThrows(IllegalArgumentException.class, () ->
                myHabitsForToday.addToday(habit2, "10/19/2021", "TUESDAY"));

        // Throws assertion if the same habit is added again
        weekdays.put("THURSDAY", true);
        assertThrows(IllegalArgumentException.class, () ->
                myHabitsForToday.addToday(habit1, "11/04/2021", "THURSDAY"));

        myHabitsForToday.addToday(habit2, "11/04/2021", "THURSDAY");

        // Checks to make sure the added habit is in the list
        assertEquals(2, myHabitsForToday.getHabits().size());
        assertTrue(myHabitsForToday.getHabits().contains(habit2));
    }

    /
     * This test will check to make sure that a completed habit event
     * is added to the list with required information and throws assertions for
     * incorrect inputs (user story 02.01.01, 02.02.01)
     /
    @Test
    void testAddHabitEvent() {
        HabitInstanceList habitInstanceList = mockHabitInstanceList();
        assertEquals(1, habitInstanceList.countInstances());

        // Adds a habit event
        HabitInstance instance2 = new HabitInstance("uniqueInstanceID2", "uniqueUserID1",
                "uniqueHabitID1", "Read 2 pages", "11/04/2021", 40, null, null, Boolean.TRUE);
        habitInstanceList.add(instance2);

        // Checks to see if the habit event is added correctly
        assertEquals(2, habitInstanceList.countInstances());
        assertTrue(habitInstanceList.getInstances().contains(instance2));

        // Throws assertion if the same habit event is added again
        assertThrows(IllegalArgumentException.class, () -> habitInstanceList.add(instance2));

        // Throws assertion if the comment has more than 20 characters
        HabitInstance instance3 = new HabitInstance("uniqueInstanceID3", "uniqueUserID1",
                "uniqueHabitID2", "Morning meditation in the hills",
                "11/04/2021", 10, null, null, Boolean.TRUE);
        assertThrows(IllegalArgumentException.class, () -> habitInstanceList.add(instance3));
    }

    /
     * This test will check to make sure that a completed habit event
     * is contained in the list to view (user story 02.04.01)
     /
    @Test
    void testHasHabitEvent() {
        HabitInstanceList habitInstanceList = mockHabitInstanceList();

        // Adds completed habit events
        HabitInstance instance2 = new HabitInstance("uniqueInstanceID2", "uniqueUserID1",
                "uniqueHabitID1", "Read 2 pages", "11/04/2021", 40, null, null, Boolean.TRUE);
        HabitInstance instance3 = new HabitInstance("uniqueInstanceID3", "uniqueUserID1",
                "uniqueHabitID2", "Morning meditation", "11/04/2021", 10, null, null, Boolean.TRUE);
        habitInstanceList.add(instance2);
        habitInstanceList.add(instance3);

        HabitInstance instance4 = new HabitInstance("uniqueInstanceID4", "uniqueUserID1",
                "uniqueHabitID1", "", "11/04/2021", 60, null, null, Boolean.TRUE);

        // Checking if the habit event exists in the list
        assertTrue(habitInstanceList.hasInstances(instance2));
        assertFalse(habitInstanceList.hasInstances(instance4));

        // Checking details of a habit event
        assertEquals(habitInstanceList.getInstances().get(1)
                .getOpt_comment(), "Read 2 pages");
        assertNotEquals(habitInstanceList.getInstances().get(2)
                .getOpt_comment(), "");
    }

    /
     * This test will check to make sure that the edits made to
     * a completed habit event are saved correctly and throws assertion for
     * incorrect inputs (user story 02.05.01)

    @Test
    void testEditHabitEvent() {
        HabitInstanceList habitInstanceList = mockHabitInstanceList();

        // Adds habit events
        HabitInstance instance2 = new HabitInstance("uniqueInstanceID2", "uniqueUserID1",
                "uniqueHabitID1", "Read 2 pages", "11/04/2021", 40, null, null, Boolean.TRUE);
        HabitInstance instance3 = new HabitInstance("uniqueInstanceID3", "uniqueUserID1",
                "uniqueHabitID2", "Morning meditation", "11/04/2021", 10, null, null, Boolean.TRUE);
        habitInstanceList.add(instance2);
        habitInstanceList.add(instance3);

        // Checking if the edits to the details are saved in the instances list as well
        habitInstanceList.editCommentDetails(instance2,"Read 5 pages" );
        assertEquals(habitInstanceList.getInstances().get(1)
                .getOpt_comment(), "Read 5 pages");

        // Throws assertion if the edit comment has more than 20 characters
        assertThrows(IllegalArgumentException.class, () ->
                habitInstanceList.editCommentDetails(instance3,"Morning meditation in the hills" ));

        // Throws assertion if the edit duration does not have digits
        assertThrows(IllegalArgumentException.class, () ->
                habitInstanceList.editDurationDetails(instance3,"Thirty" ));

    }


     * This test will check to make sure that when a completed habit event
     * is deleted, it is also deleted from the list (user story 02.06.01)
    @Test
    void testDeleteHabitEvent() {
        HabitInstanceList habitInstanceList = mockHabitInstanceList();

        // Adds habit events
        HabitInstance instance2 = new HabitInstance("uniqueInstanceID2", "uniqueUserID1",
                "uniqueHabitID1", "Read 2 pages", "11/04/2021", 40, null, null, Boolean.TRUE);
        HabitInstance instance3 = new HabitInstance("uniqueInstanceID3", "uniqueUserID1",
                "uniqueHabitID2", "Morning meditation", "11/04/2021", 10, null, null, Boolean.TRUE);
        habitInstanceList.add(instance2);
        habitInstanceList.add(instance3);

        habitInstanceList.delete(instance2);

        // Checks if the habit events are deleted from the list
        assertFalse(habitInstanceList.getInstances().contains(instance2));
        assertEquals(2, habitInstanceList.countInstances());

        // Throws assertion if there is a deletion call on a non existent event
        assertThrows(IllegalArgumentException.class, () -> habitInstanceList.delete(instance2));
    }
**/

}
