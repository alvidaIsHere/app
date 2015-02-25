/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

/**
 *
 * @author alice
 */
public class User {

    String mac_address;
    String date_time;

    /**
     * This constructor creates a user object with a mac address and date time
     * log of the user
     *
     * @param mac_address the mac address for this user
     * @param date_time the date time stamp for this user
     */
    public User(String mac_address, String date_time) {
        this.mac_address = mac_address;
        this.date_time = date_time;
    }
}
