/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

/**
 *
 * @author Langxin
 */
public class CompanionTimeSpent implements Comparable<CompanionTimeSpent> {

    private int rank;
    private String mac_address;
    private String email;
    private int timeSpent;

    /**
     * Companion with email can be found in demographics table
     *
     * @param mac_address the mac address of this companion
     * @param timeSpent the time spent of this companion
     * @param email the email address of this companion
     */
    public CompanionTimeSpent(String mac_address, int timeSpent, String email) {
        this.mac_address = mac_address;
        this.email = email;
        this.timeSpent = timeSpent;
    }

    /**
     * Companion with email cannot be found in demographics table
     *
     * @param mac_address the mac address of this companion
     * @param timeSpent the time stamp of this companion
     */
    public CompanionTimeSpent(String mac_address, int timeSpent) {
        this.mac_address = mac_address;
        this.timeSpent = timeSpent;
        email = "";
    }

    /**
     * @return the mac address of this companion
     */
    public String getMac_Address() {
        return mac_address;
    }

    /**
     * @return the email address of this companion
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set for this companion
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the time spent of this companion
     */
    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     * @param rank the rank to set for this companion
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the rank of this companion
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param c the companion to compare with
     * @return 1 if time spent matches, -1 if time spent do not match and 0 if
     * time spent is equal
     */
    @Override
    public int compareTo(CompanionTimeSpent c) {
        int compareTime = c.getTimeSpent();
        return compareTime - this.timeSpent;
    }
}
