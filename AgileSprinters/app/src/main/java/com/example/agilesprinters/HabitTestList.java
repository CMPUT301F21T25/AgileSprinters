package com.example.agilesprinters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is specifically for testing as testing with the firestore was not working. It tracks
 * and modifies a list of habits.
 */
public class HabitTestList {
    private ArrayList<Habit> habitList = new ArrayList<>();

    /**
     * This method adds a habit to the list
     * @param habit hbait object being added to the list
     */
    public void addHabit(Habit habit){
        if (habitList.contains(habit)){
            throw new IllegalArgumentException();
        }
        habitList.add(habit);
    }

    /**
     * This method edits the item at a certain position by removing the old item and adding the
     * edited one to the list.
     * @param position position of the item that the user wishes to edit
     * @param habit the updated habit being readded
     */
    public void editHabit(int position, Habit habit){
        if (habitList.isEmpty()){
            throw new IllegalArgumentException();
        }
        if (habitList.size()-1 > position){
            throw new IndexOutOfBoundsException();
        }

        habitList.remove(position);
        habitList.add(habit);
    }

    /**
     * This method deletes a given habit
     * @param habit the habit that the user wishes to delete
     */
    public void deleteHabit(Habit habit){
        if(habitList.isEmpty()){
            throw new IllegalArgumentException();
        }
        if(habitList.contains(habit)){
            habitList.remove(habit);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This returns a list of habits
     * @return
     * Return the list
     */
    public ArrayList<Habit> getHabits() {
        ArrayList<Habit> list = habitList;
        return list;
    }
}
