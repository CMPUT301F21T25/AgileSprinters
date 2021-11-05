package com.example.agilesprinters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This is an object class called Habit. It is responsible for creating habits and storing their information
 */
public class Habit implements Serializable {
    private String UID;
    private String HID;
    private String title;
    private String reason;
    private String dateToStart;
    private HashMap<String, Boolean> weekdays;
    private String privacySetting;

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

    public Habit(String HID,String UID, String title, String reason, String dateToStart, HashMap<String,Boolean> weekdays, String privacySetting) {
        this.UID = UID;
        this.HID = HID;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.weekdays = weekdays;
        this.privacySetting = privacySetting;

    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public HashMap<String, Boolean> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(HashMap<String,Boolean> weekdays) {
        this.weekdays = weekdays;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateToStart() {
        return dateToStart;
    }

    public void setDateToStart(String dateToStart) {
        this.dateToStart = dateToStart;
    }
}
