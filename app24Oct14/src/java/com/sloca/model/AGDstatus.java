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
public class AGDstatus {
    private String status;
    private int total_users;
    private int total_group;
    private ArrayList<GroupsFound> groups;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the total_users
     */
    public int getTotal_users() {
        return total_users;
    }

    /**
     * @param total_users the total_users to set
     */
    public void setTotal_users(int total_users) {
        this.total_users = total_users;
    }

    /**
     * @return the total_group
     */
    public int getTotal_group() {
        return total_group;
    }

    /**
     * @param total_group the total_group to set
     */
    public void setTotal_group(int total_group) {
        this.total_group = total_group;
    }

    /**
     * @return the groups
     */
    public ArrayList<GroupsFound> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(ArrayList<GroupsFound> groups) {
        this.groups = groups;
    }
    
    
}
