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
public class AGDstatus {

    @Expose
    private String status;
    @Expose
    @SerializedName("total-users")
    private int total_users;
    @Expose
    @SerializedName("total-groups")
    private int total_group;
    @Expose
    private ArrayList<GroupsFound> groups;

    /**
     * @return the status of the AGD function
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status of the group detection result to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the total number of users in AGD status
     */
    public int getTotal_users() {
        return total_users;
    }

    /**
     * @param total_users the total number of users to set
     */
    public void setTotal_users(int total_users) {
        this.total_users = total_users;
    }

    /**
     * @return the total number of groups count
     */
    public int getTotal_group() {
        return total_group;
    }

    /**
     * @param total_group the total number of groups to set
     */
    public void setTotal_group(int total_group) {
        this.total_group = total_group;
    }

    /**
     * @return the total number of groups found
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

    /**
     * This method sorts the groups according to size in descending order, time
     * spent in descending order, and email addresses/mac-address in ascending
     * order
     */
    public void sort() {
        for (GroupsFound gf : groups) {
            gf.sort();
        }
        Collections.sort(groups);
    }

}
