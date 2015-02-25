/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author alice
 */
public class GroupsFound implements Comparable<GroupsFound> {

    @Expose
    private int size;
    @Expose
    @SerializedName("total-time-spent")
    private long total_time_spent;
    @Expose
    private ArrayList<Members> members;
    @Expose
    private ArrayList<LocationTimeSpent> locations;

    /**
     * This constructor creates a GroupsFound object with the list of members in
     * a group and the location and time spent at each location
     *
     * @param members the list of members within the group found
     * @param locations the list of locations that the group has been to
     */
    public GroupsFound(ArrayList<Members> members, ArrayList<LocationTimeSpent> locations) {
        this.members = members;
        this.locations = locations;
        setSize(members.size());
        long total_time = 0;
        // find and set total time spent
        for (LocationTimeSpent lts : locations) {
            total_time += lts.getTime_spent();
        }
        setTotal_time_spent(total_time);
    }

    /**
     * @return the size of the group found
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size of the group to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the total_time_spent for the group
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
     * @return the members in the group
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
     * @return the locations in the group
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
     * This method sorts the members and locations in ascending order
     */
    public void sort() {
        Collections.sort(members);
        Collections.sort(locations);
    }

    /**
     *
     * @param otherGroup another group to compare with
     * @return the difference in total time spent for the compared groups
     */
    @Override
    public int compareTo(GroupsFound otherGroup) {
        int compareGroupSize = otherGroup.getSize();

        // If group size is tied, compare group time spent together
        if (compareGroupSize - this.getSize() == 0) {
            // Descending oder
            long compareGroupTime = otherGroup.getTotal_time_spent();
            long sizeOrder = compareGroupTime - this.getTotal_time_spent();

            // If group time spent together is tied, compare members
            if (sizeOrder == 0) {

                String myEmailMac = "";
                String otherEmailMac = "";

                for (Members m : members) {
                    myEmailMac += m.getEmail();
                    myEmailMac += m.getMac_address();
                }

                for (Members m : otherGroup.getMembers()) {
                    otherEmailMac += m.getEmail();
                    otherEmailMac += m.getMac_address();
                }

                return myEmailMac.compareTo(otherEmailMac);

            }

            return (int) sizeOrder;
        }

        // Descending order
        return compareGroupSize - this.getSize();
    }

}
