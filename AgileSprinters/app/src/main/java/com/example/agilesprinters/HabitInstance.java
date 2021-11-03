package com.example.agilesprinters;

import java.io.Serializable;
import java.util.Date;

public class HabitInstance implements Serializable {
    private String EID;
    private String UID; // self ID
    private String HID; // parent ID
    private String opt_comment;
    private String date;
    private int duration;

    public HabitInstance(String EID, String UID, String HID, String opt_comment, String date, int duration) {
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

    public void setEID(String EID) {
        this.EID = EID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getHID() {
        return HID;
    }

    public void setHID(String HID) {
        this.HID = HID;
    }

    public String getOpt_comment() {
        return opt_comment;
    }

    public void setOpt_comment(String opt_comment) {
        this.opt_comment = opt_comment;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
