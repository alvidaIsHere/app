/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author alice
 */
public class UserLocationInterval implements Comparable<UserLocationInterval> {

    private String email;
    private String mac_address;
    private Interval interval;
    private String start_time;
    private String end_time;
    private String location_id;

    // constructor to create user object upon database query (based on only start_time)        
    /**
     * This constructor creates a UserLocationInterval object with the details
     * of the user, the location of the user, and the start and end time spent
     * at the location
     *
     * @param mac_address the mac address of this user
     * @param email the email address of this user
     * @param location_id the location ID of this user
     * @param start_time the start time stamp of this user
     * @param end_time the end time stamp of this user
     */
    public UserLocationInterval(String mac_address, String email, String location_id, String start_time, String end_time) {

        // populate respective populatable variables first
        this.mac_address = mac_address;
        this.email = email;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location_id = location_id;

        // create interval object based on start and end time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // convert both to 'Date' objects
        try {
            Date start_d = sdf.parse(getStart_time());
            Date end_d = sdf.parse(getEnd_time());
            DateTime start_dt = new DateTime(start_d);
            DateTime end_dt = new DateTime(end_d);
            setInterval(new Interval(start_dt, end_dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the mac_address
     */
    public String getMac_address() {
        return mac_address;
    }

    /**
     * @param mac_address the mac_address to set
     */
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    /**
     * @return the interval
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * @return the start_time
     */
    public String getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public String getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(String end_time) {

        // make sure that the interval is updated as well after setting the end_time
        this.end_time = end_time;
        String temp_start_time = this.start_time;
        String temp_end_time = this.end_time;
        // create interval object based on start and end time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // convert both to 'Date' objects
        try {
            Date start_d = sdf.parse(temp_start_time);
            Date end_d = sdf.parse(temp_end_time);
            DateTime start_dt = new DateTime(start_d);
            DateTime end_dt = new DateTime(end_d);
            //System.out.println("eee");
            setInterval(new Interval(start_dt, end_dt));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the location_id
     */
    public String getLocation_id() {
        return location_id;
    }

    /**
     * @param location_id the location_id to set
     */
    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @param anotherList
     */
    public void mergeWith(ArrayList<UserLocationInterval> anotherList) {

    }

    //e: for group top K next places
    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof UserLocationInterval) {
            UserLocationInterval m = (UserLocationInterval) object;
            if (this.mac_address.equals(m.mac_address)) {
                sameSame = true;
            } else {
                sameSame = false;
            }
        }

        return sameSame;
    }

    /**
     *
     * @param other another user to compare with
     * @return 0 if the mac address is the same, positive if another object is
     * more lexicographically and negative if another object is less
     * lexicographically
     */
    @Override
    public int compareTo(UserLocationInterval other) {
        return this.mac_address.compareTo(other.mac_address);
    }

}
