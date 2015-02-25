/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.sloca.dao.DateUtilityDAO;
import java.text.ParseException;
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
    //private ArrayList<UserLocIntervals> members;
    private ArrayList<LocationTimeSpent> locations;
    private long totalTimeSpent;
    private int size;

    // overloading constructor
    /**
     * This constructor creates a GroupLocations object with a list of users and
     * their intervals at each location, and the location and time spent at the
     * location
     *
     * @param users the list of users with their intervals at locations
     * @param locations the list of locations the users have been to
     */
    public GroupLocations(ArrayList<UserLocationInterval> users, ArrayList<LocationTimeSpent> locations) {
        this.users = users;
        this.locations = locations;
        tabulateLocations();
        tabulateUsers();
    }

    /**
     * This method filters the list of users in the group to remove duplicates
     * such that each user is unique
     */
    public void tabulateUsers() {
        // create hashmap to remove duplicates in user_ids
        HashMap<String, UserLocationInterval> user_list = new HashMap<>();
        for (UserLocationInterval user : getUsers()) {
            if (!user_list.containsKey(user.getMac_address())) {
                user_list.put(user.getMac_address(), user);
            }
        }

        // clear this user list
        this.getUsers().clear();

        // put hashmap results in
        Iterator it = user_list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            UserLocationInterval user = (UserLocationInterval) pairs.getValue();
            getUsers().add(user);
        }
    }

    // method to tabulate locations and total time spent at each location
    /**
     * This method removes duplicated locations and collates the time spent
     * together for similar locations
     */
    public void tabulateLocations() {

        // create hashmap to remove duplicate location_ids and collate time spent together
        HashMap<String, Long> timeLocMap = new HashMap<>();
        HashMap<String, String> startTime = new HashMap<>();
        HashMap<String, String> endTime = new HashMap<>();

        // try to put values into hashMap
        for (LocationTimeSpent lts : getLocations()) {
            if (!timeLocMap.containsKey(lts.getLocation())) {
                timeLocMap.put(lts.getLocation(), lts.getTime_spent());
                startTime.put(lts.getLocation(), lts.getStart_time());
                endTime.put(lts.getLocation(), lts.getEnd_time());
            } else {
                Long time = timeLocMap.get(lts.getLocation());
                time += lts.getTime_spent();
                timeLocMap.put(lts.getLocation(), time);

                String prevStartTime = startTime.get(lts.getLocation());
                String prevEndTime = endTime.get(lts.getLocation());

                try {
                    if (DateUtilityDAO.isAfter(lts.getStart_time(), prevStartTime)) {
                        startTime.put(lts.getLocation(), lts.getStart_time());
                    }

                    if (DateUtilityDAO.isAfter(lts.getEnd_time(), prevEndTime)) {
                        endTime.put(lts.getLocation(), lts.getEnd_time());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // clear current array list
        this.getLocations().clear();

        // put hashmap results in
        Iterator it = timeLocMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String locID = (String) pairs.getKey();
            getLocations().add(new LocationTimeSpent(locID, (Long) pairs.getValue(), startTime.get(locID), endTime.get(locID)));
        }

        // tabulate total time spent
        long total_time = 0;

        // iterate through list and tabulate total time spent together
        for (LocationTimeSpent lts : getLocations()) {
            total_time += lts.getTime_spent();
        }

        // set total time
        this.totalTimeSpent = total_time;
    }

    // tostring for debugging purposes
    public String toString() {
        String s = "";
        //e: for group top K next Place
        for (UserLocationInterval user : users) {
            s += user.getMac_address() + " ";
        }
        for (LocationTimeSpent lts : getLocations()) {
            s += " " + lts + " ";
        }
        s += " size : " + getSize();
        return s;
    }

    /**
     * @return the total time spent of this group of users and the locations
     * that they have been to
     */
    public long getTotalTimeSpent() {
        long total_time = 0;

        // iterate through locationTimeSpent objects to tabulate time
        for (LocationTimeSpent lts : locations) {
            total_time += lts.getTime_spent();
        }
        setTotalTimeSpent(total_time);
        return total_time;
    }

    /**
     * @param totalTimeSpent the total time spent to be set
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
     * @return the locations this group of users have been to
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

    /**
     *
     * @param locations the location that this group have been to
     */
    public void addLocations(ArrayList<LocationTimeSpent> locations) {

        for (int i = 0; i < locations.size(); i++) {
            for (int j = 0; j < this.locations.size(); j++) {
                if (!locations.get(i).getLocation().equals(this.locations.get(j).getLocation())) {
                    this.locations.add(locations.get(i));
                }
            }
        }

        // make unique
        HashMap<String, LocationTimeSpent> map = new HashMap<>();
        ArrayList<LocationTimeSpent> temp_location_time_spent = new ArrayList<LocationTimeSpent>();
        for (LocationTimeSpent lts : this.locations) {
            if (!map.containsKey(lts.getLocation())) {
                map.put(lts.getLocation(), lts);
                temp_location_time_spent.add(lts);
            }
        }
        // set value
        setLocations(temp_location_time_spent);
    }

    /**
     * @return the size of this group of users
     */
    public int getSize() {
        return this.users.size();
    }

    /**
     * @param size the size to set for this group of users
     */
    public void setSize(int size) {
        this.size = size;
    }
}
