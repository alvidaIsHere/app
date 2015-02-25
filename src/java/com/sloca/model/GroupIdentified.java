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
 */
public class GroupIdentified {

    private ArrayList<Group> groupList;

    /**
     *
     * @return returns the total number of users in the list of groups
     */
    public int getTotalUser() {
        if (groupList == null) {
            return 0;
        } else {
            int count = 0;

            for (Group g : groupList) {
                count += g.getSize();
            }

            return count;
        }
    }

    /**
     *
     * @return returns the total number of groups found
     */
    public int getTotalGroup() {
        if (groupList == null) {
            return 0;
        } else {
            return groupList.size();
        }
    }

    /**
     *
     * @return returns the total number of group objects
     */
    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    /**
     *
     * @param groupList sets the group list
     */
    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

}
