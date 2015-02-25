/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author Jimmy
 */
public class LocationTimeSpent implements Comparable<LocationTimeSpent> {

    @Expose
    private String location;
    @Expose
    @SerializedName("time-spent")
    private long time_spent;
    private Interval interval;
    private String start_time;
    private String end_time;

    // overload
    /**
     * This constructor creates a LocationTimeSpent object that with a location,
     * start time, end time, and time spent at the location
     *
     * @param location the location
     * @param time_spent the total time spent at this location
     * @param start_time the start time stamp at this location
     * @param end_time the end time stamp at this location
     */
    public LocationTimeSpent(String location, long time_spent, String start_time, String end_time) {

        this.location = location;
        this.time_spent = time_spent;
        this.start_time = start_time;
        this.end_time = end_time;

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
     *
     * @return the location, total time spent at this location, and period from
     * start to end time at this location
     */
    public String toString() {
        //e: for groupTopKNextPlace debug
        return getLocation() + " : " + getTime_spent() + " from " + start_time + " to " + end_time;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the time_spent
     */
    public long getTime_spent() {
        return time_spent;
    }

    /**
     * @param time_spent the time_spent to set
     */
    public void setTime_spent(long time_spent) {
        this.time_spent = time_spent;
    }

    /**
     * @return the start_time
     */
    public String getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start time stamp for this location
     */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end time stamp for this location
     */
    public String getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end time stamp for this location
     */
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    /**
     *
     * @param otherLocation another location time spent object to compare with
     * @return 0 of the locations are similar
     */
    @Override
    public int compareTo(LocationTimeSpent otherLocation) {
        // Ascending
        return location.compareTo(otherLocation.getLocation());
    }
}
