/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author alice
 */
public class GroupLocations {
    
    private ArrayList<UserLocationInterval> users;
    private ArrayList<UserLocIntervals> members;
    private ArrayList<LocationTimeSpent> locations;
    private long totalTimeSpent;
        
    
    // overloading constructor
    public GroupLocations(ArrayList<UserLocationInterval> users, ArrayList<LocationTimeSpent> locations)    {
        this.users = users;
        this.locations = locations;
        tabulateLocations();
    }
    
    // method to tabulate locations and total time spent at each location
    public void tabulateLocations() {
        // create hashmap to remove duplicate location_ids and collate time spent together
        HashMap<String, Long> timeLocMap = new HashMap<>();
        // try to put values into hashMap
        for(LocationTimeSpent lts : getLocations())   {
            if(!timeLocMap.containsKey(lts.getLocation()))    {
                timeLocMap.put(lts.getLocation(), lts.getTime_spent());
            }
            else    {
                Long time = timeLocMap.get(lts.getLocation());
                time += lts.getTime_spent();
                timeLocMap.put(lts.getLocation(), time);
            }
        }
        
        // clear current array list
        this.getLocations().clear();
        
        // put hashmap results in
        Iterator it = timeLocMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            getLocations().add(new LocationTimeSpent((String)pairs.getKey(), (Long)pairs.getValue()));
        }
        
        // tabulate total time spent
        long total_time = 0;
        
        // iterate through list and tabulate total time spent together
        for(LocationTimeSpent lts : getLocations())   {
            total_time += lts.getTime_spent();
        }
        
        // set total time
        this.totalTimeSpent = total_time;
    }
    
    // tostring for debugging purposes
    public String toString()    {
        String s = "";
        for(UserLocIntervals usr : members)   {
            s += usr.getMac_address() + " ";
        }
        for(LocationTimeSpent lts : getLocations())   {
            s += " are both at " + lts + " for " + totalTimeSpent;
        }
        return s;
    }

    /**
     * @return the totalTimeSpent
     */
    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }

    /**
     * @param totalTimeSpent the totalTimeSpent to set
     */
    public void setTotalTimeSpent(long totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    /**
     * @return the users
     */
    public ArrayList<UserLocationInterval> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<UserLocationInterval> users) {
        this.users = users;
    }

    /**
     * @return the locations
     */
    public ArrayList<LocationTimeSpent> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(ArrayList<LocationTimeSpent> locations) {
        this.locations = locations;
    }
    
    public void addLocations(ArrayList<LocationTimeSpent> locations)    {
        for(LocationTimeSpent lts : locations)   {
            if(!this.locations.contains(lts))    {
                this.locations.add(lts);
            }
        }
    }
}
