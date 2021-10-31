package com.example.agilesprinters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Habit implements Serializable {
    private String title;
    private String reason;
    private String dateToStart;
    private ArrayList<String> weekdays;
    private String privacySetting;

    public Habit(String title, String reason, String dateToStart, ArrayList<String> weekdays, String privacySetting) {
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

    public ArrayList<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<String> weekdays) {
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
