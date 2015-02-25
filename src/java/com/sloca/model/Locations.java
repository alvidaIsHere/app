/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

/**
 *
 * @author jeremy.kuah.2013
 */
public class Locations {

    private String locationID;
    private int timeSpent;

    /**
     * This constructor creates a location object with a location ID and time
     * spent for the location
     *
     * @param locationID the location ID for this location
     * @param timeSpent the time spent at this location
     */
    public Locations(String locationID, int timeSpent) {
        this.locationID = locationID;
        this.timeSpent = timeSpent;
    }

    /**
     *
     * @return the location ID
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     *
     * @return the time spent in a particular location
     */
    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     *
     * @param timeSpent in a particular location ID
     */
    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
}
