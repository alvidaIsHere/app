/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author jeremy.kuah.2013
 *
 */
public class Group {

    private final ArrayList<Members> memberList;
    private final ArrayList<Locations> locationList;

    /**
     * This constructor creates a group object with a list of members and
     * locations that they have been to
     *
     * @param memberList the list of members
     * @param locationList the list of locations
     */
    public Group(ArrayList<Members> memberList, ArrayList<Locations> locationList) {
        this.memberList = memberList;
        this.locationList = locationList;
    }

    /**
     *
     * @return returns the size of the group
     */
    public int getSize() {
        if (memberList != null) {
            return memberList.size();
        } else {
            return 0;
        }
    }

    /**
     *
     * @return returns the list of members in the group
     */
    public ArrayList<Members> getMemberList() {
        return memberList;
    }

    /**
     *
     * @param member to be added into the group
     */
    public void addMembers(Members member) {
        memberList.add(member);
    }

    /**
     *
     * @return returns the list of locations that the group has been to
     */
    public ArrayList<Locations> getLocationList() {
        return locationList;
    }

    /**
     *
     * @param location to be added into the group
     */
    public void addLocations(Locations location) {
        locationList.add(location);
    }
}
