package com.example.agilesprinters;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a habit instance/event
 */
public class HabitInstance implements Serializable {
    private String EID; // self ID
    private String UID; // grandparent ID
    private String HID; // parent ID
    private String opt_comment;
    private String date;
    private int duration;

    /**
     * This is a constructor that takes the eventID, habitID, userID,
     * optional comment, date of the event, duration of the event as input
     * to create a habit instance
     * @param EID
     *  event ID given as string
     * @param HID
     *  habit ID given as string
     * @param UID
     *  user ID given as string
     * @param opt_comment
     *  optional comment given as string
     * @param date
     *  date of the event given as string
     * @param duration
     *  duration of the event given as an int
     */
    public HabitInstance(String EID, String UID, String HID,
                         String opt_comment, String date, int duration) {
        this.EID = EID;
        this.UID = UID;
        this.HID = HID;
        this.opt_comment = opt_comment;
        this.date = date;
        this.duration = duration;
    }

    // Getters and setters
    public String getEID() {
        return EID;
    }

    public String getUID() {
        return UID;
    }

    public String getHID() {
        return HID;
    }

    public String getOpt_comment() {
        return opt_comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }
}