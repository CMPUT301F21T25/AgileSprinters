package com.example.agilesprinters;

import java.io.Serializable;

/**
 * This class represents a habit instance/event
 *
 * @author Riyaben Patel
 */
public class HabitInstance implements Serializable {
    private String EID; // self ID
    private String UID; // grandparent ID
    private String HID; // parent ID
    private String opt_comment;
    private String date;
    private int duration;
    private String IID;
    private String FID;
    private Boolean isShared;

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
                         String opt_comment, String date, int duration, String IID, String FID, Boolean isShared) {
        this.FID = FID;
        this.EID = EID;
        this.UID = UID;
        this.HID = HID;
        this.IID = IID;
        this.opt_comment = opt_comment;
        this.date = date;
        this.duration = duration;
        this.isShared = isShared;
    }

    public String getIID() {
        return IID;
    }

    public void setIID(String IID) {
        this.IID = IID;
    }

    /**
     * This function is used to get the self ID of the instance
     * @return
     *  returns the self ID as a string
     */
    public String getEID() {
        return EID;
    }

    /**
     * This function is used to get the user ID of the instance
     * @return
     *  returns the user ID as a string
     */
    public String getUID() {
        return UID;
    }

    /**
     * This function is used to get the habit ID of the instance
     * @return
     *  returns the habit ID as a string
     */
    public String getHID() {
        return HID;
    }

    /**
     * This function is used to get the optional comment entered in habit event
     * @return
     *  returns the optional comment as a string
     */
    public String getOpt_comment() {
        return opt_comment;
    }

    /**
     * This function is used to get the date on which the event happened
     * @return
     *  returns the event date as a string
     */
    public String getDate() {
        return date;
    }

    /**
     * This function is used to set the date of the event
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This function is used to get the duration of the event
     * @return
     *  returns the event duration as a int
     */
    public int getDuration() {
        return duration;
    }

    /**
     * This function is used to set the comment for the event
     */
    public void setOpt_comment(String opt_comment) {
        this.opt_comment = opt_comment;
    }

    /**
     * This function is used to set the duration of the event
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * This function is used to set the self ID of the instance
     */
    public void setEID(String EID) {
        this.EID = EID;
    }

    /**
     * This function is used to set the user ID of the instance
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * This function is used to set the habit ID of the instance
     */
    public void setHID(String HID) {
        this.HID = HID;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public Boolean getShared() {
        return isShared;
    }

    public void setShared(Boolean shared) {
        isShared = shared;
    }
}