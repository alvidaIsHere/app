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

    public Group(ArrayList<Members> memberList, ArrayList<Locations> locationList) {
        this.memberList = memberList;
        this.locationList = locationList;
    }

    public int getSize() {
        if(memberList != null){
            return memberList.size();
        }else{
            return 0;
        }
    }

    public ArrayList<Members> getMemberList() {
        return memberList;
    }

    public void addMembers(Members member) {
        memberList.add(member);
    }

    public ArrayList<Locations> getLocationList() {
        return locationList;
    }

    public void addLocations(Locations location) {
        locationList.add(location);
    }
    
    
}
