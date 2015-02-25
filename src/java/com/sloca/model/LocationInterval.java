/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author alice
 */
public class LocationInterval {

    private String location_id;
    private Interval interval;

    // additional attributes for debug purposes
    private String start_time;
    private String end_time;

    // constructor
    /**
     * This constructor creates a LocationInterval object with a location ID,
     * and a start time and end time
     *
     * @param location_id the location ID for this location interval
     * @param start_time the start time stamp for this location interval
     * @param end_time the end time stamp for this location interval
     */
    public LocationInterval(String location_id, String start_time, String end_time) {
        this.location_id = location_id;
        this.start_time = start_time;
        this.end_time = end_time;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start_date1 = sdf1.parse(start_time);
            Date end_date1 = sdf1.parse(end_time);
            DateTime startDT = new DateTime(start_date1);
            DateTime endDT = new DateTime(end_date1);
            this.interval = new Interval(startDT, endDT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // to string method
    public String toString() {
        return getLocation_id() + " " + start_time + " to " + end_time;
    }

    /**
     * @return the interval for a particular
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

}
