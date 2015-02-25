/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author alice
 */
public class UserLocIntervals {
    private String mac_address;
    private ArrayList<LocationInterval> locationInt;
    private LocationInterval loc_interval;
    
    // constructor
    public UserLocIntervals(String mac_address, ArrayList<LocationInterval> locationInt)   {
        this.mac_address = mac_address;
        this.locationInt = locationInt;
    }
    
    // overriding constructor
    public UserLocIntervals(String mac_address, LocationInterval loc_interval)   {
        this.mac_address = mac_address;
        this.loc_interval = loc_interval;
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
    
    // method to check if this person is together with another person by cross referencing their location intervals
    public GroupLocations isTogetherWith(UserLocIntervals anotherStudent)   {
        
        if(this.getMac_address().equals(anotherStudent.getMac_address()))    {
            return null;
        }
        
        // create group location object
        GroupLocations group = null;
        
        // retrieve this user location intervals
        ArrayList<LocationInterval> thisIntervals = this.getLocationInt();
        ArrayList<LocationInterval> otherIntervals = anotherStudent.getLocationInt();
        
        // initialise group storage objects/containers
        ArrayList<LocationTimeSpent> time_spent_together_at_locations = new ArrayList<LocationTimeSpent>();
        ArrayList<UserLocIntervals> mac_addresses = new ArrayList<UserLocIntervals>();
        
        // initialise user loction interval objects
        UserLocIntervals person1 = null;
        UserLocIntervals person2 = null;
        
        // check for similar location
        for(int i = 0; i < thisIntervals.size(); i++)   {
            LocationInterval this_interval = thisIntervals.get(i);
            
            for(int j = 0; j < otherIntervals.size(); j++)   {
                LocationInterval other_interval = otherIntervals.get(j);

                if(this_interval.getLocation_id().equals(other_interval.getLocation_id()))    {                    
                    // if similar location, check for overlap in intervals 
                    Interval overlap = this_interval.getInterval().overlap(other_interval.getInterval());
                    
                    // if overlap is true, tabulate amount of time overlap and add them into grouplocation object
                    if(overlap != null)    {
                        
                        // get amount of time overlap (subtract/get duration)
                        long overlap_in_milliseconds = overlap.toDurationMillis();
                        // convert to minutes
                        //long minutes = TimeUnit.MILLISECONDS.toMinutes(overlap_in_milliseconds);
                        
                        // for better accuracy (overwrite)
                        long minutes = overlap_in_milliseconds;
                        
                        // create new location time spent object
                        LocationTimeSpent time_spent_at_location = new LocationTimeSpent(this_interval.getLocation_id(), minutes);
                        time_spent_together_at_locations.add(time_spent_at_location);
                        
                        // create new user interval object
                        person1 = new UserLocIntervals(this.mac_address, this_interval);
                        person2 = new UserLocIntervals(anotherStudent.getMac_address(), other_interval);
                        
                    }
                }
            }            
        }
        
        // finally, if there are time spent together, they are considered a group. add details into group location object
        if(time_spent_together_at_locations.size() >= 1)    {
            mac_addresses.add(person1);
            mac_addresses.add(person2);
            //group = new GroupLocations(mac_addresses, time_spent_together_at_locations);
        }
        else    {
            group = null;
        }
        
        return group;
    }
    
    // to string
    public String toString()    {
        String result = "";
        // iterate through time and locations and print out in table format
        for(LocationInterval loc : getLocationInt())   {
            result += "<tr><td>" + loc + "</td></tr>";
        }
        return result;
    }

    /**
     * @return the locationInt
     */
    public ArrayList<LocationInterval> getLocationInt() {
        return locationInt;
    }

    /**
     * @param locationInt the locationInt to set
     */
    public void setLocationInt(ArrayList<LocationInterval> locationInt) {
        this.locationInt = locationInt;
    }

    /**
     * @return the loc_interval
     */
    public LocationInterval getLoc_interval() {
        return loc_interval;
    }

    /**
     * @param loc_interval the loc_interval to set
     */
    public void setLoc_interval(LocationInterval loc_interval) {
        this.loc_interval = loc_interval;
    }
}
