package com.example.agilesprinters;
import java.util.ArrayList;
import java.util.List;
/**
 * This is a class that keeps track of a list of habit instance objects
 */
public class HabitInstanceList {
    private final List<HabitInstance> instances = new ArrayList<>();

    /**
     * This adds a instance to the list if the instance does not exist
     * @param instance
     * This is a candidate instance to add
     */
    public void add(HabitInstance instance) {
        if (instances.contains(instance)) {
            throw new IllegalArgumentException();
        } else if (instance.getOpt_comment().length() > 20) {
            throw new IllegalArgumentException();
        }
        instances.add(instance);
    }

    /**
     * This returns a list of instances completed that day
     * @return
     * Return the completed instances
     */
    public List<HabitInstance> getInstances() {
        return instances;
    }

    /**
     * This checks if the instance is in the list
     * and returns a boolean accordingly
     * @param instance
     * This is a candidate instance to check
     * @return
     * Return a boolean
     */
    public boolean hasInstances(HabitInstance instance) {
        return instances.contains(instance);
    }

    /**
     * This checks if the instance is in the list
     * and deletes or throws an exception accordingly
     * @param instance
     * This is a candidate instance to delete
     */
    public void delete(HabitInstance instance) {
        if (instances.contains(instance)) {
            instances.remove(instance);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This returns the number of instances present
     * @return
     * Return the number of instances
     */
    public int countInstances() {

        return instances.size();
    }

    /**
     * This edits the contents of the comment section
     * in a instance or throws an exception accordingly
     * @param instance, value
     * This is a candidate instance to edit
     * The older comment is replaced with this comment param
     */
    public void editCommentDetails(HabitInstance instance, String comment) {
        if (comment.length() > 20) {
            throw new IllegalArgumentException();
        } else {
            instance.setOpt_comment(comment);
        }
    }

    /**
     * This edits the contents of the duration section
     * in a instance or throws an exception accordingly
     * @param instance, duration
     * This is a candidate instance to edit
     * The older duration is replaced with this duration param
     */
    public void editDurationDetails(HabitInstance instance, String duration) {
        if (!duration.matches("[0-9]+") ) {
            throw new IllegalArgumentException();
        } else {
            instance.setDuration(Integer.parseInt(duration));
        }
    }

}
