/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.sloca.dao.Utility;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Langxin
 */
public class Companion {

    private String location_id;
    private String mac_address;
    private String startTime;
    private String endTime;
    private String email;

    /**
     * Constructor for student whose email cannot be found in the demographics
     * table and no new record within 9 minutes
     *
     * @param location_id the location of this student
     * @param mac_address the mac address of this student
     * @param startTime the time stamp of this student
     */
    public Companion(String location_id, String mac_address, String startTime) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;

        try {
            endTime = Utility.getEndDate(startTime, "yyyy-MM-dd HH:mm:ss", 9);
        } catch (ParseException ex) {
            Logger.getLogger(Companion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructor for student whose email can be found in the demographics
     * table and no new record within 9 minutes
     *
     * @param location_id the location of this student
     * @param mac_address the mac address of this student
     * @param startTime the start time stamp of this student
     * @param email the email address of this student
     */
    public Companion(String location_id, String mac_address, String startTime, String email) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;
        this.email = email;

        try {
            endTime = Utility.getEndDate(startTime, "yyyy-MM-dd HH:mm:ss", 9);
        } catch (ParseException ex) {
            Logger.getLogger(Companion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructor for student whose email can be found in the demographics
     * table and there is record within the next 9 Mins
     *
     * @param location_id the location ID of this student
     * @param mac_address the mac address of this student
     * @param startTime the start time stamp of this student
     * @param endTime the end time stamp of this student
     * @param email the email address of this student
     */
    public Companion(String location_id, String mac_address, String startTime, String endTime, String email) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
    }

    /**
     * @return the location id of this companion
     */
    public String getLocation_ID() {
        return location_id;
    }

    /**
     * @return the mac address of this companion
     */
    public String getMac_Address() {
        return mac_address;
    }

    /**
     *
     * @return the start time stamp of this companion
     *
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @return the end time stamp of this companion
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the end time spent with this companion
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the email address of this companion
     */
    public String getEmail() {
        return email;
    }

}
