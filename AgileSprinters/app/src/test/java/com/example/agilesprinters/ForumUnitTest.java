package com.example.agilesprinters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.HashMap;

/**
 * This class provides testing for the calendar activity
 */

public class ForumUnitTest {
    private HabitInstanceList mockHabitInstanceList() {
        HabitInstanceList instanceList = new HabitInstanceList();
        instanceList.add(mockHabitInstance());
        return instanceList;
    }

    private Habit_list mockHabitList() {
        return new Habit_list();
    }
    //get habit instances
    private HabitInstance mockHabitInstance() {
        return new HabitInstance("uniqueInstanceID", "uniqueUserID", "uniqueHabitID",
                "Running", "11/25/2021", 5, null, null, Boolean.TRUE);
    }


}
