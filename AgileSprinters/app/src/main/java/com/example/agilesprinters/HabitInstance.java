package com.example.agilesprinters;

import java.io.Serializable;
import java.util.Date;

public class HabitInstance implements Serializable {
    private int uniqueId;
    private boolean status; //completed or not
    private String opt_comment;
    private String date;
    private int duration;

    public HabitInstance(int uniqueId, boolean status, String opt_comment, String date, int duration) {
        this.uniqueId = uniqueId;
        this.status = status;
        this.opt_comment = opt_comment;
        this.date = date;
        this.duration = duration;
    }

    // Getters and setters

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
