/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

/**
 *
 * @author alice
 */
public class LocationTime {
    private String location;
    private String date_time;
    
    public LocationTime(String location, String date_time)   {
        this.location = location;
        this.date_time = date_time;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the date_time
     */
    public String getDate_time() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
