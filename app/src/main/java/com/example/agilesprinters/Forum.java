package com.example.agilesprinters;

/**
 * This class represents a forum event
 *
 * @author Riyaben Patel
 */
public class Forum {
    private String firstName;
    private String lastName;
    private String eventDate;
    private String duration;
    private String comment;
    private String location;
    private String image;

    /**
     * This function is used to get the first name of the user
     *
     * @return returns user's first name as a string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This function is used to set the first name of the user
     *
     * @param firstName sets the firstname of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This function is used to get the last name of the user
     *
     * @return returns user's last name as a string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This function is used to set the last name of the user
     *
     * @param lastName sets the lastname of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This function is used to get the date of forum event
     *
     * @return returns the date of the habit event as a string
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * This function is used to set the date of forum event
     *
     * @param eventDate sets the date of habit event
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * This function is used to get the comment of forum event
     *
     * @return returns the comment of habit event as a string
     */
    public String getComment() {
        return comment;
    }

    /**
     * This function is used to set the comment of forum event
     *
     * @param comment set the comment of habit event
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * This function is used to get the duration of the event
     *
     * @return returns duration of the event as a string
     */
    public String getDuration() {
        return duration;
    }

    /**
     * This function is used to set the duration of the event
     *
     * @param duration sets the duration of the event
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * This function is used to get the image of the event
     *
     * @return returns the image of the event
     */
    public String getImage() {
        return image;
    }

    /**
     * This function is used to set the image of the event
     *
     * @param image sets the image of the event
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * This function is used to set the location of the event
     *
     * @return sets the location of the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This function is used to get the location of the event
     *
     * @return returns the location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * This is a constructor that takes the firstName, lastName, eventDate,
     * duration, comment, image of the event as input to create a forum instance
     *
     * @param firstName first name is given as a string
     * @param lastName  last name is given as a string
     * @param eventDate event date is give as a string
     * @param duration  duration of event is given as a string
     * @param comment   comment is given as a string
     * @param image     image is given as string
     * @param location  location is given as string
     */
    public Forum(String firstName, String lastName, String eventDate, String duration, String comment, String image, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eventDate = eventDate;
        this.duration = duration;
        this.comment = comment;
        this.image = image;
        this.location = location;
    }


}
