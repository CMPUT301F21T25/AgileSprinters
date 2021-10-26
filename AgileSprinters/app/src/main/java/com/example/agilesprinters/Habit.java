package com.example.agilesprinters;

import java.util.Date;

public class Habit {
    private String title;
    private String reason;
    private String dateToStart;

    public Habit(String title, String reason, String dateToStart) {
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
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
