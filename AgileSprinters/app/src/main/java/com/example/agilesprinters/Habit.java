package com.example.agilesprinters;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This is an object class called Habit. It is responsible for creating habits and storing their
 * information
 *
 * @author Hannah Desmarais and Hari Bheesetti
 */
public class Habit implements Serializable {
    private String UID;
    private String HID;
    private String title;
    private String reason;
    private String dateToStart;
    private HashMap<String, Boolean> weekdays;
    private String privacySetting;

    /**
     * This will return the value of the user ID, UID.
     * @return
     * Returns a string containing the user ID.
     */
    public String getUID() {
        return UID;
    }

    /**
     * This method will set the user ID, UID, to a specified value.
     * @param UID The value UID is being set to given as a string.
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * This is a method will pass the value for the habit ID, HID.
     * @return
     * It will return the value of the habit object's HID as a string.
     */
    public String getHID() {
        return HID;
    }

    /**
     * This method will set the habit ID, HID, to a specified value.
     * @param HID The value HID is being set to given as a string.
     */
    public void setHID(String HID) {
        this.HID = HID;
    }

    /**
     * This is a constructor which creates a habit object with values specified in the parameters.
     * @param HID The habit ID given as a string.
     * @param UID The user ID given as a string.
     * @param title The habit title given as a string.
     * @param reason The habit reason given as a string.
     * @param dateToStart The date the habit will start given as a string.
     * @param weekdays The weekdays the habit will occur on given as a hashmap with string keys and
     *                 boolean values.
     * @param privacySetting The privacy setting of the habit given as a string.
     */
    public Habit(String HID,String UID, String title, String reason, String dateToStart, HashMap<String,Boolean> weekdays, String privacySetting) {
        this.UID = UID;
        this.HID = HID;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.weekdays = weekdays;
        this.privacySetting = privacySetting;

    }

    /**
     * This is a method which gets the value of the privacySetting of a habit.
     * @return
     * Returns the value of privacySetting as a string.
     */
    public String getPrivacySetting() {
        return privacySetting;
    }

    /**
     * This is a method which will set the value of privacySetting in a habit.
     * @param privacySetting The value the privacySetting is being set to given as a string.
     */
    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    /**
     * This is a method that gets the weekdays which a habit occurs on.
     * @return
     * Returns a hashmap of days of the week and their value, being either true if it is occurring
     * on that day, or false if it is not occurring on that day.
     */
    public HashMap<String, Boolean> getWeekdays() {
        return weekdays;
    }

    /**
     * This method will set the weekdays that a habit will occur on.
     * @param weekdays This is a hashmap containing days of the week as keys and booleans as values.
     *                 A day of the week will be set to true if the habit is occurring that day, or
     *                 false if it is not.
     */
    public void setWeekdays(HashMap<String,Boolean> weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * This is a method which will get the title of a habit object.
     * @return
     * Returns the value of title as a string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This is a method which will set the title of a habit object.
     * @param title The title a user wishes to set a habit as given as a string.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This is a method which will get the reason for a habit.
     * @return
     * Returns the value of reason as a string.
     */
    public String getReason() {
        return reason;
    }

    /**
     * This is a method which will set the reason the habit is occurring as specified by the user.
     * @param reason The reason given by the user as a string.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * This method will get the date a habit object is supposed to start.
     * @return
     * Returns dateToStart as a string.
     */
    public String getDateToStart() {
        return dateToStart;
    }

    /**
     * This method will set the date a habit is meant to start according to the date a user chose.
     * @param dateToStart The date the user chose given as a string.
     */
    public void setDateToStart(String dateToStart) {
        this.dateToStart = dateToStart;
    }
}
