/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

/**
 *
 * @author Jimmy
 */
public class LocationTimeSpent {
    private String location; //location id
    private long time_spent;
    
    // constructor
    public LocationTimeSpent(String location, long time_spent)   {
        this.location = location;
        this.time_spent = time_spent;
    }
    
    public String toString()    {
        return getLocation() + " : " + getTime_spent();
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
}
