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

    public Locations(String locationID, int timeSpent) {
        this.locationID = locationID;
        this.timeSpent = timeSpent;
    }

    public String getLocationID() {
        return locationID;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    
    
    
}
