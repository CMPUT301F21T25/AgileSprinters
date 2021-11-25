package com.example.agilesprinters;

import android.media.Image;

public class Forum {
    private String firstName;
    private String lastName;
    private String eventDate;
    private String duration;
    private String comment;
    private String location;
    private String image;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String  getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Forum(String firstName, String lastName, String eventDate, String duration, String comment, String image) {
    public void setLocation(String location){this.location=location;}

    public String getLocation(){return location;}


    public Forum(String firstName, String lastName, String eventDate, String duration, String comment, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eventDate = eventDate;
        this.duration = duration;
        this.comment = comment;
        this.location = location;
        this.image = image;
    }


}
