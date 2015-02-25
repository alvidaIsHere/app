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
public class UsersInLocation {
    private String location_id;
    private ArrayList<User> users;
    // temporary string to store database group concat reuslts
    private String users_list;
    private String mac_address;
    private ArrayList<LocationTime> locations;
    
    public UsersInLocation(String mac_address, ArrayList<LocationTime> locations)    {
        this.mac_address = mac_address;
        this.locations = locations;
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
     * @return the users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * @return the users_list
     */
    public String getUsers_list() {
        return users_list;
    }

    /**
     * @param users_list the users_list to set
     */
    public void setUsers_list(String users_list) {
        this.users_list = users_list;
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
    
    public String toString()    {
        String result = "";
        // iterate through time and locations and print out in table format
        for(LocationTime locTime : getLocations())   {
            result += "<tr><td>" + locTime.getLocation() + "</td><td>" + locTime.getDate_time() + "</td></tr>";
        }
        return result;
    }

    /**
     * @return the locations
     */
    public ArrayList<LocationTime> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(ArrayList<LocationTime> locations) {
        this.locations = locations;
    }

}
