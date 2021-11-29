package com.example.agilesprinters;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class is specifically for testing as testing with the firestore was not working. It tracks
 * and modifies a list of habits.
 *
 * @author Hannah Desmarais
 */
public class HabitTestList {
    private ArrayList<Habit> habitList = new ArrayList<>();

    /**
     * This method adds a habit to the list
     *
     * @param habit habit object being added to the list
     */
    public void addHabit(Habit habit) {
        if (habitList.contains(habit)) {
            throw new IllegalArgumentException();
        }
        habitList.add(habit);
    }

    /**
     * This method edits the item at a certain position by removing the old item and adding the
     * edited one to the list.
     *
     * @param position position of the item that the user wishes to edit
     * @param habit    the updated habit being re-added
     */
    public void editHabit(int position, Habit habit) {
        if (habitList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (habitList.size() - 1 > position) {
            throw new IndexOutOfBoundsException();
        }

        habitList.remove(position);
        habitList.add(habit);
    }

    /**
     * This method deletes a given habit
     *
     * @param habit the habit that the user wishes to delete
     */
    public void deleteHabit(Habit habit) {
        if (habitList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (habitList.contains(habit)) {
            habitList.remove(habit);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This is a method which will place a habit in a different desired location in the habit list.
     *
     * @param habit       The habit the user wishes to move.
     * @param newPosition The position the user would like to move the habit to in the list.
     */
    public void reorderHabit(Habit habit, int newPosition) {
        if (habitList.isEmpty()) {
            throw new IllegalArgumentException();
        } else if (!habitList.contains(habit)) {
            throw new IllegalArgumentException();
        } else if (newPosition > habitList.size() - 1) {
            throw new IndexOutOfBoundsException();
        } else if (newPosition < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            if (newPosition > habit.getListPosition()) {
                for (int i = newPosition; i > habit.getListPosition(); i--) {
                    habitList.get(i).setListPosition(i - 1);
                }
            }
            if (newPosition < habit.getListPosition()) {
                for (int i = newPosition; i < habit.getListPosition(); i++) {
                    habitList.get(i).setListPosition(i + 1);
                }
            }
            habit.setListPosition(newPosition);

            habitList.sort(Comparator.comparing(Habit::getListPosition));
        }
    }

    /**
     * This returns a list of habits
     *
     * @return Return the list
     */
    public ArrayList<Habit> getHabits() {
        ArrayList<Habit> list = habitList;
        return list;
    }
}
