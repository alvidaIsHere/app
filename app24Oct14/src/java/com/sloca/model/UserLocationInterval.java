/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import com.sloca.dao.Utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author alice
 */
public class UserLocationInterval {
    private String email;
    private String mac_address;
    private Interval interval;
    private String start_time;
    private String end_time;
    private String location_id;
    
    // constructor to create user object upon database query (based on only start_time)
    public UserLocationInterval(String mac_address, String email, String location_id, String start_time)   {
        
        // populate respective populatable variables first
        this.mac_address = mac_address;
        this.email = email;
        this.start_time = start_time;
        this.location_id = location_id;
        
        // add 9 minutes to obtain end time by invoking utility method
        try {
            setEnd_time(Utility.getEndDate(start_time, "yyyy-MM-dd HH:mm:ss", 9));
        } catch(ParseException e)   {
            e.printStackTrace();
        }
        
        // create interval object based on start and end time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String temp_start_time = this.start_time;
        String temp_end_time = this.end_time;

        // convert both to 'Date' objects
        try {
            Date start_d = sdf.parse(temp_start_time);
            Date end_d = sdf.parse(temp_end_time);
            DateTime start_dt = new DateTime(start_d);
            DateTime end_dt = new DateTime(end_d);
            setInterval(new Interval(start_dt, end_dt));
        } catch(ParseException e)   {
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
        } catch(ParseException e)   {
            e.printStackTrace();
        } catch(Exception ex)   {
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
}
