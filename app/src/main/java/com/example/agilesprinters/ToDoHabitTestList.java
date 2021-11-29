package com.example.agilesprinters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that keeps track of a list of habit objects
 *
 * @author Sai Rasazna Ajerla
 */
public class ToDoHabitTestList {
    private final List<Habit> habits = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * This adds a habit to the list if the habit
     * is planned for the day and does not already exist
     *
     * @param habit, date, day
     *               This is a candidate habit to add
     *               The date is used to check if the habit is started before the param date
     *               The day is used to check if the habit is planned to take place that day
     */
    public void addToday(Habit habit, String date, String day) throws ParseException {
        String habitStartDate = habit.getDateToStart();

        if (habits.contains(habit)) {
            throw new IllegalArgumentException();
        }
        if (sdf.parse(habitStartDate).after(sdf.parse(date))) {
            throw new IllegalArgumentException();
        }
        if (!habit.getWeekdays().get(day)) {
            throw new IllegalArgumentException();
        }

        habits.add(habit);
    }

    /**
     * This returns a list of habits
     *
     * @return Return the list
     */
    public List<Habit> getHabits() {
        return habits;
    }
}
