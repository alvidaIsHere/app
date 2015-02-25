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

    private String mac_address;
    private String email;
    private int timeSpent;

    public CompanionTimeSpent(String mac_address, int timeSpent, String email) {
        this.mac_address = mac_address;
        this.email = email;
        this.timeSpent = timeSpent;
    }

    public CompanionTimeSpent(String mac_address, int timeSpent) {
        this.mac_address = mac_address;
        this.timeSpent = timeSpent;
        email = null;
    }

    public String getMac_Address() {
        return mac_address;
    }

    public String getEmail() {
        return email;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public int compareTo(CompanionTimeSpent c) {
        int compareTime = c.getTimeSpent();
        return compareTime - this.timeSpent;
    }
}
