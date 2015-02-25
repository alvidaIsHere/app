/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author alice
 */
public class GroupsFound {
    private int size;
    private long total_time_spent; //in second
    private ArrayList<Members> members;
    private ArrayList<LocationTimeSpent> locations;

    public GroupsFound(ArrayList<Members> members, ArrayList<LocationTimeSpent> locations)    {
        this.members = members;
        this.locations = locations;
        setSize(members.size());        
        long total_time = 0;
        // find and set total time spent
        for(LocationTimeSpent lts : locations)   {
            total_time += lts.getTime_spent();
        }
        setTotal_time_spent(total_time);
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the total_time_spent
     */
    public long getTotal_time_spent() {
        return total_time_spent;
    }

    /**
     * @param total_time_spent the total_time_spent to set
     */
    public void setTotal_time_spent(long total_time_spent) {
        this.total_time_spent = total_time_spent;
    }

    /**
     * @return the members
     */
    public ArrayList<Members> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(ArrayList<Members> members) {
        this.members = members;
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
}
