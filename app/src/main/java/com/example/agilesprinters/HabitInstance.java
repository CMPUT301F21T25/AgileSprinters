package com.example.agilesprinters;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private String optLoc;
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
     * @param IID
     *  image ID given as string
     * @param FID
     *  forum ID given as string
     * @param isShared
     *  shared status given as boolean
     */
    public HabitInstance(String EID, String UID, String HID,
                         String opt_comment, String date, int duration, String IID, String FID, Boolean isShared, String opt_loc) {
        this.FID = FID;
        this.EID = EID;
        this.UID = UID;
        this.HID = HID;
        this.IID = IID;
        this.opt_comment = opt_comment;
        this.date = date;
        this.duration = duration;
        this.optLoc = opt_loc;
        this.isShared = isShared;
    }

    /**
     * This function is used to get the image id of the instance
     * @return
     *  returns the image ID as a string
     */
    public String getIID() {
        return IID;
    }

    /**
     * This function is used to set the image ID of the instance
     */
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


    /**
     * This function is used to get the value of the forum id
     * @return
     *  return FID as a string
     */
    public String getFID() {
        return FID;
    }

    /**
     * This  function is used to set the value of the forum id
     * @param FID
     * set the value of the user FID
     */
    public void setFID(String FID) {
        this.FID = FID;
    }

    /**
     * This  function is used to get the boolean value of shared instance
     * @return
     *  return boolean expression of getShared instance
     */
    public Boolean getShared() {
        return isShared;
    }

    /**
     * This  function is used to set the boolean value of shared instance
     * @param shared
     * set boolean expression of getShared instance
     */
    public void setShared(Boolean shared) {
        isShared = shared;
    }

    /**
     * This  function is used to get the location of the instance
     * if its empty
     * @return
     *  return location as a empty string
     */
    public String getOptLoc() {
        if (optLoc==null)
            return "";
        return optLoc;
    }

    /**
     * This function is used to set the location of the instance
     */
    public void setOptLoc(String opt_loc) {
        this.optLoc = opt_loc;
    }

    /**
     * This  function is used to get the longitude/latitude
     * of the instance in terms of a string
     * @return
     *  return location as a string
     */
    public String getDisplayLocStr(Geocoder geocoder){
        if (Objects.isNull(optLoc) | optLoc.equals("")) return "";

        List<Address> addresses = null;
        String[] latLng = optLoc.split(",");
        System.out.println(latLng);
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]),1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] location = new String[3];
        String returnStr = "";
        location[0] = addresses.get(0).getLocality();
        location[1] = addresses.get(0).getAdminArea();
        location[2] = addresses.get(0).getCountryName();
        for (int i = 0; i<location.length;i++){
            System.out.println("location: "+location[i]);
            if (location[i]!=null)
                returnStr += location[i]+", ";
        }

        return returnStr.substring(0,returnStr.length()-2);
    }
}